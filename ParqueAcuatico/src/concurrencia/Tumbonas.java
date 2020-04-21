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
 *
 * @authores 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Tumbonas {
    private final JTextArea colaTumbonas;
    private final JTextArea areaTumbonas;
    private final JTextField monitorTumbonas;

    private boolean accesoPermitido = false;
    
    private final Semaphore semTumbonas = new Semaphore(20);
    private final Semaphore semTumbonas0 = new Semaphore(0, true);
    private final CopyOnWriteArrayList<Usuario> colaEntrarTumbonas = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Usuario> tumbonas = new CopyOnWriteArrayList<>();
    private final BlockingQueue colaMonitorTumbonas = new LinkedBlockingQueue();
    
    private final FuncionesGenerales fg;
    
    public Tumbonas(JTextArea colaTumbonas, JTextArea areaTumbonas, JTextField monitorTumbonas, FuncionesGenerales fg) {
        this.colaTumbonas = colaTumbonas;
        this.areaTumbonas = areaTumbonas;
        this.monitorTumbonas = monitorTumbonas;
        
        this.fg = fg;
    }
       
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
    }
    
    public void salirTumbonas(Usuario u){
        tumbonas.remove(u);
        fg.imprimir(areaTumbonas, tumbonas.toString());
        semTumbonas.release();
    }
    
    public Usuario controlarTumbonas(){
        try {
            Usuario u = (Usuario) colaMonitorTumbonas.take();
            monitorTumbonas.setText(u.toString());
            
            return u;
        } catch (InterruptedException ex) {
            return null;
        } 
    }
    
    public void controlarTumbonas(Usuario u){
        if( u.getEdad() >= 15 ) {
            accesoPermitido = true;
        } else {
            accesoPermitido = false;
        }
        
        semTumbonas.release();
        monitorTumbonas.setText("");
    }
    
    public boolean isAccesoPermitido() {
        return accesoPermitido;
    }

    public void setAccesoPermitido(boolean accesoPermitido) {
        this.accesoPermitido = accesoPermitido;
    }
}