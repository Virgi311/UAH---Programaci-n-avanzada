package clienteServidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @authores 
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
                        cerrar();
                    }
                }
            } catch( IOException ex ) {
                System.out.println( "ERROR: " + ex );
            }
        }
    }

    public void pausar() {
        System.out.println( "Cliente ejecuta: Pausar()" );
        try {
            salida.writeUTF( "PAUSAR" );
        } catch( IOException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    }
    
    public void reanudar() {
        System.out.println( "Cliente ejecuta: Reanudar()" );
        try {
            salida.writeUTF( "REANUDAR" );
        } catch( IOException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    }
    
    public void abrirEntrada() {
        System.out.println( "Cliente ejecuta: Abrir Entrada()" );
        try {
            salida.writeUTF( "ABRIRENTRADA" );
        } catch( IOException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    }
    
    public void cerrarEntrada() {
        System.out.println( "Cliente ejecuta: Cerrar Entrada()" );
        try {
            salida.writeUTF( "CERRARENTRADA" );
        } catch( IOException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    }
    
    public void cerrar() {
        System.out.println( "Cliente ejecuta: Cerrar()" );
        try {
            salida.writeUTF( "CERRAR" );
            cliente.close();
        } catch( IOException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    }   
}