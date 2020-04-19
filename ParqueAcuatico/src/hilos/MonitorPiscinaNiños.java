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
public class MonitorPiscinaNiños extends Thread {
    
    private final Parque parque;

    public MonitorPiscinaNiños(Parque parque) {
        this.parque = parque;
    }
    
    @Override
    public void run() {
        while( true ) {
            Usuario u = parque.getPiscinaNiños().controlarPiscinaNiños();
            dormir();
            parque.getPiscinaNiños().controlarPiscinaNiños(u);
        }
    }

    private void dormir() {
        try {
            sleep( 1000 + (int)( 1500 * Math.random() ) );
        } catch (InterruptedException ex) {
            
        }
    }
}