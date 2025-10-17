/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
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
        v.getButtonAgregar().addActionListener(this);
        v.getButtonResolver().addActionListener(this);
        v.getButtonLimpiar().addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    
        if (e.getSource() == v.getButtonAgregar()) onAgregar();
        else if (e.getSource() == v.getButtonResolver()) onResolver();
        else if (e.getSource() == v.getButtonLimpiar()) onLimpiar();
    
    
    }
    
    
    /**  restricción para agregar  */
    private void onAgregar() {
        try {
            if (nVars == 0) {
                double[] c = parseFuncion(v.getTxtFuncion().getText());
                if (c.length == 0) throw new IllegalArgumentException("Función vacía");
                nVars = c.length;
            }
            double[] fila = parseRestriccion(v.getTxtRestriccion().getText(), nVars);
            restricciones.add(fila);

            StringBuilder sb = new StringBuilder(v.getListaRestriccion().getText());
            if (sb.length() > 0) sb.append("\n");
            for (int i = 0; i < nVars; i++) sb.append(fila[i]).append("x").append(i+1).append(i<nVars-1?" + ":" ");
            sb.append("<= ").append(fila[nVars]);
            v.getListaRestriccion().setText(sb.toString());

            v.getTxtRestriccion().setText("");

        } catch (Exception ex) {
            v.getSolucion().setText("Error: " + ex.getMessage());
        }
    }

    /** boton de resorlver */
    private void onResolver() {
        try {
            double[] c = parseFuncion(v.getTxtFuncion().getText());
            if (c.length == 0) throw new IllegalArgumentException("Función vacía");
            nVars = c.length;
            if (restricciones.isEmpty()) throw new IllegalArgumentException("Agrega al menos una restricción.");
            int m = restricciones.size();

            // Construcción de matrices A y b
            double[][] A = new double[m][nVars];
            double[] b = new double[m];
            for (int i = 0; i < m; i++) {
                double[] fila = restricciones.get(i);
                for (int j = 0; j < nVars; j++) A[i][j] = fila[j];
                b[i] = fila[nVars];
            }

            // Ejecutar Simplex
            MetodoSimplex.Resultado r = MetodoSimplex.maximizar(c, A, b);
            ultimoResultado = r;

            // Limpiar 
            v.getTabIteraciones().removeAll();

            for (int k = 0; k < r.tablas.size(); k++) {
                int[] pivote = r.pivotes.get(k); // cada iteración tiene su pivote
                JTable tabla = crearTablaIteracion(r.tablas.get(k), r.headers, r.basicas.get(k), pivote);
                JScrollPane scroll = new JScrollPane(tabla);
                v.getTabIteraciones().addTab("Iteración " + k, scroll);
            }

            // Solución final
            StringBuilder sbSol = new StringBuilder();
            sbSol.append("Estado: ").append(r.estado).append("\n\n");
            for (int j = 0; j < r.x.length; j++) {
                sbSol.append("x").append(j + 1).append(" = ")
                     .append(String.format("%.4f", r.x[j])).append("\n");
            }
            sbSol.append("z* = ").append(String.format("%.4f", r.z)).append("\n");
            v.getSolucion().setText(sbSol.toString());

            // Restricciones con no negatividad
            StringBuilder restr = new StringBuilder();
            for (int i = 0; i < m; i++) {
                double[] fila = restricciones.get(i);
                for (int j = 0; j < nVars; j++) {
                    if (j > 0 && fila[j] >= 0) restr.append("+ ");
                    restr.append(fila[j]).append("x").append(j+1).append(" ");
                }
                restr.append("<= ").append(fila[nVars]).append("\n");
            }
            restr.append("x1");
            for (int j = 2; j <= nVars; j++) restr.append(", x").append(j);
            restr.append(" >= 0");
            v.getListaRestriccion().setText(restr.toString());

            // Tabla final en tblSimplex
            double[][] T = r.tablas.get(r.tablas.size() - 1);
            pintarTabla(v.getTblSimplex(), T, r.headers);

        } catch (Exception ex) {
            v.getSolucion().setText("Error: " + ex.getMessage());
        }
    }

    /** Limpiar todo */
    private void onLimpiar() {
        restricciones.clear();
        nVars = 0;
        v.getTxtFuncion().setText("");
        v.getTxtRestriccion().setText("");
        v.getListaRestriccion().setText("");
        v.getSolucion().setText("");
        v.getTblSimplex().setModel(new DefaultTableModel());
        v.getTabIteraciones().removeAll();
    }

    /** Parser de función objetivo */
    private double[] parseFuncion(String s) {
        s = s.trim();
        if (s.isEmpty()) return new double[0];
        if (s.matches("([\\d\\s\\-+.]+)")) {
            String[] p = s.split("\\s+");
            double[] c = new double[p.length];
            for (int i = 0; i < p.length; i++) c[i] = Double.parseDouble(p[i]);
            return c;
        }
        s = s.replace(" ", "").replace("-", "+-");
        String[] terms = s.split("\\+");
        int maxVar = 0;
        for (String t : terms) if (t.contains("x")) {
            int idx = Integer.parseInt(t.substring(t.indexOf("x") + 1));
            if (idx > maxVar) maxVar = idx;
        }
        double[] c = new double[maxVar];
        for (String t : terms) {
            if (t.isEmpty()) continue;
            int sign = 1;
            if (t.startsWith("-")) { sign = -1; t = t.substring(1); }
            else if (t.startsWith("+")) { t = t.substring(1); }
            String coefStr = "";
            int varIndex = -1;
            for (int k = 0; k < t.length(); k++) if (Character.isLetter(t.charAt(k))) {
                varIndex = Integer.parseInt(t.substring(k+1)) - 1;
                coefStr = t.substring(0, k);
                break;
            }
            double coef = coefStr.isEmpty() ? 1 : Double.parseDouble(coefStr);
            c[varIndex] += sign * coef;
        }
        return c;
    }

    /** Parser de restricciones */
    private double[] parseRestriccion(String s, int n) {
        if (!s.contains("<=")) throw new IllegalArgumentException("Usa '<=' en la restricción");
        s = s.replace("≤", "<=").replace("=<", "<=").replace("-", "+-");
        String[] partes = s.split("<=");
        String izquierda = partes[0].trim();
        String derecha = partes[1].trim();
        double[] fila = new double[n+1];
        if (izquierda.matches("([\\d\\s\\-+.]+)")) {
            String[] p = izquierda.split("\\s+");
            for (int i = 0; i < n; i++) fila[i] = Double.parseDouble(p[i]);
        } else {
            izquierda = izquierda.replace(" ", "");
            String[] terms = izquierda.split("\\+");
            for (String t : terms) {
                if (t.isEmpty()) continue;
                int sign = 1;
                if (t.startsWith("-")) { sign = -1; t = t.substring(1); }
                else if (t.startsWith("+")) { t = t.substring(1); }
                String coefStr = "";
                int varIndex = -1;
                for (int k = 0; k < t.length(); k++) if (Character.isLetter(t.charAt(k))) {
                    varIndex = Integer.parseInt(t.substring(k+1)) - 1;
                    coefStr = t.substring(0, k);
                    break;
                }
                double coef = coefStr.isEmpty() ? 1 : Double.parseDouble(coefStr);
                fila[varIndex] += sign * coef;
            }
        }
        fila[n] = Double.parseDouble(derecha);
        return fila;
    }

    /** colores para laas celdas */
    private JTable crearTablaIteracion(double[][] T, String[] headers, int[] basicas, int[] pivote) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Variables básicas");
        model.addColumn("Ecuaciones");
        for (String h : headers) model.addColumn(h);

        // Fila Z
        Object[] fz = new Object[headers.length+2];
        fz[0] = "Z";
        fz[1] = "0";
        for (int j = 0; j < headers.length; j++) fz[j+2] = String.format("%.2f", T[0][j]);
        model.addRow(fz);

        // Filas restricciones
        for (int i = 1; i < T.length; i++) {
            Object[] fi = new Object[headers.length+2];
            int varIndex = basicas[i-1];
            fi[0] = headers[varIndex];
            fi[1] = String.valueOf(i);
            for (int j = 0; j < headers.length; j++) fi[j+2] = String.format("%.2f", T[i][j]);
            model.addRow(fi);
        }

        JTable tabla = new JTable(model);

        //  para decorar celdas
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                java.awt.Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);

                // Fila Z 
                if (row == 0) c.setBackground(new Color(230, 230, 230));

                // Columna LD 
                if (col == tbl.getColumnCount()-1) c.setBackground(new Color(200, 255, 200));

                // Números negativos en rojo
                try {
                    double val = Double.parseDouble(value.toString());
                    if (val < 0) c.setForeground(Color.RED);
                } catch (Exception ex) {}

                // Pivote 
                if (pivote != null && pivote[0] >= 0 && pivote[1] >= 0) {
                    int rowPivot = pivote[0];
                    int colPivot = pivote[1]+2; // +2 por columnas extra
                    if (row == rowPivot && col == colPivot) {
                        c.setBackground(Color.YELLOW);
                        c.setFont(c.getFont().deriveFont(java.awt.Font.BOLD));
                    }
                }

                return c;
            }
        });

        return tabla;
    }

    /** Pintar la tabla final en tblSimplex con mismo estilo */
    private void pintarTabla(JTable table, double[][] T, String[] headers) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Variables básicas");
        model.addColumn("Ecuaciones");
        for (String h : headers) model.addColumn(h);

        Object[] fz = new Object[headers.length+2];
        fz[0] = "Z";
        fz[1] = "0";
        for (int j = 0; j < headers.length; j++) fz[j+2] = String.format("%.2f", T[0][j]);
        model.addRow(fz);

        int[] basicas = ultimoResultado.basicas.get(ultimoResultado.basicas.size()-1);
        for (int i = 1; i < T.length; i++) {
            Object[] fi = new Object[headers.length+2];
            int varIndex = basicas[i-1];
            fi[0] = headers[varIndex];
            fi[1] = String.valueOf(i);
            for (int j = 0; j < headers.length; j++) fi[j+2] = String.format("%.2f", T[i][j]);
            model.addRow(fi);
        }
        table.setModel(model);

        int[] pivote = ultimoResultado.pivotes.get(ultimoResultado.pivotes.size()-1);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                java.awt.Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);

                if (row == 0) c.setBackground(new Color(230, 230, 230));
                if (col == tbl.getColumnCount()-1) c.setBackground(new Color(200, 255, 200));

                try {
                    double val = Double.parseDouble(value.toString());
                    if (val < 0) c.setForeground(Color.RED);
                } catch (Exception ex) {}

                if (pivote != null && pivote[0] >= 0 && pivote[1] >= 0) {
                    int rowPivot = pivote[0];
                    int colPivot = pivote[1]+2;
                    if (row == rowPivot && col == colPivot) {
                        c.setBackground(Color.YELLOW);
                        c.setFont(c.getFont().deriveFont(java.awt.Font.BOLD));
                    }
                }

                return c;
            }
        });
    }
    
    
}
