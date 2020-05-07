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
    private boolean tryPiscinaNiños;
    private boolean tryPiscinaOlas;
    private boolean trytumbonas;
    private boolean trytoboganes;
    private boolean accesoPermitido;
    
    private final FuncionesGenerales fg;
    
    public Usuario(Parque parque, CyclicBarrier barrera, int identificador, int edad, int numAtracciones, Paso paso, FuncionesGenerales fg) {
        this.parque = parque;
        this.barrera = barrera;
        this.identificador = identificador;
        this.edad = edad;
        this.numAtracciones = numAtracciones;
        this.tryPiscinaNiños = false;
        this.tryPiscinaOlas = false;
        this.trytumbonas = false;
        this.trytoboganes = false;
        
        this.accesoPermitido = true;

        this.esAcompañante = false;
        this.codigo = "ID" + identificador + "-" + edad;
        this.paso = paso;
        
        this.fg = fg;
        this.controlNumAtracciones = 0;
    } // Cierre del método

    @Override
    public void run() {
        //Si es menos de 18 aumentamos el contador de menores
        if( edad < 18 ) {
            parque.setMenoresEntra();
        }
        
        paso.mirar();
        parque.entrarParque(this);
            
        paso.mirar();
        parque.getVestuario().entrarVestuarios(this);
        
        if( esAcompañante ) {
            //Si es acompañante esperamos en la barrera ciclica
            try {
                barrera.await();
            } catch( BrokenBarrierException | InterruptedException ex ) {
                System.out.println("ERROR: " + ex);
            }
        } else if( edad < 11 ) {
            //Si es niño con acompañante dormimos y despues entramos en laa barrera ciclica
            fg.dormir(3000, 3000);
            try {
                barrera.await();
            } catch( BrokenBarrierException | InterruptedException ex ) {
                System.out.println("ERROR: " + ex);
            }
        } else {
            fg.dormir(3000, 3000);
        }
        
        paso.mirar();
        parque.getVestuario().salirVestuarios(this);
        
        if( !esAcompañante && edad > 10 ) {
            //Si no es acompañante y tiene mas de 10 años
            while( numAtracciones > controlNumAtracciones ) {
                atraccionAleatoria(1);
                accesoPermitido = true;
            }
        } else {
            //Es acompañante o un niño de 10 o menos años
            while( numAtracciones > controlNumAtracciones ) {
                atraccionAleatoria(2);
                accesoPermitido = true;
            }
        }
        
        paso.mirar();
        
        parque.getVestuario().entrarVestuarios(this);
        
        if( esAcompañante ) {
            try {
                barrera.await();
            } catch( BrokenBarrierException | InterruptedException ex ) {
                System.out.println("ERROR: " + ex);
            }
        } else if( edad < 11 ) {
            fg.dormir(3000, 3000);
            try {
                barrera.await();
            } catch( BrokenBarrierException | InterruptedException ex ) {
                System.out.println("ERROR: " + ex);
            }
        } else {
            fg.dormir(3000, 3000);
        }
        
        paso.mirar();
        parque.getVestuario().salirVestuarios(this);
        
        paso.mirar();
        parque.salirParque(this);
        
        //Si es menos de 18 años disminuimos el contador de menores
        if( edad < 18 ) {
            parque.setMenoresSale();
        }
    } // Cierre del método
    
    //Metodo para escoger una atraccion de forma aleatoria
    public void atraccionAleatoria(int tipo) {
        //Generamos un numero de forma aleatoria
        int num = (int)(5 * Math.random());
        
        if( tipo == 2 && !esAcompañante ) {
            //Si es un niño de 10 o menos años le asignamos la atraccion que va a realizar y lo metemos en laa barrera ciclica
            actividadNiño = num;
            try {
                barrera.await();
            } catch( BrokenBarrierException | InterruptedException ex ) {
                System.out.println("ERROR: " + ex);
            }
        } else if ( esAcompañante ) {
            //Si es un acompañante lo metemos en la barrera ciclica para que cuando esten los dos salga y recoja la actividad que va a realizar el niño
            try {
                barrera.await();
            } catch( BrokenBarrierException | InterruptedException ex ) {
                System.out.println("ERROR: " + ex);
            }
            num = acompañante.getActividadNiño();
        }
    
        //Control para no intentar acceder a una atraccion de la que ya nos denegaron el acceso
        if( ( tryPiscinaNiños && num == 0 ) || ( tryPiscinaOlas && num == 1 ) || ( trytumbonas && num == 3 ) || ( trytoboganes && num == 4 ) ) {
            switch( num ) {
                case 0:
                    fg.writeDebugFile("Usuario: " + codigo + " ya intento entrar en picina niños, no lo vuelve a intentar.\n");
                    break;
                    
                case 1:
                    fg.writeDebugFile("Usuario: " + codigo + " ya intento entrar en piscina olas, no lo vuelve a intentar.\n");
                    break;
                
                case 3:
                    fg.writeDebugFile("Usuario: " + codigo + " ya intento entrar en tumbonas, no lo vuelve a intentar.\n");
                    break;
                 
                case 4:
                    fg.writeDebugFile("Usuario: " + codigo + " ya intento entrar en toboganes, no lo vuelve a intentar.\n");
                    break;
            }
        } else {
            switch( num ) {
                case 0: //Piscina de niños
                    fg.writeDebugFile("Usuario: " + codigo + " inicia atraccion numero: " + controlNumAtracciones + " y la atraccion es la Piscina niños.\n");
                    paso.mirar();
                    if( parque.getPiscinaNiños().entrarPiscinaNiños(this) ) {
                        if( esAcompañante ) {
                            //Si es acompañante entra en la barrera ciclica
                            try {
                                barrera.await();
                            } catch( BrokenBarrierException | InterruptedException ex ) {
                                System.out.println("ERROR: " + ex);
                            }
                        } else if( edad < 11 ){
                            //Si es un niño de menos de 10 años se duerme y despues entra en la barrear ciclica para continuar el acompañante y el niño
                            fg.dormir(1000, 3000);
                            try {
                                barrera.await();
                            } catch( BrokenBarrierException | InterruptedException ex ) {
                                System.out.println("ERROR: " + ex);
                            }
                        } else {
                            fg.dormir(1000, 3000);
                        }

                        paso.mirar();
                        parque.getPiscinaNiños().salirPiscinaNiños(this);
                      
                        controlNumAtracciones++;
                    } else {
                        //Si nos rechazan activamos la variable para no volver a intentarlo y hacemos que agote una atraccion
                        tryPiscinaNiños = true;
                        controlNumAtracciones++;
                        fg.writeDebugFile("Usuario: " + codigo + " no se le permite el acceso a la piscina niños.\n");
                    }

                    break;

                case 1: //Piscina de olas
                    fg.writeDebugFile("Usuario: " + codigo + " inicia atraccion numero: " + controlNumAtracciones + " y la atraccion es la Piscina de olas.\n");
                    paso.mirar();
                    if( parque.getPiscinaOlas().entrarPiscinaOlas(this) ) {
                        if( esAcompañante ) {
                            //Si es acompañante entra en la barrera ciclica
                            try {
                                barrera.await();
                            } catch( BrokenBarrierException | InterruptedException ex ) {
                                System.out.println("ERROR: " + ex);
                            }
                        } else if( edad < 11 ) {
                            //Si es un niño de menos de 10 años se duerme y despues entra en la barrear ciclica para continuar el acompañante y el niño
                            fg.dormir(2000, 5000);
                            try {
                                barrera.await();
                            } catch( BrokenBarrierException | InterruptedException ex ) {
                                System.out.println("ERROR: " + ex);
                            }
                        } else {
                            fg.dormir(2000, 5000);
                        }

                        paso.mirar();
                        parque.getPiscinaOlas().salirPiscinaOlas(this);
                        
                        controlNumAtracciones++;
                    } else {
                        //Si nos rechazan activamos la variable para no volver a intentarlo y hacemos que agote una atraccion
                        tryPiscinaOlas = true;
                        controlNumAtracciones++;
                        fg.writeDebugFile("Usuario: " + codigo + " no se le permite el acceso a la piscina olas.\n");
                    }

                    break;

                case 2: //Piscina grande
                    fg.writeDebugFile("Usuario: " + codigo + " inicia atraccion numero: " + controlNumAtracciones + " y la atraccion es la Piscina grande.\n");
                    paso.mirar();
                    parque.getPiscinaGrande().entrarPiscinaGrande(this);
                    if( esAcompañante ) {
                        //Si es acompañante entra en la barrera ciclica
                        try {
                            barrera.await();
                        } catch( BrokenBarrierException | InterruptedException ex ) {
                            System.out.println("ERROR: " + ex);
                        }
                    } else if( edad < 11 ) {
                        //Si es un niño de menos de 10 años se duerme y despues entra en la barrear ciclica para continuar el acompañante y el niño
                        fg.dormir(3000, 5000);
                        try {
                            barrera.await();
                        } catch( BrokenBarrierException | InterruptedException ex ) {
                            System.out.println("ERROR: " + ex);
                        }
                    } else {
                        fg.dormir(3000, 5000);
                    }

                    paso.mirar();
                    parque.getPiscinaGrande().salirPiscinaGrande(this);
                    
                    controlNumAtracciones++;

                    break;

                case 3: //Tumbonas
                    fg.writeDebugFile("Usuario: " + codigo + " inicia atraccion numero: " + controlNumAtracciones + " y la atraccion es las tumbonas.\n");
                    paso.mirar();
                    if( parque.getTumbonas().entrarTumbonas(this) ) {
                        fg.dormir(2000, 4000);

                        paso.mirar();
                        parque.getTumbonas().salirTumbonas(this);
                        controlNumAtracciones++;
                    } else {
                        //Si nos rechazan activamos la variable para no volver a intentarlo y hacemos que agote una atraccion
                        trytumbonas = true;
                        controlNumAtracciones++;
                        fg.writeDebugFile("Usuario: " + codigo + " no se le permite el acceso a las tumbonas.\n");
                    }

                    break;

                case 4: //Toboganes
                    fg.writeDebugFile("Usuario: " + getCodigo() + " inicia atraccion numero: " + controlNumAtracciones + " y la atraccion es los toboganes.\n");
                    paso.mirar();
                    if( parque.getToboganes().entrarToboganes(this) ) {
                        fg.dormir(2000, 3000);

                        paso.mirar();
                        parque.getToboganes().accesoPiscinaGrande(this);
                        controlNumAtracciones++;
                    } else {
                        //Si nos rechazan activamos la variable para no volver a intentarlo y hacemos que agote una atraccion
                        trytoboganes = true;
                        controlNumAtracciones++;
                        fg.writeDebugFile("Usuario: " + codigo + " no se le permite el acceso a los toboganes.\n");
                    }

                    break;
            }
        }
        
        if( esAcompañante ) {
            //Si es acompañante entramos en la barrera ciclica para ir con el niño, y cuando llega obtenemos el numero de atracciones que lleva
            try {
                barrera.await();
            } catch( BrokenBarrierException | InterruptedException ex ) {
                System.out.println("ERROR: " + ex);
            }
            controlNumAtracciones = acompañante.getControlNumAtracciones();
        } else if( edad < 11 ) {
            //Si es menor de 10 años entramos en la barrera ciclica para ir con el acompañante
            try {
                barrera.await();
            } catch( BrokenBarrierException | InterruptedException ex ) {
                System.out.println("ERROR: " + ex);
            }
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

    public void setTryPiscinaOlas(boolean tryPiscinaOlas) {
        this.tryPiscinaOlas = tryPiscinaOlas;
    } // Cierre del método

    public boolean getAccesoPermitido() {
        return accesoPermitido;
    } // Cierre del método

    public void setAccesoPermitido(boolean accesoPermitido) {
        this.accesoPermitido = accesoPermitido;
    } // Cierre del método

    public CyclicBarrier getBarrera() {
        return barrera;
    } // Cierre del método
} // Cierre de la clase