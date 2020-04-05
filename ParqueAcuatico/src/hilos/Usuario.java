/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import concurrencia.Parque;
import static java.lang.Thread.sleep;
import java.util.Random;
import concurrencia.Buffer;
import concurrencia.Paso;

/**
 *
 * @authors Virginia Vallejo y Javier Gonzalez
 */
public class Usuario extends Thread {
    private int id, edad;
    private Parque parque;
    private Paso paso;
    private Buffer buf;
    
    public Usuario (int id, int edad, Parque parque) {
        this.id = id;
        this.parque = parque;
        this.edad = edad;
        this.paso = paso;
        this.buf = buf;
    }
        
        @Override
    public void run() {
        paso.mirar();
        parque.entrarParque(id); 
        buf.añadirMensaje(parque.dameHoraActual() + ": " + id + " entra al parque");
        parque.entrarVestuario(id); //Está un tiempo dentro del vestuario
        parque.salirVestuario(); //Sale del vestuario
        paso.mirar();
        Random r = new Random();
        int aleatorio = r.nextInt(16) + 5;  // Entre 5 y 15

        switch (aleatorio) {
            case 1:
                buf.añadirMensaje(parque.dameHoraActual() + ": " + id + " va a la piscina de niños");
                parque.entrarPiscinaNiños();
                dormir();
                paso.mirar();
                parque.salirPiscinaNiños();
                break;
            case 2:
                buf.añadirMensaje(parque.dameHoraActual() + ": " + id + " va a la piscina de olas");
                parque.entrarPiscinaOlas();
                dormir();
                paso.mirar();
                parque.salirPiscinaOlas();
                break;
            case 3:
                buf.añadirMensaje(parque.dameHoraActual() + ": " + id + " va a la piscina grande");
                parque.entrarPiscinaGrande();
                dormir();
                paso.mirar();
                parque.salirPiscinaGrande();
                break;
             case 4:
                buf.añadirMensaje(parque.dameHoraActual() + ": " + id + " va a las tumbonas");
                parque.entrarTumbonas();
                dormir();
                paso.mirar();
                parque.salirTumbonas();
                break;
            case 5:
                buf.añadirMensaje(parque.dameHoraActual() + ": " + id + " va a los toboganes");
                parque.entrarToboganes();
                dormir();
                paso.mirar();
                parque.salirToboganes();
                break;
        }
        buf.añadirMensaje(parque.dameHoraActual() + ": " + id + " va a los vestuarios para prepararse a abandonar el parque");
        parque.entrarVestuario(id);
        paso.mirar();
        buf.añadirMensaje(parque.dameHoraActual() + ": " + id + " sale del parque");
        parque.salirParque();
        
    }

    public void dormir() {
        try {
            sleep(1000 + (int) (Math.random() * 10000));
        } catch (InterruptedException ex) {
            ex.toString();
        }
    }
}
