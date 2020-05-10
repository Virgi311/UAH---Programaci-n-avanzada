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
            /* Creamos el servidor y espera las conexiones de los servidores
             * Cuando un cliente se conecta crea la conexion y esta se encarga de todas las comunicaciones
             */
            servidor = new ServerSocket( 5000 );
            ip = InetAddress.getLocalHost();
            //Mostramos la IP donde se aloja el servidor
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
                    //Creamos la conexion
                    Conexion conexion = new Conexion( conexiones.size() + 1, socket, this );
                    conexiones.add( conexion );
                    conexion.start();
                }
            } catch( IOException ex ) {
                System.out.println( "ERROR: " + ex );
            }
        }
    } // Cierre del método
    
    //Metodo para eliminar una conexion
    public void eliminar( Conexion conexion ) {
        conexiones.remove( conexion );
    } // Cierre del método
    
    //Pausa la ejecucion de los hilos
    public void detener() {
        System.out.println("Ejecutando metodo Detener()");
        paso.pausar();
    } // Cierre del método
    
    //Reanuda la ejecucion de los hilos
    public void reanudar() {
        System.out.println("Ejecutando metodo Reanudar()");
        paso.reanudar();
    } // Cierre del método
    
    //Metodo para buscar al usuario solicitado por el cliente
    public String buscarUbicacion( String usuario ) {
        for( Object usuArray : parque.getColaEntrarParque().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Cola de entrada";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getVestuario().getColaVestuarios().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Cola del vestuario";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        /* Debido a la volatilidad referencial de la variable capturamos la excepcion NullPointer debido a que es posible que surja
         * Si el usuario sale del monitor antes de comprobar todas las condiciones salta la excepcion y significa que esta en el siguiente punto
         * Con el control de NullPointer hacemos que si cambia en el momento de la consulta continue para que nos informe de la ubicacion correctamente
         */
        try {
            if( ( parque.getVestuario().getMonitorVestuarioUsuario() != null ) && ( parque.getVestuario().getMonitorVestuarioUsuario().toString().equals(usuario) ) ) {
                return "UBICACION!" + parque.getVestuario().getMonitorVestuarioUsuario().getControlNumAtracciones() + "!Monitor del Vestuario";
            }
        } catch(NullPointerException ex) {}
        
        for( Object usuArray : parque.getVestuario().getVestuarios().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Vestuario";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getPiscinaNiños().getColaEntrarPiscinaNiños().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Cola de la piscina de niños";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        /* Debido a la volatilidad referencial de la variable capturamos la excepcion NullPointer debido a que es posible que surja
         * Si el usuario sale del monitor antes de comprobar todas las condiciones salta la excepcion y significa que esta en el siguiente punto
         * Con el control de NullPointer hacemos que si cambia en el momento de la consulta continue para que nos informe de la ubicacion correctamente
         */
        try {
            if( ( parque.getPiscinaNiños().getMonitorPiscinaNiñosUsuario() != null ) && ( parque.getVestuario().getMonitorVestuarioUsuario().toString().equals(usuario) ) ) {
                return "UBICACION!" + parque.getPiscinaNiños().getMonitorPiscinaNiñosUsuario().getControlNumAtracciones() + "!Monitor de la piscina de niños";
            }
        } catch(NullPointerException ex) {}
        
        for( Object usuArray : parque.getPiscinaNiños().getPiscinaNiños().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Piscina de niños";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getPiscinaNiños().getEsperaAdultos().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Espera de adultos en la piscina de los niños";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getPiscinaGrande().getColaEntrarPiscinaGrande().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Cola de la piscina grande";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        /* Debido a la volatilidad referencial de la variable capturamos la excepcion NullPointer debido a que es posible que surja
         * Si el usuario sale del monitor antes de comprobar todas las condiciones salta la excepcion y significa que esta en el siguiente punto
         * Con el control de NullPointer hacemos que si cambia en el momento de la consulta continue para que nos informe de la ubicacion correctamente
         */
        try {
            if( ( parque.getPiscinaGrande().getMonitorPiscinaGrandeUsuario() != null ) && ( parque.getVestuario().getMonitorVestuarioUsuario().toString().equals(usuario) ) ) {
                return "UBICACION!" + parque.getVestuario().getMonitorVestuarioUsuario().getControlNumAtracciones() + "!Monitor de la piscina grande";
            }
        } catch(NullPointerException ex) {}
        
        for( Object usuArray : parque.getPiscinaGrande().getPiscinaGrande().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Piscina grande";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getPiscinaOlas().getColaEntrarPiscinaOlas().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Cola de la piscina de olas";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        /* Debido a la volatilidad referencial de la variable capturamos la excepcion NullPointer debido a que es posible que surja
         * Si el usuario sale del monitor antes de comprobar todas las condiciones salta la excepcion y significa que esta en el siguiente punto
         * Con el control de NullPointer hacemos que si cambia en el momento de la consulta continue para que nos informe de la ubicacion correctamente
         */
        try {
            if( ( parque.getPiscinaOlas().getMonitorPiscinaOlasUsuario() != null ) && ( parque.getPiscinaOlas().getMonitorPiscinaOlasUsuario().toString().equals(usuario) ) ) {
                return "UBICACION!" + parque.getPiscinaOlas().getMonitorPiscinaOlasUsuario().getControlNumAtracciones() + "!Monitor de la piscina de olas";
            }
        } catch(NullPointerException ex) {}
        
        for( Object usuArray : parque.getPiscinaOlas().getPiscinaOlas().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Piscina de olas";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getTumbonas().getColaEntrarTumbonas().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Cola de las tumbonas";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        /* Debido a la volatilidad referencial de la variable capturamos la excepcion NullPointer debido a que es posible que surja
         * Si el usuario sale del monitor antes de comprobar todas las condiciones salta la excepcion y significa que esta en el siguiente punto
         * Con el control de NullPointer hacemos que si cambia en el momento de la consulta continue para que nos informe de la ubicacion correctamente
         */
        try {
            if( ( parque.getTumbonas().getMonitorTumbonasUsuario() != null ) && ( parque.getTumbonas().getMonitorTumbonasUsuario().toString().equals(usuario) ) ) {
                return "UBICACION!" + parque.getTumbonas().getMonitorTumbonasUsuario().getControlNumAtracciones() + "!Monitor de las Tumbonas";
            }
        } catch(NullPointerException ex) {}
        
        for( Object usuArray : parque.getTumbonas().getTumbonas().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Tumbonas";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        for( Object usuArray : parque.getToboganes().getColaEntrarToboganes().toArray() ) {
            Usuario usu = (Usuario) usuArray;
            if( usu.toString().equals(usuario) ) {
                int num = usu.getControlNumAtracciones();
                String ubicacion = "Cola de los toboganes";
                
                return "UBICACION!" + num + "!" + ubicacion;
            }
        }
        
        /* Debido a la volatilidad referencial de la variable capturamos la excepcion NullPointer debido a que es posible que surja
         * Si el usuario sale del monitor antes de comprobar todas las condiciones salta la excepcion y significa que esta en el siguiente punto
         * Con el control de NullPointer hacemos que si cambia en el momento de la consulta continue para que nos informe de la ubicacion correctamente
         */
        try {
            if( ( parque.getToboganes().getMonitorToboganAUsuario() != null ) && ( parque.getToboganes().getMonitorToboganAUsuario().toString().equals(usuario) ) ) {
                return "UBICACION!" + parque.getToboganes().getMonitorToboganAUsuario().getControlNumAtracciones() + "!Monitor del tobogan A";
            }
        } catch(NullPointerException ex){}

        /* Debido a la volatilidad referencial de la variable capturamos la excepcion NullPointer debido a que es posible que surja
         * Si el usuario sale del monitor antes de comprobar todas las condiciones salta la excepcion y significa que esta en el siguiente punto
         * Con el control de NullPointer hacemos que si cambia en el momento de la consulta continue para que nos informe de la ubicacion correctamente
         */
        try {
            if( ( parque.getToboganes().getMonitorToboganBUsuario() != null ) && ( parque.getToboganes().getMonitorToboganBUsuario().toString().equals(usuario) ) ) {
                return "UBICACION!" + parque.getToboganes().getMonitorToboganBUsuario().getControlNumAtracciones() + "!Monitor del Tobogan B";
            }
        } catch(NullPointerException ex) {}
        
        /* Debido a la volatilidad referencial de la variable capturamos la excepcion NullPointer debido a que es posible que surja
         * Si el usuario sale del monitor antes de comprobar todas las condiciones salta la excepcion y significa que esta en el siguiente punto
         * Con el control de NullPointer hacemos que si cambia en el momento de la consulta continue para que nos informe de la ubicacion correctamente
         */
        try {
            if( ( parque.getToboganes().getMonitorToboganCUsuario()!= null ) && ( parque.getToboganes().getMonitorToboganCUsuario().toString().equals(usuario) ) ) {
                return "UBICACION!" + parque.getToboganes().getMonitorToboganCUsuario().getControlNumAtracciones() + "!Monitor del tobogan C";
            }
        } catch(NullPointerException ex) {}

        /* Debido a la volatilidad referencial de la variable capturamos la excepcion NullPointer debido a que es posible que surja
         * Si el usuario sale del monitor antes de comprobar todas las condiciones salta la excepcion y significa que esta en el siguiente punto
         * Con el control de NullPointer hacemos que si cambia en el momento de la consulta continue para que nos informe de la ubicacion correctamente
         */
        try {
            if( ( parque.getToboganes().getToboganAUsuario() != null ) && ( parque.getToboganes().getToboganAUsuario().toString().equals(usuario) ) ) {
                return "UBICACION!" + parque.getToboganes().getToboganAUsuario().getControlNumAtracciones() + "!Tobogan A";
            }
        } catch(NullPointerException ex) {}
        
        /* Debido a la volatilidad referencial de la variable capturamos la excepcion NullPointer debido a que es posible que surja
         * Si el usuario sale del monitor antes de comprobar todas las condiciones salta la excepcion y significa que esta en el siguiente punto
         * Con el control de NullPointer hacemos que si cambia en el momento de la consulta continue para que nos informe de la ubicacion correctamente
         */
        try {
            if( ( parque.getToboganes().getToboganBUsuario() != null ) && ( parque.getToboganes().getToboganBUsuario().toString().equals(usuario) ) ) {
                return "UBICACION!" + parque.getToboganes().getToboganBUsuario().getControlNumAtracciones() + "!Tobogan B";
            }
        } catch(NullPointerException ex) {}

        /* Debido a la volatilidad referencial de la variable capturamos la excepcion NullPointer debido a que es posible que surja
         * Si el usuario sale del monitor antes de comprobar todas las condiciones salta la excepcion y significa que esta en el siguiente punto
         * Con el control de NullPointer hacemos que si cambia en el momento de la consulta continue para que nos informe de la ubicacion correctamente
         */
        try {
            if( ( parque.getToboganes().getToboganCUsuario() != null ) && ( parque.getToboganes().getToboganCUsuario().toString().equals(usuario) ) ) {
                return "UBICACION!" + parque.getToboganes().getToboganCUsuario().getControlNumAtracciones() + "!Tobogan C";
            }
        } catch(NullPointerException ex) {}
        
        return null;
    } // Cierre del método
    
    //Metodo para devolver el numero de menores en el parque
    public int buscarMenores() {
        return parque.getMenores();
    } // Cierre del método
    
    //Metodo para devolver lus usuarios que estan usando los toboganes
    public String buscarToboganes() {
        return ( ( parque.getToboganes().getToboganAUsuario() != null ) ? parque.getToboganes().getToboganAUsuario().toString() : " " )
                + "!" + ( ( parque.getToboganes().getToboganBUsuario() != null ) ? parque.getToboganes().getToboganBUsuario().toString() : " " ) 
                + "!" + ( ( parque.getToboganes().getToboganCUsuario() != null ) ? parque.getToboganes().getToboganCUsuario().toString() : " " );
    } // Cierre del método
    
    //Metodo para mostrar el numero de usuarios en cada localizacion
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
                            + ( ( parque.getToboganes().getToboganAUsuario() != null ) ? 1 : 0 )
                            + ( ( parque.getToboganes().getToboganBUsuario() != null ) ? 1 : 0 )
                            + ( ( parque.getToboganes().getToboganCUsuario() != null ) ? 1 : 0 );
        
        int total = numVestuario + numPiscinaOlas + numPiscinaNiños + numPiscinaGrande + numTumbonas + numToboganes;
        
        return "AFORO!" + numVestuario + "!" + numPiscinaOlas + "!" + numPiscinaNiños + "!" + numPiscinaGrande + "!" + numTumbonas + "!" + numToboganes + "!" + total;
    } // Cierre del método
    
    //Metodo que cierra toda la aplicacion, incluyendo todos los usuarios conectados
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