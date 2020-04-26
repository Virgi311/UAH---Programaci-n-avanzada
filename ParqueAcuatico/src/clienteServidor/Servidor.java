package clienteServidor;

import concurrencia.Parque;
import concurrencia.Paso;
import hilos.Usuario;
import java.io.IOException;
import static java.lang.System.exit;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Clase Conexión Servidor 
 * 
 * Contiene información del servidor en la conexión
 *
 * @author
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Servidor extends Thread {
    
    private ServerSocket servidor;
    private InetAddress ip;
    private ArrayList<Conexion> conexiones;
    private boolean run = true;
    
    private Paso paso;
    private Parque parque;
    
    public Servidor( Paso paso, Parque parque ) {
        this.paso = paso;
        this.parque = parque;
        try {
            servidor = new ServerSocket( 5000 );
            ip = InetAddress.getLocalHost();
            System.out.println( "La direccion IP del servidor es: " + ip.getHostAddress() );
            conexiones = new ArrayList<>();
        } catch( IOException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    } // Cierre del método
    
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
    } // Cierre del método
    
    public void eliminar( Conexion conexion ) {
        conexiones.remove( conexion );
    } // Cierre del método
    
    public void detener() {
        System.out.println("Ejecutando metodo Detener()");
        paso.detener();
    } // Cierre del método
    
    public void reanudar() {
        System.out.println("Ejecutando metodo Reanudar()");
        paso.reanudar();
    } // Cierre del método
    
    public String buscarUbicacion( String usuario ) {
        for( Object usuArray : parque.getColaEntrarParque().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Cola de entrada.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getVestuario().getColaVestuarios().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Cola del vestuario.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        if( parque.getMonitorVestuario().getText().equals(usuario) ) {
            return "UBICACION!" + 0 + "!Monitor del Vestuario.";
        }
        
        for( Object usuArray : parque.getVestuario().getVestuarios().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Vestuario.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getPiscinaNiños().getColaEntrarPiscinaNiños().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Cola de la piscina de niños.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        if( parque.getMonitorPiscinaNiños().getText().equals(usuario) ) {
            return "UBICACION!" + 0 + "!Monitor del Vestuario.";
        }
        
        for( Object usuArray : parque.getPiscinaNiños().getPiscinaNiños().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Piscina de niños.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getPiscinaNiños().getEsperaAdultos().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Espera de adultos en la piscina de los niños.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getPiscinaGrande().getColaEntrarPiscinaGrande().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Cola de la piscina grande.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        if( parque.getMonitorPiscinaGrande().getText().equals(usuario) ) {
            return "UBICACION!" + 0 + "!Monitor de la piscina grande.";
        }
        
        for( Object usuArray : parque.getPiscinaGrande().getPiscinaGrande().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Piscina grande.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getPiscinaOlas().getColaEntrarPiscinaOlas().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Cola de la piscina de olas.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        if( parque.getMonitorPiscinaOlas().getText().equals(usuario) ) {
            return "UBICACION!" + 0 + "!Monitor de la piscina de olas.";
        }
        
        for( Object usuArray : parque.getPiscinaOlas().getPiscinaOlas().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Piscina de olas.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getTumbonas().getColaEntrarTumbonas().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Cola de las tumbonas.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getTumbonas().getColaMonitorTumbonas().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Cola del monitor de las tumbonas.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getTumbonas().getTumbonas().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Tumbonas.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getToboganes().getColaEntrarToboganes().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Cola de los toboganes.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getToboganes().getColaToboganA().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Tobogan A.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getToboganes().getColaToboganB().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Tobogan B.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getToboganes().getColaToboganC().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Tobogan C.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        return null;
    }
    
    public int buscarMenores() {
        return parque.getMenores();
    }
    
    public String buscarToboganes() {
        return parque.getToboganes().getToboganA() + "!" + parque.getToboganes().getToboganB() + "!" + parque.getToboganes().getToboganC();
    }
    
    public void buscarAforo() {
        
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
    } // Cierre del método  
} // Cierre de la clase