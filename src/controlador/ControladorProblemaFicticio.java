package controlador;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.HashSet;
import java.util.Set;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import util.MetodoEsquinaNoroeste;
import vista.VistaMetodoEsquinaNoroeste;
import vista.VistaPrincipal;
import vista.VistaProblemaFicticio;

/**
 *
 * @author jhosu
 */
public class ControladorProblemaFicticio {
    
    
    private final VistaProblemaFicticio v;
    private final Set<Long> celdasUsadas = new HashSet<>();

    public ControladorProblemaFicticio(VistaProblemaFicticio vista) {
        this.v = vista;

      
        v.getResultado().setFont(new Font("Monospaced", Font.PLAIN, 14));
        v.getGenerar().addActionListener(e -> generarModelo());
        v.getResolver().addActionListener(e -> onResolver());
        v.getLimpiar().addActionListener(e -> onLimpiar());
        v.getButtonVolver().addActionListener(e -> volverMenuPrincipal());
        generarModelo();                 
        centrarHeader(v.getTabla());
    }

    /** Construye la tabla (m+1) x (n+1): m×n costos*/
    private void generarModelo() {
        int m = (int) v.getOferta().getValue();   // (filas de costos)
        int n = (int) v.getDemanda().getValue();  //  (columnas de costos)

        //  Aqui solo bloqueamos la esquina inferior derecha que no se usa)
        DefaultTableModel model = new DefaultTableModel(m + 1, n + 1) {
            @Override public boolean isCellEditable(int r, int c) {
                return !(r == getRowCount() - 1 && c == getColumnCount() - 1);
            }
        };

        // Encabezados de tabalas
        String[] headers = new String[n + 1];
        for (int j = 0; j < n; j++) headers[j] = "  S" + (j + 1);
        headers[n] = "Oferta";
        model.setColumnIdentifiers(headers);

        // Inicializar con 0
        for (int r = 0; r < m + 1; r++)
            for (int c = 0; c < n + 1; c++)
                model.setValueAt(0, r, c);

        JTable t = v.getTabla();
        t.setModel(model);
        t.setRowHeight(26);
        t.setEnabled(true);
        t.setCellSelectionEnabled(true);
        t.setSurrendersFocusOnKeystroke(true);
        t.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        t.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()));

        centrarHeader(t);
        celdasUsadas.clear();
        aplicarRenderer(); 
    }

    /** Resuelve el problema con el método Esquina Noroeste */
    private void onResolver() {
        try {
            JTable tbl = v.getTabla();
            if (tbl.isEditing()) tbl.getCellEditor().stopCellEditing(); 

            int m = (int) v.getOferta().getValue();
            int n = (int) v.getDemanda().getValue();

            celdasUsadas.clear();

            int[][] costos = new int[m][n];
            int[] oferta = new int[m];
            int[] demanda = new int[n];

            // Leer matriz de costos y columna Oferta
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++)
                    costos[i][j] = parseInt(tbl.getValueAt(i, j));
                oferta[i] = parseInt(tbl.getValueAt(i, n));
            }

            // Leer fila Demanda
            for (int j = 0; j < n; j++)
                demanda[j] = parseInt(tbl.getValueAt(m, j));

            // Resolver
            MetodoEsquinaNoroeste.Resultado R =
                    MetodoEsquinaNoroeste.resolver(costos, oferta, demanda);

            // Guardar celdas usadas 
            for (MetodoEsquinaNoroeste.Paso p : R.pasos)
                if (p.i < m && p.j < n)
                    celdasUsadas.add(key(p.i, p.j));

            aplicarRenderer();
            v.getResultado().setText(MetodoEsquinaNoroeste.formatearDetalle(R));

        } catch (Exception ex) {
            v.getResultado().setText("Error: " + ex.getMessage());
        }
    }

    /** Limpia la tabla y el área de resultados */
    private void onLimpiar() {
        v.getResultado().setText("");
        DefaultTableModel dm = (DefaultTableModel) v.getTabla().getModel();
        for (int r = 0; r < dm.getRowCount(); r++)
            for (int c = 0; c < dm.getColumnCount(); c++)
                dm.setValueAt(0, r, c);
        celdasUsadas.clear();
        aplicarRenderer();
    }

    // detalles de la tablita

    private int parseInt(Object v) {
        if (v == null) return 0;
        String s = v.toString().trim();
        if (s.isEmpty()) return 0;
        return (int) Math.round(Double.parseDouble(s));
    }

    private long key(int i, int j) { return (((long) i) << 32) ^ (j & 0xffffffffL); }

    /** color de las celdas*/
    private void aplicarRenderer() {
        JTable t = v.getTabla();
        TableCellRenderer base = t.getDefaultRenderer(Object.class);

        t.setDefaultRenderer(Object.class, (tbl, value, isSel, hasFocus, r, c) -> {
            Component comp = base.getTableCellRendererComponent(tbl, value, isSel, hasFocus, r, c);
            comp.setBackground(Color.WHITE);

            int m = (int) v.getOferta().getValue();
            int n = (int) v.getDemanda().getValue();

            if (r < m && c < n) { 
                long k = key(r, c);
                JLabel lab = new JLabel(value == null ? "" : value.toString(), SwingConstants.CENTER);
                lab.setOpaque(true);
                lab.setBackground(celdasUsadas.contains(k) ? new Color(255, 255, 170) : Color.WHITE);
                lab.setFont(new Font("Segoe UI", Font.BOLD, 12));
                return lab;
            }

            if ((r < m && c == n) || (r == m && c < n)) { // color de oferta/demanda
                JLabel lab = new JLabel(value == null ? "" : value.toString(), SwingConstants.CENTER);
                lab.setOpaque(true);
                lab.setBackground(new Color(245, 245, 245));
                return lab;
            }

            return comp; 
        });

        t.repaint();
    }

    private void centrarHeader(JTable t) {
        ((JLabel) t.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    private void volverMenuPrincipal(){
        VistaPrincipal vistaP = new VistaPrincipal();
        vistaP.setVisible(true);
        
        v.dispose();
    }
    
    
    
}
