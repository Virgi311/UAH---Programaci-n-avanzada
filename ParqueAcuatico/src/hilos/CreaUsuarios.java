/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import concurrencia.*;
import java.util.concurrent.CyclicBarrier;
        

/**
 *
 * @author Virginia Vallejo y Javier Gonzalez
 */
public class CreaUsuarios extends Thread {
    private final int aforo = 20;
    private final int mayoria_edad = 18;
    private final Parque parque;
    
    public CreaUsuarios(Parque parque) {
        this.parque = parque;
    }

    @Override
    public void run() {

        for (int id = 1; id <= aforo; id++) {
            CyclicBarrier barrera = new CyclicBarrier(2);
            int edadUsuario = getEdadAleatoria(1);
            Usuario usuarioPrincipal = new Usuario(parque, barrera, id, edadUsuario);

            if (edadUsuario < 11) {
                id++;
 
                Usuario usuarioAcompañante = new Usuario(parque, barrera, id, getEdadAleatoria(mayoria_edad));
                usuarioPrincipal.setAcompañante(usuarioAcompañante);
                usuarioPrincipal.setNombre(usuarioPrincipal.getNombre() + "-" + usuarioAcompañante.getIdentificador());
                usuarioAcompañante.setNombre(usuarioAcompañante.getNombre() + "-" + usuarioPrincipal.getIdentificador());
                usuarioAcompañante.setEsAcompañante(true);
                usuarioAcompañante.start();
            }
            usuarioPrincipal.start();
            dormir(400, 700);
        }

    }

    private int getEdadAleatoria(int min) {
        Random aleatoriedad = new Random(System.currentTimeMillis());
        return aleatoriedad.nextInt(50 - min) + min;
    }
    
    private void dormir(int min, int max) {
        try {
            Thread.sleep(min + (int) ((max - min) * Math.random()));
        } catch (InterruptedException ex) {
            Logger.getLogger(CreaUsuarios.class.getName()).log(Level.SEVERE, "Problemas mientras duerme", ex);
        }
    }
    
}
