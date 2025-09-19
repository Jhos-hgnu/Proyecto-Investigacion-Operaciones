package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import modelo.ModeloCostoMinimo;

/**
 *
 * @author jhosu
 */
public class ControladorCostoMinimo implements ActionListener {

    ModeloCostoMinimo modelo;

    public ControladorCostoMinimo(ModeloCostoMinimo modelo) {
        this.modelo = modelo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == modelo.getVistaMetodoCostoMin().btnAgregarOferta) {
            agregarOferta();
        } else if (e.getSource() == modelo.getVistaMetodoCostoMin().btnAgregarDemanda){
            agregarDemanda();
        } else if (e.getSource() == modelo.getVistaMetodoCostoMin().btnAgregarEncabezados){
            prepararDatosNumTabla();
        }

    }

    DefaultTableModel modeloTabla = new DefaultTableModel();
    DefaultTableModel modelTablaDemanda = new DefaultTableModel();
    DefaultTableModel modelTablaDatosNum = new DefaultTableModel();
    
    public void agregarOferta() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        String texto = modelo.getVistaMetodoCostoMin().txtOferta.getText().trim();

        if (!texto.isEmpty()) {
            try {
                System.out.println("Hola desde try");
                int cantidad = Integer.parseInt(texto);
                modeloTabla.setColumnIdentifiers(new Object[]{"Oferta"});
                modeloTabla.addRow(new Object[]{cantidad});
                
                
                modelo.getVistaMetodoCostoMin().tblOferta.setModel(modeloTabla);
                modelo.getVistaMetodoCostoMin().tblOferta.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

                modelo.getVistaMetodoCostoMin().tblOferta.revalidate();
                modelo.getVistaMetodoCostoMin().tblOferta.repaint();
                modelo.getVistaMetodoCostoMin().txtOferta.setText("");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ingresa un número válido");

            }
        } else {
            JOptionPane.showMessageDialog(null, "Ingresa un número no se puede ingresar texto vacío");

        }

    }
    
    public void agregarDemanda(){
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        String texto = modelo.getVistaMetodoCostoMin().txtDemanda.getText().trim();

        if (!texto.isEmpty()) {
            try {
                System.out.println("Hola desde try");
                int cantidad = Integer.parseInt(texto);
                modelTablaDemanda.setColumnIdentifiers(new Object[]{"Demanda"});
                modelTablaDemanda.addRow(new Object[]{cantidad});
                
                
                modelo.getVistaMetodoCostoMin().tblDemanda.setModel(modelTablaDemanda);
                modelo.getVistaMetodoCostoMin().tblDemanda.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

                modelo.getVistaMetodoCostoMin().tblDemanda.revalidate();
                modelo.getVistaMetodoCostoMin().tblDemanda.repaint();
                modelo.getVistaMetodoCostoMin().txtDemanda.setText("");
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ingresa un número válido");

            }
        } else {
            JOptionPane.showMessageDialog(null, "Ingresa un número no se puede ingresar texto vacío");

        }
    }
    
    public void prepararDatosNumTabla(){
        
        modelo.getVistaMetodoCostoMin().labelDatosNum.setVisible(true);
        modelo.getVistaMetodoCostoMin().scrollContenedor.setVisible(true);
        modelo.getVistaMetodoCostoMin().btnResolver.setVisible(true);
        modelo.getVistaMetodoCostoMin().btnLimpiar.setVisible(true);
        
        int numColumnas = Integer.parseInt(String.valueOf(modelo.getVistaMetodoCostoMin().spinnerDestino.getValue()));
        System.out.println(numColumnas);
        int numFilas = Integer.parseInt(String.valueOf(modelo.getVistaMetodoCostoMin().spinnerSuministro.getValue()));
        
        String[] nombresColumnas = new String[numColumnas + 1];
        nombresColumnas[0] = "";
        
        for(int i = 1; i <= numColumnas; i++){
            nombresColumnas[i] = "D" + i;
        }
        
        modelTablaDatosNum.setColumnIdentifiers(nombresColumnas);
        
        for(int i = 0; i < numFilas; i++){
            Object[] fila = new Object[numColumnas + 1];
            fila[0] = "Suministro " + (i + 1);
            
            modelTablaDatosNum.addRow(fila);
        }
        
        
        modelo.getVistaMetodoCostoMin().tblDatosNum.setModel(modelTablaDatosNum);
        modelo.getVistaMetodoCostoMin().tblDatosNum.getColumnModel().getColumn(0).setCellEditor(null);
        modelo.getVistaMetodoCostoMin().tblDatosNum.revalidate();
        modelo.getVistaMetodoCostoMin().tblDatosNum.repaint();
        
        
        
        
        
    }

}
