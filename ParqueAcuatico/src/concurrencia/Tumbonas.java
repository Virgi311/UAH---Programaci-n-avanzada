/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrencia;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import hilos.Usuario;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author User
 */
public class Tumbonas {
    private final JTextArea colaTumbonas;
    private final JTextArea areaTumbonas;
    private final JTextField monitorTumbonas;

    public Tumbonas(JTextArea colaTumbonas, JTextArea areaTumbonas, JTextField monitorTumbonas) {
        this.colaTumbonas = colaTumbonas;
        this.areaTumbonas = areaTumbonas;
        this.monitorTumbonas = monitorTumbonas;
    }
    private boolean accesoPermitido = false;
    
    private final Semaphore semTumbonas = new Semaphore(20);
    private final Semaphore semTumbonas0 = new Semaphore(0, true);
    private final CopyOnWriteArrayList<Usuario> colaEntrarTumbonas = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Usuario> tumbonas = new CopyOnWriteArrayList<>();
    private final BlockingQueue colaMonitorTumbonas = new LinkedBlockingQueue();
    
    public boolean entrarTumbonas(Usuario u){
        colaEntrarTumbonas.add(u);
        imprimir(colaTumbonas, colaEntrarTumbonas.toString());
        try {
            semTumbonas.acquire();
            colaEntrarTumbonas.remove(u);
            
            colaMonitorTumbonas.put(u);
            semTumbonas0.acquire();
            if(!accesoPermitido){
                return false;
            }
            tumbonas.add(u);
            imprimir(areaTumbonas, tumbonas.toString());
            
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Tumbonas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return true;
    }
    
    public void salirTumbonas(Usuario u){
        tumbonas.remove(u);
        imprimir(areaTumbonas, tumbonas.toString());
        semTumbonas.release();
    }
    
    public Usuario controlarTumbonas() {
        Usuario u = null;
        try {
            u = (Usuario) colaEntrarTumbonas.take();
            imprimir(colaTumbonas, colaEntrarTumbonas.toString());
            monitorTumbonas.setText(u.toString());

        } catch (InterruptedException ex) {

        }

        return u;
    }

    public void controlarPiscinaOlas(Usuario u) {
        if (u.getEdad() <= 5) {
            accesoPermitido = false;
            semTumbonas0.release();
            try {
                colaEntrarTumbonas.take();
                imprimir(colaTumbonas, colaEntrarTumbonas.toString());
                semTumbonas0.release();
            } catch (InterruptedException ex) {

            }
        } else if (u.getEdad() <= 10) {
            try {
                semTumbonas.acquire(2);
                semTumbonas.release(2);
                accesoPermitido = true;
                semTumbonas0.release();
                colaEntrarTumbonas.take();
                imprimir(colaTumbonas, colaEntrarTumbonas.toString());
                semTumbonas0.release();
            } catch (InterruptedException ex) {

            }
        } else { // Que no tiene acompañante
            try {
                semTumbonas.acquire();
                semTumbonas.release();
                accesoPermitido = true;
                semTumbonas0.release();
                colaEntrarTumbonas.take();
                imprimir(colaTumbonas, colaEntrarTumbonas.toString());
                semTumbonas0.release();
            } catch (InterruptedException ex) {
               
            }

        }

        monitorTumbonas.setText("");
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
 
}

