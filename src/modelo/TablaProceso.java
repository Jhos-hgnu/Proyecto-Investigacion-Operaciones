/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jhosu
 */
public class TablaProceso {
    
    private String titulo;
    private String descripcion;
    private int[][] datos;
    private String[][] datosConMarcas; // Para ceros marcados, líneas, etc.
    
    public TablaProceso(String titulo, String descripcion, int[][] datos) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.datos = datos;
        this.datosConMarcas = new String[datos.length][datos[0].length];
        inicializarDatosConMarcas();
    }
    
    private void inicializarDatosConMarcas() {
        for (int i = 0; i < datos.length; i++) {
            for (int j = 0; j < datos[i].length; j++) {
                datosConMarcas[i][j] = String.valueOf(datos[i][j]);
            }
        }
    }
    
    // Getters
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public int[][] getDatos() { return datos; }
    public String[][] getDatosConMarcas() { return datosConMarcas; }
    
    // Métodos para agregar marcas especiales
    public void marcarCero(int fila, int columna) {
        datosConMarcas[fila][columna] = "[" + datos[fila][columna] + "]";
    }
    
    public void marcarLineaHorizontal(int fila) {
        for (int j = 0; j < datosConMarcas[fila].length; j++) {
            datosConMarcas[fila][j] = "─" + datosConMarcas[fila][j] + "─";
        }
    }
    
    public DefaultTableModel toTableModel() {
    int rows = datosConMarcas.length;
    int cols = datosConMarcas[0].length;
    DefaultTableModel model = new DefaultTableModel();
    // Si quieres encabezados numéricos o vacíos:
    String[] headers = new String[cols];
    for (int j = 0; j < cols; j++) headers[j] = "A" + (j+1);
    model.setColumnIdentifiers(headers);

    for (int i = 0; i < rows; i++) {
        model.addRow(datosConMarcas[i]);
    }
    return model;
}

// Método utilitario para marcar muchos ceros (opcional)
public void marcarCeros(List<int[]> pares) {
    for (int[] p : pares) {
        int f = p[0], c = p[1];
        if (f >= 0 && f < datos.length && c >= 0 && c < datos[0].length) {
            datosConMarcas[f][c] = "[" + datos[f][c] + "]";
        }
    }
}

public void marcarAsignacionConOriginal(int fila, int columna, int valorOriginal) {
    if (fila >= 0 && fila < datos.length && columna >= 0 && columna < datos[0].length) {
        datosConMarcas[fila][columna] = "[" + datos[fila][columna] + "(" + valorOriginal + ")]";
    }
}
    
    
}
