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
    //Concurrencia
    private final Semaphore semPiscinaOlas = new Semaphore(20, true);
    private final Semaphore semPiscinaOlas0 = new Semaphore(0, true);
    private final BlockingQueue colaEntrarPiscinaOlas = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> piscinaOlas = new CopyOnWriteArrayList<>();
    private final CyclicBarrier barreraPiscinaOlas = new CyclicBarrier(2);
    private boolean accesoPermitido = false;
    private final FuncionesGenerales fg;
    private final Paso paso;

    public PiscinaOlas(JTextField monitorPiscinaOlas, JTextArea areaPiscinaOlas, JTextArea colaPiscinaOlas, FuncionesGenerales fg, Paso paso) {
        this.monitorPiscinaOlas = monitorPiscinaOlas;
        this.areaPiscinaOlas = areaPiscinaOlas;
        this.colaPiscinaOlas = colaPiscinaOlas;
        
        this.fg = fg;
        this.paso = paso;
    } // Cierre del método

    public boolean entrarPiscinaOlas(Usuario u) {
        try {
            colaEntrarPiscinaOlas.put(u);
            fg.imprimir(colaPiscinaOlas, colaEntrarPiscinaOlas.toString());
            semPiscinaOlas0.acquire();
            
            if( !accesoPermitido ) {
                return false;
            }
            
            if( u.getEdad() > 10 && !u.getEsAcompañante() ) {
                try {
                    semPiscinaOlas.acquire();
                    barreraPiscinaOlas.await();
                    
                    piscinaOlas.add(u);
                    
                    fg.imprimir(areaPiscinaOlas, piscinaOlas.toString());
                } catch(BrokenBarrierException ex) {
                    System.out.println("ERROR: " + ex);
                }
            } else {
                semPiscinaOlas.acquire();
                piscinaOlas.add(u);
                fg.imprimir(areaPiscinaOlas, piscinaOlas.toString());
            }
        } catch(InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }

        return true;
    } // Cierre del método

    public void salirPiscinaOlas(Usuario u) {
        piscinaOlas.remove(u);
        fg.imprimir(areaPiscinaOlas, piscinaOlas.toString());
        paso.mirar();
        semPiscinaOlas.release();
    } // Cierre del método

    public Usuario controlarPiscinaOlas() {
        Usuario u = null;
        try {
            u = (Usuario) colaEntrarPiscinaOlas.take();
            fg.imprimir(colaPiscinaOlas, colaEntrarPiscinaOlas.toString());
            monitorPiscinaOlas.setText(u.toString());
        } catch (InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }

        return u;
    } // Cierre del método

    public void controlarPiscinaOlas(Usuario u) {
        if( u.getEdad() <= 5 ) {
            accesoPermitido = false;
            paso.mirar();
            semPiscinaOlas0.release();
            try {
                colaEntrarPiscinaOlas.take();
                fg.imprimir(colaPiscinaOlas, colaEntrarPiscinaOlas.toString());
                paso.mirar();
                semPiscinaOlas0.release();
            } catch(InterruptedException ex) {
                System.out.println("ERROR: " + ex);
            }
        } else if( u.getEdad() <= 10 ) {
            try {
                semPiscinaOlas.acquire(2);
                paso.mirar();
                semPiscinaOlas.release(2);
                accesoPermitido = true;
                paso.mirar();
                semPiscinaOlas0.release();
                colaEntrarPiscinaOlas.take();
                fg.imprimir(colaPiscinaOlas, colaEntrarPiscinaOlas.toString());
                paso.mirar();
                semPiscinaOlas0.release();
            } catch(InterruptedException ex) {
                System.out.println("ERROR: " + ex);
            }
        } else {
            try {
                semPiscinaOlas.acquire();
                paso.mirar();
                semPiscinaOlas.release();
                accesoPermitido = true;
                paso.mirar();
                semPiscinaOlas0.release();
            } catch(InterruptedException ex) {
                System.out.println("ERROR: " + ex);
            }
        }

        monitorPiscinaOlas.setText("");
    } // Cierre del método

    public BlockingQueue getColaEntrarPiscinaOlas() {
        return colaEntrarPiscinaOlas;
    } // Cierre del método

    public CopyOnWriteArrayList<Usuario> getPiscinaOlas() {
        return piscinaOlas;
    } // Cierre del método
} // Cierre de la clase