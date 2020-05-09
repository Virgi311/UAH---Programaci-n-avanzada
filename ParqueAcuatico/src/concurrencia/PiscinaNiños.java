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
    
    //Atributos extra
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
    
    //Metodo para entrar en la piscina de niños
    public boolean entrarPiscinaNiños(Usuario u) {
        try {
            paso.mirar();
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " esta en la entrada de la piscina niños.\n");
            colaEntrarPiscinaNiños.put(u);
            fg.imprimir(colaPiscinaNiños, colaEntrarPiscinaNiños.toString());
            
            paso.mirar();
            semPiscinaNiños0.acquire();
            
            //Si es rechazado por el monitor aqui se le expulsa de la piscina
            if( !u.getAccesoPermitido() ) {
                return false;
            }
            
            paso.mirar();
            if( u.getEdad() < 11 || ( u.getEsAcompañante() && u.getAcompañante().getEdad() < 6 ) ) { 
                //Se trata de un niño o un acompañante de un niño de 5 o menos años
                fg.writeDebugFile("Usuario: " + u.getCodigo() + " entra en la piscina de niños.\n");
                piscinaNiños.add(u);
                fg.imprimir(areaPiscinaNiños, piscinaNiños.toString());
            } else {  
                //Se trata de un acompañante de un niño mayor de 5 años
                fg.writeDebugFile("Usuario: " + u.getCodigo() + " entra en la espera de adultos de la piscina de niños.\n");
                esperaAdultos.add(u);
                fg.imprimir(areaEsperaAdultos, esperaAdultos.toString());
            }
        } catch(InterruptedException ex) {
            return false;
        }

        return true;
    } // Cierre del método
     
    //Metodo para salir de la piscina de niños
    public void salirPiscinaNiños(Usuario u) {
        paso.mirar();
        if( u.getEsAcompañante() && u.getAcompañante().getEdad() > 5 ) {
            //Se trata de un acompañante de un niño de mas de 5 años
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale de la espera de adultos de la piscina de niños.\n");
            esperaAdultos.remove(u);
            fg.imprimir(areaEsperaAdultos, esperaAdultos.toString());
        } else {
            //Se trata de un niño o un acompañante de un niño de 5 o menos años
            piscinaNiños.remove(u);
            fg.imprimir(areaPiscinaNiños, piscinaNiños.toString());
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale de la piscina de niños.\n");
            semPiscinaNiños.release();
        }
    } // Cierre del método
    
    //Metodo para que el monitor recoja a una persona de la cola de entrada
    public Usuario controlarPiscinaNiños() {
        try {
            paso.mirar();
            Usuario u = (Usuario) colaEntrarPiscinaNiños.take();
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " es atendido por el monitor de la piscina de niños.\n");
            fg.imprimir(colaPiscinaNiños, colaEntrarPiscinaNiños.toString());
            monitorPiscinaNiños.setText(u.toString());
            monitorPiscinaNiñosUsuario = u;
            
            return u;
        } catch (InterruptedException ex) {
            return null;
        }
    } // Cierre del método

    //Metodo para que el monitor decida a donde debe ir cada usuario
    public void controlarPiscinaNiños(Usuario u) {
        try {
            paso.mirar();
            if( u.getEdad() > 10 && !u.getEsAcompañante() ) {
                //Si tiene mas de 10 años y no es un acompañante
                u.setAccesoPermitido(false);
            } else if( ( u.getEdad() > 5 && !u.getEsAcompañante() ) || ( u.getEsAcompañante() && u.getAcompañante().getEdad() < 6 ) ) {  
                //Si es un acompañante de un niño de 5 años o menos o si es un niño de maas de 5 años
                semPiscinaNiños.acquire();
            } else if( u.getEdad() < 6 ){
                //Si es unniño de 5 años o menos adquiere dos permisos para liberar el que coge el acompañante
                semPiscinaNiños.acquire(2);
                paso.mirar();
                semPiscinaNiños.release();
            }
                
            monitorPiscinaNiños.setText("");
            monitorPiscinaNiñosUsuario = null;
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " finaliza la atencion del monitor de la piscina de niños.\n");
            
            paso.mirar();
            semPiscinaNiños0.release();
        } catch(InterruptedException ex) {
            System.out.println("ERROR: " + ex);
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