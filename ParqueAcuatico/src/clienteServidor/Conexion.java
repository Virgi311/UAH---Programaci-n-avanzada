package clienteServidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Clase Conexión
 *
 * Contiene información de la conexión
 *
 * @author 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Conexion extends Thread {
    
    private int id;
    private Socket conexion;
    private Servidor servidor;
    
    private DataInputStream entrada;
    private DataOutputStream salida;
    
    private boolean conexionBool = false;
    
    public Conexion( int id, Socket conexion, Servidor servidor ) {
        this.id = id;
        try {
            this.conexion = conexion;
            this.servidor = servidor;
            
            entrada = new DataInputStream( conexion.getInputStream() );
            salida = new DataOutputStream( conexion.getOutputStream() );
            
            conexionBool = true;
        } catch( IOException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    } // Cierre del método
    
    @Override
    public void run () {
        String comunicado;
        while( conexionBool ) {
            try {
                comunicado = entrada.readUTF();
                if( comunicado != null && !comunicado.isEmpty() ) {
                    if( !comunicado.equals("CERRARAPLICACION") ) {
                        System.out.println( "La conexion " + id + " envia la orden: " + comunicado );
                    } else if( comunicado.equals("CERRARAPLICACION") ) {
                        comunicado = "CERRAR";
                    }
                    
                    if( comunicado.split("!").length == 2 && comunicado.split("!")[0].equals("UBICACION") ) {
                        buscarUbicacion( comunicado.split("!")[1] );
                    } else {
                        switch( comunicado ) {
                            case "CERRAR":
                                cerrar( false );
                                break;
                        
                            case "AFORO":
                                buscarAforo();
                                break;
                        
                            case "TOBOGANES":
                                buscarToboganes();
                                break;
                            
                            case "MENORES":
                                buscarMenores();
                                break;
                        }
                    }
                }
            } catch (IOException ex) {
                System.out.println( "ERROR: " + ex );
            }
        }
    } // Cierre del método
    
    //TODO: Metodos de busqueda del cliente en el servidor
     
    public void buscarUbicacion( String codigo ) {
        String ubicacion = servidor.buscarUbicacion(codigo);
        try {
            if( ubicacion == null ) {
                ubicacion = "UBICACION!No encontrado";
            }
            salida.writeUTF(ubicacion);
        } catch( IOException ex ) {
            System.out.println("ERROR: " + ex);
        }
    } // Cierre del método
    
    public void buscarMenores() {
        int menores = servidor.buscarMenores();
        try {
            salida.writeUTF("MENORES!" + menores);
        } catch( IOException ex ) {
            System.out.println("ERROR: " + ex);
        }
    } // Cierre del método
    
    public void buscarAforo() {
        
    } // Cierre del método
    
    public void buscarToboganes() {
        String toboganes = servidor.buscarToboganes();
        try {
            salida.writeUTF("TOBOGANES!" + toboganes);
        } catch( IOException ex ) {
            System.out.println("ERROR: " + ex);
        }
    } // Cierre del método
    
    public int getIdName() {
        return id;
    } // Cierre del método
    
    public void cerrar( boolean servidorBool ) {
        if( !servidorBool ) {
            System.out.println( "\tCerrando cliente..." );
        }
        try {
            if( !servidorBool ) {
                salida.writeUTF("CERRAR");
                
                entrada.close();
                salida.close();
                conexion.close();
            
                conexionBool = false;
            
                servidor.eliminar( this );
            } else {
                salida.writeUTF("CERRARSERVER");
            }
        } catch( IOException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    } // Cierre del método
} // Cierre de la clase