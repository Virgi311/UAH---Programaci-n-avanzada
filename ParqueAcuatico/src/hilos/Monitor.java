/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import concurrencia.Buffer;
import concurrencia.Parque;
import concurrencia.Paso;
import static java.lang.Thread.sleep;
        

/**
 *
 * @authors Virginia Vallejo y Javier Gonzalez
 */

public class Monitor extends Thread {
    
    private int id;
    private Parque parque;
    private Paso paso;
    private Buffer buf;

    
    public Monitor() {
        this.id = id;
        this.parque = parque;
        this.paso = paso;
        this.buf = buf;
    }
    
    @Override
    public void run(){
        while (true) {
            paso.mirar();
            if(id.equals("monitor1")){
                buf.añadirMensaje(parque.dameHoraActual() + ": Monitor 1 atiende a: " + parque.getCajeraUnoAtiende());
            } if(id.equals("monitor2")){
                buf.añadirMensaje(parque.dameHoraActual() + ": Monitor 2 atiende a: " + parque.getCajeraUnoAtiende());
            } if(id.equals("monitor3")){
                buf.añadirMensaje(parque.dameHoraActual() + ": Monitor 3 atiende a: " + parque.getCajeraUnoAtiende());
            }if(id.equals("monitor4")){
                buf.añadirMensaje(parque.dameHoraActual() + ": Monitor 4 atiende a: " + parque.getCajeraUnoAtiende());
            }if(id.equals("monitor5")){
                buf.añadirMensaje(parque.dameHoraActual() + ": Monitor 5 atiende a: " + parque.getCajeraUnoAtiende());
            }if(id.equals("monitor6")){
                buf.añadirMensaje(parque.dameHoraActual() + ": Monitor 6 atiende a: " + parque.getCajeraUnoAtiende());
            }if(id.equals("monitor7")){
                buf.añadirMensaje(parque.dameHoraActual() + ": Monitor 7 atiende a: " + parque.getCajeraUnoAtiende());
            } else{
                buf.añadirMensaje(parque.dameHoraActual() + ": Monitor 8 atiende a: " + parque.getCajeraDosAtiende());
            }
            dormir();
            paso.mirar();
        }
    }
    
    public void dormir() {
        try {
            sleep(3000 + (int) (Math.random() * 2000));
        } catch (InterruptedException ex) {
            ex.toString();
        }
    }   
 }