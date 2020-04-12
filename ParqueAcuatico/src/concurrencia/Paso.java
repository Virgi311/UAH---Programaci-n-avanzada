/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrencia;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class Paso {
    
    private boolean bloqueoActivo;
    private boolean finalizar = false;

    public Paso() {
        bloqueoActivo = true;
    }

    public synchronized void mirar() {
        try {
            if (bloqueoActivo) {
                wait();
            }
        } catch (InterruptedException ex) {
            }
    }

    public synchronized void detener() {
        bloqueoActivo = true;
    }

    public synchronized void reanudarUno() {
        bloqueoActivo = false;
        notify();
    }

    public synchronized void reanudarTodos() {
        bloqueoActivo = false;
        notifyAll();
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
