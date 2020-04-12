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

    
    //Concurrencia
    private final Semaphore semEntrarparque = new Semaphore(100, true);
    private final BlockingQueue colaEntrarParque = new LinkedBlockingQueue();
    
    private Vestuario vestuario;
    private PiscinaNiños piscinaNiños;
    private PiscinaOlas piscinaOlas;

    public Parque(JTextField monitorVestuario, JTextArea areaVestuario, JTextArea colaVestuario, JTextArea colaEntrada, JTextArea colaPiscinaNiños, JTextField monitorPiscinaNiños, JTextArea areaPiscinaNiños, JTextArea colaEsperaAdultos, JTextArea colaPiscinaOlas, JTextField monitorPiscinaOlas, JTextArea areaPiscinaOlas) {
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
    
    
}
