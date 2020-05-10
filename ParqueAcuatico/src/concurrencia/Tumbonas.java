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
    private final Semaphore semTumbonas = new Semaphore(20);
    private final CopyOnWriteArrayList<Usuario> colaEntrarTumbonas = new CopyOnWriteArrayList<>();
    private final BlockingQueue colaEntrarTumbonasMonitor = new LinkedBlockingQueue();
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
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " entra en la cola de entrada de las tumbonas.\n");
            colaEntrarTumbonas.add(u);
            fg.imprimir(colaTumbonas, colaEntrarTumbonas.toString());
            
            /* Si es un acompañante hacemos que espere en su cyclicbarrier hasta que el niño sea atendido por el monitor
             * Si no intentaran coger un permiso del semaforo no justo y se introduciran en la cola ficticia del semaforo
             */
            if( u.getEsAcompañante() ) {
                try {
                    u.getBarrera().await();
                } catch(BrokenBarrierException ex) {
                    System.out.println("ERROR: " + ex);
                }
            } else {
                paso.mirar();
                semTumbonas.acquire();
                colaEntrarTumbonasMonitor.put(u);
            }
            //Llegado a este punto todos los usuarios ya sean acompañantes o no, cogen el permiso de su semaforo de 0 permisos a la espera de ser atendidos por el monitor
            u.getSemUsu().acquire();
            
            //Si es rechazado por el monitor aqui se le expulsa de la piscina y si es un niño libera el permiso adquirido
            if( !u.getAccesoPermitido() ) {
                if( u.getEdad() < 15 ) {
                    semTumbonas.release();
                }
                return false;
            }
            
            paso.mirar();
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " entra en las tumbonas. \n");
            tumbonas.add(u);
            fg.imprimir(areaTumbonas, tumbonas.toString());
        } catch( InterruptedException ex ) {
            return false;
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
            Usuario u = (Usuario) colaEntrarTumbonasMonitor.take();
            
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " es atendido por el monitor de las tumbonas. \n");
            colaEntrarTumbonas.remove(u);
            fg.imprimir(colaTumbonas, colaEntrarTumbonas.toString());
            monitorTumbonas.setText(u.toString());
            monitorTumbonasUsuario = u;
            
            /* Si el usuario es un niño que lleva un acompañante, realizamos toda la atencion del monitor al niño en esta funcion
             * Para despues liberarlo y recoger al usuario acompañante para liberarlo en la funcion controlarTumbonas(Usuario)
             * De esta manera evitamos expulsar a un niño y que este tenga que esperar al acompañante fuera de la atraccion un tiempo indeterminado
             */
            if( u.getEdad() < 11 ) {
                try {
                    u.getBarrera().await();
                } catch(BrokenBarrierException ex) {
                    System.out.println("ERROR: " + ex);
                }
                
                fg.dormir(500, 900);
                
                u.setAccesoPermitido(false);
                
                fg.writeDebugFile("Usuario: " + u.getCodigo() + " finaliza la atencion del monitor de las tumbonas. \n");
                monitorTumbonas.setText("");
                monitorTumbonasUsuario = null;
        
                paso.mirar();
                u.getSemUsu().release();
                
                //Una vez finalizado el niño, cogemos a su acompañante y le atendemos de la misma forma para expulsarlo y que el niño no espere de manera indeterminada
                fg.writeDebugFile("Usuario: " + u.getAcompañante().getCodigo() + " es atendido por el monitor de las tumbonas. \n");
                colaEntrarTumbonas.remove(u.getAcompañante());
                fg.imprimir(colaTumbonas, colaEntrarTumbonas.toString());
                monitorTumbonas.setText(u.getAcompañante().toString());
                monitorTumbonasUsuario = u.getAcompañante();
                return u.getAcompañante();
            }
            
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
        }
        
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " finaliza la atencion del monitor de las tumbonas. \n");
        monitorTumbonas.setText("");
        monitorTumbonasUsuario = null;
        
        paso.mirar();
        u.getSemUsu().release();
    } // Cierre del método
    
    public CopyOnWriteArrayList<Usuario> getTumbonas() {
        return tumbonas;
    } // Cierre del método

    public Usuario getMonitorTumbonasUsuario() {
        return monitorTumbonasUsuario;
    } // Cierre del método

    public CopyOnWriteArrayList<Usuario> getColaEntrarTumbonas() {
        return colaEntrarTumbonas;
    } // Cierre del método
} // Cierre de la clase