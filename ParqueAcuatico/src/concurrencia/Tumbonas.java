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
 * Clase Tumbonas
 *
 * Define la forma y funcionamiento de las tumbonas
 *
 * @author 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Tumbonas {
    //Elementos de la interfaz
    private final JTextArea colaTumbonas;
    private final JTextArea areaTumbonas;
    private final JTextField monitorTumbonas;
    
    //Concurrencia
    private final Semaphore semTumbonas = new Semaphore(20, true);
    private final Semaphore semTumbonas0 = new Semaphore(0, true);
    private final CopyOnWriteArrayList<Usuario> colaEntrarTumbonas = new CopyOnWriteArrayList<>();
    private final BlockingQueue colaEntrarTumbonas0 = new LinkedBlockingQueue();
    private final BlockingQueue colaEntrarTumbonas1 = new LinkedBlockingQueue();
    private final BlockingQueue colaEntrarTumbonas2 = new LinkedBlockingQueue();
    private final BlockingQueue colaEntrarTumbonasNiñoAcompañante = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> tumbonas = new CopyOnWriteArrayList<>();

    //Atributos extra
    private Usuario monitorTumbonasUsuario;
    private final FuncionesGenerales fg;
    private final Paso paso;
    
    public Tumbonas(JTextArea colaTumbonas, JTextArea areaTumbonas, JTextField monitorTumbonas, FuncionesGenerales fg, Paso paso) {
        this.colaTumbonas = colaTumbonas;
        this.areaTumbonas = areaTumbonas;
        this.monitorTumbonas = monitorTumbonas;
        
        this.monitorTumbonasUsuario = null;
        this.fg = fg;
        this.paso = paso;
    } // Cierre del método
       
    //Metodo para entrar en las tumbonas
    public boolean entrarTumbonas(Usuario u){
        try {
            paso.mirar();
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " entra en la cola de entrada de las tumbonas.\n");
            colaEntrarTumbonas.add(u);
            if( u.getEsAcompañante() || u.getEdad() < 11 ) {
                colaEntrarTumbonasNiñoAcompañante.put(u);
            } else {
                switch( (int) ( 1 * Math.random() ) ) {
                    case 0:
                        colaEntrarTumbonas0.put(u);
                        break;

                    case 1:
                        colaEntrarTumbonas1.put(u);
                        break;

                    case 2:
                        colaEntrarTumbonas2.put(u);
                        break;
                }
            }
            fg.imprimir(colaTumbonas, colaEntrarTumbonas.toString());
            
            paso.mirar();
            semTumbonas0.acquire();
            
            //Si es rechazado por el monitor aqui se le expulsa de la piscina
            if( !u.getAccesoPermitido() /*|| u.getEdad() < 15 || u.getEsAcompañante() */) {
                return false;
            }
            
            paso.mirar();
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " entra en las tumbonas. \n");
            tumbonas.add(u);
            fg.imprimir(areaTumbonas, tumbonas.toString());
        } catch( InterruptedException ex ) {
            return false;
        }
    
        return true;
    } // Cierre del método
    
    //Metodo para salir de la tumbonas
    public void salirTumbonas(Usuario u){
        paso.mirar();
        tumbonas.remove(u);
        fg.imprimir(areaTumbonas, tumbonas.toString());
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale de las tumbonas.\n");
        semTumbonas.release();
    } // Cierre del método
    
    //Metodo por el cual el monitor recoge a un usuario de la cola de entrada
    public Usuario controlarTumbonas(){
        paso.mirar();
        try {
            Usuario u = null;
            if( colaEntrarTumbonasNiñoAcompañante.size() > 0 ) {
                u = (Usuario) colaEntrarTumbonasNiñoAcompañante.take();
            } else {
                switch( (int) ( 1 * Math.random() ) ) {
                    case 0:
                        u = (Usuario) colaEntrarTumbonas0.take();
                        break;

                    case 1:
                        u = (Usuario) colaEntrarTumbonas1.take();
                        break;

                    case 2:
                        u = (Usuario) colaEntrarTumbonas2.take();
                        break;
                }
            }
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " es atendido por el monitor de las tumbonas. \n");
            colaEntrarTumbonas.remove(u);
            fg.imprimir(colaTumbonas, colaEntrarTumbonas.toString());
            monitorTumbonas.setText(u.toString());
            monitorTumbonasUsuario = u;
            
            return u;
        } catch( InterruptedException ex ) {
            return null;
        } 
    } // Cierre del método
    
    //Metodo por el cual el monitor decide por donde debe ir cada usuario
    public void controlarTumbonas(Usuario u) {
        paso.mirar();
        if( u.getEdad() < 15 || u.getEsAcompañante() ) {
            //Si tiene menos de 15 años o es acompañante
            u.setAccesoPermitido(false);
        } else {
            try {
                semTumbonas.acquire();
            } catch(InterruptedException ex) {
                System.out.println("ERROR: " + ex);
            } 
        }
        
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " finaliza la atencion del monitor de las tumbonas. \n");
        monitorTumbonas.setText("");
        monitorTumbonasUsuario = null;
        
        paso.mirar();
        semTumbonas0.release();
    } // Cierre del método
    
    public CopyOnWriteArrayList<Usuario> getTumbonas() {
        return tumbonas;
    } // Cierre del método

    public Usuario getMonitorTumbonasUsuario() {
        return monitorTumbonasUsuario;
    } // Cierre del método

    public CopyOnWriteArrayList<Usuario> getColaEntrarTumbonas() {
        return colaEntrarTumbonas;
    } // Cierre del método
} // Cierre de la clase