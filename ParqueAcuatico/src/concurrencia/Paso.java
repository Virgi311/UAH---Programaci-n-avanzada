package concurrencia;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @authores 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Paso {
    
    private boolean bloqueoActivo, finalizar;
    private final Lock cerrojo;
    private final Condition parar;

    public Paso() {
        bloqueoActivo = false;
        finalizar = false;
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

    /*public synchronized void notifyUno() {
        bloqueoActivo = false;
        notify();
    }*/

    public void notifyTodos() {
        try {
            cerrojo.lock();
            bloqueoActivo = false;
            parar.signalAll();
        } finally {
            cerrojo.unlock();
        }
    }
    
    public boolean isFinalizar() {
        return finalizar;
    }

    public void setFinalizar(boolean finalizar) {
        this.finalizar = finalizar;
    }

    public boolean isBloqueoActivo() {
        return bloqueoActivo;
    }
}