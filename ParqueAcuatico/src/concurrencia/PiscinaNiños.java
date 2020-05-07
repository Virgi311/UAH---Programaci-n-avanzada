package concurrencia;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import hilos.Usuario;
import java.util.concurrent.BrokenBarrierException;
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
    
    private Usuario monitorPiscinaNiñosUsuario;
    private final FuncionesGenerales fg;
    private final Paso paso;
    
    public PiscinaNiños(JTextArea colaPiscinaNiños, JTextField monitorPiscinaNiños, JTextArea areaPiscinaNiños, JTextArea areaEsperaAdultos, FuncionesGenerales fg, Paso paso) {
        this.colaPiscinaNiños = colaPiscinaNiños;
        this.monitorPiscinaNiños = monitorPiscinaNiños;
        this.areaPiscinaNiños = areaPiscinaNiños;
        this.areaEsperaAdultos = areaEsperaAdultos;
        
        this.monitorPiscinaNiñosUsuario = null;
        this.fg = fg;
        this.paso = paso;
    } // Cierre del método
    
    public boolean entrarPiscinaNiños(Usuario u) {
        try {
            paso.mirar();
            
            if( u.getEdad() < 11 || u.getEsAcompañante() ) {
                try {
                    u.getBarrera().await();
                } catch( BrokenBarrierException | InterruptedException ex) {
                    System.out.println("ERROR: " + ex);
                }
            }
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en la cola de entrada de la piscina niños.\n");
            colaEntrarPiscinaNiños.put(u);
            fg.imprimir(colaPiscinaNiños, colaEntrarPiscinaNiños.toString());
            
            paso.mirar();
            semPiscinaNiños0.acquire();
            
            
            if( !u.getAccesoPermitido() ) {
                return false;
            }
            
            paso.mirar();
            if( u.getEdad() < 6 || ( u.getEsAcompañante() && u.getAcompañante().getEdad() < 6 ) ) { 
                //Se trata de un niño de 5 o menos años
                fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en la piscina de niños.\n");
                piscinaNiños.add(u);
                fg.imprimir(areaPiscinaNiños, piscinaNiños.toString());
            } else if( u.getEdad() < 11 ) { //se trata de un niño mayor de 5 años
                fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en la piscina de niños.\n");
                piscinaNiños.add(u);
                fg.imprimir(areaPiscinaNiños, piscinaNiños.toString());
            } else {  // se trata de un acompañante de un niño mayor de 5 años
                fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en la espera de adultos de la piscina de niños.\n");
                esperaAdultos.add(u);
                fg.imprimir(areaEsperaAdultos, esperaAdultos.toString());
            }
        } catch(InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }

        return true;
    } // Cierre del método
     
    public void salirPiscinaNiños(Usuario u) {
        paso.mirar();
        if( u.getEsAcompañante() && u.getAcompañante().getEdad() > 5 ) {
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale de la espera de adultos de la piscina de niños.\n");
            esperaAdultos.remove(u);
            fg.imprimir(areaEsperaAdultos, esperaAdultos.toString());
        } else {
            piscinaNiños.remove(u);
            fg.imprimir(areaPiscinaNiños, piscinaNiños.toString());
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale de la piscina de niños.\n");
            semPiscinaNiños.release();
        }
    } // Cierre del método
    
    
    public Usuario controlarPiscinaNiños() {
        try {
            paso.mirar();
            Usuario u = (Usuario) colaEntrarPiscinaNiños.take();
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en el monitor de la piscina de niños.\n");
            fg.imprimir(colaPiscinaNiños, colaEntrarPiscinaNiños.toString());
            monitorPiscinaNiños.setText(u.toString());
            monitorPiscinaNiñosUsuario = u;
            
            return u;
        } catch (InterruptedException ex) {
            return null;
        }
    } // Cierre del método

    
    public void controlarPiscinaNiños(Usuario u) {
        try {
            paso.mirar();
            if( u.getEdad() > 10 && !u.getEsAcompañante() ) {
                u.setAccesoPermitido(false);
            } else if( ( u.getEdad() > 5 && !u.getEsAcompañante() ) || ( u.getEsAcompañante() && u.getAcompañante().getEdad() < 5 ) ) {      
                semPiscinaNiños.acquire();
                buscarAcompañante(u.getAcompañante().toString());
            } else if( u.getEdad() < 5 ){
                semPiscinaNiños.acquire(2);
                buscarAcompañante(u.getAcompañante().toString());
                paso.mirar();
                semPiscinaNiños.release();
            }
                
            monitorPiscinaNiños.setText("");
            monitorPiscinaNiñosUsuario = null;
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale del monitor de la piscina de niños.\n");
            
            semPiscinaNiños0.release();
        } catch(InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }
    } // Cierre del método
    
    public void buscarAcompañante(String u) {
        if( colaEntrarPiscinaNiños.peek() != null && !colaEntrarPiscinaNiños.peek().toString().equals(u) ) {
            try {
                BlockingQueue cAux = new LinkedBlockingQueue();
                Usuario uAux = (Usuario) colaEntrarPiscinaNiños.take();
                Usuario uAuxEnc = null;
                cAux.put(uAux);
                while( colaEntrarPiscinaNiños.size() > 0 ) {
                    uAux = (Usuario) colaEntrarPiscinaNiños.take();
                    if( uAux.toString().equals(u) ) {
                        uAuxEnc = uAux;
                    } else {
                        cAux.put(uAux);
                    }
                }
                
                if( uAuxEnc != null ) {
                    colaEntrarPiscinaNiños.put(uAuxEnc);
                }
                
                while( cAux.size() > 0 ) {
                    uAux = (Usuario) cAux.take();
                    colaEntrarPiscinaNiños.put(uAux);
                }
            } catch(InterruptedException ex) {
                System.out.println("ERROR: " + ex);
            }
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

    public Usuario getMonitorPiscinaNiñosUsuario() {
        return monitorPiscinaNiñosUsuario;
    } // Cierre del método   
} // Cierre de la clase