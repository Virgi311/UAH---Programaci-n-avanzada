/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrencia;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author User
 */
public class Paso {
    
    private boolean pausar = false;
    private boolean finalizar = false;
    private boolean monitor = false;
    
    private ReentrantLock lock = new ReentrantLock();
    private ReentrantLock lock2 = new ReentrantLock();
    
    public synchronized void mirar() {
        while (pausar) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.toString();
            }
        }
    }

    public void pausar() {
        pausar = true;
    }

    public synchronized void reanudar() {
        pausar = false;
        notifyAll();
    }
    
    public boolean isPausar() {
        return pausar;
    }

    public void setPausar(boolean pausar) {
        this.pausar = pausar;
    }

    public boolean isFinalizar() {
        return finalizar;
    }

    public void setFinalizar(boolean finalizar) {
        this.finalizar = finalizar;
    }
  
    public ReentrantLock getLock() {
        return lock;
    }

    public void setLock(ReentrantLock lock) {
        this.lock = lock;
    }
   
    public ReentrantLock getLock2() {
        return lock2;
    }

    public void setLock2(ReentrantLock lock2) {
        this.lock2 = lock2;
    }
}
