package concurrencia;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import hilos.Usuario;
import util.FuncionesGenerales;

/**
 * Clase Vestuario
 *
 * Define la forma y funcionamiento del vestuario
 *
 * @author
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Vestuario {
    //Elementos de la interfaz
    private final JTextArea colaVestuario;
    private final JTextField monitorVestuario;
    private final JTextArea areaVestuario;
    
    //Concurrencia
    private final Semaphore semVestuarioAdulto = new Semaphore(20, true);
    private final Semaphore semVestuarioNiño = new Semaphore(10, true);
    private final BlockingQueue colaEntrarVestuario = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> vestuario = new CopyOnWriteArrayList<>();
    
    //Atributos extra
    private Usuario monitorVestuarioUsuario;
    private final FuncionesGenerales fg;
    private final Paso paso;

    public Vestuario(JTextArea colaVestuario, JTextField monitorVestuario, JTextArea areaVestuario, FuncionesGenerales fg, Paso paso) {
        this.colaVestuario = colaVestuario;
        this.monitorVestuario = monitorVestuario;
        this.areaVestuario = areaVestuario;
        
        this.monitorVestuarioUsuario = null;
        this.fg = fg;
        this.paso = paso;
    } // Cierre del método
    
    //Metodo para entrar en el vestuario
    public void entrarVestuarios(Usuario u) {
        paso.mirar();
        try {
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " entra en la cola del vestuario. \n");
            colaEntrarVestuario.put(u);
            fg.imprimir(colaVestuario, colaEntrarVestuario.toString());
            
            paso.mirar();
            u.getSemUsu().acquire();
            
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " entra en el vestuario. \n");
            vestuario.add(u);
            fg.imprimir(areaVestuario, vestuario.toString());
        } catch( InterruptedException ex ) {
            System.out.println("ERROR: " + ex);
        }
    } // Cierre del método

    //Metodo para salir del vestuario
    public void salirVestuarios(Usuario u) {
        paso.mirar();
        
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale del vestuario. \n");
        vestuario.remove(u);
        fg.imprimir(areaVestuario, vestuario.toString());
        
        paso.mirar();
        if( u.getEsAcompañante() || u.getEdad() < 18 ) {
            //Si es acompañante o tiene menos de 18 años
            semVestuarioNiño.release();
        } else {
            //Mayores de 18 años
            semVestuarioAdulto.release();
        }
    } // Cierre del método

    //Metodo por el que el monitor recoge un usuario de la cola de entrada
    public Usuario controlarVestuario() {
        paso.mirar();
        try {
            Usuario u = (Usuario) colaEntrarVestuario.take();
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " es atendido por el monitor del vestuario. \n");
            fg.imprimir(colaVestuario, colaEntrarVestuario.toString());
            monitorVestuario.setText(u.toString());
            monitorVestuarioUsuario = u;

            return u;
        } catch (InterruptedException ex) {
            return null;
        }
    } // Cierre del método

    //Metodo por el cual el monitor decide por donde deben ir cada usuario
    public void controlarVestuario(Usuario u) {
        paso.mirar();
        try {
            if( u.getEdad() > 17 && !u.getEsAcompañante() ) {
                //Si es mayor de 18 años y no es acompañante
                semVestuarioAdulto.acquire();
            } else if( u.getEdad() < 11 ) {
                //Si tiene 10 años o menos adquiere dos y libera uno para el acompañante
                semVestuarioNiño.acquire(2);
                paso.mirar();
                semVestuarioNiño.release();
            } else { 
                //Si es acompañante o tiene menos de 18 años y mas de 10
                semVestuarioNiño.acquire();
            }       
        } catch( InterruptedException ex ) {
            System.out.println("ERROR: " + ex);
        }
        
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " finaliza la atencion del monitor del vestuario. \n");
        monitorVestuario.setText("");
        monitorVestuarioUsuario = null;
        
        paso.mirar();
        u.getSemUsu().release();
    } // Cierre del método
    
    public CopyOnWriteArrayList<Usuario> getVestuarios() {
        return vestuario;
    } // Cierre del método
    
    public BlockingQueue getColaVestuarios() {
        return colaEntrarVestuario;
    } // Cierre del método

    public Usuario getMonitorVestuarioUsuario() {
        return monitorVestuarioUsuario;
    } // Cierre del método
} // Cierre de la clase