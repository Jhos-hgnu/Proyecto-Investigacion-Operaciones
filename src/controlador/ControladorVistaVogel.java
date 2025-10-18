/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author jhosu
 */

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import vista.VistaMetodoAproxVogel;

public class ControladorVistaVogel implements java.awt.event.ActionListener {

    private final VistaMetodoAproxVogel v;

    public ControladorVistaVogel(VistaMetodoAproxVogel vista) {
        this.v = vista;

        // Para que el resultado se vea alineado
        v.getResultado().setFont(new Font("Monospaced", Font.PLAIN, 14));

        v.getGenerar().addActionListener(this);
        v.getResolver().addActionListener(this);
        v.getLimpiar().addActionListener(this);

        // Si los spinners ya tienen valor, genera una tabla vacía
        generarTabla();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object src = e.getSource();
        if (src == v.getGenerar()) {
            generarTabla();
        } else if (src == v.getResolver()) {
            resolverVogel();
        } else if (src == v.getLimpiar()) {
            limpiar();
        }
    }

    // =======================
    // 1) Generar tabla (m+1) x (n+1)
    // =======================
    private void generarTabla() {
        int m = (int) v.getOferta().getValue();   // filas (suministros)
        int n = (int) v.getDemanda().getValue();  // columnas (destinos)

        if (m <= 0 || n <= 0) {
            v.getResultado().setText(""); // sin texto inicial
            v.getTabla().setModel(new DefaultTableModel());
            return;
        }

        // Columnas: D1..Dn + "Oferta"
        String[] cols = new String[n + 1];
        for (int j = 0; j < n; j++) cols[j] = "D" + (j + 1);
        cols[n] = "Oferta";

        // (m+1) filas: m de costos y la última fila (m) será la Demanda
        DefaultTableModel model = new DefaultTableModel(cols, m + 1) {
            @Override public boolean isCellEditable(int r, int c) { return true; }
        };

        // Inicializamos con 0 (tabla limpia)
        for (int r = 0; r < m + 1; r++) {
            for (int c = 0; c < n + 1; c++) {
                if (model.getValueAt(r, c) == null) model.setValueAt(0, r, c);
            }
        }

        v.getTabla().setModel(model);
        v.getTabla().setRowHeight(24);

        // Resultado sin mensajes
        v.getResultado().setText("");
    }

    // =======================
    // 2) Resolver con VAM
    // =======================
    private void resolverVogel() {
        JTable t = v.getTabla();
        JTextArea out = v.getResultado();

        if (t.getRowCount() == 0) {
            out.setText("Primero pulsa Generar y llena costos/oferta/demanda.");
            return;
        }

        int m = t.getRowCount() - 1;  // última fila = demanda
        int n = t.getColumnCount() - 1; // última columna = oferta

        if (m <= 0 || n <= 0) {
            out.setText("Oferta/Demanda deben ser > 0.");
            return;
        }

        double[][] cost = new double[m][n];
        int[] supply = new int[m];
        int[] demand = new int[n];

        try {
            // Costos
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    cost[i][j] = toDouble(t.getValueAt(i, j));
                }
            }
            // Oferta (última columna)
            for (int i = 0; i < m; i++) supply[i] = (int) Math.round(toDouble(t.getValueAt(i, n)));
            // Demanda (última fila)
            for (int j = 0; j < n; j++) demand[j] = (int) Math.round(toDouble(t.getValueAt(m, j)));
        } catch (Exception ex) {
            out.setText("Hay celdas vacías o no numéricas. Revísalas.");
            return;
        }

        int sumS = Arrays.stream(supply).sum();
        int sumD = Arrays.stream(demand).sum();
        if (sumS != sumD) {
            out.setText("Problema no balanceado. Oferta=" + sumS + " Demanda=" + sumD + ".");
            return;
        }

        // --- Aproximación de Vogel ---
        boolean[] rowDone = new boolean[m];
        boolean[] colDone = new boolean[n];
        int[][] alloc = new int[m][n];
        List<String> pasos = new ArrayList<>();
        int costoTotal = 0;

        int pendientes = sumS; // cantidad por asignar
        while (pendientes > 0) {
            // Penalización por fila
            int bestRow = -1, bestRowPen = -1;
            for (int i = 0; i < m; i++) if (!rowDone[i] && supply[i] > 0) {
                int[] two = twoSmallest(cost[i], colDone);
                int pen = (two[1] == Integer.MAX_VALUE) ? two[0] : (two[1] - two[0]);
                if (pen > bestRowPen) { bestRowPen = pen; bestRow = i; }
            }
            // Penalización por columna
            int bestCol = -1, bestColPen = -1;
            for (int j = 0; j < n; j++) if (!colDone[j] && demand[j] > 0) {
                int[] two = twoSmallestCol(cost, j, rowDone);
                int pen = (two[1] == Integer.MAX_VALUE) ? two[0] : (two[1] - two[0]);
                if (pen > bestColPen) { bestColPen = pen; bestCol = j; }
            }

            boolean tomoFila = bestRowPen > bestColPen || (bestRowPen == bestColPen && bestRow != -1);
            int iSel, jSel;
            if (tomoFila) {
                iSel = bestRow;
                jSel = minCostCol(cost[iSel], colDone);
            } else {
                jSel = bestCol;
                iSel = minCostRow(cost, jSel, rowDone);
            }

            int x = Math.min(supply[iSel], demand[jSel]);
            alloc[iSel][jSel] = x;
            pendientes -= x;
            supply[iSel] -= x;
            demand[jSel] -= x;

            int c = (int) Math.round(cost[iSel][jSel]);
            pasos.add(String.format("S%d -> D%d: %d x %d = %d", iSel + 1, jSel + 1, x, c, x * c));
            costoTotal += x * c;

            if (supply[iSel] == 0) rowDone[iSel] = true;
            if (demand[jSel] == 0) colDone[jSel] = true;
        }

        // Salida
        StringBuilder sb = new StringBuilder();
        sb.append("Asignaciones (x * c):\n\n");
        for (String p : pasos) sb.append(p).append("\n");
        sb.append("\nCosto total = ").append(costoTotal).append("\n");
        out.setText(sb.toString());
    }

    // =======================
    // 3) Limpiar
    // =======================
    private void limpiar() {
        v.getTabla().setModel(new DefaultTableModel());
        v.getResultado().setText("");
    }

    // =======================
    // Helpers
    // =======================
    private static double toDouble(Object o) {
        if (o == null) return 0;
        String s = o.toString().trim();
        if (s.isEmpty()) return 0;
        return Double.parseDouble(s);
    }

    private static int[] twoSmallest(double[] row, boolean[] colDone) {
        int a = Integer.MAX_VALUE, b = Integer.MAX_VALUE;
        for (int j = 0; j < row.length; j++) if (!colDone[j]) {
            int v = (int) Math.round(row[j]);
            if (v < a) { b = a; a = v; }
            else if (v < b) { b = v; }
        }
        return new int[]{a, b};
    }

    private static int[] twoSmallestCol(double[][] cost, int col, boolean[] rowDone) {
        int a = Integer.MAX_VALUE, b = Integer.MAX_VALUE;
        for (int i = 0; i < cost.length; i++) if (!rowDone[i]) {
            int v = (int) Math.round(cost[i][col]);
            if (v < a) { b = a; a = v; }
            else if (v < b) { b = v; }
        }
        return new int[]{a, b};
    }

    private static int minCostCol(double[] row, boolean[] colDone) {
        int best = -1, val = Integer.MAX_VALUE;
        for (int j = 0; j < row.length; j++) if (!colDone[j]) {
            int v = (int) Math.round(row[j]);
            if (v < val) { val = v; best = j; }
        }
        return best;
    }

    private static int minCostRow(double[][] cost, int col, boolean[] rowDone) {
        int best = -1, val = Integer.MAX_VALUE;
        for (int i = 0; i < cost.length; i++) if (!rowDone[i]) {
            int v = (int) Math.round(cost[i][col]);
            if (v < val) { val = v; best = i; }
        }
        return best;
    }
}
