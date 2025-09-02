/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import modelo.ModeloVistaProgramacionLineal;
import vista.VistaMetodoGrafico;
import vista.VistaMetodoSimplex;
import vista.VistaPrincipal;

/**
 *
 * @author jhosu
 */
public class ControladorVistaProgramacionLineal implements ActionListener{
    
    ModeloVistaProgramacionLineal modelo;

    public ControladorVistaProgramacionLineal(ModeloVistaProgramacionLineal modelo) {
        this.modelo = modelo;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == modelo.getVistaProgramacionLineal().btnVolver) {
            VistaPrincipal vistaPrincipal = new VistaPrincipal();
            vistaPrincipal.setVisible(true);
            
            modelo.getVistaProgramacionLineal().dispose();
            
        } else if (e.getSource() == modelo.getVistaProgramacionLineal().btnMetodoGrafico){
            VistaMetodoGrafico panelMG = new VistaMetodoGrafico(); 
            mostrarPanel(panelMG);
            
        } else if (e.getSource() == modelo.getVistaProgramacionLineal().btnMetodoSimpleX){
            VistaMetodoSimplex panelMS = new VistaMetodoSimplex();
            mostrarPanel(panelMS);
            
            
            
        }
        
        
        
        
    }
    
    
    
    
      public void mostrarPanel(JPanel p) {

        p.setSize(1075, 595);
        p.setLocation(0, 0);

        modelo.getVistaProgramacionLineal().panelContenedor.removeAll();
        modelo.getVistaProgramacionLineal().panelContenedor.add(p, BorderLayout.CENTER);
        modelo.getVistaProgramacionLineal().panelContenedor.revalidate();
        modelo.getVistaProgramacionLineal().panelContenedor.repaint();
    }
    
}
