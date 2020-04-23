package hilos;

import concurrencia.Parque;
import util.FuncionesGenerales;

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

    private final Parque parque;
    private Usuario usuario;
    
    /* Variable mapeada para escoger el monitor de la atraccion
     *
     * 1 -> Vestuario
     * 2 -> Piscina Niños
     * 3 -> Piscina Olas
     * 4 -> Piscina Grande
     * 5 -> Monitor Tumbonas
     * 6 -> Tobogan 1
     * 7 -> Tobogan 2
     * 8 -> Tobogan 3
     */
    private final int atraccion;
    private final int time;
    private final int randomTime;
    private final boolean run;
    private final FuncionesGenerales fg;
    private final String identificador;
    
    public Monitor(Parque parque, int time, int randomTime, int atraccion, FuncionesGenerales fg, String identificador) {
        this.parque = parque;
        this.atraccion = atraccion;
        this.time = time;
        this.randomTime = randomTime;
        this.identificador =identificador;
        
        this.run = true;
        this.fg = fg;
    } // Cierre del método
    
    @Override
    public void run() {
        while( run ) {
            switch( atraccion ) {
                case 1:
                    usuario= parque.getVestuario().controlarVestuario();
                    fg.dormir(time, randomTime);
                    parque.getVestuario().controlarVestuario(usuario);
                    break;
                    
                case 2:
                    usuario = parque.getPiscinaNiños().controlarPiscinaNiños();
                    fg.dormir(time, randomTime);
                    parque.getPiscinaNiños().controlarPiscinaNiños(usuario);
                    break;
                    
                case 3:
                    usuario = parque.getPiscinaOlas().controlarPiscinaOlas();
                    fg.dormir(time, randomTime);
                    parque.getPiscinaOlas().controlarPiscinaOlas(usuario);
                    break;
                    
                case 4:
                    while (true) {
                        fg.dormir(time, randomTime);
                        Usuario u = parque.getPiscinaGrande().monitorExpulsa();
                        if (u != null) {
                        fg.dormir(time, randomTime);
                        parque.getPiscinaGrande().monitorExpulsa(u);
                        break;
                    } 
                }
                
                case 5:
                    usuario = parque.getTumbonas().controlarTumbonas();
                    fg.dormir(time, randomTime);
                    parque.getTumbonas().controlarTumbonas(usuario);
                    break;
                    
                case 6:
                    while (true) {
                        switch (identificador) {
                            case "A":
                                usuario = parque.getToboganes().monitorToboganA();
                                fg.dormir(time, randomTime);
                                parque.getToboganes().monitorToboganA(usuario);
                                break;

                            case "B":
                                usuario = parque.getToboganes().monitorToboganB();
                                fg.dormir(time, randomTime);
                                parque.getToboganes().monitorToboganB(usuario);
                                break;

                            case "C":
                                usuario = parque.getToboganes().monitorToboganC();
                                fg.dormir(time, randomTime);
                                parque.getToboganes().monitorToboganC(usuario);
                                break;
                        }
                         
                    }
            }
        }
    } // Cierre del método
} // Cierre de la clase
