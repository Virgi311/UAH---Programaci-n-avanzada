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
    private final Semaphore semTumbonas = new Semaphore(20, true);
    private final Semaphore semTumbonas0 = new Semaphore(0, true);
    private final BlockingQueue colaEntrarTumbonas = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> tumbonas = new CopyOnWriteArrayList<>();
    
    private Usuario monitorTumbonasUsuario;
    private final FuncionesGenerales fg;
    private final Paso paso;
    
    public Tumbonas(JTextArea colaTumbonas, JTextArea areaTumbonas, JTextField monitorTumbonas, FuncionesGenerales fg, Paso paso) {
        this.colaTumbonas = colaTumbonas;
        this.areaTumbonas = areaTumbonas;
        this.monitorTumbonas = monitorTumbonas;
        
        this.monitorTumbonasUsuario = null;
        this.fg = fg;
        this.paso = paso;
    } // Cierre del método
       
    public boolean entrarTumbonas(Usuario u){
        paso.mirar();
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en la cola de entrada de las tumbonas.\n");
        colaEntrarTumbonas.add(u);
        fg.imprimir(colaTumbonas, colaEntrarTumbonas.toString());
        
        paso.mirar();
        try {
            semTumbonas0.acquire();
            
            if( !u.getAccesoPermitido() ) {
                return false;
            }
            
            paso.mirar();
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en las tumbonas. \n");
            tumbonas.add(u);
            fg.imprimir(areaTumbonas, tumbonas.toString());
        } catch( InterruptedException ex ) {
            System.out.println("ERROR: " + ex);
        }
    
        return true;
    } // Cierre del método
    
    public void salirTumbonas(Usuario u){
        paso.mirar();
        tumbonas.remove(u);
        fg.imprimir(areaTumbonas, tumbonas.toString());
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale de las tumbonas.\n");
        semTumbonas.release();
    } // Cierre del método
    
    public Usuario controlarTumbonas(){
        paso.mirar();
        try {
            Usuario u = (Usuario) colaEntrarTumbonas.take();
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en el monitor de las tumbonas. \n");
            fg.imprimir(colaTumbonas, colaEntrarTumbonas.toString());
            monitorTumbonas.setText(u.toString());
            monitorTumbonasUsuario = u;
            
            return u;
        } catch( InterruptedException ex ) {
            return null;
        } 
    } // Cierre del método
    
    public void controlarTumbonas(Usuario u) {
        paso.mirar();
        if( u.getEdad() < 15 || u.getEsAcompañante() ) {
            u.setAccesoPermitido(false);
        } else {
            try {
                semTumbonas.acquire();
            } catch(InterruptedException ex) {
                System.out.println("ERROR: " + ex);
            } 
        }
        
        competicion();
        
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale del monitor de las tumbonas. \n");
        monitorTumbonas.setText("");
        monitorTumbonasUsuario = null;
        
        paso.mirar();
        semTumbonas0.release();
    } // Cierre del método

    public void competicion() {
        try {
            int pelea = (int) ( ( colaEntrarTumbonas.size() - 1 ) * Math.random() );
            
            while( pelea > 0 ) {
                Usuario u = (Usuario) colaEntrarTumbonas.take();
                colaEntrarTumbonas.add(u);
                fg.imprimir(colaTumbonas, colaEntrarTumbonas.toString());
                pelea--;
            }
        } catch( InterruptedException ex ) {
            System.out.println("ERROR: " + ex);
        } 
    } // Cierre del método
    
    public CopyOnWriteArrayList<Usuario> getTumbonas() {
        return tumbonas;
    } // Cierre del método

    public Usuario getMonitorTumbonasUsuario() {
        return monitorTumbonasUsuario;
    } // Cierre del método

    public BlockingQueue getColaEntrarTumbonas() {
        return colaEntrarTumbonas;
    } // Cierre del método
} // Cierre de la clase