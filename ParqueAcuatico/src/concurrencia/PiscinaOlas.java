/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrencia;

/**
 *
 * @author User
 */

import hilos.Usuario;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class PiscinaOlas {

    private final JTextField monitorPiscinaOlas;
    private final JTextArea areaPiscinaOlas;
    private final JTextArea colaPiscinaOlas;

    public PiscinaOlas(JTextField monitorPiscinaOlas, JTextArea areaPiscinaOlas, JTextArea colaPiscinaOlas) {
        this.monitorPiscinaOlas = monitorPiscinaOlas;
        this.areaPiscinaOlas = areaPiscinaOlas;
        this.colaPiscinaOlas = colaPiscinaOlas;
    }

    private final Semaphore semPiscinaOlas = new Semaphore(20, true);
    private final Semaphore semPiscinaOlas0 = new Semaphore(0, true);

    private final BlockingQueue colaEntrarPiscinaOlas = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> piscinaOlas = new CopyOnWriteArrayList<>();

    private final CyclicBarrier barreraPiscinaOlas = new CyclicBarrier(2);
    private boolean accesoPermitido = false;

    public boolean entrarPiscinaOlas(Usuario u) {
        try {
            colaEntrarPiscinaOlas.put(u);
            imprimir(colaPiscinaOlas, colaEntrarPiscinaOlas.toString());
            semPiscinaOlas0.acquire();
            if (!accesoPermitido) {
                return false;
            }
            if (u.getEdad() > 10 && !u.getEsAcompañante()) {
                try {
                    semPiscinaOlas.acquire();
                    barreraPiscinaOlas.await();
                    
                    piscinaOlas.add(u);
                    
                    imprimir(areaPiscinaOlas, piscinaOlas.toString());
                } catch (BrokenBarrierException ex) {

                }
            } else {
                semPiscinaOlas.acquire();
                piscinaOlas.add(u);
                imprimir(areaPiscinaOlas, piscinaOlas.toString());
            }

        } catch (InterruptedException ex) {

        }

        return true;

    }

    public void salirPiscinaOlas(Usuario u) {
        piscinaOlas.remove(u);
        imprimir(areaPiscinaOlas, piscinaOlas.toString());
        semPiscinaOlas.release();
    }

    public Usuario controlarPiscinaOlas() {
        Usuario u = null;
        try {
            u = (Usuario) colaEntrarPiscinaOlas.take();
            imprimir(colaPiscinaOlas, colaEntrarPiscinaOlas.toString());
            monitorPiscinaOlas.setText(u.toString());

        } catch (InterruptedException ex) {

        }

        return u;
    }

    public void controlarPiscinaOlas(Usuario u) {
        if (u.getEdad() <= 5) {
            accesoPermitido = false;
            semPiscinaOlas0.release();
            try {
                colaEntrarPiscinaOlas.take();
                imprimir(colaPiscinaOlas, colaEntrarPiscinaOlas.toString());
                semPiscinaOlas0.release();
            } catch (InterruptedException ex) {

            }
        } else if (u.getEdad() <= 10) {
            try {
                semPiscinaOlas.acquire(2);
                semPiscinaOlas.release(2);
                accesoPermitido = true;
                semPiscinaOlas0.release();
                colaEntrarPiscinaOlas.take();
                imprimir(colaPiscinaOlas, colaEntrarPiscinaOlas.toString());
                semPiscinaOlas0.release();
            } catch (InterruptedException ex) {

            }
        } else { // Que no tiene acompañante
            try {
                semPiscinaOlas.acquire();
                semPiscinaOlas.release();
                accesoPermitido = true;
                semPiscinaOlas0.release();
                colaEntrarPiscinaOlas.take();
                imprimir(colaPiscinaOlas, colaEntrarPiscinaOlas.toString());
                semPiscinaOlas0.release();
            } catch (InterruptedException ex) {
               
            }

        }

        monitorPiscinaOlas.setText("");
    }

    private synchronized void imprimir(JTextArea campo, String contenido) {
        campo.setText(contenido);
    }

    public boolean isAccesoPermitido() {
        return accesoPermitido;
    }

    public void setAccesoPermitido(boolean accesoPermitido) {
        this.accesoPermitido = accesoPermitido;
    }

    public CyclicBarrier getBarrera() {
        return barreraPiscinaOlas;
    }

}
