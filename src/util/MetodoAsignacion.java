/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import modelo.ResultadoAsignacion;
import modelo.TablaProceso;

/**
 *
 * @author jhosu
 */
public class MetodoAsignacion {

    private int[][] matrizCostos;
    private int[][] matrizTemporal;
    private int n;
    private List<TablaProceso> historialProcesos;
    private int[][] asignacionOptima;
    private int costoTotal;

    public MetodoAsignacion(int[][] matrizCostos) {
        validarMatriz(matrizCostos);
        this.matrizCostos = copiarMatriz(matrizCostos);
        this.n = matrizCostos.length;
        this.matrizTemporal = new int[n][n];
        this.historialProcesos = new ArrayList<>();
    }
    
    public ResultadoAsignacion resolver() {
        try {
            reiniciarProceso();
            
            reducirFilas();
            reducirColumnas();
            //Buscar asignación óptima
            buscarAsignacionOptima();
            
            return new ResultadoAsignacion(asignacionOptima, costoTotal, historialProcesos, true);
            
        } catch (Exception e) {
            return new ResultadoAsignacion(null, 0, historialProcesos, false);
        }
    }


    // Métodos principales del algoritmo (se implementarán después)
    private void reducirFilas() {
        int n = matrizTemporal.length;
        for (int i = 0; i < n; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (matrizTemporal[i][j] < min) {
                    min = matrizTemporal[i][j];
                }
            }
            if (min == Integer.MAX_VALUE) {
                min = 0;
            }
            for (int j = 0; j < n; j++) {
                matrizTemporal[i][j] = matrizTemporal[i][j] - min;
            }
        }
        guardarPaso("Paso 1: Reducción de Filas",
                "Se resta el mínimo de cada fila a todos los elementos de la fila",
                copiarMatriz(matrizTemporal));
    }
    
    
    private void reducirColumnas() {
    int n = matrizTemporal.length;
    for (int j = 0; j < n; j++) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            if (matrizTemporal[i][j] < min) min = matrizTemporal[i][j];
        }
        if (min == Integer.MAX_VALUE) min = 0;
        for (int i = 0; i < n; i++) {
            matrizTemporal[i][j] = matrizTemporal[i][j] - min;
        }
    }
    guardarPaso("Paso 2: Reducción de Columnas",
               "Se resta el mínimo de cada columna a todos los elementos de la columna",
               copiarMatriz(matrizTemporal));
}
    
    private void buscarAsignacionOptima() {
    int n = matrizTemporal.length;
    asignacionOptima = new int[n][2]; // pares (fila, columna)
    boolean[] filasAsignadas = new boolean[n];
    boolean[] colsAsignadas = new boolean[n];

    int asignIndex = 0;
    // Buscamos ceros en la matriz reducida
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (!filasAsignadas[i] && !colsAsignadas[j] && matrizTemporal[i][j] == 0) {
                asignacionOptima[asignIndex][0] = i;
                asignacionOptima[asignIndex][1] = j;
                asignIndex++;
                filasAsignadas[i] = true;
                colsAsignadas[j] = true;
                break; // pasa a la siguiente fila
            }
        }
    }

    // Si no asignamos todas las filas (p. ej. no hay ceros suficientes), asignamos por mínimos en matriz original
    for (int i = 0; i < n; i++) {
        if (!filasAsignadas[i]) {
            int minCol = -1;
            int minVal = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (!colsAsignadas[j] && matrizCostos[i][j] < minVal) {
                    minVal = matrizCostos[i][j];
                    minCol = j;
                }
            }
            if (minCol >= 0) {
                asignacionOptima[asignIndex][0] = i;
                asignacionOptima[asignIndex][1] = minCol;
                asignIndex++;
                filasAsignadas[i] = true;
                colsAsignadas[minCol] = true;
            }
        }
    }

    // Calculamos costo total usando matrizCostos original
    costoTotal = 0;
    for (int k = 0; k < asignIndex; k++) {
        int i = asignacionOptima[k][0];
        int j = asignacionOptima[k][1];
        costoTotal += matrizCostos[i][j];
    }

    // Guardar paso final con marcas (copiamos para no mutar datos)
    int[][] copiaFinal = copiarMatriz(matrizTemporal);
    // crear tabla con marcas visuales
    guardarPaso("Paso Final: Asignación",
               "Asignación obtenida, el costo es de: " + costoTotal,
               copiaFinal);
}
    
    

    private void guardarPaso(String titulo, String descripcion, int[][] datos) { // Se crea una nueva instancia de TablaProceso con una copia profunda de datos
        int[][] copia = copiarMatriz(datos);
        TablaProceso tabla = new TablaProceso(titulo, descripcion, copia);
        // Puedes añadir marcas aquí si quieres (p. ej. marcar ceros). Lo haremos desde la lógica principal si se desea.
        historialProcesos.add(tabla);
    }

    // Métodos de validación y utilidad
    private void validarMatriz(int[][] matriz) {
        if (matriz == null || matriz.length == 0) {
            throw new IllegalArgumentException("La matriz de costos no puede ser nula o vacía");
        }
        // Validar que sea cuadrada
        for (int[] fila : matriz) {
            if (fila.length != matriz.length) {
                throw new IllegalArgumentException("La matriz debe ser cuadrada");
            }
        }
    }

    private int[][] copiarMatriz(int[][] original) {
        int[][] copia = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copia[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copia;
    }

    private void reiniciarProceso() {
        this.historialProcesos.clear();
        this.matrizTemporal = copiarMatriz(matrizCostos);
    }

}
