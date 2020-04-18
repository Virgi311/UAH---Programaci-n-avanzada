package concurrencia;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import hilos.Usuario;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authores 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class PiscinaNiños {
    
    private final JTextArea colaPiscinaNiños;
    private final JTextField monitorPiscinaNiños;
    private final JTextArea areaPiscinaNiños;
    private final JTextArea areaEsperaAdultos;
    
    private final Semaphore semPiscinaNiños = new Semaphore(15, true);
    private final Semaphore semPiscinaNiños0 = new Semaphore(0, true);
    
    private final BlockingQueue colaEntrarPiscinaNiños = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> piscinaNiños = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Usuario> esperaAdultos = new CopyOnWriteArrayList<>();
    
    private final CyclicBarrier barreraPiscinaNiños = new CyclicBarrier(2);
    private boolean accesoPermitido = false;
    
    public PiscinaNiños(JTextArea colaPiscinaNiños, JTextField monitorPiscinaNiños, JTextArea areaPiscinaNiños, JTextArea areaEsperaAdultos) {
        this.colaPiscinaNiños = colaPiscinaNiños;
        this.monitorPiscinaNiños = monitorPiscinaNiños;
        this.areaPiscinaNiños = areaPiscinaNiños;
        this.areaEsperaAdultos = areaEsperaAdultos;
    }
    
    public boolean entrarPiscinaNiños(Usuario u) {
        try {
            colaEntrarPiscinaNiños.put(u);
            imprimir(colaPiscinaNiños, colaEntrarPiscinaNiños.toString());
            semPiscinaNiños0.acquire();
            
            if( !accesoPermitido ) {
                return false;
            }
            
            if( u.getEdad() <= 5 || (u.getEsAcompañante() && u.getAcompañante().getEdad() <= 5) ) { 
                semPiscinaNiños.acquire();
                piscinaNiños.add(u);
                imprimir(areaPiscinaNiños, piscinaNiños.toString());
            } else if( u.getEdad() <= 10 ) {
                semPiscinaNiños.acquire();
                piscinaNiños.add(u);
                imprimir(areaPiscinaNiños, piscinaNiños.toString());
            } else { 
                esperaAdultos.add(u);
                imprimir(areaEsperaAdultos, esperaAdultos.toString());
            }

        } catch(InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }

        return true;
    }
     
    public void salirPiscinaNiños(Usuario u) {
        if( u.getEsAcompañante() && u.getAcompañante().getEdad() > 5 ) {
            esperaAdultos.remove(u);
            imprimir(areaEsperaAdultos, esperaAdultos.toString());
        } else {
            piscinaNiños.remove(u);
            imprimir(areaPiscinaNiños, piscinaNiños.toString());
            semPiscinaNiños.release();
        }
    }
    
    
    public Usuario controlarPiscinaNiños() {
        try {
            Usuario u = (Usuario) colaEntrarPiscinaNiños.take();
            imprimir(colaPiscinaNiños, colaEntrarPiscinaNiños.toString());
            monitorPiscinaNiños.setText(u.toString());

            return u;
        } catch (InterruptedException ex) {
            return null;
        }
    }

    
    public void controlarPiscinaNiños(Usuario u) {
        try {
            if( u.getEdad() > 10 && !u.getEsAcompañante() ) {
                accesoPermitido = false;
                semPiscinaNiños0.release();
            } else if( u.getEsAcompañante() ) {
                accesoPermitido = true;
                semPiscinaNiños0.release();
            } else if( u.getEdad() > 5 ) {
                accesoPermitido = true;       
                semPiscinaNiños.acquire();
                semPiscinaNiños.release();
                semPiscinaNiños0.release();
            } else{
                accesoPermitido = true;
                semPiscinaNiños.acquire(2);
                semPiscinaNiños.release(2);
                semPiscinaNiños0.release();
            }

            monitorPiscinaNiños.setText("");
        } catch(InterruptedException ex) {
            Logger.getLogger(PiscinaNiños.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        return barreraPiscinaNiños;
    }
}