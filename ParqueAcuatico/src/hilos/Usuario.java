package hilos;

import concurrencia.*;
import java.util.concurrent.CyclicBarrier;
import util.FuncionesGenerales;

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
    private String codigo;
    private boolean esAcompañante;
    private final int numAtracciones;
    
    private final FuncionesGenerales fg;
    
    public Usuario(Parque parque, CyclicBarrier barrera, int identificador, int edad, int numAtracciones, Paso paso, FuncionesGenerales fg) {
        this.parque = parque;
        this.barrera = barrera;
        this.identificador = identificador;
        this.edad = edad;
        this.numAtracciones = numAtracciones;

        this.esAcompañante = false;
        this.codigo = "ID" + identificador + "-" + edad;
        this.paso = paso;
        
        this.fg = fg;
    }

    @Override
    public void run() {
        paso.mirar();
        parque.entrarParque(this);
            
        paso.mirar();
        parque.getVestuario().entrarVestuarios(this);
        fg.dormir(3000, 0);
            
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
        fg.dormir(3000, 0);
            
        paso.mirar();
        parque.getVestuario().salirVestuarios(this);

        paso.mirar();
        parque.salirParque();
    }
    
    public void atraccionAleatoria(String tipo) {
        int num = (int)(5 * Math.random());
        
        switch( num ) {
            case 0:
                paso.mirar();
                parque.getPiscinaNiños().entrarPiscinaNiños(this);
                   
                fg.dormir(1000, 20000);
                    
                paso.mirar();
                parque.getPiscinaNiños().salirPiscinaNiños(this);
                
                break;
            
            case 1:
                paso.mirar();
                parque.getPiscinaOlas().entrarPiscinaOlas(this);
                    
                fg.dormir(2000, 5000);
                    
                paso.mirar();
                parque.getPiscinaOlas().salirPiscinaOlas(this);
                
                break;
                
            case 2:
                paso.mirar();
                parque.getPiscinaGrande().entrarPiscinaGrande(this);
                    
                fg.dormir(3000, 2000);
                    
                paso.mirar();
                parque.getPiscinaGrande().salirPiscinaGrande(this);
                
                break;
                
            case 3:
                paso.mirar();
                parque.getTumbonas().entrarTumbonas(this);
                    
                fg.dormir(3000, 2000);
                    
                paso.mirar();
                parque.getTumbonas().salirTumbonas(this);
                
                break;
                
            case 4:
                paso.mirar();
                
                fg.dormir(3000, 2000);
                
                paso.mirar();
                
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
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String nombre) {
        this.codigo = nombre;
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
        return codigo;
    }
}