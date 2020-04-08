/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;
 
import concurrencia.Parque;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Virginia Vallejo y Javier Gonzalez
 */
public class MonitorPiscinaNiños extends Thread {
    
    private final Parque parque;

    public MonitorPiscinaNiños(Parque parque) {
        this.parque = parque;
    }
    
    @Override
    public void run() {
        while (true) {
            Usuario u = parque.getPiscinaNiños().controlaPiscinaNiños();
            dormir();
            parque.getPiscinaNiños().controlaPiscinaNiños(u);
            
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
