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
 *
 * @authors 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Toboganes {
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
    
    public Toboganes(JTextField areaToboganA, JTextField areaToboganB, JTextField areaToboganC, JTextField monitorToboganA, JTextField monitorToboganB, JTextField monitorToboganC, JTextArea colaToboganes, PiscinaGrande piscinaGrande, FuncionesGenerales fg, Paso paso ) {
        this.areaToboganA = areaToboganA;
        this.areaToboganB = areaToboganB;
        this.areaToboganC = areaToboganC;
        this.monitorToboganA = monitorToboganA;
        this.monitorToboganB = monitorToboganB;
        this.monitorToboganC = monitorToboganC;
        this.colaToboganes = colaToboganes;
        this.piscinaGrande = piscinaGrande;
        this.fg = fg;
        this.paso = paso;
    }
    
    private final CopyOnWriteArrayList<Usuario> colaEntrarToboganes = new CopyOnWriteArrayList<>();

    private String toboganA;
    private String toboganB;
    private String toboganC;

    private final BlockingQueue colaToboganA = new LinkedBlockingQueue();
    private final BlockingQueue colaToboganB = new LinkedBlockingQueue();
    private final BlockingQueue colaToboganC = new LinkedBlockingQueue();

    private final Semaphore semToboganA = new Semaphore(1, true);
    private final Semaphore semToboganB = new Semaphore(1, true);
    private final Semaphore semToboganC = new Semaphore(1, true);

    private final Semaphore semToboganA0 = new Semaphore(0, true);
    private final Semaphore semToboganB0 = new Semaphore(0, true);
    private final Semaphore semToboganC0 = new Semaphore(0, true);

    public boolean entrarToboganes(Usuario u) {
        try {
            if (u.getEdad() < 11 || u.getEsAcompañante()) {
                return false;
            }
            colaEntrarToboganes.add(u);
            fg.imprimir(colaToboganes, colaEntrarToboganes.toString());
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

            piscinaGrande.haySitioEnPiscina();

            colaEntrarToboganes.remove(u);
            fg.imprimir(colaToboganes, colaEntrarToboganes.toString());

            if (u.getEdad() < 15) {
                toboganA = u.toString();
                areaToboganA.setText(toboganA);

            } else if (u.getEdad() < 18) {
                toboganB = u.toString();
                areaToboganB.setText(toboganB);
            } else {
                toboganC = u.toString();
                areaToboganC.setText(toboganC);
            }

        } catch (InterruptedException ex) {
            return false;
        }
        return true;
    }

    public void toboganApiscinaGrande(Usuario u) {
        if (u.getEdad() < 15) {

            toboganA = "";
            areaToboganA.setText("");
            semToboganA.release();

        } else if (u.getEdad() < 18) {
            toboganB = "";
            areaToboganB.setText("");
            semToboganB.release();

        } else {
            toboganC = "";
            areaToboganC.setText("");
            semToboganC.release();

        }

        piscinaGrande.entrarPorTobogan(u);

        nadarPiscina(3000, 5000, u);

        piscinaGrande.salirPiscinaGrande(u);

    }

    private void nadarPiscina(int min, int max, Usuario u) {
        try {
            Thread.sleep(min + (int) ((max - min) * Math.random()));
        } catch (InterruptedException ex) {
            piscinaGrande.salirPiscinaGrande(u);
        }
    }

    public Usuario monitorToboganA() {

        try {
            Usuario u = (Usuario) colaToboganA.take();
            monitorToboganA.setText(u.toString());

            return u;
        } catch (InterruptedException ex) {
            return null;
        }

    }

    public void monitorToboganA(Usuario u) {

        monitorToboganA.setText("");
        int edad = u.getEdad();

        if (edad < 15) {
            semToboganA0.release();
        }

    }

    public Usuario monitorToboganB() {

        try {
            Usuario u = (Usuario) colaToboganB.take();
            monitorToboganB.setText(u.toString());
            return u;
        } catch (InterruptedException ex) {
            return null;
        }

    }

    public void monitorToboganB(Usuario u) {

        monitorToboganB.setText("");
        int edad = u.getEdad();

        if (edad < 18) {
            semToboganB0.release();
        }

    }

    public Usuario monitorToboganC() {

        try {
            Usuario u = (Usuario) colaToboganC.take();
            monitorToboganC.setText(u.toString());
            return u;
        } catch (InterruptedException ex) {
            return null;
        }

    }

    public void monitorToboganC(Usuario u) {

        monitorToboganC.setText("");
        int edad = u.getEdad();

        if (edad >= 18) {
            semToboganC0.release();
        }

    }

    public String getToboganA() {
        return toboganA;
    }

    public void setToboganA(String toboganA) {
        this.toboganA = toboganA;
    }

    public String getToboganB() {
        return toboganB;
    }

    public void setToboganB(String toboganB) {
        this.toboganB = toboganB;
    }

    public String getToboganC() {
        return toboganC;
    }

    public void setToboganC(String toboganC) {
        this.toboganC = toboganC;
    }

    
}
