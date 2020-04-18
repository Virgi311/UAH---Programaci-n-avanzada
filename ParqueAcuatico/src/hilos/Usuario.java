package hilos;

import concurrencia.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authores 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Usuario extends Thread {

    private final Parque parque;
    private final CyclicBarrier barrera;
    private Usuario acompañante;
    private final Paso paso;
    
    private int identificador;
    private int edad;
    private String nombre;
    private boolean esAcompañante;
    private final int numAtracciones;
    
    public Usuario(Parque parque, CyclicBarrier barrera, int identificador, int edad, int numAtracciones, Paso paso) {
        this.parque = parque;
        this.barrera = barrera;
        this.identificador = identificador;
        this.edad = edad;
        this.numAtracciones = numAtracciones;

        this.esAcompañante = false;
        this.nombre = "ID" + identificador + "-" + edad;
        this.paso = paso;
    }

    @Override
    public void run() {
        paso.mirar();
        parque.entrarParque(this);
            
        paso.mirar();
        parque.getVestuario().entrarVestuarios(this);
        hacerSleep();
            
        paso.mirar();
        parque.getVestuario().salirVestuarios(this);
        
        if( !esAcompañante && edad > 10 ) {
            while( numAtracciones > 0 ) {
                atraccionAleatoria("peque");
            }
        } else if( edad <= 10 ) {
            while( numAtracciones > 0 ) {
                atraccionAleatoria("mayor");
            }
        } else {
            while( numAtracciones > 0 ) {
                atraccionAleatoria("acompañante");
            }
        }
        
        paso.mirar();
        parque.getVestuario().entrarVestuarios(this);
        hacerSleep();
            
        paso.mirar();
        parque.getVestuario().salirVestuarios(this);

        paso.mirar();
        parque.salirParque();
    }

    public void hacerSleep() {
        try {
            sleep(3000);
        } catch (InterruptedException ex) {
            System.out.println("ERROR: " + ex);
        }
    }
    
    public void atraccionAleatoria(String tipo) {
        int num = (int)(5 * Math.random());
        
        switch( num ) {
            case 0:
                try {
                    paso.mirar();
                    parque.getPiscinaNiños().entrarPiscinaNiños(this);
                    
                    sleep( 1000 + (int)( 2000 * Math.random() ) );
                    
                    paso.mirar();
                    parque.getPiscinaNiños().salirPiscinaNiños(this);
                } catch(InterruptedException ex) {
                    System.out.println("ERROR: " + ex);
                }
                
                break;
            
            case 1:
                try {
                    paso.mirar();
                    parque.getPiscinaOlas().entrarPiscinaOlas(this);
                    
                    sleep( 2000 + (int)( 5000 * Math.random() ) );
                    
                    paso.mirar();
                    parque.getPiscinaOlas().salirPiscinaOlas(this);
                } catch(InterruptedException ex) {
                    System.out.println("ERROR: " + ex);
                }
                
                break;
                
            case 2:
                try {
                    paso.mirar();
                    parque.getPiscinaGrande().entrarPiscinaGrande(this);
                    
                    sleep( 3000 + (int)( 2000 * Math.random() ) );
                    
                    paso.mirar();
                    parque.getPiscinaGrande().salirPiscinaGrande(this);
                } catch(InterruptedException ex) {
                    System.out.println("ERROR: " + ex);
                }
                
                break;
                
            case 3:
                try {
                    paso.mirar();
                    parque.getTumbonas().entrarTumbonas(this);
                    
                    sleep( 3000 + (int)( 2000 * Math.random() ) );
                    
                    paso.mirar();
                    parque.getTumbonas().salirTumbonas(this);
                } catch(InterruptedException ex) {
                    System.out.println("ERROR: " + ex);
                }
                
                break;
                
            case 4:
                try {
                    paso.mirar();
                    sleep( 3000 + (int)( 2000 * Math.random() ) );
                    paso.mirar();
                } catch(InterruptedException ex) {
                    System.out.println("ERROR: " + ex);
                }
                
                break;
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
    
    public int getNumAtracciones() {
        return numAtracciones;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}