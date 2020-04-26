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
    private final Semaphore semTumbonas = new Semaphore(20);
    private final Semaphore semTumbonas0 = new Semaphore(0, true);
    private final CopyOnWriteArrayList<Usuario> colaEntrarTumbonas = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Usuario> tumbonas = new CopyOnWriteArrayList<>();
    private final BlockingQueue colaMonitorTumbonas = new LinkedBlockingQueue();
    private boolean accesoPermitido = false;
    private final FuncionesGenerales fg;
    private final Paso paso;
    
    public Tumbonas(JTextArea colaTumbonas, JTextArea areaTumbonas, JTextField monitorTumbonas, FuncionesGenerales fg, Paso paso) {
        this.colaTumbonas = colaTumbonas;
        this.areaTumbonas = areaTumbonas;
        this.monitorTumbonas = monitorTumbonas;
        
        this.fg = fg;
        this.paso = paso;
    } // Cierre del método
       
    public boolean entrarTumbonas(Usuario u){
        if( u.getEdad() <= 10 || u.getEsAcompañante() ) {
            return false;
        }
        
        colaEntrarTumbonas.add(u);
        fg.imprimir(colaTumbonas, colaEntrarTumbonas.toString());
        try {
            semTumbonas.acquire();
            colaEntrarTumbonas.remove(u);
            fg.imprimir(colaTumbonas, colaEntrarTumbonas.toString());
            colaMonitorTumbonas.put(u);
            semTumbonas.acquire();
            if( !accesoPermitido ) {
                return false;
            }
            
            tumbonas.add(u);
            fg.imprimir(areaTumbonas, tumbonas.toString());
        } catch(InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }
    
        return true;
    } // Cierre del método
    
    public void salirTumbonas(Usuario u){
        tumbonas.remove(u);
        fg.imprimir(areaTumbonas, tumbonas.toString());
        paso.mirar();
        semTumbonas.release();
    } // Cierre del método
    
    public Usuario controlarTumbonas(){
        try {
            Usuario u = (Usuario) colaMonitorTumbonas.take();
            monitorTumbonas.setText(u.toString());
            
            return u;
        } catch (InterruptedException ex) {
            return null;
        } 
    } // Cierre del método
    
    public void controlarTumbonas(Usuario u){
        if( u.getEdad() >= 15 ) {
            accesoPermitido = true;
        } else {
            accesoPermitido = false;
        }
        paso.mirar();
        semTumbonas.release();
        monitorTumbonas.setText("");
    } // Cierre del método
    
    public boolean isAccesoPermitido() {
        return accesoPermitido;
    } // Cierre del método

    public void setAccesoPermitido(boolean accesoPermitido) {
        this.accesoPermitido = accesoPermitido;
    } // Cierre del método

    public CopyOnWriteArrayList<Usuario> getColaEntrarTumbonas() {
        return colaEntrarTumbonas;
    } // Cierre del método

    public CopyOnWriteArrayList<Usuario> getTumbonas() {
        return tumbonas;
    } // Cierre del método

    public BlockingQueue getColaMonitorTumbonas() {
        return colaMonitorTumbonas;
    } // Cierre del método
} // Cierre de la clase