/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import modelo.ModeloMetodoAsignacion;
import vista.VistaPrincipal;

/**
 *
 * @author jhosu
 */
public class ControladorMetodoAsignación implements ActionListener {
    
    ModeloMetodoAsignacion modelo;

    public ControladorMetodoAsignación(ModeloMetodoAsignacion modelo) {
        this.modelo = modelo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == modelo.getVistaAsignacion().btnVolver){
            VistaPrincipal vistaP = new VistaPrincipal();
            vistaP.setVisible(true);
            
            modelo.getVistaAsignacion().dispose();
        } else if (e.getSource() == modelo.getVistaAsignacion().btnAgregarEncabezados){
            prepararDatosNumTabla();
        }
        
    }
    
    DefaultTableModel modelTablaDatosNum = new DefaultTableModel();
    public void prepararDatosNumTabla() {


        int numColumnas = Integer.parseInt(String.valueOf(modelo.getVistaAsignacion().spinnerDestino.getValue()));
        System.out.println(numColumnas);
        int numFilas = Integer.parseInt(String.valueOf(modelo.getVistaAsignacion().spinnerSuministro.getValue()));

        String[] nombresColumnas = new String[numColumnas +1];
//        nombresColumnas[0] = "A";

        for (int i = 0; i <= numColumnas; i++) {
            nombresColumnas[i] = "A" + i;
        }

        modelTablaDatosNum.setColumnIdentifiers(nombresColumnas);

        for (int i = 0; i < numFilas; i++) {
            Object[] fila = new Object[numColumnas + 1];
            fila[0] = "A" + (i + 1);

            modelTablaDatosNum.addRow(fila);
        }

        modelo.getVistaAsignacion().tblDatosNum.setModel(modelTablaDatosNum);
        modelo.getVistaAsignacion().tblDatosNum.getColumnModel().getColumn(0).setCellEditor(null);
        modelo.getVistaAsignacion().tblDatosNum.revalidate();
        modelo.getVistaAsignacion().tblDatosNum.repaint();

    }
    
    
    
    
}
