/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrencia;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import hilos.Usuario;

/**
 *
 * @authors Virginia Vallejo y Javier Gonzalez
 */
public class Parque {
    //Elementos interfaz
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
    
    //Concurrencia
    private final Semaphore semEntrarparque = new Semaphore(100, true);
    private final BlockingQueue colaEntrarParque = new LinkedBlockingQueue();
    
    private Vestuario vestuario;
    private PiscinaNiños piscinaNiños;
    private PiscinaOlas piscinaOlas;
    private PiscinaGrande piscinaGrande;
    private Tumbonas tumbonas;

    public Parque(JTextField monitorVestuario, JTextArea areaVestuario, JTextArea colaVestuario, JTextArea colaEntrada, JTextArea colaPiscinaNiños, JTextField monitorPiscinaNiños, JTextArea areaPiscinaNiños, JTextArea colaEsperaAdultos, JTextArea colaPiscinaOlas, JTextField monitorPiscinaOlas, JTextArea areaPiscinaOlas, JTextField monitorPiscinaGrande, JTextArea areaPiscinaGrande, JTextArea colaPiscinaGrande, JTextArea colaTumbonas, JTextArea areaTumbonas, JTextField monitorTumbonas) {
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
        this.piscinaNiños = new PiscinaNiños(colaPiscinaNiños, monitorPiscinaNiños, areaPiscinaNiños, colaEsperaAdultos);
        this.piscinaOlas = new PiscinaOlas(monitorPiscinaOlas, areaPiscinaOlas, colaPiscinaOlas);
        this.piscinaGrande = new PiscinaGrande(monitorPiscinaGrande, areaPiscinaGrande, colaPiscinaGrande);
        this.tumbonas = new Tumbonas(colaTumbonas, areaTumbonas, monitorTumbonas);
        this.vestuario = new Vestuario (colaVestuario, monitorVestuario, areaVestuario);
    }

    public void entrarParque(Usuario u) {
        try {
            colaEntrarParque.put(u);
            imprimir(colaEntrada, colaEntrarParque.toString());
            semEntrarparque.acquire();
            colaEntrarParque.take();
            imprimir(colaEntrada, colaEntrarParque.toString());
        } catch (InterruptedException ex) {
            
        }
    }

    public void salirParque() {
        semEntrarparque.release();

    }


    private synchronized void imprimir(JTextArea campo, String contenido) {
        campo.setText(contenido);
    }

    public Vestuario getVestuario() {
        return vestuario;
    }

    public void setVestuario(Vestuario vestuario) {
        this.vestuario = vestuario;
    }

    public PiscinaNiños getPiscinaNiños() {
        return piscinaNiños;
    }

    public void setPiscinaNiños(PiscinaNiños piscinaNiños) {
        this.piscinaNiños = piscinaNiños;
    }

    
    public JTextField getMonitorVestuario() {
        return monitorVestuario;
    }

    public JTextArea getAreaVestuario() {
        return areaVestuario;
    }

    public JTextArea getColaVestuario() {
        return colaVestuario;
    }

    public JTextArea getColaEntrada() {
        return colaEntrada;
    }

    public JTextArea getColaPiscinaNiños() {
        return colaPiscinaNiños;
    }

    public JTextField getMonitorPiscinaNiños() {
        return monitorPiscinaNiños;
    }

    public JTextArea getAreaPiscinaNiños() {
        return areaPiscinaNiños;
    }

    public JTextArea getColaEsperaAdultos() {
        return colaEsperaAdultos;
    }

    public Semaphore getSemEntrarparque() {
        return semEntrarparque;
    }

    public BlockingQueue getColaEntrarParque() {
        return colaEntrarParque;
    }

    public PiscinaOlas getPiscinaOlas() {
        return piscinaOlas;
    }

    public void setPiscinaOlas(PiscinaOlas piscinaOlas) {
        this.piscinaOlas = piscinaOlas;
    }

    public JTextArea getColaPiscinaOlas() {
        return colaPiscinaOlas;
    }

    public JTextField getMonitorPiscinaOlas() {
        return monitorPiscinaOlas;
    }

    public JTextArea getAreaPiscinaOlas() {
        return areaPiscinaOlas;
    }

    public PiscinaGrande getPiscinaGrande() {
        return piscinaGrande;
    }

    public void setPiscinaGrande(PiscinaGrande piscinaGrande) {
        this.piscinaGrande = piscinaGrande;
    }

    public Tumbonas getTumbonas() {
        return tumbonas;
    }

    public void setTumbonas(Tumbonas tumbonas) {
        this.tumbonas = tumbonas;
    }

    public JTextField getMonitorPiscinaGrande() {
        return monitorPiscinaGrande;
    }

    public JTextArea getAreaPiscinaGrande() {
        return areaPiscinaGrande;
    }

    public JTextArea getColaPiscinaGrande() {
        return colaPiscinaGrande;
    }

    public JTextArea getColaTumbonas() {
        return colaTumbonas;
    }

    public JTextArea getAreaTumbonas() {
        return areaTumbonas;
    }

    public JTextField getMonitorTumbonas() {
        return monitorTumbonas;
    }
    
    
}
