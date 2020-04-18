/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

/**
 *
 * @author User
 */
        
import concurrencia.Parque;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
      
public class MonitorTumbonas extends Thread {
    private final Parque parque;

    public MonitorTumbonas(Parque parque) {
        this.parque = parque;
    }
    @Override
    public void run() {
        while (true) {

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
 
