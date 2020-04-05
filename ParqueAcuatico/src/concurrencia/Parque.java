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

/**
 *
 * @authors Virginia Vallejo y Javier Gonzalez
 */
public class Parque {
    private final Object[] parque;
    private int in = 0, out = 0, numElem = 0, maximo = 0, numElemAux = 0;
    private final Lock control = new ReentrantLock();
    private final Condition lleno = control.newCondition();
    private final Condition vacio = control.newCondition();
    
    private Semaphore semEntrarParque = new Semaphore(100, true);
    private Semaphore semVestuario = new Semaphore(100, true);
    private Semaphore semPiscinaNiños = new Semaphore(100, true);
    private Semaphore semPiscinaGrande = new Semaphore(100, true);
    private Semaphore semPiscinaOlas = new Semaphore(100, true);
    private Semaphore semTumbonas = new Semaphore(100, true);
    private Semaphore semToboganA = new Semaphore(100, true);
    private Semaphore semToboganB = new Semaphore(100, true);
    private Semaphore semToboganC = new Semaphore(100, true);

    private BlockingQueue colaEntrarParque = new LinkedBlockingQueue();
    private BlockingQueue colaVestuarios = new LinkedBlockingQueue(20);
    private BlockingQueue colaPiscinaNiños = new LinkedBlockingQueue(20);
    private BlockingQueue colaPiscinaOlas = new LinkedBlockingQueue(20);
    private BlockingQueue colaPiscinaGrande = new LinkedBlockingQueue(20);
    private BlockingQueue colaTumbonas = new LinkedBlockingQueue(20);
    private BlockingQueue colaToboganA = new LinkedBlockingQueue(20);
    private BlockingQueue colaToboganB = new LinkedBlockingQueue(20);
    private BlockingQueue colaToboganC = new LinkedBlockingQueue(20);

    private String usoVestuarios;
    private String usoPiscinaNiños;
    private String usoPiscinaGrande;
    private String usoPiscinaOlas;
    private String usoTumbonas;
    private String usoToboganA;
    private String usoToboganB;
    private String usoToboganC;
           
    public Parque( int max ) { 
        this.maximo = max;
        parque = new Object[ max ];
    } 

    // Método para atender usuarios que llegan al parque.
    public void entrarParque(String id) {
        try {
            colaEntrarParque.put(id);
            semEntrarParque.acquire();
            colaEntrarParque.take();

        } catch (InterruptedException ex) {
            ex.toString();
        }
    }

    // Método para atender usuarios que abandonan el parque.
    public void salirParque() {
        semEntrarParque.release();
    }
    
    // Método para atender usuarios que van a los vestuarios
    public void entrarVestuario(String id) {
        try {
            colaVestuarios.put(id);
            semVestuario.acquire();
        } catch (InterruptedException ex) {
            ex.toString();
        }
    }

    public void usarVestuario() {
        try {
            usoVestuarios = colaVestuarios.take().toString();

        } catch (InterruptedException ex) {
            ex.toString();
        }
    }

    public void salirVestuario() {
        semVestuario.release();
    }
    // Método para atender usuarios que van a la piscina de niños
    public void entrarPiscinaNiños(String id) {
        try {
            colaPiscinaNiños.put(id);
            semPiscinaNiños.acquire();
        } catch (InterruptedException ex) {
            ex.toString();
        }
    }

    public void usarPiscinaNiños() {
        try {
            usoPiscinaNiños = colaPiscinaNiños.take().toString();

        } catch (InterruptedException ex) {
            ex.toString();
        }
    }

    public void salirPiscinaNiños() {
        semPiscinaNiños.release();
    }  
    
    // Método para atender usuarios que van a la piscina de olas
    public void entrarPiscinaOlas(String id){ 
        try {
            colaPiscinaOlas.put(id);
            semPiscinaOlas.acquire();
        } catch (InterruptedException ex) {
            ex.toString();
        }
    }

    public void usarPiscinaOlas() {
        try {
            usoPiscinaOlas = colaPiscinaOlas.take().toString();

        } catch (InterruptedException ex) {
            ex.toString();
        }
    }

    public void salirPiscinaOlas() {
        semPiscinaOlas.release();
    } 

    // Método para atender usuarios que van a la piscina grande
    public void entrarPiscinaGrande (String id){ 
        try {
            colaPiscinaGrande.put(id);
            semPiscinaGrande.acquire();
        } catch (InterruptedException ex) {
            ex.toString();
        }
    }

    public void usarPiscinaGrande (){ 
        try {
            usoPiscinaGrande = colaPiscinaGrande.take().toString();

        } catch (InterruptedException ex) {
            ex.toString();
        }
    }

    public void salirPiscinaGrande() {
        semPiscinaGrande.release();
    } 
    
    // Método para atender usuarios que van a las tumbonas
    public void entrarTumbonas (String id){ 
        try {
            colaTumbonas.put(id);
            semTumbonas.acquire();
        } catch (InterruptedException ex) {
            ex.toString();
        }
    }

    public void usarTumbonas (){ 
        try {
            usoTumbonas = colaTumbonas.take().toString();

        } catch (InterruptedException ex) {
            ex.toString();
        }
    }

    public void salirTumbonas() {
        semTumbonas.release();
    }
    
    // Método para atender usuarios que van a los toboganes
    public void entraToboganes (String id){ 
        try {
            colaToboganes.put(id);
            semToboganes.acquire();
        } catch (InterruptedException ex) {
            ex.toString();
        }
    }

    public void usarToboganA (){ 
        try {
            usoToboganA = colaToboganA.take().toString();

        } catch (InterruptedException ex) {
            ex.toString();
        }
    }

    public void salirToboganA() {
        semToboganA.release();
    }
    
    public String dameHoraActual() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
