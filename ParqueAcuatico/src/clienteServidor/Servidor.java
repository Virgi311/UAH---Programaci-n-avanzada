package clienteServidor;

import concurrencia.Paso;
import java.io.IOException;
import static java.lang.System.exit;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @authores 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Servidor extends Thread {
    
    private ServerSocket servidor;
    private InetAddress ip;
    private ArrayList<Conexion> conexiones;
    private boolean run = true;
    
    private Paso paso;
    
    public Servidor( Paso paso ) {
        this.paso = paso;
        try {
            servidor = new ServerSocket( 5000 );
            ip = InetAddress.getLocalHost();
            System.out.println( "La direccion IP del servidor es: " + ip.getHostAddress() );
            conexiones = new ArrayList<>();
        } catch( IOException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    }
    
    @Override
    public void run() {
        if( servidor == null ) {
            return;
        }
        
        while( run ) {
            try {
                Socket socket = servidor.accept();
                if( socket != null ) {
                    Conexion conexion = new Conexion( conexiones.size() + 1, socket, this );
                    conexiones.add( conexion );
                    conexion.start();
                }
            } catch( IOException ex ) {
                System.out.println( "ERROR: " + ex );
            }
        }
    }
    
    public void eliminar( Conexion conexion ) {
        conexiones.remove( conexion );
    }
    
    public void detener() {
        System.out.println("Ejecutando metodo Detener()");
        paso.detener();
    }
    
    public void reanudar() {
        System.out.println("Ejecutando metodo Reanudar()");
        paso.reanudar();
    }
    
    public void cerrar( boolean finalizar ) {
        System.out.println("Ejecutando el cierre de toda la aplicacion");
        
        for (int i = 0; i < conexiones.size(); i++) {
            System.out.println("Cerrando la conexion: " + conexiones.get(i).getIdName() );
            conexiones.get(i).cerrar( true );
        }
        
        run = false;
        
        if( finalizar ) {
            exit(0);
        }
    }   
}