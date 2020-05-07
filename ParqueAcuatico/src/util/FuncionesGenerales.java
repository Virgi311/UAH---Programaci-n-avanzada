package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextArea;

/**
 *
 * @authors 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public final class FuncionesGenerales {
    
    private final String ruta;
    private final File file;
    private final boolean debug;
    
    /* Creamos esta clase para las funciones comunes que se usan en todas las clases
     * Y para implementar un sistema de logs de ejecucion
     */
    public FuncionesGenerales(boolean debug) {
        //Variable que determina si va a dejar log o no
        this.debug = debug;
        
        //Declaramos la ruta del log o el nombre unicamente y se creara en la carpeta del proyecto
        ruta = "debug.log";
        this.file = new File(ruta);
        
        if(debug) {
            //Funcion para crear el log
            createDebugFile();
        }
    }
    
    //Metodo para realizar un sleep segun los tiempos que se le pasen
    public void dormir(int min, int max) {
        try {
            sleep( min + (int)( ( max - min ) * Math.random() ) );
        } catch( InterruptedException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    } // Cierre del metodo
    
    //Metodo para imprimir cada uno de los campos de la vista del programa principal
    public synchronized void imprimir(JTextArea campo, String contenido) {
        campo.setText( contenido );
    } // Cierre del metodo
    
    //Metodo para crear el archivo de debug donde se van a ir escribiendo los logs
    public void createDebugFile() {
        try {
            String contenido = "DEBUG PARCTICA DE LABORATORIO 3 - PARQUE ACUATICO\n\n";
                
            if( file.exists() ) {
                //Si existe lo borramos y creamos uno nuevo al inicio de la ejecucion del programa
                if( file.delete() ){
                    file.createNewFile();
                }
            } else {
                file.createNewFile();
            }
            //Mostramos la ruta absoluta del fichero
            System.out.println("Fichero creado en la ruta: " + file.getAbsolutePath());
            
            FileWriter fw = new FileWriter(file);
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(contenido);
            }
        } catch(IOException ex) {
            System.out.println("ERROR: " + ex);
        }
    } // Cierre del método
    
    //Metodo para escribir en el log en caso de que la variable debug este activada
    public synchronized void writeDebugFile(String contenido) {
        if(debug) {
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                FileWriter fw = new FileWriter(file, true);
                try (BufferedWriter bw = new BufferedWriter(fw)) {
                    bw.write( dateFormat.format(date) + " => " + contenido );
                }
            } catch(IOException ex) {
                System.out.println("ERROR: " + ex);
            }
        }
    } // Cierre del método
} // Cierre de la clase
