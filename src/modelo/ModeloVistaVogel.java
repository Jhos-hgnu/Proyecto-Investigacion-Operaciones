/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author coloc
 */


import util.MetodoVogel;
import vista.VistaMetodoEsquinaNoroeste;

/** Modelo MVC para el método de Vogel (VAM). */
public class ModeloVistaVogel {

    // --- Estado (opcional): matrices y último resultado ---
    private int[][] costos;
    private int[] oferta;
    private int[] demanda;
    private MetodoVogel.Resultado ultimoResultado;

    // --- Vista asociada a este modelo ---
    private final VistaMetodoEsquinaNoroeste vista;

    public ModeloVistaVogel() {
        // Reutilizamos tu panel sencillo (spinners, tabla y textarea)
        this.vista = new VistaMetodoEsquinaNoroeste();
    }

    // ---- getters/setters de estado (por si luego quieres persistir) ----
    public int[][] getCostos() { return costos; }
    public void setCostos(int[][] costos) { this.costos = costos; }

    public int[] getOferta() { return oferta; }
    public void setOferta(int[] oferta) { this.oferta = oferta; }

    public int[] getDemanda() { return demanda; }
    public void setDemanda(int[] demanda) { this.demanda = demanda; }

    public MetodoVogel.Resultado getUltimoResultado() { return ultimoResultado; }
    public void setUltimoResultado(MetodoVogel.Resultado r) { this.ultimoResultado = r; }

    // ---- Vista ----
    public VistaMetodoEsquinaNoroeste getVista() { return vista; }
}
