package hilos;

import concurrencia.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import util.FuncionesGenerales;

/**
 * Clase Usuario
 *
 * Define la forma y funcionamiento de los usuarios del parque
 *
 * @author 
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
    private int controlNumAtracciones;
    private int actividadNiño;
    
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
        this.controlNumAtracciones = 0;
    } // Cierre del método

    @Override
    public void run() {
        if( edad < 18 ) {
            parque.setMenoresEntra();
        }
        
        paso.mirar();
        parque.entrarParque(this);
            
        paso.mirar();
        parque.getVestuario().entrarVestuarios(this);
        fg.dormir(3000, 0);
            
        if( edad <= 10 || esAcompañante ) {
            try {
                barrera.await();
            } catch( BrokenBarrierException | InterruptedException ex ) {
                System.out.println("ERROR: " + ex);
            }
        }
        
        paso.mirar();
        parque.getVestuario().salirVestuarios(this);
        
        if( !esAcompañante && edad > 10 ) {
            while( numAtracciones > controlNumAtracciones ) {
                atraccionAleatoria(1);
            }
        } else if( edad <= 10 ) {
            while( numAtracciones > controlNumAtracciones ) {
                atraccionAleatoria(2);
            }
        } else {
            while( numAtracciones > controlNumAtracciones ) {
                atraccionAleatoria(3);
            }
        }
        
        paso.mirar();
        
        if( edad <= 10 ) {
            try {
                barrera.await();
            } catch( BrokenBarrierException | InterruptedException ex ) {
                System.out.println("ERROR: " + ex);
            }
        }
        
        parque.getVestuario().entrarVestuarios(this);
        fg.dormir(3000, 0);
            
        if( esAcompañante ) {
            try {
                barrera.await();
            } catch( BrokenBarrierException | InterruptedException ex ) {
                System.out.println("ERROR: " + ex);
            }
        }
        
        paso.mirar();
        parque.getVestuario().salirVestuarios(this);

        if( edad <= 10 ) {
            try {
                barrera.await();
            } catch( BrokenBarrierException | InterruptedException ex ) {
                System.out.println("ERROR: " + ex);
            }
        }
        
        paso.mirar();
        parque.salirParque();
        if( edad < 18 ) {
            parque.setMenoresSale();
        }
    } // Cierre del método
    
    public void atraccionAleatoria(int tipo) {
        int num = (int)(5 * Math.random());
        
        if( tipo == 2 ) {
            actividadNiño = num;
            try {
                barrera.await();
            } catch( BrokenBarrierException | InterruptedException ex ) {
                System.out.println("ERROR: " + ex);
            }
        } else if ( tipo == 3 ) {
            try {
                barrera.await();
            } catch( BrokenBarrierException | InterruptedException ex ) {
                System.out.println("ERROR: " + ex);
            }
            num = acompañante.getActividadNiño();
        }
        
        switch( num ) {
            case 0:
                paso.mirar();
                if( parque.getPiscinaNiños().entrarPiscinaNiños(this) ) {   
                    if( tipo == 3 ) {
                        try {
                            barrera.await();
                        } catch( BrokenBarrierException | InterruptedException ex ) {
                            System.out.println("ERROR: " + ex);
                        }
                    } else {
                        fg.dormir(1000, 20000);
                    }
                    
                    paso.mirar();
                    parque.getPiscinaNiños().salirPiscinaNiños(this);
                    if( tipo == 2 ) {
                        try {
                            barrera.await();
                        } catch( BrokenBarrierException | InterruptedException ex ) {
                            System.out.println("ERROR: " + ex);
                        }
                    }
                    controlNumAtracciones++;
                }
                
                break;
            
            case 1:
                paso.mirar();
                if( parque.getPiscinaOlas().entrarPiscinaOlas(this) ) {  
                    if( tipo == 3 ) {
                        try {
                            barrera.await();
                        } catch( BrokenBarrierException | InterruptedException ex ) {
                            System.out.println("ERROR: " + ex);
                        }
                    } else {
                        fg.dormir(2000, 50000);
                    }
                    
                    paso.mirar();
                    parque.getPiscinaOlas().salirPiscinaOlas(this);
                    if( tipo == 2 ) {
                        try {
                            barrera.await();
                        } catch( BrokenBarrierException | InterruptedException ex ) {
                            System.out.println("ERROR: " + ex);
                        }
                    }
                    controlNumAtracciones++;
                }
                
                break;
                
            case 2:
                paso.mirar();
                parque.getPiscinaGrande().entrarPiscinaGrande(this);
                
                if( tipo == 3 ) {
                    try {
                        barrera.await();
                    } catch( BrokenBarrierException | InterruptedException ex ) {
                        System.out.println("ERROR: " + ex);
                    }
                } else {
                    fg.dormir(3000, 20000);
                }
                    
                paso.mirar();
                parque.getPiscinaGrande().salirPiscinaGrande(this);
                if( tipo == 2 ) {
                    try {
                        barrera.await();
                    } catch( BrokenBarrierException | InterruptedException ex ) {
                        System.out.println("ERROR: " + ex);
                    }
                }
                controlNumAtracciones++;
                
                break;
                
            case 3:
                paso.mirar();
                if( parque.getTumbonas().entrarTumbonas(this) ) {
                    if( tipo == 3 ) {
                        try {
                            barrera.await();
                        } catch( BrokenBarrierException | InterruptedException ex ) {
                            System.out.println("ERROR: " + ex);
                        }
                    } else {
                        fg.dormir(3000, 20000);
                    }
                    
                    paso.mirar();
                    parque.getTumbonas().salirTumbonas(this);
                    if( tipo == 2 ) {
                        try {
                            barrera.await();
                        } catch( BrokenBarrierException | InterruptedException ex ) {
                            System.out.println("ERROR: " + ex);
                        }
                    }
                    controlNumAtracciones++;
                }
                
                break;
                
            case 4:
                paso.mirar();
                if( parque.getToboganes().entrarToboganes(this) ) {
                    if( tipo == 3 ) {
                        try {
                            barrera.await();
                        } catch( BrokenBarrierException | InterruptedException ex ) {
                            System.out.println("ERROR: " + ex);
                        }
                    } else {
                        fg.dormir(3000, 20000);
                    }
                
                    paso.mirar();
                    parque.getToboganes().toboganApiscinaGrande(this);
                    if( tipo == 2 ) {
                        try {
                            barrera.await();
                        } catch( BrokenBarrierException | InterruptedException ex ) {
                            System.out.println("ERROR: " + ex);
                        }
                    }
                    controlNumAtracciones++;
                }

                break;
        }
    } // Cierre del método

    public int getIdentificador() {
        return identificador;
    } // Cierre del método

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    } // Cierre del método

    public Integer getEdad() {
        return edad;
    } // Cierre del método

    public void setEdad(Integer edad) {
        this.edad = edad;
    } // Cierre del método

    public Usuario getAcompañante() {
        return acompañante;
    } // Cierre del método

    public void setAcompañante(Usuario acompañante) {
        this.acompañante = acompañante;
    } // Cierre del método
    
    public String getCodigo() {
        return codigo;
    } // Cierre del método

    public void setCodigo(String nombre) {
        this.codigo = nombre;
    } // Cierre del método
    
    public boolean getEsAcompañante() {
        return esAcompañante;
    } // Cierre del método

    public void setEsAcompañante(boolean esAcompañante) {
        this.esAcompañante = esAcompañante;
    } // Cierre del método
    
    public int getNumAtracciones() {
        return numAtracciones;
    } // Cierre del método
    
    public int getControlNumAtracciones() {
        return controlNumAtracciones;
    } // Cierre del método
    
    public int getActividadNiño() {
        return actividadNiño;
    }
    
    @Override
    public String toString() {
        return codigo;
    } // Cierre del método
} // Cierre de la clase