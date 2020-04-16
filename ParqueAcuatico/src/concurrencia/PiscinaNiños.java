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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import hilos.Usuario;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class PiscinaNiños {
    
    private final JTextArea colaPiscinaNiños;
    private final JTextField monitorPiscinaNiños;
    private final JTextArea areaPiscinaNiños;
    private final JTextArea areaEsperaAdultos;

    public PiscinaNiños(JTextArea colaPiscinaNiños, JTextField monitorPiscinaNiños, JTextArea areaPiscinaNiños, JTextArea areaEsperaAdultos) {
        this.colaPiscinaNiños = colaPiscinaNiños;
        this.monitorPiscinaNiños = monitorPiscinaNiños;
        this.areaPiscinaNiños = areaPiscinaNiños;
        this.areaEsperaAdultos = areaEsperaAdultos;
    }
    
    private final Semaphore semPiscinaNiños = new Semaphore(15, true);
    private final Semaphore semPiscinaNiños0 = new Semaphore(0, true);
    
    private final BlockingQueue colaEntrarPiscinaNiños = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> piscinaNiños = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Usuario> esperaAdultos = new CopyOnWriteArrayList<>();
    
    private final CyclicBarrier barreraPiscinaNiños = new CyclicBarrier(2);
    private boolean accesoPermitido = false;
    
    
     public boolean entrarPiscinaNiños(Usuario u) {
        try {
            colaEntrarPiscinaNiños.put(u);
            imprimir(colaPiscinaNiños, colaEntrarPiscinaNiños.toString());
            semPiscinaNiños0.acquire();
            
            if (u.getEdad() <= 5) { // Menores de 5 años pueden entrar acompañados    
                try {
                    semPiscinaNiños.acquire();
                    barreraPiscinaNiños.await();
                    
                    piscinaNiños.add(u);
                    
                    imprimir(areaPiscinaNiños, piscinaNiños.toString());
                    imprimir(areaEsperaAdultos, piscinaNiños.toString());
                } catch (BrokenBarrierException ex) {

                }    
            }
            if (u.getEdad() > 10 && !u.getEsAcompañante()) {
                try {
                    semPiscinaNiños.acquire();
                    piscinaNiños.add(u);
                    imprimir(areaPiscinaNiños, piscinaNiños.toString());
                } catch (InterruptedException ex) {

                }
            } else {
                return false;
            }
            
        } catch (InterruptedException ex) {

        } return true;

    }
     
    public void salirPiscinaNiños(Usuario u) {
        piscinaNiños.remove(u);
        imprimir(areaPiscinaNiños, piscinaNiños.toString());
        semPiscinaNiños.release();
    }

    public Usuario controlarPiscinaNiños() {
        Usuario u = null;
        try {
            u = (Usuario) colaEntrarPiscinaNiños.take();
            imprimir(colaPiscinaNiños, colaEntrarPiscinaNiños.toString());
            monitorPiscinaNiños.setText(u.toString());

        } catch (InterruptedException ex) {

        }

        return u;
    }

public void controlarPiscinaNiños(Usuario u) {
        if (u.getEdad() <= 5) { // Menores de 5 años pueden entrar acompañados
            try {
            semPiscinaNiños.acquire(2);
            semPiscinaNiños.release(2);

            accesoPermitido = true;
            semPiscinaNiños0.release();

            colaEntrarPiscinaNiños.take();
            imprimir(colaPiscinaNiños, colaEntrarPiscinaNiños.toString());
            semPiscinaNiños0.release();                
      
            } catch (InterruptedException ex) {

            }
            
        } else if (u.getEdad() <= 10) { // Entre 6 y 10 años pueden entrar solos
            try {
                semPiscinaNiños.acquire();
                semPiscinaNiños.release();
                accesoPermitido = true;
                semPiscinaNiños0.release();
        
            } catch (InterruptedException ex) {

            }
            
        } else { // De 11 años en adelante no pueden pasar
            try {
                accesoPermitido = false;
                semPiscinaNiños0.release();
                try {
                   colaEntrarPiscinaNiños.take();
                   imprimir(colaPiscinaNiños, colaEntrarPiscinaNiños.toString());
                   semPiscinaNiños0.release();
                } catch (InterruptedException ex) {
                
                }
            }catch(Exception ex){}
        }

        monitorPiscinaNiños.setText("");
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
        return barreraPiscinaNiños;
    }
}
