package util;

import static java.lang.Thread.sleep;
import javax.swing.JTextArea;

/**
 *
 * @authors 
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class FuncionesGenerales {
    
    public FuncionesGenerales() { }
    
    public void dormir(int time, int randomTime) {
        try {
            sleep( time + (int)( randomTime * Math.random() ) );
        } catch( InterruptedException ex ) {
            System.out.println( "ERROR: " + ex );
        }
    }
    
    public synchronized void imprimir(JTextArea campo, String contenido) {
        campo.setText( contenido );
    }
}
