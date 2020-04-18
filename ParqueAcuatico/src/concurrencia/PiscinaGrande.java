package concurrencia;

import hilos.Usuario;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @authores 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class PiscinaGrande {

    private final JTextField monitorPiscinaGrande;
    private final JTextArea areaPiscinaGrande;
    private final JTextArea colaPiscinaGrande;

    private final Semaphore semPiscinaGrande = new Semaphore(50, true);
    private final Semaphore semPiscinaGrande0 = new Semaphore(0, true);

    private final BlockingQueue colaEntrarPiscinaGrande = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> piscinaGrande = new CopyOnWriteArrayList<>();

    private final CyclicBarrier barreraPiscinaGrande = new CyclicBarrier(2);
    private boolean accesoPermitido = false;
    
    public PiscinaGrande(JTextField monitorPiscinaGrande, JTextArea areaPiscinaGrande, JTextArea colaPiscinaGrande) {
        this.monitorPiscinaGrande = monitorPiscinaGrande;
        this.areaPiscinaGrande = areaPiscinaGrande;
        this.colaPiscinaGrande = colaPiscinaGrande;
    }

    public void entrarPiscinaGrande(Usuario u) {
        try {
            colaEntrarPiscinaGrande.put(u);
            imprimir(colaPiscinaGrande, colaEntrarPiscinaGrande.toString());
            semPiscinaGrande0.acquire();
            
            if( u.getEdad() > 10 && !u.getEsAcompañante() ) {
                try {
                    semPiscinaGrande.acquire();
                    barreraPiscinaGrande.await();
                    
                    piscinaGrande.add(u);
                    
                    imprimir(areaPiscinaGrande, piscinaGrande.toString());
                } catch(BrokenBarrierException ex) {
                    System.out.println("ERROR: " + ex);
                }
            } else {
                semPiscinaGrande.acquire();
                piscinaGrande.add(u);
                imprimir(areaPiscinaGrande, piscinaGrande.toString());
            }

        } catch(InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }
    }

    public void salirPiscinaGrande(Usuario u) {
        piscinaGrande.remove(u);
        imprimir(areaPiscinaGrande, piscinaGrande.toString());
        semPiscinaGrande.release();
    }

    public Usuario controlarPiscinaGrande() {
        Usuario u = null;
        try {
            u = (Usuario) colaEntrarPiscinaGrande.take();
            imprimir(colaPiscinaGrande, colaEntrarPiscinaGrande.toString());
            monitorPiscinaGrande.setText(u.toString());

        } catch(InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }

        return u;
    }

    public void controlarPiscinaGrande(Usuario u) {
        if( u.getEdad() <= 10 ) {
            try {
                semPiscinaGrande.acquire(2);
                semPiscinaGrande.release(2);

                accesoPermitido = true;
                semPiscinaGrande0.release();

                colaEntrarPiscinaGrande.take();
                imprimir(colaPiscinaGrande, colaEntrarPiscinaGrande.toString());
                semPiscinaGrande0.release();
            } catch (InterruptedException ex) {
                System.out.println("ERROR: " + ex);
            }
        } else {
            try {
                semPiscinaGrande.acquire();
                semPiscinaGrande.release();
                accesoPermitido = true;
                semPiscinaGrande0.release();
            } catch(InterruptedException ex) {
                Logger.getLogger(PiscinaOlas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        monitorPiscinaGrande.setText("");
    }

    private synchronized void imprimir(JTextArea campo, String contenido) {
        campo.setText(contenido);
    }

    public boolean isAccesoPermitido() {
        return accesoPermitido;
    }

    public void setAccesoPermitido(boolean accesoPermitido) {
        this.accesoPermitido = accesoPermitido;
    }

    public CyclicBarrier getBarrera() {
        return barreraPiscinaGrande;
    }
}