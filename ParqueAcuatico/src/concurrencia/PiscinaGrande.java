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
import java.util.Random;

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

    private final BlockingQueue colaEntrarPiscinaGrande = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> piscinaGrande = new CopyOnWriteArrayList<>();
    
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
            imprimir(colaPiscinaGrande, colaEntrarPiscinaGrande.toString());
            if(u.getEdad() < 11){
                semPiscinaGrande.acquire(2);
                semPiscinaGrande.release(1);
            } else{
                semPiscinaGrande.acquire();
            }
            colaEntrarPiscinaGrande.take();
            imprimir(colaPiscinaGrande, colaEntrarPiscinaGrande.toString());
            piscinaGrande.add(u);
            imprimir(areaPiscinaGrande, piscinaGrande.toString());

        } catch (InterruptedException ex) {
        }
    }

    public void salirPiscinaGrande(Usuario u) {
        piscinaGrande.remove(u);
        imprimir(areaPiscinaGrande, piscinaGrande.toString());
        semPiscinaGrande.release();
    }
    
    public Usuario monitorExpulsa(){
        if(piscinaGrande.isEmpty()){
            return null;
        }
        int pos = getNumAleatorio(piscinaGrande.size());
        Usuario u = piscinaGrande.get(pos);
        if(u.getEsAcompañante()){
            return null;
        }
        monitorPiscinaGrande.setText(u.toString());
        return u;
    }
    
    public void monitorExpulsa(Usuario u){
        
        monitorPiscinaGrande.setText("");
        u.interrupt();
        
    }
    
    public void haySitioEnPiscina(){
        try {
            semPiscinaGrande.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(PiscinaGrande.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void entrarPorTobogan(Usuario u){
        piscinaGrande.add(u);
        imprimir(areaPiscinaGrande, piscinaGrande.toString());
    }


    private synchronized void imprimir(JTextArea campo, String contenido) {
        campo.setText(contenido);
    }
    
    private int getNumAleatorio(int max) {
        Random aleatoriedad = new Random(System.currentTimeMillis());
        return aleatoriedad.nextInt(max);
    }
}



