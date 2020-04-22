package hilos;

import concurrencia.Parque;
import util.FuncionesGenerales;

/**
 *
 * @authors 
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
    
    public Monitor(Parque parque, int time, int randomTime, int atraccion, FuncionesGenerales fg) {
        this.parque = parque;
        this.atraccion = atraccion;
        this.time = time;
        this.randomTime = randomTime;
        
        this.run = true;
        this.fg = fg;
    }
    
    @Override
    public void run() {
        while( run ) {
            switch( atraccion ) {
                case 1:
                    usuario = parque.getVestuario().controlarVestuario();
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
                    usuario = parque.getPiscinaGrande().controlarPiscinaGrande();
                    fg.dormir(time, randomTime);
                    parque.getPiscinaGrande().controlarPiscinaGrande(usuario);
                    break;
                
                case 5:
                    usuario = parque.getTumbonas().controlarTumbonas();
                    fg.dormir(time, randomTime);
                    parque.getTumbonas().controlarTumbonas(usuario);
                    break;
                    
                case 6:
                    //TODO: logica del monitor de tobogan 1
                    fg.dormir(time, randomTime);
                    break;
                    
                case 7:
                    //TODO: logica del monitor de tobogan 2
                    fg.dormir(time, randomTime);
                    break;
                
                case 8:
                    //TODO: logica del monitor de tobogan 3
                    fg.dormir(time, randomTime);
                    break;
            }
        }
    }
}
