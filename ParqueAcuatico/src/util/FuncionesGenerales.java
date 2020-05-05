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
    
    //Campos de la clase
    public FuncionesGenerales(boolean debug) {
        this.debug = debug;
        
        if (System.getProperty("os.name").startsWith("Windows")) {
            ruta = "C://Users/User/Desktop/debug.log";
        } else {
            ruta = "/tmp/debug.log";
        } 
        this.file = new File(ruta);
        
        if(debug) {
            createDebugFile();
        }
    }
    
    public void dormir(int min, int max) {
        try {
            sleep( min + (int)( ( max - min ) * Math.random() ) );
        } catch( InterruptedException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    } // Cierre del metodo
    
    public synchronized void imprimir(JTextArea campo, String contenido) {
        campo.setText( contenido );
    } // Cierre del metodo
    
    public void createDebugFile() {
        try {
            String contenido = "DEBUG PARCTICA DE LABORATORIO 3 - PARQUE ACUATICO\n\n";
                
            if( file.exists() ) {
                if( file.delete() ){
                    file.createNewFile();
                }
            } else {
                file.createNewFile();
            }
            
            
            FileWriter fw = new FileWriter(file);
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(contenido);
            }
        } catch(IOException ex) {
            System.out.println("ERROR: " + ex);
        }
    }
    
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
    }
} // Cierre de la clase
