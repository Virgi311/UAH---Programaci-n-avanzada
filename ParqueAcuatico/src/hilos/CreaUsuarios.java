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
    private final int capacidad = 5000;
    private final int mayoria_edad = 18;
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
        for( int id = 1; id <= capacidad; id++ ) {
            CyclicBarrier barrera = new CyclicBarrier(2);
            int edadUsuario = getAleatorio(1, 50);
            Usuario usuarioPrincipal = new Usuario(parque, barrera, id, edadUsuario, getAleatorio(10, 15), paso, fg);
            Usuario usuarioAcompañante = null;
            if( edadUsuario < 11 ){
                id++;
 
                usuarioAcompañante = new Usuario(parque, barrera, id, getAleatorio(mayoria_edad, 50), usuarioPrincipal.getNumAtracciones(), paso, fg);
                usuarioPrincipal.setCodigo(usuarioPrincipal.getCodigo() + "-" + usuarioAcompañante.getIdentificador());
                usuarioAcompañante.setAcompañante(usuarioPrincipal);
                usuarioAcompañante.setCodigo(usuarioAcompañante.getCodigo() + "-" + usuarioPrincipal.getIdentificador());
                usuarioAcompañante.setEsAcompañante(true);
                usuarioPrincipal.setAcompañante(usuarioAcompañante);
            }
            
            usuarioPrincipal.start();
            if (usuarioAcompañante != null) {
                usuarioAcompañante.start();
            }
            fg.dormir(400, 700);
            paso.mirar();
        }
    } // Cierre del método

    private int getAleatorio(int min, int max) {
        return (int)( min + ( ( max - min ) * Math.random() ) );
    } // Cierre del método
} // Cierre de la clase