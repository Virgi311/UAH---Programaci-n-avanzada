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
 * Clase PiscinaNiños
 *
 * Define la forma y funcionamiento de la piscina de niños
 *
 * @author
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class PiscinaNiños {
    //Elementos de la interfaz
    private final JTextArea colaPiscinaNiños;
    private final JTextField monitorPiscinaNiños;
    private final JTextArea areaPiscinaNiños;
    private final JTextArea areaEsperaAdultos;
    
    //Concurrencia
    private final Semaphore semPiscinaNiños = new Semaphore(15, true);
    private final Semaphore semPiscinaNiños0 = new Semaphore(0, true);  
    private final BlockingQueue colaEntrarPiscinaNiños = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> piscinaNiños = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Usuario> esperaAdultos = new CopyOnWriteArrayList<>();
    
    private boolean accesoPermitido = false;
    private final FuncionesGenerales fg;
    private final Paso paso;
    
    public PiscinaNiños(JTextArea colaPiscinaNiños, JTextField monitorPiscinaNiños, JTextArea areaPiscinaNiños, JTextArea areaEsperaAdultos, FuncionesGenerales fg, Paso paso) {
        this.colaPiscinaNiños = colaPiscinaNiños;
        this.monitorPiscinaNiños = monitorPiscinaNiños;
        this.areaPiscinaNiños = areaPiscinaNiños;
        this.areaEsperaAdultos = areaEsperaAdultos;
        
        this.fg = fg;
        this.paso = paso;
    } // Cierre del método
    
    public boolean entrarPiscinaNiños(Usuario u) {
        try {
            colaEntrarPiscinaNiños.put(u);
            fg.imprimir(colaPiscinaNiños, colaEntrarPiscinaNiños.toString());
            
            semPiscinaNiños0.acquire();
            
            if( !accesoPermitido ) {
                return false;
            }
            
            if( u.getEdad() <= 5 || (u.getEsAcompañante() && u.getAcompañante().getEdad() <= 5) ) { 
                //Se trata de un niño de 5 o menos o de un acompañante de un niño de 5 o menos
                semPiscinaNiños.acquire();
                piscinaNiños.add(u);
                fg.imprimir(areaPiscinaNiños, piscinaNiños.toString());
            } else if( u.getEdad() <= 10 ) { //se trata de un niño mayor de 5 años
                semPiscinaNiños.acquire();
                piscinaNiños.add(u);
                fg.imprimir(areaPiscinaNiños, piscinaNiños.toString());
            } else {  // se trata de un acompañante de un niño mayor de 5 años
                esperaAdultos.add(u);
                fg.imprimir(areaEsperaAdultos, esperaAdultos.toString());
            }

        } catch(InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }

        return true;
    } // Cierre del método
     
    public void salirPiscinaNiños(Usuario u) {
        if( u.getEsAcompañante() && u.getAcompañante().getEdad() > 5 ) {
            esperaAdultos.remove(u);
            fg.imprimir(areaEsperaAdultos, esperaAdultos.toString());
        } else {
            piscinaNiños.remove(u);
            fg.imprimir(areaPiscinaNiños, piscinaNiños.toString());
            paso.mirar();
            semPiscinaNiños.release();
        }
    } // Cierre del método
    
    
    public Usuario controlarPiscinaNiños() {
        try {
            Usuario u = (Usuario) colaEntrarPiscinaNiños.take();
            fg.imprimir(colaPiscinaNiños, colaEntrarPiscinaNiños.toString());
            monitorPiscinaNiños.setText(u.toString());

            return u;
        } catch (InterruptedException ex) {
            return null;
        }
    } // Cierre del método

    
    public void controlarPiscinaNiños(Usuario u) {
        try {
            if( u.getEdad() > 10 && !u.getEsAcompañante() ) {
                accesoPermitido = false;
                paso.mirar();
                semPiscinaNiños0.release();
            } else if( u.getEsAcompañante() ) {
                accesoPermitido = true;
                paso.mirar();
                semPiscinaNiños0.release();
            } else if( u.getEdad() > 5 ) {
                accesoPermitido = true;       
                semPiscinaNiños.acquire();
                paso.mirar();
                semPiscinaNiños.release();
                paso.mirar();
                semPiscinaNiños0.release();
            } else{
                accesoPermitido = true;
                semPiscinaNiños.acquire(2);
                paso.mirar();
                semPiscinaNiños.release(2);
                paso.mirar();
                semPiscinaNiños0.release();
            }

            monitorPiscinaNiños.setText("");
        } catch(InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }
    } // Cierre del método
    
    public BlockingQueue getColaEntrarPiscinaNiños() {
        return colaEntrarPiscinaNiños;
    } // Cierre del método

    public CopyOnWriteArrayList<Usuario> getPiscinaNiños() {
        return piscinaNiños;
    } // Cierre del método

    public CopyOnWriteArrayList<Usuario> getEsperaAdultos() {
        return esperaAdultos;
    } // Cierre del método    
} // Cierre de la clase