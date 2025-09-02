package controlador;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.ModeloPunto;
import modelo.ModeloVistaMetodoGrafico;
import util.DialogMensaje;
import util.MetodoGrafico;
import util.PanelGrafico;

public class ControladorVistaMetodoGrafico implements ActionListener {

    ModeloVistaMetodoGrafico modelo;

    public ControladorVistaMetodoGrafico(ModeloVistaMetodoGrafico modelo) {
        this.modelo = modelo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == modelo.getVistaMetodoGrafico().btnAgregar) {
            obtenerDatos();
            System.out.println("Hola");
        } else if (e.getSource() == modelo.getVistaMetodoGrafico().btnEliminarRestriccion) {
            eliminarDatosTabla();
        } else if (e.getSource() == modelo.getVistaMetodoGrafico().btnCalcular) {
            calcularMetodoGrafico();

        }

    }

    //==============================
    //Metodos para obtener datos
    public void obtenerDatosCalcular() {

        double cx = Double.parseDouble(modelo.getVistaMetodoGrafico().txtCoeficienteX1.getText());
        double cy = Double.parseDouble(modelo.getVistaMetodoGrafico().txtCoeficienteX2.getText());
        boolean maximizar = modelo.getVistaMetodoGrafico().radBtnMaximizar.isSelected();

        //Restricciones
        List<ModeloVistaMetodoGrafico> listaRestricciones = MetodoGrafico.obtenerRestricciones(modelo.getVistaMetodoGrafico().tablaRestricciones);

        //Intersercciones
        Set<ModeloPunto> candidatos = new HashSet<>();
        for (ModeloVistaMetodoGrafico r : listaRestricciones) {
            candidatos.addAll(MetodoGrafico.interseccionesConEjes(r));
        }

        for (int i = 0; i < listaRestricciones.size(); i++) {
            for (int j = i + 1; j < listaRestricciones.size(); j++) {
                ModeloPunto p = MetodoGrafico.interseccionEntreRestricciones(listaRestricciones.get(i), listaRestricciones.get(j));
            }
        }

        //factibles
        List<ModeloPunto> factibles = new ArrayList<>();
        for (ModeloPunto p : candidatos) {
            if (MetodoGrafico.esFactible(p, listaRestricciones)) {
                factibles.add(p);
            }
        }

        //Óptimo
        ModeloPunto optimo = MetodoGrafico.encontrarOptimo(factibles, cx, cy, maximizar);

        //Mostrar resultado
        if (optimo != null) {
            double z = MetodoGrafico.evaluarFuncionObjetivo(optimo, cx, cy);
            modelo.getVistaMetodoGrafico().lblResultado.setText("Optimo en " + optimo + " con Z = " + z);
            //Hacer la gráfica
            PanelGrafico plano = new PanelGrafico(listaRestricciones, factibles);

            modelo.getVistaMetodoGrafico().panelGrafica.removeAll();
            modelo.getVistaMetodoGrafico().panelGrafica.setLayout(new BorderLayout());
            modelo.getVistaMetodoGrafico().panelGrafica.add(plano, BorderLayout.CENTER);

            modelo.getVistaMetodoGrafico().panelGrafica.revalidate();
            modelo.getVistaMetodoGrafico().panelGrafica.repaint();

        } else {
            modelo.getVistaMetodoGrafico().lblResultado.setText("No hay solución factible");
            modelo.getVistaMetodoGrafico().panelGrafica.removeAll();
            modelo.getVistaMetodoGrafico().panelGrafica.revalidate();
            modelo.getVistaMetodoGrafico().panelGrafica.repaint();
        }

//        //Hacer la gráfica
//        PanelGrafico plano = new PanelGrafico(listaRestricciones, factibles);
//        
//        modelo.getVistaMetodoGrafico().panelGrafica.removeAll();
//        modelo.getVistaMetodoGrafico().panelGrafica.setLayout(new BorderLayout());
//        modelo.getVistaMetodoGrafico().panelGrafica.add(plano,BorderLayout.CENTER);
//        
//        
//        modelo.getVistaMetodoGrafico().panelGrafica.revalidate();
//        modelo.getVistaMetodoGrafico().panelGrafica.repaint();
        for (ModeloVistaMetodoGrafico r : listaRestricciones) {
            System.out.println(r);
        }
    }

    public void obtenerDatos() {

        String x1Restriccion = modelo.getVistaMetodoGrafico().txtX1Restriccion.getText();
        String x2Restriccion = modelo.getVistaMetodoGrafico().txtX2Restriccion.getText();

        String simboloRestriccion = String.valueOf(modelo.getVistaMetodoGrafico().comboBoxSimbolo.getSelectedItem());
        String ladoDerechoInc = modelo.getVistaMetodoGrafico().txtLadoDerecho.getText();

        agregarDatosTabla(x1Restriccion, x2Restriccion, simboloRestriccion, ladoDerechoInc);

    }

    private void agregarDatosTabla(String x1, String x2, String simbolo, String ladoD) {

        if (verificarDatosVacios() == true) {
            mostrarPanelesInfo();
        } else {
            DefaultTableModel modeloTabla = (DefaultTableModel) modelo.getVistaMetodoGrafico().tablaRestricciones.getModel();
            System.out.println("Agregando");
            modeloTabla.addRow(new Object[]{x1, x2, simbolo, ladoD});

            limpiarDatos();
        }

    }

    private void calcularMetodoGrafico() {

        if (verificarDatosVaciosCalcular() == true) {
            DialogMensaje.mostrarMensaje(1);
        } else {
            System.out.println("Hacer método");
            modelo.getVistaMetodoGrafico().lblResultado.setVisible(true);
            modelo.getVistaMetodoGrafico().txtResultado.setVisible(true);
            obtenerDatosCalcular();
        }

    }

    //==========================
    //Eliminar o limpiar datos
    private void eliminarDatosTabla() {
        int filaSeleccionada = modelo.getVistaMetodoGrafico().tablaRestricciones.getSelectedRow();

        if (filaSeleccionada >= 0) {
            DefaultTableModel modeloTabla = (DefaultTableModel) modelo.getVistaMetodoGrafico().tablaRestricciones.getModel();
            modeloTabla.removeRow(filaSeleccionada);
        } else {
            JOptionPane.showInternalMessageDialog(null, "No hay ninguna fila seleccionada", "ERROR \"DATOS NO SELECCIONADOS\"", JOptionPane.WARNING_MESSAGE);

        }

    }

    private void limpiarDatos() {
        modelo.getVistaMetodoGrafico().txtX1Restriccion.setText("");
        modelo.getVistaMetodoGrafico().txtX2Restriccion.setText("");
        modelo.getVistaMetodoGrafico().txtLadoDerecho.setText("");
        modelo.getVistaMetodoGrafico().comboBoxSimbolo.setSelectedIndex(0);
    }

    //=========================
    //Metodos validar datos vacios
    private boolean verificarDatosVacios() {

        boolean datosVacios = true;
        String x1 = modelo.getVistaMetodoGrafico().txtX1Restriccion.getText();
        String x2 = modelo.getVistaMetodoGrafico().txtX2Restriccion.getText();

        String ladoDerecho = modelo.getVistaMetodoGrafico().txtLadoDerecho.getText();

        if (x1.isEmpty() || x2.isEmpty() || ladoDerecho.isEmpty()) {
            datosVacios = true;
        } else {
            datosVacios = false;
        }

        return datosVacios;
    }

    private boolean verificarDatosVaciosCalcular() {

        boolean datosVacios = true;
        String X1F = modelo.getVistaMetodoGrafico().txtCoeficienteX1.getText();
        String x2F = modelo.getVistaMetodoGrafico().txtCoeficienteX2.getText();
        modelo.getVistaMetodoGrafico().btnGroupObjetivo.getSelection();
        DefaultTableModel tablaRes = (DefaultTableModel) modelo.getVistaMetodoGrafico().tablaRestricciones.getModel();

        if (X1F.isEmpty() || x2F.isEmpty() || tablaRes.getRowCount() == 0 || modelo.getVistaMetodoGrafico().btnGroupObjetivo.getSelection() == null) {
            datosVacios = true;
        } else {
            datosVacios = false;
        }

        return datosVacios;
    }

    private void mostrarPanelesInfo() {
        JOptionPane.showInternalMessageDialog(null, "Por favor debe de ingresar todos los datos", "ERROR \"DATOS VACIOS\"", JOptionPane.WARNING_MESSAGE);

    }

}
