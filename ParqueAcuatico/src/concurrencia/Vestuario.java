package concurrencia;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import hilos.Usuario;

/**
 *
 * @authores 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Vestuario {
    private final JTextArea colaVestuario;
    private final JTextField monitorVestuario;
    private final JTextArea areaVestuario;
     
    private final Semaphore semVestuarioAdulto = new Semaphore(20, true);
    private final Semaphore semVestuarioNiño = new Semaphore(10, true);
    private final Semaphore semVestuario = new Semaphore(0, true);
    
    private final BlockingQueue colaEntrarVestuario = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> vestuario = new CopyOnWriteArrayList<>();

    public Vestuario(JTextArea colaVestuario, JTextField monitorVestuario, JTextArea areaVestuario) {
        this.colaVestuario = colaVestuario;
        this.monitorVestuario = monitorVestuario;
        this.areaVestuario = areaVestuario;
    }
    
    public void entrarVestuarios(Usuario u) {
        try {
            colaEntrarVestuario.put(u);
            imprimir(colaVestuario, colaEntrarVestuario.toString());
            semVestuario.acquire();

            vestuario.add(u);
            imprimir(areaVestuario, vestuario.toString());
            if( u.getEsAcompañante() || u.getEdad() < 18 ) {
                semVestuarioNiño.acquire();
            } else {
                semVestuarioAdulto.acquire();
            }
        } catch(InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }
    }

    public void salirVestuarios(Usuario u) {
        vestuario.remove(u);
        imprimir(areaVestuario, vestuario.toString());
        if( u.getEsAcompañante() || u.getEdad() < 18 ) {
            semVestuarioNiño.release();
        } else {
            semVestuarioAdulto.release();
        }
    }

    public Usuario controlarVestuario() {
        try {
            Usuario u = (Usuario) colaEntrarVestuario.take();

            monitorVestuario.setText(u.toString());
            imprimir(colaVestuario, colaEntrarVestuario.toString());

            return u;
        } catch (InterruptedException ex) {
            return null;
        }
    }

    public void controlarVestuario(Usuario u) {
        try {
            if( u.getEdad() > 17 && !u.getEsAcompañante() ) {
                semVestuarioAdulto.acquire();
                semVestuarioAdulto.release();
                semVestuario.release();
            } else if( u.getEdad() <= 10 ) {
                semVestuarioNiño.acquire(2);
                semVestuarioNiño.release(2);

                semVestuario.release();
            } else if( u.getEsAcompañante() ) {
                semVestuario.release();
            } else { 
                semVestuarioNiño.acquire();
                semVestuarioNiño.release();
                semVestuario.release();
            }

            monitorVestuario.setText("");
        } catch (InterruptedException e) {
            System.out.println("ERROR: " + e);
        }
    }
    
    private synchronized void imprimir(JTextArea campo, String contenido) {
        campo.setText(contenido);
    }   
}