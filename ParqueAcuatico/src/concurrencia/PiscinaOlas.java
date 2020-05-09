package concurrencia;

import hilos.Usuario;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import util.FuncionesGenerales;

/**
 * Clase PiscinaOlas
 *
 * Define la forma y funcionamiento de la piscina de olas
 *
 * @author
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class PiscinaOlas {
    //Elementos de la interfaz
    private final JTextField monitorPiscinaOlas;
    private final JTextArea areaPiscinaOlas;
    private final JTextArea colaPiscinaOlas;
    private final JTextField esperaCompañero;
    
    //Concurrencia
    private final Semaphore semPiscinaOlas = new Semaphore(20, true);
    private final Semaphore semPiscinaOlas0 = new Semaphore(0, true);
    private final BlockingQueue colaEntrarPiscinaOlas = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> piscinaOlas = new CopyOnWriteArrayList<>();
    private final CyclicBarrier barreraPiscinaOlas = new CyclicBarrier(2);
    
    //Atributos extra
    private Usuario monitorPiscinaOlasUsuario;
    private Usuario esperaCompañeroUsuario;
    private boolean accesoCerrado;
    private final FuncionesGenerales fg;
    private final Paso paso;

    public PiscinaOlas(JTextField monitorPiscinaOlas, JTextArea areaPiscinaOlas, JTextArea colaPiscinaOlas, FuncionesGenerales fg, Paso paso, JTextField esperaCompañero) {
        this.monitorPiscinaOlas = monitorPiscinaOlas;
        this.areaPiscinaOlas = areaPiscinaOlas;
        this.colaPiscinaOlas = colaPiscinaOlas;
        this.esperaCompañero = esperaCompañero;
        
        this.monitorPiscinaOlasUsuario = null;
        this.esperaCompañeroUsuario = null;
        this.accesoCerrado = false;
        this.fg = fg;
        this.paso = paso;
    } // Cierre del método

    public boolean entrarPiscinaOlas(Usuario u) {
        try {
            //Comprobacion de que no este cerrada la atraccion por no haber suficientes usuarios en el parque
            if( accesoCerrado ) {
                return false;
            }
            
            paso.mirar();
            
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en la cola de la piscina de olas.\n");
            colaEntrarPiscinaOlas.put(u);
            fg.imprimir(colaPiscinaOlas, colaEntrarPiscinaOlas.toString());
            
            paso.mirar();
            semPiscinaOlas0.acquire();
            
            //Si es rechazado por el monitor aqui se le expulsa de la piscina
            if( !u.getAccesoPermitido() || u.getEdad() < 6 || ( u.getEsAcompañante() && u.getAcompañante().getEdad() < 6 ) ) {
                return false;
            }
            
            paso.mirar();
            if( u.getEdad() > 10 && !u.getEsAcompañante() ) {
                //Si no es un niño con acompañante o un acompañante entra para esperar a una pareja con la que entrar a la piscina 
                esperaCompañero.setText(u.toString());
                esperaCompañeroUsuario = u;
                barreraPiscinaOlas.await();
                esperaCompañero.setText("");
                esperaCompañeroUsuario = null;
            }
            
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en la piscina de olas.\n");
            piscinaOlas.add(u);
            fg.imprimir(areaPiscinaOlas, piscinaOlas.toString());
        } catch( InterruptedException | BrokenBarrierException ex ) {
            return false;
        }

        return true;
    } // Cierre del método

    //Metodo para salir de la piscina de olas
    public void salirPiscinaOlas(Usuario u) {
        paso.mirar();
        piscinaOlas.remove(u);
        fg.imprimir(areaPiscinaOlas, piscinaOlas.toString());
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale de la piscina de olas.\n");
        semPiscinaOlas.release();
    } // Cierre del método

    //Metodo por el que el monitor recoge un usuario de la cola de entrada
    public Usuario controlarPiscinaOlas() {
        try {
            paso.mirar();
            Usuario u = (Usuario) colaEntrarPiscinaOlas.take();
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en el monitor de la piscina de olas.\n");
            
            fg.imprimir(colaPiscinaOlas, colaEntrarPiscinaOlas.toString());
            
            monitorPiscinaOlas.setText(u.toString());
            monitorPiscinaOlasUsuario = u;
            
            return u;
        } catch( InterruptedException ex ) {
            return null;
        }

    } // Cierre del método

    //Metodo por el que el monitor decide a donde tiene que  ir cada usuario
    public void controlarPiscinaOlas(Usuario u) {
        paso.mirar();
        if( u.getEdad() < 6 || ( u.getEsAcompañante() && u.getAcompañante().getEdad() < 6 ) ) {
            //Si es un niño de 5 años o menos o de un acompañante de un niño de 5 años o menos
            u.setAccesoPermitido(false);
        } else if( u.getEdad() < 11 ) {
            //Si es un niño de mas de 5 años y menos de 10 adquiere dos permisos y libera uno para el acompañante
            try {
                semPiscinaOlas.acquire(2);
                semPiscinaOlas.release();
            } catch( InterruptedException ex ) {
                System.out.println("ERROR: " + ex);
            }
        } else {
            //Si es un acompañante de un niño de mas de 5 años o un usuario 11 o mas años
            try {
                semPiscinaOlas.acquire();
            } catch( InterruptedException ex ) {
                System.out.println("ERROR: " + ex);
            }
        }
        
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale del monitor de la piscina de olas.\n");
        monitorPiscinaOlas.setText("");
        monitorPiscinaOlasUsuario = null;
        
        paso.mirar();
        semPiscinaOlas0.release();
    } // Cierre del método

    public BlockingQueue getColaEntrarPiscinaOlas() {
        return colaEntrarPiscinaOlas;
    } // Cierre del método

    public CopyOnWriteArrayList<Usuario> getPiscinaOlas() {
        return piscinaOlas;
    } // Cierre del método

    public Usuario getMonitorPiscinaOlasUsuario() {
        return monitorPiscinaOlasUsuario;
    } // Cierre del método

    public Usuario getEsperaCompañeroUsuario() {
        return esperaCompañeroUsuario;
    } // Cierre del método

    public void setEsperaCompañeroUsuario(Usuario esperaCompañeroUsuario) {
        this.esperaCompañeroUsuario = esperaCompañeroUsuario;
    } // Cierre del método

    public JTextField getEsperaCompañero() {
        return esperaCompañero;
    } // Cierre del método

    public void setAccesoCerrado(boolean accesoCerrado) {
        this.accesoCerrado = accesoCerrado;
    } // Cierre del método

    public Semaphore getSemPiscinaOlas() {
        return semPiscinaOlas;
    } // Cierre del método
} // Cierre de la clase