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
    private final Semaphore semMonitorPiscinaGrande = new Semaphore(1, true);
    private final BlockingQueue colaEntrarPiscinaGrande = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> piscinaGrande = new CopyOnWriteArrayList<>();
    
    //Atributos extra
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

    //Metodo para entrar en la piscina grande
    public void entrarPiscinaGrande(Usuario u) {
        try {
            paso.mirar();
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " esta en la entrada a la piscina grande.\n");
            colaEntrarPiscinaGrande.put(u);
            fg.imprimir(colaPiscinaGrande, colaEntrarPiscinaGrande.toString());
            
            paso.mirar();
            u.getSemUsu().acquire();
            
            paso.mirar();
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " entra a la piscina grande.\n");
            piscinaGrande.add(u);
            fg.imprimir(areaPiscinaGrande, piscinaGrande.toString());
        } catch( InterruptedException ex ) {
            System.out.println("ERROR:" + ex);
        }
    } // Cierre del método

    //Metodo para salir de la piscina grande
    public void salirPiscinaGrande(Usuario u) {
        paso.mirar();
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale de la piscina grande.\n");
        piscinaGrande.remove(u);
        fg.imprimir(areaPiscinaGrande, piscinaGrande.toString());
        
        paso.mirar();
        semPiscinaGrande.release();
    } // Cierre del método
    
    //Metodo para que el monitor recoja a un usuario de la cola de entrada
    public Usuario controlarPiscinaGrande() {
        try {
            //Si el aforo esta completo se expulsa aleatoriamente a alguna persona
            if( excesoAforo() ) {
                Usuario usuario = monitorExpulsa();
                fg.dormir(500, 1000);
                monitorExpulsa(usuario);
            }
            
            paso.mirar();
            semMonitorPiscinaGrande.acquire();
            paso.mirar();
            Usuario u = (Usuario) colaEntrarPiscinaGrande.take();
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " es atendido el monitor de la piscina grande.\n");
            fg.imprimir(colaPiscinaGrande, colaEntrarPiscinaGrande.toString());
            
            monitorPiscinaGrande.setText(u.toString());
            monitorPiscinaGrandeUsuario = u;

            return u;
        } catch( InterruptedException ex ) {
            return null;
        }
    } // Cierre del método
    
    //Metodo que evalua los permisos que va a coger el usuario
    public void controlarPiscinaGrande( Usuario u ) {
        paso.mirar();
        try {
            if( u.getEdad() < 11 ) {
                //Si es un niño coge dos y libera uno para que el acompañante lo coja
                semPiscinaGrande.acquire(2);
                paso.mirar();
                semPiscinaGrande.release();
            } else {
                semPiscinaGrande.acquire();
            }
        } catch( InterruptedException ex ) {
            System.out.println("ERROR: " + ex);
        }  
        
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " finaliza la atencion del monitor de la piscina grande.\n");
        monitorPiscinaGrande.setText("");
        monitorPiscinaGrandeUsuario = null;
        
        paso.mirar();
        u.getSemUsu().release();
        paso.mirar();
        semMonitorPiscinaGrande.release();
    } // Cierre del método
    
    //Metodo para expulsar a un usuario de la piscina grande si esta completamente llena
    public Usuario monitorExpulsa(){
        int pos = (int) ( ( piscinaGrande.size() * Math.random() ) - 1 );
        paso.mirar();
        Usuario u = piscinaGrande.get(pos);
        
        try {
            semMonitorPiscinaGrande.acquire();
            u.getSemUsu().acquire();
        } catch( InterruptedException ex ) {
            System.out.println("ERROR: " + ex);
        }
        
        if(u.getEsAcompañante()){
            Usuario ua = u.getAcompañante();
            
            fg.writeDebugFile("Usuario: " + ua.getCodigo() + " es expulsado para dar espacio al usuario del tobogan.\n");
            piscinaGrande.remove(ua);
            monitorPiscinaGrande.setText(ua.toString());
            monitorPiscinaGrandeUsuario = ua;
            
            return ua;
        }
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " es expulsado para dar espacio al usuario del tobogan.\n");
        
        piscinaGrande.remove(u);
        monitorPiscinaGrande.setText(u.toString());
        monitorPiscinaGrandeUsuario = u;
        
        return u;
    } // Cierre del método
    
    //Metodo que expulsa al usuario aleatorio
    public void monitorExpulsa(Usuario u){
        if( u.getEdad() < 11 ) {
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
            semPiscinaGrande.release(2);
        } else {
            monitorPiscinaGrande.setText("");
            monitorPiscinaGrandeUsuario = null;
            
            paso.mirar();
            u.interrupt();  
            semPiscinaGrande.release();
        }
        paso.mirar();
        u.getSemUsu().release();
        paso.mirar();
        semMonitorPiscinaGrande.release();
    } // Cierre del método
    
    //Metodo para que el usuario del tobogan adquiera un permiso
    public void cogerSitioPiscina() {
        try {
            semPiscinaGrande.acquire();
        } catch(InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }
    }
    
    //Metodo para comprobar si no hay sitio en la piscina grande para un usuario del tobogan
    public boolean excesoAforo(){
        return semPiscinaGrande.availablePermits() == 0; //Devuelve true si no hay permisos para conceder
    } // Cierre del método
        
    //Metodo para que el usuario del tobogan entre en la piscina y salga del tobogan
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


