/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package implementacion;

import modelo.ModeloCostoMinimo;

/**
 *
 * @author jhosu
 */
public class MetodoCostoMinimo {
    
    public void resolverCostoMinimo(){
        
        ModeloCostoMinimo modelo = new ModeloCostoMinimo();
        
        
        //Extrar datos de tablas
        int numFilasO = modelo.getVistaMetodoCostoMin().tblOferta.getRowCount();
        int [] oferta = new int[numFilasO];
        
        for(int i = 0; i < numFilasO; i++){
            Object valor = modelo.getVistaMetodoCostoMin().tblOferta.getValueAt(i, 0);
            oferta[i] = Integer.parseInt(valor.toString());
        }
        
        
        int numFilasD = modelo.getVistaMetodoCostoMin().tblDemanda.getRowCount();
        int[] demanda = new int[numFilasD];
        
        for(int i = 0; i < numFilasD; i++){
            Object valor = modelo.getVistaMetodoCostoMin().tblDemanda.getValueAt(i, 0);
            demanda[i] = Integer.parseInt(valor.toString());
        }
        
        
        //Validar si el método se encuentra baleanceado
        
        
        
    }
    
    
}
