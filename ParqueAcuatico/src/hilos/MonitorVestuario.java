/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;
 
import concurrencia.Parque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Virginia Vallejo y Javier Gonzalez
 */
public class MonitorVestuario extends Thread {
    
    private final Parque parque;

    public MonitorVestuario(Parque parque) {
        this.parque = parque;
    }
    
    @Override
    public void run() {
        while (true) {
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