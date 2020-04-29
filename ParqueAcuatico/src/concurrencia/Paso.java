package concurrencia;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase Paso
 *
 * Define la forma en que se contrala la ejecución de los hilos
 *
 * @author
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Paso {
    //Campos de la clase
    private boolean bloqueoActivo;
    private final Lock cerrojo;
    private final Condition parar;

    public Paso() {
        bloqueoActivo = false;
        cerrojo = new ReentrantLock();
        parar = cerrojo.newCondition();
    } // Cierre del método

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
    } // Cierre del método

    public void detener() {
        try {
            cerrojo.lock();
            bloqueoActivo = true;
            parar.signalAll();
        } finally {
            cerrojo.unlock();
        }
    } // Cierre del método

    public void reanudar() {
        try {
            cerrojo.lock();
            bloqueoActivo = false;
            parar.signalAll();
        } finally {
            cerrojo.unlock();
        }
    } // Cierre del método
} // Cierre de la clase