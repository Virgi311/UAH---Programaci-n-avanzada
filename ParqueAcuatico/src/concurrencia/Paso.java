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
    private Condition condCarnicero = lock.newCondition();
    private Condition condPescadero = lock2.newCondition();
    
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
    
    public void pararCarnicero(){
        carnicero = true;
    }
    
    public void reanudarCarnicero(){
        lock.lock();
        try {
            carnicero = false;
            condCarnicero.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public void mirarCarnicero(){
        if(carnicero){
            try {
                lock.lock();
                try {
                    condCarnicero.await();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException ex) {
                ex.toString();
            }
        }
    }
    
    public void pararPescadero(){
        pescadero = true;
    }
    
    public void reanudarPescadero(){
        lock2.lock();
        try {
            pescadero = false;
            condPescadero.signalAll();
        } finally {
            lock2.unlock();
        }
    }
    
    public void mirarPescadero(){
        if(pescadero){
            try {
                lock2.lock();
                try {
                    condPescadero.await();
                } finally {
                    lock2.unlock();
                }
            } catch (InterruptedException ex) {
                ex.toString();
            }
        }
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

    public boolean isCarnicero() {
        return carnicero;
    }

    public void setCarnicero(boolean carnicero) {
        this.carnicero = carnicero;
    }

    public boolean isPescadero() {
        return pescadero;
    }

    public void setPescadero(boolean pescadero) {
        this.pescadero = pescadero;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public void setLock(ReentrantLock lock) {
        this.lock = lock;
    }

    public Condition getCondCarnicero() {
        return condCarnicero;
    }

    public void setCondCarnicero(Condition condCarnicero) {
        this.condCarnicero = condCarnicero;
    }

    public Condition getCondPescadero() {
        return condPescadero;
    }

    public void setCondPescadero(Condition condPescadero) {
        this.condPescadero = condPescadero;
    }

    public ReentrantLock getLock2() {
        return lock2;
    }

    public void setLock2(ReentrantLock lock2) {
        this.lock2 = lock2;
    }
}
