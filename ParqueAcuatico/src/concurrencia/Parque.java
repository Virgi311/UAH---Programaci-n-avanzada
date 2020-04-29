package concurrencia;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import hilos.Usuario;
import util.FuncionesGenerales;

/**
 * Clase Parque
 *
 * Define la forma y funcionamiento del parque
 *
 * @author
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Parque {
    //Elementos de la interfaz
    private final JTextField monitorVestuario;
    private final JTextArea colaEntrada;
    private final JTextField monitorPiscinaNiños;
    private final JTextField monitorPiscinaOlas;
    private final JTextField monitorPiscinaGrande;
    private final JTextField monitorTumbonas;
    private final JTextField monitorToboganA;
    private final JTextField monitorToboganB;
    private final JTextField monitorToboganC;
    //Concurrencia
    private final Semaphore semEntrarparque = new Semaphore(100, true);
    private final BlockingQueue colaEntrarParque = new LinkedBlockingQueue();
    private final Vestuario vestuario;
    private final PiscinaNiños piscinaNiños;
    private final PiscinaOlas piscinaOlas;
    private final PiscinaGrande piscinaGrande;
    private final Tumbonas tumbonas;
    private final Toboganes toboganes;
    
    private final FuncionesGenerales fg;
    private final Paso paso;
    private int menores;

    public Parque(JTextField monitorVestuario, JTextArea areaVestuario, JTextArea colaVestuario, JTextArea colaEntrada, JTextArea colaPiscinaNiños, JTextField monitorPiscinaNiños, JTextArea areaPiscinaNiños, JTextArea colaEsperaAdultos, JTextArea colaPiscinaOlas, JTextField monitorPiscinaOlas, JTextArea areaPiscinaOlas, JTextField monitorPiscinaGrande, JTextArea areaPiscinaGrande, JTextArea colaPiscinaGrande, JTextArea colaTumbonas, JTextArea areaTumbonas, JTextField monitorTumbonas, JTextField areaToboganA, JTextField areaToboganB, JTextField areaToboganC, JTextField monitorToboganA, JTextField monitorToboganB, JTextField monitorToboganC, JTextArea colaToboganes, FuncionesGenerales fg, Paso paso) {
        this.monitorVestuario = monitorVestuario;
        this.colaEntrada = colaEntrada;
        this.monitorPiscinaNiños = monitorPiscinaNiños;
        this.monitorPiscinaOlas = monitorPiscinaOlas;
        this.monitorPiscinaGrande = monitorPiscinaGrande;
        this.monitorTumbonas = monitorTumbonas;
        this.monitorToboganA = monitorToboganA;
        this.monitorToboganB = monitorToboganB;
        this.monitorToboganC = monitorToboganC;
        this.fg = fg;
        this.paso = paso;
        this.menores = 0;
        
        this.piscinaNiños = new PiscinaNiños(colaPiscinaNiños, monitorPiscinaNiños, areaPiscinaNiños, colaEsperaAdultos, fg, paso);
        this.piscinaOlas = new PiscinaOlas(monitorPiscinaOlas, areaPiscinaOlas, colaPiscinaOlas, fg, paso);
        this.piscinaGrande = new PiscinaGrande(monitorPiscinaGrande, areaPiscinaGrande, colaPiscinaGrande, fg, paso);
        this.tumbonas = new Tumbonas(colaTumbonas, areaTumbonas, monitorTumbonas, fg, paso);
        this.vestuario = new Vestuario (colaVestuario, monitorVestuario, areaVestuario, fg, paso);
        this.toboganes = new Toboganes(areaToboganA, areaToboganB, areaToboganC, monitorToboganA, monitorToboganB, monitorToboganC, colaToboganes, piscinaGrande, fg, paso);
    } // Cierre del método

    public void entrarParque(Usuario u) {
        try {
            colaEntrarParque.put(u);
            fg.imprimir(colaEntrada, colaEntrarParque.toString());
            
            semEntrarparque.acquire();
            colaEntrarParque.take();
            fg.imprimir(colaEntrada, colaEntrarParque.toString());
        } catch(InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }
    } // Cierre del método

    public void salirParque() {
        paso.mirar();
        semEntrarparque.release();
    } // Cierre del método
    
    public Vestuario getVestuario() {
        return vestuario;
    } // Cierre del método

    public PiscinaNiños getPiscinaNiños() {
        return piscinaNiños;
    } // Cierre del método

    public JTextField getMonitorVestuario() {
        return monitorVestuario;
    } // Cierre del método

    public JTextField getMonitorPiscinaNiños() {
        return monitorPiscinaNiños;
    } // Cierre del método

    public BlockingQueue getColaEntrarParque() {
        return colaEntrarParque;
    } // Cierre del método

    public PiscinaOlas getPiscinaOlas() {
        return piscinaOlas;
    } // Cierre del método

    public JTextField getMonitorPiscinaOlas() {
        return monitorPiscinaOlas;
    } // Cierre del método

    public PiscinaGrande getPiscinaGrande() {
        return piscinaGrande;
    } // Cierre del método

    public Tumbonas getTumbonas() {
        return tumbonas;
    } // Cierre del método

    public JTextField getMonitorPiscinaGrande() {
        return monitorPiscinaGrande;
    } // Cierre del método

    public JTextField getMonitorTumbonas() {
        return monitorTumbonas;
    }    // Cierre del método

    public Toboganes getToboganes() {
        return toboganes;
    } // Cierre del método
    
    public void setMenoresEntra() {
        this.menores++;
    } // Cierre del método
    
    public void setMenoresSale() {
        this.menores--;
    } // Cierre del método
    
    public int getMenores() {
        return menores;
    } // Cierre del método

    public JTextField getMonitorToboganA() {
        return monitorToboganA;
    } // Cierre del método

    public JTextField getMonitorToboganB() {
        return monitorToboganB;
    } // Cierre del método

    public JTextField getMonitorToboganC() {
        return monitorToboganC;
    } // Cierre del método
} // Cierre de la clase