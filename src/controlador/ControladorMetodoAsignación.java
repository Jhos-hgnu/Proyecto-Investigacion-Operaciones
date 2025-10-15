/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import modelo.ModeloMetodoAsignacion;
import modelo.ResultadoAsignacion;
import modelo.TablaProceso;
import util.MetodoAsignacion;
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

        if (e.getSource() == modelo.getVistaAsignacion().btnVolver) {
            VistaPrincipal vistaP = new VistaPrincipal();
            vistaP.setVisible(true);

            modelo.getVistaAsignacion().dispose();
        } else if (e.getSource() == modelo.getVistaAsignacion().btnAgregarEncabezados) {
            prepararDatosNumTabla();
        } else if (e.getSource() == modelo.getVistaAsignacion().btnResolver) {
            datosTablaPrincipal();
        }

    }

    DefaultTableModel modelTablaDatosNum = new DefaultTableModel();

    public void prepararDatosNumTabla() {

        int numColumnas = Integer.parseInt(String.valueOf(modelo.getVistaAsignacion().spinnerDestino.getValue()));
        System.out.println(numColumnas);
        int numFilas = Integer.parseInt(String.valueOf(modelo.getVistaAsignacion().spinnerSuministro.getValue()));

        String[] nombresColumnas = new String[numColumnas + 1];
        nombresColumnas[0] = "D";

        for (int i = 1; i <= numColumnas; i++) {
            nombresColumnas[i] = "A" + i;
        }

        modelTablaDatosNum.setColumnIdentifiers(nombresColumnas);

        for (int i = 0; i < numFilas; i++) {
            Object[] fila = new Object[numColumnas + 1];
            fila[0] = (i + 1);

            modelTablaDatosNum.addRow(fila);
        }

        modelo.getVistaAsignacion().tblDatosNum.setModel(modelTablaDatosNum);
        modelo.getVistaAsignacion().tblDatosNum.getColumnModel().getColumn(0).setCellEditor(null);
        modelo.getVistaAsignacion().tblDatosNum.revalidate();
        modelo.getVistaAsignacion().tblDatosNum.repaint();

    }

    public void datosTablaPrincipal() {

        int filas = modelo.getVistaAsignacion().tblDatosNum.getRowCount();
        int columna = modelo.getVistaAsignacion().tblDatosNum.getColumnCount();

        int n = filas;
        int m = columna - 1; // columnas de costos
        if (n != m) {
            // Para el modelo de asignación que espera cuadrada, puedes:
            // - rellenar con ceros o INF;
            // - o mostrar error. Aquí rellenamos con ceros si hay más columnas que filas o viceversa.
        }

        // Construimos matriz cuadrada n x n: usar la cantidad máxima entre filas y columnas-1
        int dim = Math.max(n, m);
        int[][] matriz = new int[dim][dim];
        final int INF = 999999; // valor alto para posiciones "inexistentes"

        // Inicializamos con INF para que no sean elegidas accidentalmente
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                matriz[i][j] = INF;
            }
        }

        // Copiamos datos desde la tabla (ignorando la primera columna)
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < columna; j++) {
                Object val = modelo.getVistaAsignacion().tblDatosNum.getValueAt(i, j);
                int x = 0;
                if (val == null || String.valueOf(val).trim().isEmpty()) {
                    x = INF; // celda vacía tratada como grande
                } else {
                    try {
                        x = Integer.parseInt(String.valueOf(val));
                    } catch (NumberFormatException ex) {
                        x = INF;
                    }
                }
                matriz[i][j - 1] = x;
            }
        }

        
        // Crear el objeto MetodoAsignacion e invocar solución
        MetodoAsignacion metodo = new MetodoAsignacion(matriz);
        ResultadoAsignacion resultado = metodo.resolver();

        // Si quieres marcar la asignación en las tablas guardadas, puedes:
        // - leer resultado.getAsignacion() y usar tablaProceso.marcarCeros(...)
        // Agregamos las tablas generadas al panel de resultados
        mostrarProcesoEnPanel(resultado.getProceso(), resultado.getAsignacion());
    }
    
    
    private void mostrarProcesoEnPanel(List<TablaProceso> pasos, int[][] asignacion) {
    JPanel panelResultados = modelo.getVistaAsignacion().panelResultados; // ejemplo
    panelResultados.removeAll();
    panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));

    // Si quieres marcar la asignación final en la última tabla:
    if (pasos != null && asignacion != null) {
        // convertir asignacion en lista de pares
        List<int[]> pares = new ArrayList<>();
        for (int i = 0; i < asignacion.length; i++) {
            int fila = asignacion[i][0];
            int col = asignacion[i][1];
            pares.add(new int[]{fila, col});
        }
        // marcar en la última TablaProceso (si existe)
        if (!pasos.isEmpty()) {
            TablaProceso ultima = pasos.get(pasos.size() - 1);
            ultima.marcarCeros(pares);
        }
    }

    if (pasos != null) {
        for (TablaProceso tp : pasos) {
            JLabel titulo = new JLabel(tp.getTitulo() + " - " + tp.getDescripcion());
            titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
            panelResultados.add(titulo);

            JTable tabla = new JTable(tp.toTableModel());
            tabla.setEnabled(false);
            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setPreferredSize(new Dimension(600, Math.min(300, tp.getDatos().length * 30)));
            scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

            panelResultados.add(scroll);
            panelResultados.add(Box.createRigidArea(new Dimension(0,10)));
        }
    }

    panelResultados.revalidate();
    panelResultados.repaint();
}

}
