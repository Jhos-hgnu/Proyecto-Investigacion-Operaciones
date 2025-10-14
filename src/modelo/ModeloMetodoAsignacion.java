/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import vista.VistaModeloAsignacion;

/**
 *
 * @author jhosu
 */
public class ModeloMetodoAsignacion {
    
    VistaModeloAsignacion vistaAsignacion;

    public ModeloMetodoAsignacion() {
    }

    public ModeloMetodoAsignacion(VistaModeloAsignacion vistaAsignacion) {
        this.vistaAsignacion = vistaAsignacion;
    }

    public VistaModeloAsignacion getVistaAsignacion() {
        return vistaAsignacion;
    }

    public void setVistaAsignacion(VistaModeloAsignacion vistaAsignacion) {
        this.vistaAsignacion = vistaAsignacion;
    }

    
    
}
