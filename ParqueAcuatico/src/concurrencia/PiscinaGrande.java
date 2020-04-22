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
 *
 * @authors 
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
    
    private final FuncionesGenerales fg;
    private final Paso paso;
    
    public PiscinaGrande(JTextField monitorPiscinaGrande, JTextArea areaPiscinaGrande, JTextArea colaPiscinaGrande, FuncionesGenerales fg, Paso paso) {
        this.monitorPiscinaGrande = monitorPiscinaGrande;
        this.areaPiscinaGrande = areaPiscinaGrande;
        this.colaPiscinaGrande = colaPiscinaGrande;
        
        this.fg = fg;
        this.paso = paso;
    }

    public void entrarPiscinaGrande(Usuario u) {
        try {
            colaEntrarPiscinaGrande.put(u);
            fg.imprimir(colaPiscinaGrande, colaEntrarPiscinaGrande.toString());
            
            semPiscinaGrande0.acquire();
            
            if( u.getEdad() > 10 && !u.getEsAcompañante() ) {
                try {
                    semPiscinaGrande.acquire();
                    barreraPiscinaGrande.await();
                    
                    piscinaGrande.add(u);
                    
                    fg.imprimir(areaPiscinaGrande, piscinaGrande.toString());
                } catch(BrokenBarrierException ex) {
                    System.out.println("ERROR: " + ex);
                }
            } else {
                semPiscinaGrande.acquire();
                piscinaGrande.add(u);
                fg.imprimir(areaPiscinaGrande, piscinaGrande.toString());
            }
        } catch(InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }
    }

    public void salirPiscinaGrande(Usuario u) {
        piscinaGrande.remove(u);
        fg.imprimir(areaPiscinaGrande, piscinaGrande.toString());
        paso.mirar();
        semPiscinaGrande.release();
    }

    public Usuario controlarPiscinaGrande() {
        Usuario u = null;
        try {
            u = (Usuario) colaEntrarPiscinaGrande.take();
            fg.imprimir(colaPiscinaGrande, colaEntrarPiscinaGrande.toString());
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
                paso.mirar();
                semPiscinaGrande.release(2);

                accesoPermitido = true;
                paso.mirar();
                semPiscinaGrande0.release();

                colaEntrarPiscinaGrande.take();
                fg.imprimir(colaPiscinaGrande, colaEntrarPiscinaGrande.toString());
                paso.mirar();
                semPiscinaGrande0.release();
            } catch (InterruptedException ex) {
                System.out.println("ERROR: " + ex);
            }
        } else {
            try {
                semPiscinaGrande.acquire();
                paso.mirar();
                semPiscinaGrande.release();
                
                accesoPermitido = true;
                
                paso.mirar();
                semPiscinaGrande0.release();
            } catch(InterruptedException ex) {
                System.out.println("ERROR:" + ex);
            }
        }
        monitorPiscinaGrande.setText("");
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