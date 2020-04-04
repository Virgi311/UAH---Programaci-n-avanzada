/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import concurrencia.Parque;
import static java.lang.Thread.sleep;
import java.util.Random;

/**
 *
 * @authors Virginia Vallejo y Javier Gonzalez
 */
public class Usuario extends Thread {
    private int id, edad;
    private Parque parque;
    private Monitor monitor;
    
    public Usuario (int id, int edad, Parque parque) {
        this.id = id;
        this.parque = parque;
        monitor = null;
        this.edad = edad;
    }
    
    //@Override
    //public void run() { 
    //    parque.entrarParque(id);  
    //    
    //    for ( int i = 1; i <= 5000; i++ ) {
    //        try
    //        {
    //            sleep( 300 + (int)( 401 * Math.random() ) );
    //        } catch (InterruptedException e){ }
    //        parque.entrar(this); //Entra en la parque acuático, si hay hueco; y sino espera en la cola
    //        parque.vestuario(this); //Está un tiempo dentro del vestuario
    //        parque.atraccion(this); //Está un tiempo dentro de una de las actividades del parque acuático
    //        parque.vestuario(this); //Está un tiempo dentro del vestuario
    //        parque.salir(this); //Sale del parque
    //    }
    //}
    
        @Override
    public void run() {
        paso.mirar();
        supermercado.entrarSuper(id);
        buf.añadirMensaje(supermercado.dameHoraActual() + ": " + id + " entra al supermercado");
        paso.mirar();
        Random r = new Random();
        int aleatorio = r.nextInt(4) + 1;  // Entre 1 y 3

        switch (aleatorio) {
            case 1:
                buf.añadirMensaje(supermercado.dameHoraActual() + ": " + id + " va a los estantes");
                supermercado.irEstante();
                dormir();
                paso.mirar();
                supermercado.salirEstante();
                break;
            case 2:
                buf.añadirMensaje(supermercado.dameHoraActual() + ": " + id + " entra en la cola de la pescaderia");
                supermercado.irPescaderia(id);
                paso.mirar();
                break;
            case 3:
                buf.añadirMensaje(supermercado.dameHoraActual() + ": " + id + " entra en la cola de la carniceria");
                supermercado.irCarniceria(id);
                paso.mirar();
                break;
        }
        buf.añadirMensaje(supermercado.dameHoraActual() + ": " + id + " entra en la cola de la caja");
        supermercado.irCaja(id);
        paso.mirar();
        buf.añadirMensaje(supermercado.dameHoraActual() + ": " + id + " sale del super");
        supermercado.salirSuper();
        
    }

    public void dormir() {
        try {
            sleep(1000 + (int) (Math.random() * 10000));
        } catch (InterruptedException ex) {
            ex.toString();
        }
    }
}
