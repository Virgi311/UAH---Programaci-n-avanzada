package hilos;

import concurrencia.*;
import java.util.concurrent.CyclicBarrier;
import util.FuncionesGenerales;
        
/**
 * Clase CreaUsuarios
 *
 * Define la forma en que se crean los hilos usuarios del parque
 *
 * @author
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class CreaUsuarios extends Thread {
    private final int CAPACIDAD = 5000;
    private final int MAYORIA_EDAD = 18;
    private final Parque parque;
    private final Paso paso;

    private final FuncionesGenerales fg;
    
    public CreaUsuarios(Parque parque, Paso paso, FuncionesGenerales fg) {
        this.parque = parque;
        this.paso = paso;
        
        this.fg = fg;
    } // Cierre del método

    @Override
    public void run() {
        for( int id = 1; id <= CAPACIDAD; id++ ) {
            //Barrera ciclica para que la compartan los usuarios niños de 10 o menos años y sus acompañantess
            CyclicBarrier barrera = new CyclicBarrier(2);
            //Creamos la edad y el numero de atracciones que van a realizar de forma aleatoria
            int edadUsuario = getAleatorio(1, 50);
            Usuario usuarioPrincipal = new Usuario(parque, barrera, id, edadUsuario, getAleatorio(5, 15), paso, fg);
            Usuario usuarioAcompañante = null;
            
            fg.writeDebugFile("Usuario: " + usuarioPrincipal.getCodigo() + " creado con numero de atracciones: " + usuarioPrincipal.getNumAtracciones() + ".\n");
            
            if( edadUsuario < 11 ){
                id++;
                //Si tiene 10 años o menos se crea el usuario acompañante con una edad aleatoria de mas de 18 años, y el numero de atracciones del niño
                usuarioAcompañante = new Usuario(parque, barrera, id, getAleatorio(MAYORIA_EDAD, 50), usuarioPrincipal.getNumAtracciones(), paso, fg);
                usuarioPrincipal.setCodigo(usuarioPrincipal.getCodigo() + "-" + usuarioAcompañante.getIdentificador());
                usuarioAcompañante.setAcompañante(usuarioPrincipal);
                usuarioAcompañante.setCodigo(usuarioAcompañante.getCodigo() + "-" + usuarioPrincipal.getIdentificador());
                usuarioAcompañante.setEsAcompañante(true);
                usuarioPrincipal.setAcompañante(usuarioAcompañante);
                
                fg.writeDebugFile("Usuario acompañante: " + usuarioAcompañante.getCodigo() + " creado para usuario: " + usuarioPrincipal.getCodigo() + ".\n");
            }
            
            usuarioPrincipal.start();
            //Hacemos que el usuario acompañante empiece despues del niño 
            if (usuarioAcompañante != null) {
                usuarioAcompañante.start();
            }
            
            fg.dormir(400, 700);
            paso.mirar();
        }
    } // Cierre del método

    //Metodo para crear la edad y el numero de atracciones de forma aleatoria
    private int getAleatorio(int min, int max) {
        return (int)( min + ( ( max - min ) * Math.random() ) );
    } // Cierre del método
} // Cierre de la clase