package clienteServidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import static java.lang.System.exit;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JTextField;


/**
 * Clase Cliente
 *
 * Contiene información del cliente en la conexión
 *
 * @author 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Cliente extends Thread {
    
    private Socket cliente;
    
    private DataInputStream entrada;
    private DataOutputStream salida;
    
    private boolean conexion = false;
    
    private final JTextField jTextField2;
    private final JTextField jTextField3;
    private final JTextField jTextField4;
    private final JTextField jTextField5;
    private final JTextField jTextField6;
    private final JTextField jTextField7;
    private final JTextField jTextField9;
    private final JTextField jTextField10;
    private final JTextField jTextField11;
    private final JTextField jTextField12;
    private final JTextField jTextField13;
    private final JTextField jTextField14;
    
    public Cliente( JTextField jTextField2, JTextField jTextField3, JTextField jTextField4, JTextField jTextField5, JTextField jTextField6, JTextField jTextField7, JTextField jTextField9, JTextField jTextField10, JTextField jTextField11, JTextField jTextField12, JTextField jTextField13, JTextField jTextField14 ) {
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
        this.jTextField2 = jTextField2;
        this.jTextField3 = jTextField3;
        this.jTextField4 = jTextField4;
        this.jTextField5 = jTextField5;
        this.jTextField6 = jTextField6;
        this.jTextField7 = jTextField7;
        this.jTextField9 = jTextField9;
        this.jTextField10 = jTextField10;
        this.jTextField11 = jTextField11;
        this.jTextField12 = jTextField12;
        this.jTextField13 = jTextField13;
        this.jTextField14 = jTextField14;
    } // Cierre del método
    
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
                    } else {
                        String control = comunicado.split("!")[0];
                        
                        switch( control ) {
                            case "UBICACION":
                                if( comunicado.split("!").length == 2 ) {
                                    jTextField2.setText(comunicado.split("!")[1]);
                                    jTextField3.setText("");
                                } else {
                                    jTextField2.setText(comunicado.split("!")[2]);
                                    jTextField3.setText(comunicado.split("!")[1]);
                                }
                                    
                                break;
                            
                            case "MENORES":
                                jTextField4.setText(comunicado.split("!")[1]);
                                break;
                                
                            case "TOBOGANES":
                                jTextField5.setText(comunicado.split("!")[1]);
                                jTextField6.setText(comunicado.split("!")[2]);
                                jTextField7.setText(comunicado.split("!")[3]);
                                break;
                               
                            case "AFORO":
                                jTextField9.setText(comunicado.split("!")[1]);
                                jTextField10.setText(comunicado.split("!")[2]);
                                jTextField11.setText(comunicado.split("!")[3]);
                                jTextField12.setText(comunicado.split("!")[4]);
                                jTextField13.setText(comunicado.split("!")[5]);
                                jTextField14.setText(comunicado.split("!")[6]);
                                break;
                        }
                    }
                }
            } catch( IOException ex ) {
                System.out.println( "ERROR: " + ex );
            }
        }
    }// Cierre del método

    public void buscarUbicacion( String codigo ) {
        System.out.println("\tBuscando ubicacion...");
        try {
            salida.writeUTF("UBICACION!" + codigo);
        } catch( IOException ex ) {
            System.out.println("ERROR: " + ex);
        }
    }// Cierre del método
    
    public void buscarMenores() {
        System.out.println("\tBuscando menores...");
        try {
            salida.writeUTF("MENORES");
        } catch( IOException ex ) {
            System.out.println("ERROR: " + ex);
        }
    }// Cierre del método
    
    public void buscarTobogan() {
        System.out.println("\tBuscando en los toboganes...");
        try {
            salida.writeUTF("TOBOGANES");
        } catch( IOException ex ) {
            System.out.println("ERROR: " + ex);
        }
    }// Cierre del método
    
    public void buscarAforo() {
        System.out.println("\tBuscando el aforo...");
        try {
            salida.writeUTF("AFORO");
        } catch( IOException ex ) {
            System.out.println("ERROR: " + ex);
        }
    }// Cierre del método
    
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
    } // Cierre del método
    
    public void emitirSeñalCerrar() {
        try {
            salida.writeUTF( "CERRAR" );
        } catch( IOException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    } // Cierre del método
} // Cierre de la clase
