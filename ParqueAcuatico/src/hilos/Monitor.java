package hilos;

import concurrencia.Parque;
import util.FuncionesGenerales;
import concurrencia.Paso;

/**
 * Clase Monitor
 *
 * Define la forma y funcionamiento de los monitores del parque
 *
 * @author
 * Virginia Vallejo Sánchez 51983578J
 * Javier González López 09067677L
 */
public class Monitor extends Thread {
    // Campos de la clase
    private final Parque parque;
    private Usuario usuario;
    /* Variable mapeada para escoger el monitor de la atraccion
     *
     * 1 -> Vestuario
     * 2 -> Piscina Niños
     * 3 -> Piscina Olas
     * 4 -> Piscina Grande
     * 5 -> Monitor Tumbonas
     * 6 -> Tobogan A
     * 7 -> Tobogan B
     * 8 -> Tobogan C
     */
    private final int atraccion;
    private final int time;
    private final int randomTime;
    private final boolean run;
    private final FuncionesGenerales fg;
    private final String identificador;
    private final Paso paso;
    
    public Monitor(Parque parque, int time, int randomTime, int atraccion, FuncionesGenerales fg, String identificador, Paso paso) {
        this.parque = parque;
        this.atraccion = atraccion;
        this.time = time;
        this.randomTime = randomTime;
        this.identificador = identificador;
        
        this.paso = paso;
        this.run = true;
        this.fg = fg;
    } // Cierre del método
    
    @Override
    public void run() {
        while( run ) {
            switch( atraccion ) {
                case 1: // Monitor del vestuario
                    paso.mirar();
                    usuario= parque.getVestuario().controlarVestuario();
                    fg.dormir(time, randomTime);
                    parque.getVestuario().controlarVestuario(usuario);
                    paso.mirar();
                    break;
                    
                case 2: // Monitor de la piscina de niños
                    paso.mirar();
                    usuario = parque.getPiscinaNiños().controlarPiscinaNiños();
                    fg.dormir(time, randomTime);
                    parque.getPiscinaNiños().controlarPiscinaNiños(usuario);
                    paso.mirar();
                    break;
                    
                case 3: // Monitor de la piscina de olas
                    paso.mirar();
                    usuario = parque.getPiscinaOlas().controlarPiscinaOlas();
                    fg.dormir(time, randomTime);
                    parque.getPiscinaOlas().controlarPiscinaOlas(usuario);
                    paso.mirar();
                    break;
                    
                case 4:
                        usuario = parque.getPiscinaGrande().controlarPiscinaGrande();
                        fg.dormir(time, randomTime);
                        parque.getPiscinaGrande().controlarPiscinaGrande(usuario);
                        break;
                        
                case 5: // Monitor de las tumbonas
                    paso.mirar();
                    usuario = parque.getTumbonas().controlarTumbonas();
                    fg.dormir(time, randomTime);
                    parque.getTumbonas().controlarTumbonas(usuario);
                    paso.mirar();
                    break;
                    
                case 6: // Monitores de los 3 toboganes
                    switch (identificador) {
                        case "A": // Monitor del tobogan A
                            paso.mirar();
                            usuario = parque.getToboganes().monitorToboganA();
                            fg.dormir(time, randomTime);
                            parque.getToboganes().monitorToboganA(usuario);
                            paso.mirar();
                            break;

                        case "B": // Monitor del tobogan B
                            paso.mirar();
                            usuario = parque.getToboganes().monitorToboganB();
                            fg.dormir(time, randomTime);
                            parque.getToboganes().monitorToboganB(usuario);
                            paso.mirar();
                            break;

                        case "C": // Monitor del tobogan C
                            paso.mirar();
                            usuario = parque.getToboganes().monitorToboganC();
                            fg.dormir(time, randomTime);
                            parque.getToboganes().monitorToboganC(usuario);
                            paso.mirar();
                            break;
                    }
                    break;
            }
        }
    } // Cierre del método
} // Cierre de la clase
