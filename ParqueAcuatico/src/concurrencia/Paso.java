package concurrencia;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @authors 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Paso {
    
    private boolean bloqueoActivo;
    private final Lock cerrojo;
    private final Condition parar;

    public Paso() {
        bloqueoActivo = false;
        cerrojo = new ReentrantLock();
        parar = cerrojo.newCondition();
    }

    public synchronized void mirar() {
        try {
            cerrojo.lock();
            if( bloqueoActivo ) {
                try {
                    parar.await();
                } catch(InterruptedException ex) {
                    System.out.println("ERROR: " + ex);
                }
            }
        } finally {
            cerrojo.unlock();
        }
    }

    public void detener() {
        try {
            cerrojo.lock();
            bloqueoActivo = true;
            parar.signalAll();
        } finally {
            cerrojo.unlock();
        }
    }

    public void reanudar() {
        try {
            cerrojo.lock();
            bloqueoActivo = false;
            parar.signalAll();
        } finally {
            cerrojo.unlock();
        }
    }
}