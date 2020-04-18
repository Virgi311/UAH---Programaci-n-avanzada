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
public class MonitorPiscinaOlas extends Thread {

    private final Parque parque;

    public MonitorPiscinaOlas(Parque parque) {
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
            sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MonitorVestuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}