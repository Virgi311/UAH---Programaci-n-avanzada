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
    //Campos de la clase
    private Socket cliente;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private boolean conexion = false;
    
    private final JTextField ubicacionBuscar;
    private final JTextField actividadBuscar;
    private final JTextField menoresBuscar;
    private final JTextField toboganABuscar;
    private final JTextField toboganBBuscar;
    private final JTextField toboganCBuscar;
    private final JTextField vestuariosBuscar;
    private final JTextField piscinaOlasBuscar;
    private final JTextField piscinaNiñosBuscar;
    private final JTextField piscinaGrandeBuscar;
    private final JTextField tumbonasBuscar;
    private final JTextField toboganesBuscar;
    
    public Cliente( JTextField ubicacionBuscar, JTextField actividadBuscar, JTextField menoresBuscar, JTextField toboganABuscar, JTextField toboganBBuscar, JTextField toboganCBuscar, JTextField vestuariosBuscar, JTextField piscinaOlasBuscar, JTextField piscinaNiñosBuscar, JTextField piscinaGrandeBuscar, JTextField tumbonasBuscar, JTextField toboganesBuscar ) {
        try {
            System.out.println( "Creando cliente..." );
            
            System.out.print( "Introducir IP del servidor: " );
            Scanner sc = new Scanner( System.in );
            String ip = sc.next();
            /* Creamos la conexion con el srevidor y declaramos las lineas de comunicaciones
             * Input y Output con la ip introducida por consola
             */
            cliente = new Socket( ip, 5000 );
            
            entrada = new DataInputStream( cliente.getInputStream() );
            salida = new DataOutputStream( cliente.getOutputStream() );
            System.out.println( "Cliente creado con exito" );
            
            conexion = true;
            
            salida.writeUTF( "Conexion estableciada" );
        } catch( IOException ex ) {
            System.out.println("ERROR: " + ex);
        }
        this.ubicacionBuscar = ubicacionBuscar;
        this.actividadBuscar = actividadBuscar;
        this.menoresBuscar = menoresBuscar;
        this.toboganABuscar = toboganABuscar;
        this.toboganBBuscar = toboganBBuscar;
        this.toboganCBuscar = toboganCBuscar;
        this.vestuariosBuscar = vestuariosBuscar;
        this.piscinaOlasBuscar = piscinaOlasBuscar;
        this.piscinaNiñosBuscar = piscinaNiñosBuscar;
        this.piscinaGrandeBuscar = piscinaGrandeBuscar;
        this.tumbonasBuscar = tumbonasBuscar;
        this.toboganesBuscar = toboganesBuscar;
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
                                    ubicacionBuscar.setText(comunicado.split("!")[1]);
                                    actividadBuscar.setText("");
                                } else {
                                    ubicacionBuscar.setText(comunicado.split("!")[2]);
                                    actividadBuscar.setText(comunicado.split("!")[1]);
                                }
                                    
                                break;
                            
                            case "MENORES":
                                menoresBuscar.setText(comunicado.split("!")[1]);
                                break;
                                
                            case "TOBOGANES":
                                toboganABuscar.setText(comunicado.split("!")[1]);
                                toboganBBuscar.setText(comunicado.split("!")[2]);
                                toboganCBuscar.setText(comunicado.split("!")[3]);
                                break;
                               
                            case "AFORO":
                                vestuariosBuscar.setText(comunicado.split("!")[1]);
                                piscinaOlasBuscar.setText(comunicado.split("!")[2]);
                                piscinaNiñosBuscar.setText(comunicado.split("!")[3]);
                                piscinaGrandeBuscar.setText(comunicado.split("!")[4]);
                                tumbonasBuscar.setText(comunicado.split("!")[5]);
                                toboganesBuscar.setText(comunicado.split("!")[6]);
                                break;
                        }
                    }
                }
            } catch( IOException ex ) {
                System.out.println( "ERROR: " + ex );
            }
        }
    }// Cierre del método

    //Metodo para comunicar al servidor que busque el usuario que le envias
    public void buscarUbicacion( String codigo ) {
        System.out.println("\tBuscando ubicacion...");
        try {
            salida.writeUTF("UBICACION!" + codigo);
        } catch( IOException ex ) {
            System.out.println("ERROR: " + ex);
        }
    }// Cierre del método
 
    //Metodo para obtener los menores que hay dentro del parque 
    public void buscarMenores() {
        System.out.println("\tBuscando menores...");
        try {
            salida.writeUTF("MENORES");
        } catch( IOException ex ) {
            System.out.println("ERROR: " + ex);
        }
    }// Cierre del método
    
    //Metodo para obtener los usuarios que estan usando los toboganes
    public void buscarTobogan() {
        System.out.println("\tBuscando en los toboganes...");
        try {
            salida.writeUTF("TOBOGANES");
        } catch( IOException ex ) {
            System.out.println("ERROR: " + ex);
        }
    }// Cierre del método
    
    //Metodo para obtener el numero de usuarios que hay en cada ubicacion
    public void buscarAforo() {
        System.out.println("\tBuscando el aforo...");
        try {
            salida.writeUTF("AFORO");
        } catch( IOException ex ) {
            System.out.println("ERROR: " + ex);
        }
    }// Cierre del método
    
    //Metodo para cerrar la conexion y el cliente con el servidor
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
    
    //Metodo para emitir la señal del cierre de la aplicacion
    public void emitirSeñalCerrar() {
        try {
            salida.writeUTF( "CERRAR" );
        } catch( IOException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    } // Cierre del método
} // Cierre de la clase
