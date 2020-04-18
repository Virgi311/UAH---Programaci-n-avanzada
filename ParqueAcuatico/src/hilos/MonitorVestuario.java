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
public class MonitorVestuario extends Thread {
    
    private final Parque parque;

    public MonitorVestuario(Parque parque) {
        this.parque = parque;
    }
    
    @Override
    public void run() {
        while( true ) {
            Usuario u = parque.getVestuario().controlarVestuario();
            dormir();
            parque.getVestuario().controlarVestuario(u);
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