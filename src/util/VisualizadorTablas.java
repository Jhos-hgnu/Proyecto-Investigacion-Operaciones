/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import modelo.ResultadoAsignacion;
import modelo.TablaProceso;

/**
 *
 * @author jhosu
 */
public class VisualizadorTablas {
    private JScrollPane scrollPane;
    private JPanel panelContenedor;
    
    public VisualizadorTablas() {
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        panelContenedor = new JPanel();
        panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.Y_AXIS));
        
        scrollPane = new JScrollPane(panelContenedor);
        scrollPane.setPreferredSize(new Dimension(800, 600));
    }
    
    public void mostrarProceso(ResultadoAsignacion resultado) {
        panelContenedor.removeAll();
        
        if (!resultado.isExito()) {
            mostrarError(resultado.getMensajeError());
            return;
        }
        
        // Mostrar cada tabla del proceso
        for (TablaProceso tabla : resultado.getProceso()) {
            agregarTablaAlPanel(tabla);
        }
        
        // Mostrar resultado final
        mostrarResultadoFinal(resultado);
        
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }
    
    private void agregarTablaAlPanel(TablaProceso tabla) {
        // Título y descripción
        JLabel lblTitulo = new JLabel(tabla.getTitulo());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel lblDescripcion = new JLabel(tabla.getDescripcion());
        lblDescripcion.setFont(new Font("Arial", Font.ITALIC, 12));
        
        // Crear tabla Swing
        JTable table = crearTablaSwing(tabla);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(700, 150));
        
        // Agregar al panel
        panelContenedor.add(lblTitulo);
        panelContenedor.add(lblDescripcion);
        panelContenedor.add(tableScrollPane);
        panelContenedor.add(Box.createRigidArea(new Dimension(0, 20))); // Espacio
    }
    
    private JTable crearTablaSwing(TablaProceso tablaProceso) {
        String[] columnNames = generarNombresColumnas(tablaProceso.getDatos()[0].length);
        String[][] data = convertirDatosParaTabla(tablaProceso);
        
        return new JTable(data, columnNames);
    }
    
    private String[] generarNombresColumnas(int numColumnas) {
        String[] columnNames = new String[numColumnas + 1];
        columnNames[0] = "D";
        for (int i = 1; i <= numColumnas; i++) {
            columnNames[i] = "A" + i;
        }
        return columnNames;
    }
    
    private String[][] convertirDatosParaTabla(TablaProceso tablaProceso) {
        int filas = tablaProceso.getDatos().length;
        int columnas = tablaProceso.getDatos()[0].length;
        String[][] data = new String[filas][columnas + 1];
        
        for (int i = 0; i < filas; i++) {
            data[i][0] = String.valueOf(i + 1); // Número de fila
            for (int j = 0; j < columnas; j++) {
                data[i][j + 1] = tablaProceso.getDatosConMarcas()[i][j];
            }
        }
        
        return data;
    }
    
    private void mostrarResultadoFinal(ResultadoAsignacion resultado) {
        JLabel lblResultado = new JLabel("ASIGNACIÓN ÓPTIMA ENCONTRADA");
        lblResultado.setFont(new Font("Arial", Font.BOLD, 18));
        lblResultado.setForeground(Color.BLUE);
        
        JLabel lblCosto = new JLabel("Costo Total: " + resultado.getCostoTotal());
        lblCosto.setFont(new Font("Arial", Font.BOLD, 14));
        
        panelContenedor.add(lblResultado);
        panelContenedor.add(lblCosto);
    }
    
    private void mostrarError(String mensaje) {
        JLabel lblError = new JLabel("ERROR: " + mensaje);
        lblError.setForeground(Color.RED);
        panelContenedor.add(lblError);
    }
    
    public JScrollPane getScrollPane() {
        return scrollPane;
    }
    
}
