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
    
    //Atributos extra
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
       
    //Metodo para entrar en las tumbonas
    public boolean entrarTumbonas(Usuario u){
        try {
            paso.mirar();
        
            //Barrera ciclica para que el niño y el acompañante entren juntos
            if( u.getEdad() < 11 || u.getEsAcompañante() ) {
                try {
                    u.getBarrera().await();
                } catch( BrokenBarrierException | InterruptedException ex) {
                    System.out.println("ERROR: " + ex);
                }
            }
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en la cola de entrada de las tumbonas.\n");
            colaEntrarTumbonas.put(u);
            fg.imprimir(colaTumbonas, colaEntrarTumbonas.toString());
        
            paso.mirar();
            semTumbonas0.acquire();
            
            //Si es rechazado por el monitor aqui se le expulsa de la piscina
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
    
    //Metodo para salir de la tumbonas
    public void salirTumbonas(Usuario u){
        paso.mirar();
        tumbonas.remove(u);
        fg.imprimir(areaTumbonas, tumbonas.toString());
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale de las tumbonas.\n");
        semTumbonas.release();
    } // Cierre del método
    
    //Metodo por el cual el monitor recoge a un usuario de la cola de entrada
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
    
    //Metodo por el cual el monitor decide por donde debe ir cada usuario
    public void controlarTumbonas(Usuario u) {
        paso.mirar();
        if( u.getEdad() < 15 || u.getEsAcompañante() ) {
            //Si tiene menos de 15 años o es acompañante
            u.setAccesoPermitido(false);
        } else {
            try {
                semTumbonas.acquire();
            } catch(InterruptedException ex) {
                System.out.println("ERROR: " + ex);
            } 
        }
        
        //Metodo que reordena la cola de entrada para simular laa lucha de todos los usuarios por conseguir entrar
        //competicion();
        
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale del monitor de las tumbonas. \n");
        monitorTumbonas.setText("");
        monitorTumbonasUsuario = null;
        
        paso.mirar();
        semTumbonas0.release();
    } // Cierre del método

    //Metodo que reordena la cola de entrada para simular laa lucha de todos los usuarios por conseguir entrar
    /*public void competicion() {
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
    */
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