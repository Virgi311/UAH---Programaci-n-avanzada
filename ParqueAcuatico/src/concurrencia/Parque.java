/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrencia;

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
    
    private Semaphore semEntrada = new Semaphore(5000, true);

    private BlockingQueue colaEntrada = new LinkedBlockingQueue();
    private BlockingQueue colaVestuarios = new LinkedBlockingQueue(20);
    private BlockingQueue colaPiscinaNiños = new LinkedBlockingQueue(20);
    private BlockingQueue colaPiscinaOlas = new LinkedBlockingQueue(20);
    private BlockingQueue colaPiscinaGrande = new LinkedBlockingQueue(20);
    private BlockingQueue colaTumbonas = new LinkedBlockingQueue(20);
    private BlockingQueue colaToboganA = new LinkedBlockingQueue(20);
    private BlockingQueue colaToboganB = new LinkedBlockingQueue(20);
    private BlockingQueue colaToboganC = new LinkedBlockingQueue(20);
    private BlockingQueue colaToboganD = new LinkedBlockingQueue(20);
      
    public Parque( int max ) { 
        this.maximo = max;
        parque = new Object[ max ];
    } 

    // Método para atender usuarios que llegan al parque.
    public void entrarParque(String id) {
        try {
            colaEntrada.put(id);
            semEntrada.acquire();
            colaEntrada.take();

        } catch (InterruptedException ex) {
            ex.toString();
        }
    }

    // Método para atender usuarios que abandonan el parque.
    public void salirParque() {
        semEntrada.release();
    }
    
      // Método para atender usuarios que van a los vestuarios
        public void irVestuarios(String id) {
        try {
            colaVestuarios.put(id);
            semPescadero.acquire();
        } catch (InterruptedException ex) {
            ex.toString();
        }
    }

    public void atenderPescaderia() {
        try {
            pescaderoAtiende = colaPescadero.take().toString();

        } catch (InterruptedException ex) {
            ex.toString();
        }

    }

    public void atendidoPescaderia() {
        pescaderoAtiende = "";
        semPescadero.release();
    }
    
    
    
    
}
