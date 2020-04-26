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
    private final JTextArea areaVestuario;
    private final JTextArea colaVestuario;
    private final JTextArea colaEntrada;
        
    private final JTextArea colaPiscinaNiños;
    private final JTextField monitorPiscinaNiños;
    private final JTextArea areaPiscinaNiños;
    private final JTextArea colaEsperaAdultos;
    
    private final JTextArea colaPiscinaOlas;
    private final JTextField monitorPiscinaOlas;
    private final JTextArea areaPiscinaOlas;

    private final JTextField monitorPiscinaGrande;
    private final JTextArea areaPiscinaGrande;
    private final JTextArea colaPiscinaGrande;
    
    private final JTextArea colaTumbonas;
    private final JTextArea areaTumbonas;
    private final JTextField monitorTumbonas;
    
    private final JTextField areaToboganA;
    private final JTextField areaToboganB;
    private final JTextField areaToboganC;
    private final JTextField monitorToboganA;
    private final JTextField monitorToboganB;
    private final JTextField monitorToboganC;
    private final JTextArea colaToboganes;
    
    //Concurrencia
    private final Semaphore semEntrarparque = new Semaphore(100, true);
    private final BlockingQueue colaEntrarParque = new LinkedBlockingQueue();
    
    private Vestuario vestuario;
    private PiscinaNiños piscinaNiños;
    private PiscinaOlas piscinaOlas;
    private PiscinaGrande piscinaGrande;
    private Tumbonas tumbonas;
    private Toboganes toboganes;
    
    private final FuncionesGenerales fg;
    private final Paso paso;
    private int menores;

    public Parque(JTextField monitorVestuario, JTextArea areaVestuario, JTextArea colaVestuario, JTextArea colaEntrada, JTextArea colaPiscinaNiños, JTextField monitorPiscinaNiños, JTextArea areaPiscinaNiños, JTextArea colaEsperaAdultos, JTextArea colaPiscinaOlas, JTextField monitorPiscinaOlas, JTextArea areaPiscinaOlas, JTextField monitorPiscinaGrande, JTextArea areaPiscinaGrande, JTextArea colaPiscinaGrande, JTextArea colaTumbonas, JTextArea areaTumbonas, JTextField monitorTumbonas, JTextField areaToboganA, JTextField areaToboganB, JTextField areaToboganC, JTextField monitorToboganA, JTextField monitorToboganB, JTextField monitorToboganC, JTextArea colaToboganes, FuncionesGenerales fg, Paso paso) {
        this.monitorVestuario = monitorVestuario;
        this.areaVestuario = areaVestuario;
        this.colaVestuario = colaVestuario;
        this.colaEntrada = colaEntrada;
        this.colaPiscinaNiños = colaPiscinaNiños;
        this.monitorPiscinaNiños = monitorPiscinaNiños;
        this.areaPiscinaNiños = areaPiscinaNiños;
        this.colaEsperaAdultos = colaEsperaAdultos;
        this.colaPiscinaOlas = colaPiscinaOlas;
        this.monitorPiscinaOlas = monitorPiscinaOlas;
        this.areaPiscinaOlas = areaPiscinaOlas;
        this.monitorPiscinaGrande = monitorPiscinaGrande;
        this.areaPiscinaGrande = areaPiscinaGrande;
        this.colaPiscinaGrande = colaPiscinaGrande;
        this.colaTumbonas = colaTumbonas;
        this.areaTumbonas = areaTumbonas;
        this.monitorTumbonas = monitorTumbonas;
        this.areaToboganA = areaToboganA;
        this.areaToboganB = areaToboganB;
        this.areaToboganC = areaToboganC;
        this.monitorToboganA = monitorToboganA;
        this.monitorToboganB = monitorToboganB;
        this.monitorToboganC = monitorToboganC;
        this.colaToboganes = colaToboganes;
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

    public void setVestuario(Vestuario vestuario) {
        this.vestuario = vestuario;
    } // Cierre del método

    public PiscinaNiños getPiscinaNiños() {
        return piscinaNiños;
    } // Cierre del método

    public void setPiscinaNiños(PiscinaNiños piscinaNiños) {
        this.piscinaNiños = piscinaNiños;
    } // Cierre del método
    
    public JTextField getMonitorVestuario() {
        return monitorVestuario;
    } // Cierre del método

    public JTextArea getAreaVestuario() {
        return areaVestuario;
    } // Cierre del método

    public JTextArea getColaVestuario() {
        return colaVestuario;
    } // Cierre del método

    public JTextArea getColaEntrada() {
        return colaEntrada;
    } // Cierre del método

    public JTextArea getColaPiscinaNiños() {
        return colaPiscinaNiños;
    } // Cierre del método

    public JTextField getMonitorPiscinaNiños() {
        return monitorPiscinaNiños;
    } // Cierre del método

    public JTextArea getAreaPiscinaNiños() {
        return areaPiscinaNiños;
    } // Cierre del método

    public JTextArea getColaEsperaAdultos() {
        return colaEsperaAdultos;
    } // Cierre del método

    public Semaphore getSemEntrarparque() {
        return semEntrarparque;
    } // Cierre del método

    public BlockingQueue getColaEntrarParque() {
        return colaEntrarParque;
    } // Cierre del método

    public PiscinaOlas getPiscinaOlas() {
        return piscinaOlas;
    } // Cierre del método

    public void setPiscinaOlas(PiscinaOlas piscinaOlas) {
        this.piscinaOlas = piscinaOlas;
    } // Cierre del método

    public JTextArea getColaPiscinaOlas() {
        return colaPiscinaOlas;
    } // Cierre del método

    public JTextField getMonitorPiscinaOlas() {
        return monitorPiscinaOlas;
    } // Cierre del método

    public JTextArea getAreaPiscinaOlas() {
        return areaPiscinaOlas;
    } // Cierre del método

    public PiscinaGrande getPiscinaGrande() {
        return piscinaGrande;
    } // Cierre del método

    public void setPiscinaGrande(PiscinaGrande piscinaGrande) {
        this.piscinaGrande = piscinaGrande;
    } // Cierre del método

    public Tumbonas getTumbonas() {
        return tumbonas;
    } // Cierre del método

    public void setTumbonas(Tumbonas tumbonas) {
        this.tumbonas = tumbonas;
    } // Cierre del método

    public JTextField getMonitorPiscinaGrande() {
        return monitorPiscinaGrande;
    } // Cierre del método

    public JTextArea getAreaPiscinaGrande() {
        return areaPiscinaGrande;
    } // Cierre del método

    public JTextArea getColaPiscinaGrande() {
        return colaPiscinaGrande;
    } // Cierre del método

    public JTextArea getColaTumbonas() {
        return colaTumbonas;
    } // Cierre del método

    public JTextArea getAreaTumbonas() {
        return areaTumbonas;
    } // Cierre del método

    public JTextField getMonitorTumbonas() {
        return monitorTumbonas;
    }    // Cierre del método

    public Toboganes getToboganes() {
        return toboganes;
    } // Cierre del método

    public void setToboganes(Toboganes toboganes) {
        this.toboganes = toboganes;
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
} // Cierre de la clase