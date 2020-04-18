package hilos;

import concurrencia.Parque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authores 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */      
public class MonitorTumbonas extends Thread {
    private final Parque parque;

    public MonitorTumbonas(Parque parque) {
        this.parque = parque;
    }
    
    @Override
    public void run() {
        while( true ) {
            Usuario u = parque.getTumbonas().controlarTumbonas();
            dormir(500,900);
            parque.getTumbonas().controlarTumbonas(u);
        }
    }

    private void dormir(int min, int max) {
        try {
            Thread.sleep(min + (int) ((max - min) * Math.random()));
        } catch (InterruptedException ex) {
            Logger.getLogger(CreaUsuarios.class.getName()).log(Level.SEVERE, "Problemas mientas duerme", ex);
        }
    }
}