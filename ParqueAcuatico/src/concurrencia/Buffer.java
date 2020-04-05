/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrencia;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class Buffer {

    private BlockingQueue bufferMensajes = new LinkedBlockingQueue();
    
    
    public void añadirMensaje(String mensaje){
        try {
            bufferMensajes.put(mensaje);
        } catch (InterruptedException ex) {
            Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String sacarMensaje(){
        try {
            return bufferMensajes.take().toString();
        } catch (InterruptedException ex) {
            Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public BlockingQueue getBufferMensajes() {
        return bufferMensajes;
    }

    public void setBufferMensajes(BlockingQueue bufferMensajes) {
        this.bufferMensajes = bufferMensajes;
    }
    
}
