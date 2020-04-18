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
public class MonitorPiscinaGrande extends Thread {
    private final Parque parque;

    public MonitorPiscinaGrande(Parque parque) {
        this.parque = parque;
    }
    
    @Override
    public void run() {
        while( true ) {
            Usuario u = parque.getPiscinaGrande().controlarPiscinaGrande();
            dormir();
            parque.getPiscinaGrande().controlarPiscinaGrande(u);
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