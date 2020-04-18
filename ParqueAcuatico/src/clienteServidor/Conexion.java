package clienteServidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @authores 
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
        try {
            this.conexion = conexion;
            this.servidor = servidor;
            
            entrada = new DataInputStream( conexion.getInputStream() );
            salida = new DataOutputStream( conexion.getOutputStream() );
            
            conexionBool = true;
        } catch( IOException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    }
    
    @Override
    public void run () {
        String comunicado;
        while( conexionBool ) {
            try {
                comunicado = entrada.readUTF();
                if( comunicado != null && !comunicado.isEmpty() ) {
                    System.out.println( "La conexion " + id + " envia la orden: " + comunicado );
                    
                    switch( comunicado ) {
                        case "PAUSAR":
                            //pausar();
                            break;
                        case "REANUDAR":
                            //reanudar();
                            break;
                        case "CERRAR":
                            cerrar();
                            break;
                        case "ABRIRENTRADA":
                            //abrirEntrada();
                            break;
                        case "CERRARENTRADA":
                            //cerrarEntrada();
                            break;
                    }
                }
            } catch (IOException ex) {
                System.out.println( "ERROR: " + ex );
            }
        }
    }
    
    //Metodo para comunicar que el servidor ejecute el metodo pausar()
    /*public void pausar() {
        System.out.println( "\tPausando..." );
        servidor.pausar();
    }
    
    //Metodo para comunicar que el servidor ejecute el metodo reanudar()
    public void reanudar() {
        System.out.println( "\tReanudando..." );
        servidor.reanudar();
    }
    
    //Metodo para comunicar que el servidor ejecute el metodo abrir entrada()
    public void abrirEntrada() {
        System.out.println("\tAbriendo la entrada...");
        servidor.abrirEntrada();
    }
    
    //Metodo para comunicar que el servidor ejecute el metodo cerrar entrada()
    public void cerrarEntrada() {
        System.out.println("\tCerrando la entrada...");
        servidor.cerrarEntrada();
    }*/
    
    //Metodo para eliminar toda la conexion y que el servidor ejecuta eliminar() sobre esta conexion
    public void cerrar() {
        System.out.println( "\tCerrando cliente..." );
        try {
            entrada.close();
            salida.close();
            conexion.close();
            
            conexionBool = false;
            
            servidor.eliminar( this );
        } catch( IOException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    }
}