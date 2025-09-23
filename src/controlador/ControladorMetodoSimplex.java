/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import util.MetodoSimplex;
import vista.VistaMetodoSimplex;

/**
 *
 * @author jhosu
 */
public class ControladorMetodoSimplex implements ActionListener{
    
    private final VistaMetodoSimplex v;
    private final List<double[]> restricciones = new ArrayList<>();
    private int nVars = 0;
    private MetodoSimplex.Resultado ultimoResultado;

    public ControladorMetodoSimplex(VistaMetodoSimplex vista) {
        this.v = vista;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
