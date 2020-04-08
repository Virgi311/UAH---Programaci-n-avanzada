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
public class Vestuario {
    private final JTextArea colaVestuario;
    private final JTextField monitorVestuario;
    private final JTextArea areaVestuario;

    public Vestuario(JTextArea colaVestuario, JTextField monitorVestuario, JTextArea areaVestuario) {
        this.colaVestuario = colaVestuario;
        this.monitorVestuario = monitorVestuario;
        this.areaVestuario = areaVestuario;
    }
     
    private final Semaphore semVestuarioAdulto = new Semaphore(20, true);
    private final Semaphore semVestuarioNiño = new Semaphore(10, true);
    private final Semaphore semVestuario = new Semaphore(0, true);
    
    private final BlockingQueue colaEntrarVestuario = new LinkedBlockingQueue();
    private final CopyOnWriteArrayList<Usuario> vestuario = new CopyOnWriteArrayList<>();
    
     public void zonaVestuarios(Usuario u) {
        try {
            colaEntrarVestuario.put(u);
            imprimir(colaVestuario, colaEntrarVestuario.toString());
            semVestuario.acquire();

            vestuario.add(u);
            imprimir(areaVestuario, vestuario.toString());
            if (u.getEsAcompañante() || u.getEdad() < 18) {

                semVestuarioNiño.acquire();

            } else {

                semVestuarioAdulto.acquire();

            }

        } catch (InterruptedException ex) {
            
        }
    }

    public void salirVestuarios(Usuario u) {
        vestuario.remove(u);
        imprimir(areaVestuario, vestuario.toString());
        if (u.getEsAcompañante() || u.getEdad() < 18) {

            semVestuarioNiño.release();

        } else {

            semVestuarioAdulto.release();

        }

    }

    public Usuario controlaVestuario() {
        try {
            Usuario u = (Usuario) colaEntrarVestuario.take();

            monitorVestuario.setText(u.toString());
            imprimir(colaVestuario, colaEntrarVestuario.toString());

            return u;

        } catch (InterruptedException ex) {
            
            return null;
        }
    }

    public void controlaVestuario(Usuario u) {
        try {
            if (u.getEdad() > 17 && !u.getEsAcompañante()) { //Adulto
                semVestuarioAdulto.acquire();
                semVestuarioAdulto.release();
                semVestuario.release();
            } else if (u.getEdad() <= 10) { //niño que necesita acompañante
                semVestuarioNiño.acquire(2);
                semVestuarioNiño.release(2);

                //Para dejar pasar al niño
                semVestuario.release();

            } else if (u.getEsAcompañante()) { //acompañante
                
                // deja pasar al acompañante directamente
                semVestuario.release();
            } else { //niño sin acompañante
                semVestuarioNiño.acquire();
                semVestuarioNiño.release();
                semVestuario.release();
            }

            monitorVestuario.setText("");

        } catch (InterruptedException e) {
            
        }
    }
    
    private synchronized void imprimir(JTextArea campo, String contenido) {
        campo.setText(contenido);
    }
    
}
