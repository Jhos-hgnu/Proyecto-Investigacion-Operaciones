package modelo;

import vista.VistaModeloTransporte;

public class ModeloMetodoModeloTransporte {

    VistaModeloTransporte vistaMT;

    public ModeloMetodoModeloTransporte() {
    }

    public ModeloMetodoModeloTransporte(VistaModeloTransporte vistaMT) {
        this.vistaMT = vistaMT;
    }

    public VistaModeloTransporte getVistaMT() {
        return vistaMT;
    }

    public void setVistaMT(VistaModeloTransporte vistaMT) {
        this.vistaMT = vistaMT;
    }

}
