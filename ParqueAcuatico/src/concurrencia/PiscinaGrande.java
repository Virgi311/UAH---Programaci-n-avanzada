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


public class PiscinaGrande {

    private final JTextField monitorPiscinaGrande;
    private final JTextArea areaPiscinaGrande;
    private final JTextArea colaPiscinaGrande;

    public PiscinaGrande(JTextField monitorPiscinaGrande, JTextArea areaPiscinaGrande, JTextArea colaPiscinaGrande) {
        this.monitorPiscinaGrande = monitorPiscinaGrande;
        this.areaPiscinaGrande = areaPiscinaGrande;
        this.colaPiscinaGrande = colaPiscinaGrande;
    }

    private final Semaphore semPiscinaGrande = new Semaphore(50, true);
    private final Semaphore semPiscinaGrande0 = new Semaphore(0, true);

    private final BlockingQueue colaEntrarPiscinaGrande = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> piscinaGrande = new CopyOnWriteArrayList<>();

    private final CyclicBarrier barreraPiscinaGrande = new CyclicBarrier(2);
    private boolean accesoPermitido = false;

    public boolean entrarPiscinaGrande(Usuario u) {
        try {
            colaEntrarPiscinaGrande.put(u);
            imprimir(colaPiscinaGrande, colaEntrarPiscinaGrande.toString());
            semPiscinaGrande0.acquire();
            if (u.getEdad() > 10 && !u.getEsAcompañante()) {
                try {
                    semPiscinaGrande.acquire();
                    barreraPiscinaGrande.await();
                    
                    piscinaGrande.add(u);
                    
                    imprimir(areaPiscinaGrande, piscinaGrande.toString());
                } catch (BrokenBarrierException ex) {

                }
            } else {
                semPiscinaGrande.acquire();
                piscinaGrande.add(u);
                imprimir(areaPiscinaGrande, piscinaGrande.toString());
            }

        } catch (InterruptedException ex) {

        }

        return true;

    }

    public void salirPiscinaGrande(Usuario u) {
        piscinaGrande.remove(u);
        imprimir(areaPiscinaGrande, piscinaGrande.toString());
        semPiscinaGrande.release();
    }

    public Usuario controlarPiscinaGrande() {
        Usuario u = null;
        try {
            u = (Usuario) colaEntrarPiscinaGrande.take();
            imprimir(colaPiscinaGrande, colaEntrarPiscinaGrande.toString());
            monitorPiscinaGrande.setText(u.toString());

        } catch (InterruptedException ex) {

        }

        return u;
    }

    public void controlarPiscinaGrande(Usuario u) {
        if (u.getEdad() <= 10) {
            try {
                semPiscinaGrande.acquire(2);
                semPiscinaGrande.release(2);

                accesoPermitido = true;
                semPiscinaGrande0.release();

                colaEntrarPiscinaGrande.take();
                imprimir(colaPiscinaGrande, colaEntrarPiscinaGrande.toString());
                semPiscinaGrande0.release();
            } catch (InterruptedException ex) {

            }
        } else { // Que no tiene acompañante
            try {
                semPiscinaGrande.acquire();
                semPiscinaGrande.release();
                accesoPermitido = true;
                semPiscinaGrande0.release();
            } catch (InterruptedException ex) {
                Logger.getLogger(PiscinaOlas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        monitorPiscinaGrande.setText("");
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
        return barreraPiscinaGrande;
    }

}