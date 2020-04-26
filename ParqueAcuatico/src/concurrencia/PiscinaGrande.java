package concurrencia;

import hilos.Usuario;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import util.FuncionesGenerales;

/**
 * Clase PiscinaGrande
 *
 * Define la forma y funcionamiento de la piscina grande
 *
 * @author
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class PiscinaGrande {
    //Elementos de la interfaz
    private final JTextField monitorPiscinaGrande;
    private final JTextArea areaPiscinaGrande;
    private final JTextArea colaPiscinaGrande;
    //Concurrencia
    private final Semaphore semPiscinaGrande = new Semaphore(50, true);
    private final BlockingQueue colaEntrarPiscinaGrande = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> piscinaGrande = new CopyOnWriteArrayList<>();
    private final Paso paso;
    private final FuncionesGenerales fg;
    
    public PiscinaGrande(JTextField monitorPiscinaGrande, JTextArea areaPiscinaGrande, JTextArea colaPiscinaGrande, FuncionesGenerales fg, Paso paso) {
        this.monitorPiscinaGrande = monitorPiscinaGrande;
        this.areaPiscinaGrande = areaPiscinaGrande;
        this.colaPiscinaGrande = colaPiscinaGrande;
        
        this.fg = fg;
        this.paso = paso;
    } // Cierre del método

    public void entrarPiscinaGrande(Usuario u) {
        try {
            colaEntrarPiscinaGrande.put(u);
            fg.imprimir(colaPiscinaGrande, colaEntrarPiscinaGrande.toString());
            
            if(u.getEdad() < 11){
                semPiscinaGrande.acquire(2);
                semPiscinaGrande.release(1);
            } else{
                semPiscinaGrande.acquire();
            }
            
            colaEntrarPiscinaGrande.take();
            fg.imprimir(colaPiscinaGrande, colaEntrarPiscinaGrande.toString());
            
            piscinaGrande.add(u);
            fg.imprimir(areaPiscinaGrande, piscinaGrande.toString());
        } catch (InterruptedException ex) {
            System.out.println("ERROR:" + ex);
        }
    } // Cierre del método

    public void salirPiscinaGrande(Usuario u) {
        piscinaGrande.remove(u);
        fg.imprimir(areaPiscinaGrande, piscinaGrande.toString());
        semPiscinaGrande.release();
    } // Cierre del método
    
    public Usuario monitorExpulsa(){
        int pos = (int) ( piscinaGrande.size() * Math.random() );
        Usuario u = piscinaGrande.get(pos);
        if(u.getEsAcompañante()){
            Usuario ua = u.getAcompañante();
            monitorPiscinaGrande.setText(ua.toString());
            return ua;
        }
        monitorPiscinaGrande.setText(u.toString());
        return u;
    } // Cierre del método
    
    public void monitorExpulsa(Usuario u){
        monitorPiscinaGrande.setText("");
        u.interrupt();  
    } // Cierre del método
    
    public boolean excesoAforo(){
        int numPersonas = piscinaGrande.size();
        if( numPersonas == 50 ) {
            return true;
        }
        
        return false;
    } // Cierre del método
    
    public void entrarPorTobogan(Usuario u){
        piscinaGrande.add(u);
        fg.imprimir(areaPiscinaGrande, piscinaGrande.toString());
    } // Cierre del método

    public BlockingQueue getColaEntrarPiscinaGrande() {
        return colaEntrarPiscinaGrande;
    } // Cierre del método

    public CopyOnWriteArrayList<Usuario> getPiscinaGrande() {
        return piscinaGrande;
    } // Cierre del método
} // Cierre de la clase


