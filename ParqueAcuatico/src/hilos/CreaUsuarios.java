package hilos;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import concurrencia.*;
import java.util.concurrent.CyclicBarrier;
        

/**
 *
 * @authores 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class CreaUsuarios extends Thread {
    private final int capacidad = 5000;
    private final int mayoria_edad = 18;
    private final Parque parque;
    private final Paso paso;
    
    public CreaUsuarios(Parque parque, Paso paso) {
        this.parque = parque;
        this.paso = paso;
    }

    @Override
    public void run() {
        for( int id = 1; id <= capacidad; id++ ) {
            CyclicBarrier barrera = new CyclicBarrier(2);
            int edadUsuario = getEdadAleatoria(1);
            Usuario usuarioPrincipal = new Usuario(parque, barrera, id, edadUsuario, 10 + (int)(6 * Math.random()), paso);

            if( edadUsuario < 11 ){
                id++;
 
                Usuario usuarioAcompañante = new Usuario(parque, barrera, id, getEdadAleatoria(mayoria_edad), usuarioPrincipal.getNumAtracciones(), paso);
                
                usuarioPrincipal.setCodigo(usuarioPrincipal.getCodigo() + "-" + usuarioAcompañante.getIdentificador());
                usuarioAcompañante.setAcompañante(usuarioPrincipal);
                usuarioAcompañante.setCodigo(usuarioAcompañante.getCodigo() + "-" + usuarioPrincipal.getIdentificador());
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
            
        }
    }
}