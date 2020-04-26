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
    private final FuncionesGenerales fg;
    private final Paso paso;

    public Vestuario(JTextArea colaVestuario, JTextField monitorVestuario, JTextArea areaVestuario, FuncionesGenerales fg, Paso paso) {
        this.colaVestuario = colaVestuario;
        this.monitorVestuario = monitorVestuario;
        this.areaVestuario = areaVestuario;
        
        this.fg = fg;
        this.paso = paso;
    } // Cierre del método
    
    public void entrarVestuarios(Usuario u) {
        try {
            colaEntrarVestuario.put(u);
            fg.imprimir(colaVestuario, colaEntrarVestuario.toString());
            semVestuario.acquire();

            vestuario.add(u);
            fg.imprimir(areaVestuario, vestuario.toString());
            if( u.getEsAcompañante() || u.getEdad() < 18 ) {
                semVestuarioNiño.acquire();
            } else {
                semVestuarioAdulto.acquire();
            }
        } catch(InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }
    } // Cierre del método

    public void salirVestuarios(Usuario u) {
        vestuario.remove(u);
        fg.imprimir(areaVestuario, vestuario.toString());
        if( u.getEsAcompañante() || u.getEdad() < 18 ) {
            paso.mirar();
            semVestuarioNiño.release();
        } else {
            paso.mirar();
            semVestuarioAdulto.release();
        }
    } // Cierre del método

    public Usuario controlarVestuario() {
        try {
            Usuario u = (Usuario) colaEntrarVestuario.take();

            monitorVestuario.setText(u.toString());
            fg.imprimir(colaVestuario, colaEntrarVestuario.toString());

            return u;
        } catch (InterruptedException ex) {
            return null;
        }
    } // Cierre del método

    public void controlarVestuario(Usuario u) {
        try {
            if( u.getEdad() > 17 && !u.getEsAcompañante() ) {
                semVestuarioAdulto.acquire();
                paso.mirar();
                semVestuarioAdulto.release();
                paso.mirar();
                semVestuario.release();
            } else if( u.getEdad() <= 10 ) {
                semVestuarioNiño.acquire(2);
                paso.mirar();
                semVestuarioNiño.release(2);
                paso.mirar();
                semVestuario.release();
            } else if( u.getEsAcompañante() ) {
                paso.mirar();
                semVestuario.release();
            } else { 
                semVestuarioNiño.acquire();
                paso.mirar();
                semVestuarioNiño.release();
                paso.mirar();
                semVestuario.release();
            }

            monitorVestuario.setText("");
        } catch (InterruptedException e) {
            System.out.println("ERROR: " + e);
        }
    } // Cierre del método
    
    public CopyOnWriteArrayList<Usuario> getVestuarios() {
        return vestuario;
    } // Cierre del método
    
    public BlockingQueue getColaVestuarios() {
        return colaEntrarVestuario;
    } // Cierre del método
} // Cierre de la clase