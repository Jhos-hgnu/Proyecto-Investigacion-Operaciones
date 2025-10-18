/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import modelo.ModeloMetodoModeloTransporte;
import vista.VistaMetodoAproxVogel;
import vista.VistaMetodoCostoMinimo;
import vista.VistaMetodoEsquinaNoroeste;
import vista.VistaPrincipal;

/**
 *
 * @author jhosu
 */
public class ControladorVistaModeloTransporte implements ActionListener{

    ModeloMetodoModeloTransporte modelo;

    public ControladorVistaModeloTransporte(ModeloMetodoModeloTransporte modelo) {
        this.modelo = modelo;
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == modelo.getVistaMT().btnVolver){
           VistaPrincipal vistaP = new VistaPrincipal();
           vistaP.setVisible(true);
           modelo.getVistaMT().dispose();
        } else if(e.getSource() == modelo.getVistaMT().btnAproxLineal){
            VistaMetodoAproxVogel vistaAproxLineal = new VistaMetodoAproxVogel();
            mostrarPanel(vistaAproxLineal);
        } else if(e.getSource() == modelo.getVistaMT().btnCostoMinimo){
            VistaMetodoCostoMinimo vistaCostoMinimo = new VistaMetodoCostoMinimo();
            mostrarPanel(vistaCostoMinimo);
        } else if (e.getSource() == modelo.getVistaMT().btnEsquinaNoroeste){
            VistaMetodoEsquinaNoroeste vistaEsquinaN = new VistaMetodoEsquinaNoroeste();
            mostrarPanel(vistaEsquinaN);
        }
        
    }
    
    
    
      public void mostrarPanel(JPanel p) {

        p.setSize(1075, 583);
        p.setLocation(0, 0);

        modelo.getVistaMT().panelContenedor.removeAll();
        modelo.getVistaMT().panelContenedor.add(p, BorderLayout.CENTER);
        modelo.getVistaMT().panelContenedor.revalidate();
        modelo.getVistaMT().panelContenedor.repaint();
    }
    
    
    
}
