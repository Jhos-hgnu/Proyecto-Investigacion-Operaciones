package modelo;

import vista.VistaModeloTransporte;

public class ModeloModeloTransporte {

    VistaModeloTransporte vistaMT;

    public ModeloModeloTransporte() {
    }

    public ModeloModeloTransporte(VistaModeloTransporte vistaMT) {
        this.vistaMT = vistaMT;
    }

    public VistaModeloTransporte getVistaMT() {
        return vistaMT;
    }

    public void setVistaMT(VistaModeloTransporte vistaMT) {
        this.vistaMT = vistaMT;
    }

}
