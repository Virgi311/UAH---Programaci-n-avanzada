/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrencia;

import hilos.Usuario;
import hilos.Usuario;
import java.util.*; 
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @authors Virginia Vallejo y Javier Gonzalez
 */
public class Parque {
    private final Object[] parque;
    private int in = 0, out = 0, numElem = 0, maximo = 0, numElemAux = 0;
    private final Lock control = new ReentrantLock();
    private final Condition lleno = control.newCondition();
    private final Condition vacio = control.newCondition();
    private ArrayList <Usuario> colaEntrada;
    private ArrayList <Usuario> colaVestuario;
    private ArrayList <Usuario> colaPiscinaOlas;
    private ArrayList <Usuario> colaPiscinaNiños;
    private ArrayList <Usuario> colaTumbonas;
    private ArrayList <Usuario> colaPiscinaGrande;
    private ArrayList <Usuario> colaTobogan1;
    private ArrayList <Usuario> colaTobogan2;
    private ArrayList <Usuario> colaTobogan3;
    
        
    public Parque( int max ) { 
        this.maximo = max;
        parque = new Object[ max ];
    } 

    
    // Método para atender usuarios que llegan al parque.
    public void atenderUsuario( Object obj ) throws InterruptedException { 
        control.lock();
        while( numElem == maximo ) {
            /* Si al intentar acceder al parque, el aforo está completo, los usuarios esperan hasta recibir la señal de que hay espacio libre.
             * Primero imprimimos el mensaje y despues bloqueamos, si fuera al reves se imprimiria el mensaje cuando fue desbloqueado.
             */
            System.out.println("Espere su turno en la cola, parque lleno.");
            lleno.await(); 
        }
        
        try { 
            parque[in] = obj;
            numElemAux = numElem;
            numElem++;
            in = ( in + 1 ) % maximo;
            if( numElemAux == 0 ) {
                /* Emitimos la señal para indicar que el parque ya no esta vacio.
                 * Asi desbloqueamos al hilo consumidor.
                 */
                vacio.signal(); 
            }
        } finally {
            control.unlock(); 
        } 
    }
}
