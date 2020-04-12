/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import concurrencia.*;

/**
 *
 * @authors Virginia Vallejo y Javier Gonzalez
 */

public class Usuario extends Thread {

    private final Parque parque;
    private final Paso paso;
    private int identificador;
    private int edad;
    private String nombre;
    private boolean esAcompañante;
    private Usuario acompañante;
    
    public Usuario(Parque parque, Paso paso, int identificador, int edad) {
        this.parque = parque;
        this.paso = paso;
        this.identificador = identificador;
        this.edad = edad;

        esAcompañante = false;
        nombre = "ID" + identificador + "-" + edad;
    }

    @Override
    public void run() {
        if (!esAcompañante && edad > 10) { //un niño sin acompañante o un adulto
            parque.entrarParque(this);
            parque.getVestuario().entrarVestuarios(this);
            hacerSleep();
            parque.getVestuario().salirVestuarios(this);
            parque.getPiscinaNiños().entrarPiscinaNiños(this);
            hacerSleep();
            parque.getPiscinaNiños().salirPiscinaNiños(this);
            parque.salirParque();

        } else if (edad <= 10) { // niño que necesita acompañante
            
            parque.entrarParque(this);
            paso.reanudarUno();
            parque.getVestuario().entrarVestuarios(this);
            paso.detener();
            hacerSleep();
            paso.reanudarUno();
            parque.getVestuario().salirVestuarios(this);
            parque.getPiscinaNiños().entrarPiscinaNiños(this);
            hacerSleep();
            parque.getPiscinaNiños().salirPiscinaNiños(this);
            parque.salirParque();

        } else { //Actúa como acompañante
            paso.mirar();
            parque.entrarParque(this);
            parque.getVestuario().entrarVestuarios(this);
            paso.mirar();
            parque.getVestuario().salirVestuarios(this);
            parque.getPiscinaNiños().entrarPiscinaNiños(this);
            hacerSleep();
            parque.getPiscinaNiños().salirPiscinaNiños(this);            
            parque.salirParque();
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
