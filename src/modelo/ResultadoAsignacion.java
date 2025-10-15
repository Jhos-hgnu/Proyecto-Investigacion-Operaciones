/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.List;

/**
 *
 * @author jhosu
 */
public class ResultadoAsignacion {
    
    private int[][] asignacion;
    private int costoTotal;
    private List<TablaProceso> proceso;
    private boolean exito;
    private String mensajeError;
    
    public ResultadoAsignacion(int[][] asignacion, int costoTotal, 
                              List<TablaProceso> proceso, boolean exito) {
        this.asignacion = asignacion;
        this.costoTotal = costoTotal;
        this.proceso = proceso;
        this.exito = exito;
    }
    
    // Getters
    public int[][] getAsignacion() { return asignacion; }
    public int getCostoTotal() { return costoTotal; }
    public List<TablaProceso> getProceso() { return proceso; }
    public boolean isExito() { return exito; }
    public String getMensajeError() { return mensajeError; }
    
    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }
    
}
