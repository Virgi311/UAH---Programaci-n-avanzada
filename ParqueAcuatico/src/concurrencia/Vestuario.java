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
    private final Semaphore semVestuario = new Semaphore(0, true);
    private final BlockingQueue colaEntrarVestuario = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> vestuario = new CopyOnWriteArrayList<>();
    
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
    
    public void entrarVestuarios(Usuario u) {
        paso.mirar();
        try {
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en la cola del vestuario. \n");
            colaEntrarVestuario.put(u);
            fg.imprimir(colaVestuario, colaEntrarVestuario.toString());
            
            paso.mirar();
            semVestuario.acquire();
            
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en el vestuario. \n");
            vestuario.add(u);
            fg.imprimir(areaVestuario, vestuario.toString());
        } catch( InterruptedException ex ) {
            System.out.println("ERROR: " + ex);
        }
    } // Cierre del método

    public void salirVestuarios(Usuario u) {
        paso.mirar();
        
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale del vestuario. \n");
        vestuario.remove(u);
        fg.imprimir(areaVestuario, vestuario.toString());
        
        paso.mirar();
        if( u.getEsAcompañante() || u.getEdad() < 18 ) {
            semVestuarioNiño.release();
        } else {
            semVestuarioAdulto.release();
        }
    } // Cierre del método

    public Usuario controlarVestuario() {
        paso.mirar();
        try {
            Usuario u = (Usuario) colaEntrarVestuario.take();
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en el monitor del vestuario. \n");
            fg.imprimir(colaVestuario, colaEntrarVestuario.toString());
            monitorVestuario.setText(u.toString());
            monitorVestuarioUsuario = u;

            return u;
        } catch (InterruptedException ex) {
            return null;
        }
    } // Cierre del método

    public void controlarVestuario(Usuario u) {
        paso.mirar();
        try {
            if( u.getEdad() > 17 && !u.getEsAcompañante() ) {
                semVestuarioAdulto.acquire();
            } else if( u.getEdad() <= 10 ) {
                semVestuarioNiño.acquire(2);
                paso.mirar();
                semVestuarioNiño.release();
            } else { 
                semVestuarioNiño.acquire();
            }       
        } catch( InterruptedException ex ) {
            System.out.println("ERROR: " + ex);
        }
        
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale del monitor del vestuario. \n");
        monitorVestuario.setText("");
        monitorVestuarioUsuario = null;
        
        paso.mirar();
        semVestuario.release();
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