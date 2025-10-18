package modelo;

import vista.VistaProblemaFicticio;

/**
 *
 * @author jhosu
 */
public class ModeloProblemaFicticio {
    
    VistaProblemaFicticio vistaFicticio;

    public ModeloProblemaFicticio() {
    }

    public ModeloProblemaFicticio(VistaProblemaFicticio vistaFicticio) {
        this.vistaFicticio = vistaFicticio;
    }

    public VistaProblemaFicticio getVistaFicticio() {
        return vistaFicticio;
    }

    public void setVistaFicticio(VistaProblemaFicticio vistaFicticio) {
        this.vistaFicticio = vistaFicticio;
    }
    
}
