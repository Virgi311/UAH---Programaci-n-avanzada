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
    
    private String toboganA;
    private String toboganB;
    private String toboganC;
    
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
        
        this.toboganA = "";
        this.toboganB = "";
        this.toboganC = "";
        this.toboganAUsuario = null;
        this.toboganBUsuario = null;
        this.toboganCUsuario = null;
                        
        this.monitorToboganCUsuario = null;
        this.monitorToboganCUsuario = null;
        this.monitorToboganCUsuario = null;
        this.fg = fg;
        this.paso = paso;
    } // Cierre del método

    public boolean entrarToboganes(Usuario u) {
        try {
            if (u.getEdad() < 11 || u.getEsAcompañante()) {
                return false;
            }
            paso.mirar();
            colaEntrarToboganes.add(u);
            fg.imprimir(colaToboganes, colaEntrarToboganes.toString());
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en la cola de los toboganes.\n");
            
            paso.mirar();
            if (u.getEdad() < 15) {
                semToboganA.acquire();
                colaToboganA.put(u);
                semToboganA0.acquire();
            } else if (u.getEdad() < 18) {
                semToboganB.acquire();
                colaToboganB.put(u);
                semToboganB0.acquire();
            } else {
                semToboganC.acquire();
                colaToboganC.put(u);
                semToboganC0.acquire();
            }
            
            paso.mirar();
            colaEntrarToboganes.remove(u);
            fg.imprimir(colaToboganes, colaEntrarToboganes.toString());

            if (u.getEdad() < 15) {
                toboganA = u.toString();
                toboganAUsuario = u;
                areaToboganA.setText(toboganA);
                fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en el tobogan A.\n");
            } else if (u.getEdad() < 18) {
                toboganB = u.toString();
                toboganBUsuario = u;
                areaToboganB.setText(toboganB);
                fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en el tobogan B.\n");
            } else {
                toboganC = u.toString();
                toboganCUsuario = u;
                areaToboganC.setText(toboganC);
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
        if (u.getEdad() < 15) {
            toboganA = "";
            toboganAUsuario = null;
            areaToboganA.setText("");
            semToboganA.release();
        } else if (u.getEdad() < 18) {
            toboganB = "";
            toboganBUsuario = null;
            areaToboganB.setText("");
            semToboganB.release();
        } else {
            toboganC = "";
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
            monitorToboganA.setText(u.toString());
            monitorToboganAUsuario = u;
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en el monitor del tobogan A. \n");
            return u;
        } catch (InterruptedException ex) {
            return null;
        }
    } // Cierre del método

    public void monitorToboganA(Usuario u) {
        paso.mirar();
        monitorToboganA.setText("");
        monitorToboganAUsuario = null;
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale del monitor del tobogan A. \n");
        int edad = u.getEdad();

        if (edad < 15) {
            semToboganA0.release();
        }

    } // Cierre del método

    public Usuario monitorToboganB() {
        paso.mirar();
        try {
            Usuario u = (Usuario) colaToboganB.take();
            monitorToboganB.setText(u.toString());
            monitorToboganBUsuario = u;
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en el monitor del tobogan B. \n");
            return u;
        } catch (InterruptedException ex) {
            return null;
        }

    } // Cierre del método

    public void monitorToboganB(Usuario u) {
        paso.mirar();
        monitorToboganB.setText("");
        monitorToboganBUsuario = null;
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale del monitor del tobogan B. \n");
        int edad = u.getEdad();

        if (edad < 18) {
            semToboganB0.release();
        }
    } // Cierre del método

    public Usuario monitorToboganC() {
        paso.mirar();
        try {
            Usuario u = (Usuario) colaToboganC.take();
            monitorToboganC.setText(u.toString());
            monitorToboganCUsuario = u;
            fg.writeDebugFile("Usuario: " + u.getCodigo() + " se coloca en el monitor del tobogan C. \n");
            return u;
        } catch (InterruptedException ex) {
            return null;
        }
    } // Cierre del método

    public void monitorToboganC(Usuario u) {
        paso.mirar();
        monitorToboganC.setText("");
        monitorToboganCUsuario = null;
        fg.writeDebugFile("Usuario: " + u.getCodigo() + " sale del monitor del tobogan C. \n");
        int edad = u.getEdad();

        if (edad >= 18) {
            semToboganC0.release();
        }
    } // Cierre del método

    public String getToboganA() {
        return toboganA;
    } // Cierre del método

    public void setToboganA(String toboganA) {
        this.toboganA = toboganA;
    } // Cierre del método

    public String getToboganB() {
        return toboganB;
    } // Cierre del método

    public void setToboganB(String toboganB) {
        this.toboganB = toboganB;
    } // Cierre del método

    public String getToboganC() {
        return toboganC;
    } // Cierre del método

    public void setToboganC(String toboganC) {
        this.toboganC = toboganC;
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
