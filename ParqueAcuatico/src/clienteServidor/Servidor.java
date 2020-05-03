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
 * Clase Servidor 
 * 
 * Contiene información del servidor en la conexión
 *
 * @author
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Servidor extends Thread {
    //Campos de la clase
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
        
        if( ( parque.getVestuario().getMonitorVestuarioUsuario() != null ) && ( parque.getVestuario().getMonitorVestuarioUsuario().toString().equals(usuario) ) ) {
            return "UBICACION!" + parque.getVestuario().getMonitorVestuarioUsuario().getControlNumAtracciones() + "!Monitor del Vestuario.";
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
        
        if( ( parque.getPiscinaNiños().getMonitorPiscinaNiñosUsuario() != null ) && ( parque.getVestuario().getMonitorVestuarioUsuario().toString().equals(usuario) ) ) {
            return "UBICACION!" + parque.getPiscinaNiños().getMonitorPiscinaNiñosUsuario().getControlNumAtracciones() + "!Monitor de la piscina de niños.";
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
        
        if( ( parque.getPiscinaGrande().getMonitorPiscinaGrandeUsuario() != null ) && ( parque.getVestuario().getMonitorVestuarioUsuario().toString().equals(usuario) ) ) {
            return "UBICACION!" + parque.getVestuario().getMonitorVestuarioUsuario().getControlNumAtracciones() + "!Monitor de la piscina grande.";
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
        
        if( ( parque.getPiscinaOlas().getMonitorPiscinaOlasUsuario() != null ) && ( parque.getPiscinaOlas().getMonitorPiscinaOlasUsuario().toString().equals(usuario) ) ) {
            return "UBICACION!" + parque.getPiscinaOlas().getMonitorPiscinaOlasUsuario().getControlNumAtracciones() + "!Monitor de la piscina de olas.";
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
        
        if( ( parque.getTumbonas().getMonitorTumbonasUsuario()!= null ) && ( parque.getTumbonas().getMonitorTumbonasUsuario().toString().equals(usuario) ) ) {
            return "UBICACION!" + parque.getTumbonas().getMonitorTumbonasUsuario().getControlNumAtracciones() + "!Monitor de las Tumbonas.";
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
                String ubicacion = "Cola tobogan A.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getToboganes().getColaToboganB().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Cola tobogan B.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getToboganes().getColaToboganC().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Cola tobogan C.";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        if( ( parque.getToboganes().getMonitorToboganAUsuario() != null ) && ( parque.getToboganes().getMonitorToboganAUsuario().toString().equals(usuario) ) ) {
            return "UBICACION!" + parque.getToboganes().getMonitorToboganAUsuario().getControlNumAtracciones() + "!Monitor del tobogan A.";
        }
        
        if( ( parque.getToboganes().getMonitorToboganBUsuario() != null ) && ( parque.getToboganes().getMonitorToboganBUsuario().toString().equals(usuario) ) ) {
            return "UBICACION!" + parque.getToboganes().getMonitorToboganBUsuario().getControlNumAtracciones() + "!Monitor del Tobogan B.";
        }
        
        if( ( parque.getToboganes().getMonitorToboganCUsuario()!= null ) && ( parque.getToboganes().getMonitorToboganCUsuario().toString().equals(usuario) ) ) {
            return "UBICACION!" + parque.getToboganes().getMonitorToboganCUsuario().getControlNumAtracciones() + "!Monitor del tobogan C.";
        }
        
        if( parque.getToboganes().getToboganA().equals(usuario) ) {
            return "UBICACION!" + parque.getToboganes().getToboganAUsuario().getControlNumAtracciones() + "!Tobogan A.";
        }
        
        if( parque.getToboganes().getToboganB().equals(usuario) ) {
            return "UBICACION!" + parque.getToboganes().getToboganBUsuario().getControlNumAtracciones() + "!Tobogan B.";
        }
        
        if( parque.getToboganes().getToboganC().equals(usuario) ) {
            return "UBICACION!" + parque.getToboganes().getToboganCUsuario().getControlNumAtracciones() + "!Tobogan C.";
        }
        
        return null;
    } // Cierre del método
    
    public int buscarMenores() {
        return parque.getMenores();
    } // Cierre del método
    
    public String buscarToboganes() {
        return ( ( !parque.getToboganes().getToboganA().equals("") ) ? parque.getToboganes().getToboganA() : " " )
                + "!" + ( ( !parque.getToboganes().getToboganB().equals("") ) ? parque.getToboganes().getToboganB() : " " ) 
                + "!" + ( ( !parque.getToboganes().getToboganC().equals("") ) ? parque.getToboganes().getToboganC() : " " );
    } // Cierre del método
    
    public String buscarAforo() {
        int numVestuario = ( parque.getVestuario().getColaVestuarios().size() )
                            + ( ( !parque.getMonitorVestuario().getText().equals("") ) ? 1 : 0 )
                            + ( parque.getVestuario().getVestuarios().size() );
        
        int numPiscinaOlas = ( parque.getPiscinaOlas().getColaEntrarPiscinaOlas().size() )
                                + ( ( !parque.getMonitorPiscinaOlas().getText().equals("") ) ? 1 :0 )
                                + ( ( !parque.getEsperaCompañero().getText().equals("") ) ? 1 : 0 )
                                + ( parque.getPiscinaOlas().getPiscinaOlas().size() );
       
        int numPiscinaNiños = ( parque.getPiscinaNiños().getColaEntrarPiscinaNiños().size() )
                                + ( ( !parque.getMonitorPiscinaNiños().getText().equals("") ) ? 1 : 0 )
                                + ( parque.getPiscinaNiños().getPiscinaNiños().size() )
                                + ( parque.getPiscinaNiños().getEsperaAdultos().size() );
        
        int numPiscinaGrande = ( parque.getPiscinaGrande().getColaEntrarPiscinaGrande().size() )
                                + ( ( !parque.getMonitorPiscinaGrande().getText().equals("") ) ? 1 : 0 )
                                + ( parque.getPiscinaGrande().getPiscinaGrande().size() );
        
        int numTumbonas = ( parque.getTumbonas().getColaEntrarTumbonas().size() )
                            + ( ( !parque.getMonitorTumbonas().getText().equals("") ? 1 : 0 ) )
                            + ( parque.getTumbonas().getTumbonas().size() );
        
        int numToboganes = ( parque.getToboganes().getColaEntrarToboganes().size() ) 
                            + ( ( !parque.getMonitorToboganA().getText().equals("") ? 1 : 0 ) )
                            + ( ( !parque.getMonitorToboganB().getText().equals("") ? 1 : 0 ) )
                            + ( ( !parque.getMonitorToboganC().getText().equals("") ? 1 : 0 ) )
                            + ( ( !parque.getToboganes().getToboganA().equals("") ) ? 1 : 0 )
                            + ( ( !parque.getToboganes().getToboganB().equals("") ) ? 1 : 0 )
                            + ( ( !parque.getToboganes().getToboganC().equals("") ) ? 1 : 0 );
        
        return "AFORO!" + numVestuario + "!" + numPiscinaOlas + "!" + numPiscinaNiños + "!" + numPiscinaGrande + "!" + numTumbonas + "!" + numToboganes;
    } // Cierre del método
    
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