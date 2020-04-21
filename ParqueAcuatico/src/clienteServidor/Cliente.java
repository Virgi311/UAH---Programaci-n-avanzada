package clienteServidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import static java.lang.System.exit;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @authors 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Cliente extends Thread {
    
    private Socket cliente;
    
    private DataInputStream entrada;
    private DataOutputStream salida;
    
    private boolean conexion = false;
    
    public Cliente() {
        try {
            System.out.println( "Creando cliente..." );
            
            System.out.print( "Introducir IP del servidor: " );
            Scanner sc = new Scanner( System.in );
            String ip = sc.next();
            cliente = new Socket( ip, 5000 );
            
            entrada = new DataInputStream( cliente.getInputStream() );
            salida = new DataOutputStream( cliente.getOutputStream() );
            System.out.println( "Cliente creado con exito" );
            
            conexion = true;
            
            salida.writeUTF( "Conexion estableciada" );
        } catch( IOException ex ) {
            System.out.println("ERROR: " + ex);
        }
    }
    
    @Override 
    public void run() {
        while( conexion ) {
            try {
                String comunicado = entrada.readUTF();
                if( comunicado != null && !comunicado.isEmpty() ) {
                    if( comunicado.equals( "CERRAR" ) ) {
                        cerrar( false );
                    } else if( comunicado.equals( "CERRARSERVER" ) ) {
                        cerrar( true );
                    }
                }
            } catch( IOException ex ) {
                System.out.println( "ERROR: " + ex );
            }
        }
    }

    //TODO: Metodos de busqueda de cada atraccion
    
    public void buscarUbicacion() {
        System.out.println("\tBuscando ubicaciones...");
    }
    
    public void buscarMenores() {
        System.out.println("\tBuscando menores...");
    }
    
    public void buscarTobogan() {
        System.out.println("\tBuscando en los toboganes...");
    }
    
    public void buscarAforo() {
        System.out.println("\tBuscando el aforo...");
    }
    
    public void cerrar( boolean servidorBool ) {
        if( servidorBool ) {
            System.out.println( "Cliente ejecuta: Cerrar() por orden del servidor" );
        } else {
            System.out.println( "Cliente ejecuta: Cerrar()" );
        }
        try {
            conexion = false;
            
            if( servidorBool ) {
                salida.writeUTF("CERRARAPLICACION");
                
                entrada.close();
                salida.close();
                
                cliente.close();
                exit(0);
            } else {
                cliente.close();
            }
        } catch( IOException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    } 
    
    public void emitirSeñalCerrar() {
        try {
            salida.writeUTF( "CERRAR" );
        } catch( IOException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    } 
}