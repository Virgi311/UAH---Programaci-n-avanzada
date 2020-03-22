/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parqueacuatico;

import static java.lang.Thread.sleep;

/**
 *
 * @author User
 */
public class Usuario extends Thread {
    private int id, edad;
    private Parque parque;
    private Monitor monitor;
    
    public Usuario (int id, Parque parque) {
        this.id = id;
        this.parque = parque;
        monitor = null;
        this.edad = edad;
    }
    
    //@Override
    //public void run() { 
    //    for ( int i = 1; i <= 5000; i++ ) {
    //        try
    //        {
    //            sleep((int)(3000*Math.random()));
    //        } catch (InterruptedException e){ }
    //        parque.entrar(this); //Entra en la exposición, si hay hueco; y sino espera en la cola
    //        parque.vestuario(this); //Está un tiempo dentro de la exposición
    //        parque.atraccion(this); //Está un tiempo dentro de la exposición
    //        parque.salir(this); //Sale de la exposición
    //    }
    //}
}
