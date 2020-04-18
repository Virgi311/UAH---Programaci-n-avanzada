package clienteServidor;

import concurrencia.Paso;
import java.io.IOException;
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
    
    private final int CONEXIONES_MAXIMAS = 10;
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
            if( conexiones.size() < CONEXIONES_MAXIMAS ) {
                try {
                    Socket socket = servidor.accept();
                    if( socket != null ) {
                        Conexion conexion = new Conexion( conexiones.size(), socket, this );
                        conexiones.add( conexion );
                        conexion.start();
                    }
                } catch( IOException ex ) {
                    System.out.println( "ERROR: " + ex );
                }
            } else {
                System.out.println("Numero maximo de conexiones superado.");
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
        paso.notifyTodos();
    }
    
    /*public void abrirEntrada() {
        System.out.println("Ejecutando metodo Abrir Entrada()");
        paso.openEntry();
    }
    
    public void cerrarEntrada() {
        System.out.println("Ejecutando metodo Cerrar Entrada()");
        paso.closeEntry();
    }*/
    
    public void cerrar() {
        System.out.println("Ejecutando el cierre de toda la aplicacion");
        
        for (int i = 0; i < conexiones.size(); i++) {
            System.out.println("Cerrando la conexion: " + ( i + 1 ) );
            conexiones.get(i).cerrar();
        }
        
        try {
            run = false;
            servidor.close();
        } catch( IOException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    }   
}