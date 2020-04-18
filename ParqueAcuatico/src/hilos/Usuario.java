/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import concurrencia.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authors Virginia Vallejo y Javier Gonzalez
 */

public class Usuario extends Thread {

    private final Parque parque;
    private final CyclicBarrier barrera;
    private int identificador;
    private int edad;
    private String nombre;
    private boolean esAcompañante;
    private Usuario acompañante;
    
    public Usuario(Parque parque, CyclicBarrier barrera, int identificador, int edad) {
        this.parque = parque;
        this.barrera = barrera;
        this.identificador = identificador;
        this.edad = edad;

        esAcompañante = false;
        nombre = "ID" + identificador + "-" + edad;
    }

    @Override
    public void run() {
        if (!esAcompañante && edad > 10) { //un niño sin acompañante o un adulto
            try {
            parque.entrarParque(this);
            parque.getVestuario().entrarVestuarios(this);
            hacerSleep();
            parque.getVestuario().salirVestuarios(this);
            parque.getPiscinaNiños().entrarPiscinaNiños(this);
            sleep( 1000 + (int)( 2000 * Math.random() ) );
            parque.getPiscinaNiños().salirPiscinaNiños(this);
            parque.getPiscinaOlas().entrarPiscinaOlas(this);
            sleep( 2000 + (int)( 5000 * Math.random() ) );
            parque.getPiscinaOlas().salirPiscinaOlas(this);
            parque.getPiscinaGrande().entrarPiscinaGrande(this);
            sleep( 3000 + (int)( 2000 * Math.random() ) );
            parque.getPiscinaGrande().salirPiscinaGrande(this);
            parque.getTumbonas().entrarTumbonas(this);
            sleep( 3000 + (int)( 2000 * Math.random() ) );
            parque.getTumbonas().salirTumbonas(this);
            parque.getVestuario().entrarVestuarios(this);
            hacerSleep();
            parque.getVestuario().salirVestuarios(this);
            parque.salirParque();
            } catch (InterruptedException ex) {
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (edad <= 10) { // niño que necesita acompañante
            try {
                parque.entrarParque(this);
                barrera.await();
                parque.getVestuario().entrarVestuarios(this);
                hacerSleep();
                barrera.await();       
                parque.getVestuario().salirVestuarios(this);
                parque.getPiscinaNiños().entrarPiscinaNiños(this);
                sleep( 1000 + (int)( 2000 * Math.random() ) );
                parque.getPiscinaNiños().salirPiscinaNiños(this);
                parque.getPiscinaOlas().entrarPiscinaOlas(this);
                sleep( 2000 + (int)( 5000 * Math.random() ) );
                parque.getPiscinaOlas().salirPiscinaOlas(this);
                parque.getPiscinaGrande().entrarPiscinaGrande(this);
                sleep( 3000 + (int)( 2000 * Math.random() ) );
                parque.getPiscinaGrande().salirPiscinaGrande(this);
                parque.getTumbonas().entrarTumbonas(this);
                sleep( 3000 + (int)( 2000 * Math.random() ) );
                parque.getTumbonas().salirTumbonas(this);
                parque.getVestuario().entrarVestuarios(this);
                hacerSleep();
                parque.getVestuario().salirVestuarios(this);
                parque.salirParque();

            } catch (InterruptedException ex) {
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BrokenBarrierException ex) {
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else { //Actúa como acompañante
            try {
                barrera.await();
                parque.entrarParque(this);
                parque.getVestuario().entrarVestuarios(this);
                barrera.await();
                parque.getVestuario().salirVestuarios(this);
                parque.getPiscinaNiños().entrarPiscinaNiños(this);
                sleep( 1000 + (int)( 2000 * Math.random() ) );
                parque.getPiscinaNiños().salirPiscinaNiños(this); 
                parque.getPiscinaOlas().entrarPiscinaOlas(this);
                sleep( 2000 + (int)( 5000 * Math.random() ) );
                parque.getPiscinaOlas().salirPiscinaOlas(this);
                parque.getPiscinaGrande().entrarPiscinaGrande(this);
                sleep( 3000 + (int)( 2000 * Math.random() ) );
                parque.getPiscinaGrande().salirPiscinaGrande(this);
                parque.getTumbonas().entrarTumbonas(this);
                sleep( 3000 + (int)( 2000 * Math.random() ) );
                parque.getTumbonas().salirTumbonas(this);
                parque.getVestuario().entrarVestuarios(this);
                hacerSleep();
                parque.getVestuario().salirVestuarios(this);
                parque.salirParque();
            } catch (InterruptedException ex) {
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BrokenBarrierException ex) {
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }

    public void hacerSleep() {
        try {
            sleep(3000);
        } catch (InterruptedException ex) {

        }
    }
    
    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Usuario getAcompañante() {
        return acompañante;
    }

    public void setAcompañante(Usuario acompañante) {
        this.acompañante = acompañante;
        esAcompañante = acompañante.getEdad() >= 18;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public boolean getEsAcompañante() {
        return esAcompañante;
    }

    public void setEsAcompañante(boolean esAcompañante) {
        this.esAcompañante = esAcompañante;
    }
    
    
    @Override
    public String toString() {
        return nombre;
    }

  
}
