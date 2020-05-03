package concurrencia;

import hilos.Usuario;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
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
    private final Semaphore semPiscinaGrande0 = new Semaphore(0, true);
    private final BlockingQueue colaEntrarPiscinaGrande = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> piscinaGrande = new CopyOnWriteArrayList<>();
    
    private Usuario monitorPiscinaGrandeUsuario;
    private final Paso paso;
    private final FuncionesGenerales fg;
    
    public PiscinaGrande(JTextField monitorPiscinaGrande, JTextArea areaPiscinaGrande, JTextArea colaPiscinaGrande, FuncionesGenerales fg, Paso paso) {
        this.monitorPiscinaGrande = monitorPiscinaGrande;
        this.areaPiscinaGrande = areaPiscinaGrande;
        this.colaPiscinaGrande = colaPiscinaGrande;
        
        this.monitorPiscinaGrandeUsuario = null;
        this.fg = fg;
        this.paso = paso;
    } // Cierre del método

    public void entrarPiscinaGrande(Usuario u) {
        try {
            paso.mirar();
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en la cola de la entrada a la piscina grande.\n");
            colaEntrarPiscinaGrande.put(u);
            fg.imprimir(colaPiscinaGrande, colaEntrarPiscinaGrande.toString());
            
            paso.mirar();
            semPiscinaGrande0.acquire();
            
            if( u.getEdad() < 11 ) {
                paso.mirar();
                semPiscinaGrande.acquire(2);
                paso.mirar();
                semPiscinaGrande.release(1);
            } else {
                paso.mirar();
                semPiscinaGrande.acquire();
            }
            
            paso.mirar();
            piscinaGrande.add(u);
            fg.imprimir(areaPiscinaGrande, piscinaGrande.toString());
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " entro a la piscina grande.\n");
        } catch( InterruptedException ex ) {
            System.out.println("ERROR:" + ex);
        }
    } // Cierre del método

    public void salirPiscinaGrande(Usuario u) {
        paso.mirar();
        piscinaGrande.remove(u);
        fg.imprimir(areaPiscinaGrande, piscinaGrande.toString());
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale de la piscina grande.\n");
        paso.mirar();
        semPiscinaGrande.release();
    } // Cierre del método
    
    public Usuario controlarPiscinaGrande() {
        try {
            paso.mirar();
            Usuario u = (Usuario) colaEntrarPiscinaGrande.take();
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en el monitor de la piscina grande.\n");
            fg.imprimir(colaPiscinaGrande, colaEntrarPiscinaGrande.toString());
            monitorPiscinaGrande.setText(u.toString());
            monitorPiscinaGrandeUsuario = u;

            return u;
        } catch( InterruptedException ex ) {
            return null;
        }
    } // Cierre del método
    
    public void controlarPiscinaGrande( Usuario usuario ) {
        try {
            monitorPiscinaGrande.setText("");
            monitorPiscinaGrandeUsuario = null;
            fg.writeDebugFile("Usuario: " + usuario.getCodigo() + " sale del monitor de la piscina grande.\n");
            paso.mirar();
            semPiscinaGrande.acquire();
            paso.mirar();
            semPiscinaGrande.release();
            paso.mirar();
            semPiscinaGrande0.release();
        } catch( InterruptedException ex ) {
            System.out.println("ERROR: " + ex);
        }
    } // Cierre del método
    
    public Usuario monitorExpulsa(){
        int pos = (int) ( piscinaGrande.size() * Math.random() );
        paso.mirar();
        Usuario u = piscinaGrande.get(pos);
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " es expulsado para dar espacio al usuario del tobogan.\n");
        if(u.getEsAcompañante()){
            Usuario ua = u.getAcompañante();
            piscinaGrande.remove(ua);
            monitorPiscinaGrande.setText(ua.toString());
            monitorPiscinaGrandeUsuario = ua;
            
            return ua;
        }
        monitorPiscinaGrande.setText(u.toString());
        monitorPiscinaGrandeUsuario = u;
        
        return u;
    } // Cierre del método
    
    public void monitorExpulsa(Usuario u){
        if( u.getEsAcompañante() ) {
            monitorPiscinaGrande.setText("");
            monitorPiscinaGrandeUsuario = null;
            paso.mirar();
            u.interrupt();
            
            piscinaGrande.remove(u.getAcompañante());
            monitorPiscinaGrande.setText(u.getAcompañante().toString());
            monitorPiscinaGrandeUsuario = u.getAcompañante();
            fg.writeDebugFile("Usuario: " +  u.getCodigo() + " su acompañante es: " + u.getAcompañante().getCodigo() + " es expulsado tambien.\n");
            paso.mirar();
            fg.dormir(500, 1000);
            monitorPiscinaGrande.setText("");
            monitorPiscinaGrandeUsuario = null;
            paso.mirar();
            u.getAcompañante().interrupt();
            semPiscinaGrande.release();
        } else {
            monitorPiscinaGrande.setText("");
            monitorPiscinaGrandeUsuario = null;
            paso.mirar();
            u.interrupt();  
        }
    } // Cierre del método
    
    public boolean excesoAforo(){
        int numPersonas = piscinaGrande.size();
        return numPersonas == 50;
    } // Cierre del método
    
    public void cogerSitioPiscina() {
        try {
            paso.mirar();
            semPiscinaGrande.acquire();
        } catch( InterruptedException ex ) {
            System.out.println("ERROR: " + ex);
        }
    } // Cierre del método
        
    public void accesoDesdeTobogan(Usuario u){
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " accede desde el tobogan a la piscina grande.\n");
        piscinaGrande.add(u);
        fg.imprimir(areaPiscinaGrande, piscinaGrande.toString());
    } // Cierre del método

    public BlockingQueue getColaEntrarPiscinaGrande() {
        return colaEntrarPiscinaGrande;
    } // Cierre del método

    public CopyOnWriteArrayList<Usuario> getPiscinaGrande() {
        return piscinaGrande;
    } // Cierre del método

    public Usuario getMonitorPiscinaGrandeUsuario() {
        return monitorPiscinaGrandeUsuario;
    } // Cierre del método
} // Cierre de la clase


