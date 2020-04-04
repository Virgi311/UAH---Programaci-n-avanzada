/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

/**
 *
 * @authors Virginia Vallejo y Javier Gonzalez
 */
public class Monitor exteands thread {
    
    private boolean libre;
    
    public Monitor() {
        libre=true;
    }

    public synchronyzed void entrarVestuario(int i) throws InterruptedException {
        while (!libre) {
            try {
                wait();
            } catch(InterruptedException e) {}
        }
        libre=false;
    }
    
    public synchronyzed void salirVestuario(int i) throws InterruptedException  {
        libre=true;
        notifyAll();
    }

    public void start() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}