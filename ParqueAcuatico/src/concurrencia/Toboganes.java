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
 * Clase Toboganes
 *
 * Define la forma y funcionamiento de los toboganes
 *
 * @author 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Toboganes {
    //Elementos de la interfaz
    private final JTextField areaToboganA;
    private final JTextField areaToboganB;
    private final JTextField areaToboganC;
    private final JTextField monitorToboganA;
    private final JTextField monitorToboganB;
    private final JTextField monitorToboganC;
    private final JTextArea colaToboganes;
    
    //Concurrencia
    private final CopyOnWriteArrayList<Usuario> colaEntrarToboganes = new CopyOnWriteArrayList<>();
    private final BlockingQueue colaToboganA = new LinkedBlockingQueue();
    private final BlockingQueue colaToboganB = new LinkedBlockingQueue();
    private final BlockingQueue colaToboganC = new LinkedBlockingQueue();
    private final Semaphore semToboganA = new Semaphore(1, true);
    private final Semaphore semToboganB = new Semaphore(1, true);
    private final Semaphore semToboganC = new Semaphore(1, true);
    private Usuario toboganAUsuario;
    private Usuario toboganBUsuario;
    private Usuario toboganCUsuario;
    private Usuario monitorToboganAUsuario;
    private Usuario monitorToboganBUsuario;
    private Usuario monitorToboganCUsuario;
    
    //Atributos extra
    private final PiscinaGrande piscinaGrande;
    private final FuncionesGenerales fg;
    private final Paso paso;
    
    public Toboganes(JTextField areaToboganA, JTextField areaToboganB, JTextField areaToboganC, JTextField monitorToboganA, JTextField monitorToboganB, JTextField monitorToboganC, JTextArea colaToboganes, PiscinaGrande piscinaGrande, FuncionesGenerales fg, Paso paso ) {
        this.areaToboganA = areaToboganA;
        this.areaToboganB = areaToboganB;
        this.areaToboganC = areaToboganC;
        this.monitorToboganA = monitorToboganA;
        this.monitorToboganB = monitorToboganB;
        this.monitorToboganC = monitorToboganC;
        this.colaToboganes = colaToboganes;
        this.piscinaGrande = piscinaGrande;
        
        this.toboganAUsuario = null;
        this.toboganBUsuario = null;
        this.toboganCUsuario = null;
                        
        this.monitorToboganAUsuario = null;
        this.monitorToboganBUsuario = null;
        this.monitorToboganCUsuario = null;
        this.fg = fg;
        this.paso = paso;
    } // Cierre del método

    //Metodo para entrar a los toboganes
    public boolean entrarToboganes(Usuario u) {
        try {
            paso.mirar();
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " entra en la cola de los toboganes.\n");
            colaEntrarToboganes.add(u);
            fg.imprimir(colaToboganes, colaEntrarToboganes.toString());
            
            paso.mirar();
            //Situamos a los usuarios en las colas ficticias de cada tobogan
            if( u.getEdad() < 15 || u.getEsAcompañante() ) {
                //Si tiene menos de 15 años o es un acompañante
                colaToboganA.put(u);
                u.getSemUsu().acquire();
            } else if( u.getEdad() < 18 ) {
                //Si tiene menos de 18 años
                colaToboganB.put(u);
                u.getSemUsu().acquire();
            } else {
                //Mayores de 18 años
                colaToboganC.put(u);
                u.getSemUsu().acquire();
            }
            
            //Si es rechazado por el monitor aqui se le expulsa de la piscina
            if( !u.getAccesoPermitido() ) {
                return false;
            }
            
            //Logica para situar a los usuarios en sus toboganes
            if( u.getEdad() < 15 || u.getEsAcompañante() ) {
                //Si tiene menos de 15 años o es acompañante (Aunque es innecesario el acompañante debido a que el monitor lo expulsa lo dejamos completo para posibles modificaciones)
                toboganAUsuario = u;
                areaToboganA.setText(toboganAUsuario.toString());
                fg.writeDebugFile("Usuario: " + u.getCodigo() + " entra en el tobogan A.\n");
            } else if( u.getEdad() < 18 ) {
                //Si tiene menos de 18 años
                toboganBUsuario = u;
                areaToboganB.setText(toboganBUsuario.toString());
                fg.writeDebugFile("Usuario: " + u.getCodigo() + " entra en el tobogan B.\n");
            } else {
                //Mayores de 18 años
                toboganCUsuario = u;
                areaToboganC.setText(toboganCUsuario.toString());
                fg.writeDebugFile("Usuario: " + u.getCodigo() + " entra en el tobogan C.\n");
            }
        } catch (InterruptedException ex) {
            return false;
        }
        return true;
    } // Cierre del método

    //Metodo para acceder a la piscina grande a traves del tobogan
    public void accesoPiscinaGrande(Usuario u) {
        paso.mirar();
        if( u.getEdad() < 15 || u.getEsAcompañante() ) {
            //Si es menor de 15 años o un acompañante
            toboganAUsuario = null;
            areaToboganA.setText("");
            semToboganA.release();
        } else if( u.getEdad() < 18 ) {
            //Si es menos de 18 años
            toboganBUsuario = null;
            areaToboganB.setText("");
            semToboganB.release();
        } else {
            //Si es mayor de 18 años
            toboganCUsuario = null;
            areaToboganC.setText("");
            semToboganC.release();
        }

        paso.mirar();
        //Accedemos a la piscina grande
        piscinaGrande.accesoDesdeTobogan(u);

        fg.dormir(3000, 5000);

        paso.mirar();
        //Salimos de la piscina grande
        piscinaGrande.salirPiscinaGrande(u);

    } // Cierre del método

    /* Metodos por los que cama monitor recoge a un usuario de la acola
     * Hay uno por cada tobogan y retorna un usuario
     */
    public Usuario monitorToboganA() {
        paso.mirar();
        try {
            Usuario u = (Usuario) colaToboganA.take();
            colaEntrarToboganes.remove(u);
            fg.imprimir(colaToboganes, colaEntrarToboganes.toString());
            
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " es atendido por el monitor del tobogan A. \n");
            monitorToboganA.setText(u.toString());
            monitorToboganAUsuario = u;
            
            return u;
        } catch (InterruptedException ex) {
            return null;
        }
    } // Cierre del método

    /* Metodos para que cada monitor decida por donde a de ir cada usuario
     * Hay uno por cada tobogan y recibe el usuario como parametro
     */
    public void monitorToboganA(Usuario u) {
        if( u.getEdad() < 11 || u.getEsAcompañante() ) {
            //Si tiene menos de 10 años o es un acompañante
            u.setAccesoPermitido(false);
        } else {
            paso.mirar();
            try {
                semToboganA.acquire();
            } catch(InterruptedException ex) {
                System.out.println("ERROR: " + ex);
            }
            
            //Comprueba si el aforo de la piscina grande esta completo dado que va a acceder ahi
            if( piscinaGrande.excesoAforo() ){
                //Si esta lleno expulsa a un usuario
                Usuario usuario = piscinaGrande.monitorExpulsa();
                fg.dormir(500, 1000);
                piscinaGrande.monitorExpulsa(usuario);
            } else {
                //Si no simplemente adquiere un permiso
                piscinaGrande.cogerSitioPiscina();
            }
        }
            
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " finaliza la atencion del monitor del tobogan A. \n");
        monitorToboganA.setText("");
        monitorToboganAUsuario = null;
        
        paso.mirar();
        u.getSemUsu().release();

    } // Cierre del método

    public Usuario monitorToboganB() {
        paso.mirar();
        try {
            Usuario u = (Usuario) colaToboganB.take();
            colaEntrarToboganes.remove(u);
            fg.imprimir(colaToboganes, colaEntrarToboganes.toString());
            
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " es atendido por el monitor del tobogan B. \n");
            monitorToboganB.setText(u.toString());
            monitorToboganBUsuario = u;
            
            return u;
        } catch (InterruptedException ex) {
            return null;
        }

    } // Cierre del método

    public void monitorToboganB(Usuario u) {
        paso.mirar();
        try {
            semToboganB.acquire();
        } catch(InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }
        
        //Comprueba si el aforo de la piscina grande esta completo dado que va a acceder ahi
        if( piscinaGrande.excesoAforo() ){
            //Si esta lleno expulsa a un usuario
            Usuario usuario = piscinaGrande.monitorExpulsa();
            fg.dormir(500, 1000);
            piscinaGrande.monitorExpulsa(usuario);
        } else {
            //Si no simplemente adquiere un permiso
            piscinaGrande.cogerSitioPiscina();
        }
        
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " finaliza la atencion del monitor del tobogan B. \n");
        monitorToboganB.setText("");
        monitorToboganBUsuario = null;
        
        paso.mirar();
        u.getSemUsu().release();
    } // Cierre del método

    public Usuario monitorToboganC() {
        paso.mirar();
        try {
            Usuario u = (Usuario) colaToboganC.take();
            colaEntrarToboganes.remove(u);
            fg.imprimir(colaToboganes, colaEntrarToboganes.toString());
            
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " es atendido por el monitor del tobogan C. \n");
            monitorToboganC.setText(u.toString());
            monitorToboganCUsuario = u;
            
            return u;
        } catch (InterruptedException ex) {
            return null;
        }
    } // Cierre del método

    public void monitorToboganC(Usuario u) {
        paso.mirar();
        try {
            semToboganC.acquire();
        } catch(InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }
        
        //Comprueba si el aforo de la piscina grande esta completo dado que va a acceder ahi
        if( piscinaGrande.excesoAforo() ){
            //Si esta lleno expulsa a un usuario
            Usuario usuario = piscinaGrande.monitorExpulsa();
            fg.dormir(500, 1000);
            piscinaGrande.monitorExpulsa(usuario);
        } else {
            //Si no simplemente adquiere un permiso
            piscinaGrande.cogerSitioPiscina();
        }
            
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " finaliza la atencion del monitor del tobogan C. \n");
        monitorToboganC.setText("");
        monitorToboganCUsuario = null;
        
        paso.mirar();
        u.getSemUsu().release();
    } // Cierre del método

    public CopyOnWriteArrayList<Usuario> getColaEntrarToboganes() {
        return colaEntrarToboganes;
    } // Cierre del método

    public BlockingQueue getColaToboganA() {
        return colaToboganA;
    } // Cierre del método

    public BlockingQueue getColaToboganB() {
        return colaToboganB;
    } // Cierre del método

    public BlockingQueue getColaToboganC() {
        return colaToboganC;
    } // Cierre del método

    public Usuario getMonitorToboganAUsuario() {
        return monitorToboganAUsuario;
    } // Cierre del método

    public Usuario getMonitorToboganBUsuario() {
        return monitorToboganBUsuario;
    } // Cierre del método

    public Usuario getMonitorToboganCUsuario() {
        return monitorToboganCUsuario;
    } // Cierre del método

    public Usuario getToboganAUsuario() {
        return toboganAUsuario;
    } // Cierre del método

    public Usuario getToboganBUsuario() {
        return toboganBUsuario;
    } // Cierre del método

    public Usuario getToboganCUsuario() {
        return toboganCUsuario;
    } // Cierre del método
} // Cierre de la clase
