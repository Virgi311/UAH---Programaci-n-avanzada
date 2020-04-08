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

/**
 *
 * @author User
 */
public class PiscinaNiños {
    
    private final JTextArea colaPiscinaNiños;
    private final JTextField monitorPiscinaNiños;
    private final JTextArea areaPiscinaNiños;
    private final JTextArea colaEsperaAdultos;

    public PiscinaNiños(JTextArea colaPiscinaNiños, JTextField monitorPiscinaNiños, JTextArea areaPiscinaNiños, JTextArea colaEsperaAdultos) {
        this.colaPiscinaNiños = colaPiscinaNiños;
        this.monitorPiscinaNiños = monitorPiscinaNiños;
        this.areaPiscinaNiños = areaPiscinaNiños;
        this.colaEsperaAdultos = colaEsperaAdultos;
    }
    

    
private final Semaphore semPiscinaNiños3 = new Semaphore(20, true);
    private final Semaphore semPiscinaNiños2 = new Semaphore(10, true);
    private final Semaphore semPiscinaNiños1 = new Semaphore(0, true);
    
    private final BlockingQueue colaEntrarPiscinaNiños = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> piscinaNiños = new CopyOnWriteArrayList<>();
    
     public void zonaVestuarios(Usuario u) {
        try {
            colaEntrarPiscinaNiños.put(u);
            imprimir(colaPiscinaNiños, colaEntrarPiscinaNiños.toString());
            semPiscinaNiños1.acquire();

            piscinaNiños.add(u);
            imprimir(areaPiscinaNiños, piscinaNiños.toString());
            if (u.getEsAcompañante() || u.getEdad() < 18) {

                semPiscinaNiños2.acquire();

            } else {

                semPiscinaNiños3.acquire();

            }

        } catch (InterruptedException ex) {
            
        }
    }

    public void salirPiscinaNiños(Usuario u) {
        piscinaNiños.remove(u);
        imprimir(areaPiscinaNiños, piscinaNiños.toString());
        if (u.getEsAcompañante() || u.getEdad() < 18) {

            semPiscinaNiños2.release();

        } else {

            semPiscinaNiños3.release();

        }

    }

    public Usuario controlaPiscinaNiños() {
        try {
            Usuario u = (Usuario) colaEntrarPiscinaNiños.take();

            monitorPiscinaNiños.setText(u.toString());
            imprimir(colaPiscinaNiños, colaEntrarPiscinaNiños.toString());

            return u;

        } catch (InterruptedException ex) {
            
            return null;
        }
    }

    public void controlaPiscinaNiños(Usuario u) {
        try {
            if (u.getEdad() > 17 && !u.getEsAcompañante()) { //Adulto
                semPiscinaNiños3.acquire();
                semPiscinaNiños3.release();
                semPiscinaNiños1.release();
            } else if (u.getEdad() <= 10) { //niño que necesita acompañante
                semPiscinaNiños2.acquire(2);
                semPiscinaNiños2.release(2);

                //Para dejar pasar al niño
               semPiscinaNiños1.release();

            } else if (u.getEsAcompañante()) { //acompañante
                
                // deja pasar al acompañante directamente
                semPiscinaNiños1.release();
            } else { //niño sin acompañante
                semPiscinaNiños2.acquire();
                semPiscinaNiños2.release();
                semPiscinaNiños1.release();
            }

            monitorPiscinaNiños.setText("");

        } catch (InterruptedException e) {
            
        }
    }
    
    private synchronized void imprimir(JTextArea campo, String contenido) {
        campo.setText(contenido);
    }
    
}

