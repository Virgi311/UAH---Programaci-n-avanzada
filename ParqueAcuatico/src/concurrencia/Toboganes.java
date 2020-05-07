package concurrencia;

import hilos.Usuario;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
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
    private final PiscinaGrande piscinaGrande;
    private final FuncionesGenerales fg;
    private final Paso paso;
    
    //Concurrencia
    private final CopyOnWriteArrayList<Usuario> colaEntrarToboganes = new CopyOnWriteArrayList<>();
    private final BlockingQueue colaToboganA = new LinkedBlockingQueue();
    private final BlockingQueue colaToboganB = new LinkedBlockingQueue();
    private final BlockingQueue colaToboganC = new LinkedBlockingQueue();
    private final Semaphore semToboganA = new Semaphore(1, true);
    private final Semaphore semToboganB = new Semaphore(1, true);
    private final Semaphore semToboganC = new Semaphore(1, true);
    private final Semaphore semToboganA0 = new Semaphore(0, true);
    private final Semaphore semToboganB0 = new Semaphore(0, true);
    private final Semaphore semToboganC0 = new Semaphore(0, true);
    
    private Usuario toboganAUsuario;
    private Usuario toboganBUsuario;
    private Usuario toboganCUsuario;
    private Usuario monitorToboganAUsuario;
    private Usuario monitorToboganBUsuario;
    private Usuario monitorToboganCUsuario;
    
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

    public boolean entrarToboganes(Usuario u) {
        try {
            paso.mirar();
            
            if( u.getEdad() < 11 || u.getEsAcompañante() ) {
                try {
                    u.getBarrera().await();
                } catch( BrokenBarrierException | InterruptedException ex) {
                    System.out.println("ERROR: " + ex);
                }
            }
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en la cola de los toboganes.\n");
            colaEntrarToboganes.add(u);
            fg.imprimir(colaToboganes, colaEntrarToboganes.toString());
            
            paso.mirar();
            if( u.getEdad() < 15 || u.getEsAcompañante() ) {
                colaToboganA.put(u);
                semToboganA0.acquire();
            } else if( u.getEdad() < 18 ) {
                colaToboganB.put(u);
                semToboganB0.acquire();
            } else {
                colaToboganC.put(u);
                semToboganC0.acquire();
            }
            
            if( !u.getAccesoPermitido() ) {
                return false;
            }
            
            if( u.getEdad() < 15 || u.getEsAcompañante() ) {
                toboganAUsuario = u;
                areaToboganA.setText(toboganAUsuario.toString());
                fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en el tobogan A.\n");
            } else if( u.getEdad() < 18 ) {
                toboganBUsuario = u;
                areaToboganB.setText(toboganBUsuario.toString());
                fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en el tobogan B.\n");
            } else {
                toboganCUsuario = u;
                areaToboganC.setText(toboganCUsuario.toString());
                fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en el tobogan C.\n");
            }
        } catch (InterruptedException ex) {
            return false;
        }
        return true;
    } // Cierre del método

    public void AccesoPiscinaGrande(Usuario u) {
        if( piscinaGrande.excesoAforo() ){
            Usuario usuario = piscinaGrande.monitorExpulsa();
            fg.dormir(500, 1000);
            piscinaGrande.monitorExpulsa(usuario);
        } else {
            piscinaGrande.cogerSitioPiscina();
        }
        
        paso.mirar();
        if( u.getEdad() < 15 || u.getEsAcompañante() ) {
            toboganAUsuario = null;
            areaToboganA.setText("");
            semToboganA.release();
        } else if( u.getEdad() < 18 ) {
            toboganBUsuario = null;
            areaToboganB.setText("");
            semToboganB.release();
        } else {
            toboganCUsuario = null;
            areaToboganC.setText("");
            semToboganC.release();
        }

        paso.mirar();
        piscinaGrande.accesoDesdeTobogan(u);

        fg.dormir(3000, 5000);

        paso.mirar();
        piscinaGrande.salirPiscinaGrande(u);

    } // Cierre del método

    public Usuario monitorToboganA() {
        paso.mirar();
        try {
            Usuario u = (Usuario) colaToboganA.take();
            colaEntrarToboganes.remove(u);
            fg.imprimir(colaToboganes, colaEntrarToboganes.toString());
            
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en el monitor del tobogan A. \n");
            monitorToboganA.setText(u.toString());
            monitorToboganAUsuario = u;
            
            return u;
        } catch (InterruptedException ex) {
            return null;
        }
    } // Cierre del método

    public void monitorToboganA(Usuario u) {
        if( u.getEdad() < 11 || u.getEsAcompañante() ) {
            u.setAccesoPermitido(false);
        } else {
            paso.mirar();
            try {
            semToboganA.acquire();
            } catch(InterruptedException ex) {
                System.out.println("ERROR: " + ex);
            }
        }
            
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale del monitor del tobogan A. \n");
        monitorToboganA.setText("");
        monitorToboganAUsuario = null;
        
        paso.mirar();
        semToboganA0.release();

    } // Cierre del método

    public Usuario monitorToboganB() {
        paso.mirar();
        try {
            Usuario u = (Usuario) colaToboganB.take();
            colaEntrarToboganes.remove(u);
            fg.imprimir(colaToboganes, colaEntrarToboganes.toString());
            
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en el monitor del tobogan B. \n");
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
        
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale del monitor del tobogan B. \n");
        monitorToboganB.setText("");
        monitorToboganBUsuario = null;
        
        paso.mirar();
        semToboganB0.release();
    } // Cierre del método

    public Usuario monitorToboganC() {
        paso.mirar();
        try {
            Usuario u = (Usuario) colaToboganC.take();
            colaEntrarToboganes.remove(u);
            fg.imprimir(colaToboganes, colaEntrarToboganes.toString());
            
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en el monitor del tobogan C. \n");
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
        
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale del monitor del tobogan C. \n");
        monitorToboganC.setText("");
        monitorToboganCUsuario = null;
        
        paso.mirar();
        semToboganC0.release();
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
    }

    public Usuario getMonitorToboganBUsuario() {
        return monitorToboganBUsuario;
    }

    public Usuario getMonitorToboganCUsuario() {
        return monitorToboganCUsuario;
    }

    public Usuario getToboganAUsuario() {
        return toboganAUsuario;
    }

    public Usuario getToboganBUsuario() {
        return toboganBUsuario;
    }

    public Usuario getToboganCUsuario() {
        return toboganCUsuario;
    }
} // Cierre de la clase
