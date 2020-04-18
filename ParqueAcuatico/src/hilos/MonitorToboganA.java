package hilos;

import concurrencia.Parque;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authores 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class MonitorToboganA extends Thread {
    
    private final Parque parque;

    public MonitorToboganA(Parque parque) {
        this.parque = parque;
    }
    
    @Override
    public void run() {
        while( true ) {
            Usuario u = parque.getPiscinaOlas().controlarPiscinaOlas();
            dormir();
            parque.getPiscinaOlas().controlarPiscinaOlas(u);
        }
    }

    private void dormir() {
        try {
            sleep( 400 + (int)( 500 * Math.random() ) );
        } catch (InterruptedException ex) {
            Logger.getLogger(MonitorVestuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}