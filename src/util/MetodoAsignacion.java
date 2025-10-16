/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
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

    // Intento inicial de matching en la matriz reducida actual
    int[] matchCol = maxMatchingEnCeros(matrizTemporal);
    int matchCount = 0;
    for (int j = 0; j < matchCol.length; j++) if (matchCol[j] != -1) matchCount++;

    // Si no hay matching completo, aplicar trazado de líneas + ajuste iterativo
    if (matchCount < n) {
        aplicarTrazadoLineasYAjusteHastaAsignar();
    }

    // Recalcular matching sobre la matriz posiblemente ajustada
    int[] finalMatchCol = maxMatchingEnCeros(matrizTemporal);

    // Convertir a lista de pares (fila, columna)
    List<int[]> asignList = new ArrayList<>();
    for (int j = 0; j < finalMatchCol.length; j++) {
        if (finalMatchCol[j] != -1) {
            asignList.add(new int[]{finalMatchCol[j], j});
        }
    }

    // Guardar en asignacionOptima
    asignacionOptima = new int[asignList.size()][2];
    for (int k = 0; k < asignList.size(); k++) {
        asignacionOptima[k][0] = asignList.get(k)[0];
        asignacionOptima[k][1] = asignList.get(k)[1];
    }

    // Calcular costo total con la matriz original
    costoTotal = 0;
    for (int k = 0; k < asignacionOptima.length; k++) {
        int i = asignacionOptima[k][0];
        int j = asignacionOptima[k][1];
        costoTotal += matrizCostos[i][j];
    }

    // Marcar en la última TablaProceso las asignaciones con los valores originales
    if (!historialProcesos.isEmpty()) {
        TablaProceso ultima = historialProcesos.get(historialProcesos.size() - 1);
        for (int k = 0; k < asignacionOptima.length; k++) {
            int i = asignacionOptima[k][0];
            int j = asignacionOptima[k][1];
            // Valor original desde matrizCostos
            int valorOriginal = matrizCostos[i][j];
            ultima.marcarAsignacionConOriginal(i, j, valorOriginal);
        }
    } else {
        // Por seguridad: crear última entrada si no existe
        guardarPaso("Asignación final", "Asignaciones con valores originales", matrizTemporal);
        TablaProceso ultima = historialProcesos.get(historialProcesos.size() - 1);
        for (int k = 0; k < asignacionOptima.length; k++) {
            int i = asignacionOptima[k][0];
            int j = asignacionOptima[k][1];
            ultima.marcarAsignacionConOriginal(i, j, matrizCostos[i][j]);
        }
    }

    // Guardar resumen final (opcional)
    guardarPaso("Resultado Final (Resumen)", "Costo total = " + costoTotal, matrizTemporal);
              
}
    
    
    private List<int[]> encontrarCeros(int[][] mat) {
    List<int[]> ceros = new ArrayList<>();
    int n = mat.length;
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (mat[i][j] == 0) ceros.add(new int[]{i, j});
        }
    }
    return ceros;
}

/**
 * Matching máximo en el grafo bipartito definido por ceros.
 * Devuelve matchCol donde matchCol[j] = i (fila i emparejada con columna j), -1 si libre.
 * Usamos DFS de aumentos (suficiente para n pequeño/mediano).
 */
private int[] maxMatchingEnCeros(int[][] mat) {
    int n = mat.length;
    List<Integer>[] adj = new ArrayList[n];
    for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (mat[i][j] == 0) adj[i].add(j);
        }
    }

    int[] matchCol = new int[n];
    Arrays.fill(matchCol, -1);
    int[] matchRow = new int[n];
    Arrays.fill(matchRow, -1);

    for (int u = 0; u < n; u++) {
        boolean[] seen = new boolean[n];
        dfsAug(u, adj, matchRow, matchCol, seen);
    }
    return matchCol;
}

private boolean dfsAug(int u, List<Integer>[] adj, int[] matchRow, int[] matchCol, boolean[] seen) {
    for (int v : adj[u]) {
        if (seen[v]) continue;
        seen[v] = true;
        if (matchCol[v] == -1 || dfsAug(matchCol[v], adj, matchRow, matchCol, seen)) {
            matchCol[v] = u;
            matchRow[u] = v;
            return true;
        }
    }
    return false;
}

/**
 * A partir de matchCol obtén la cobertura mínima (líneas).
 * Retorna boolean[][] donde [0] = coverRows, [1] = coverCols.
 *
 * Algoritmo estándar:
 * - Construir matchRow desde matchCol.
 * - Empezar desde filas no emparejadas; hacer búsqueda alternante (filas->columnas por aristas ceros, columnas->fila por arista matching).
 * - VisRows = filas alcanzadas, VisCols = columnas alcanzadas.
 * - coverRows = filas NOT VisRows, coverCols = VisCols.
 */
private boolean[][] obtenerMinLineasDesdeMatching(int[][] mat, int[] matchCol) {
    int n = mat.length;
    int[] matchRow = new int[n];
    Arrays.fill(matchRow, -1);
    for (int j = 0; j < n; j++) {
        if (matchCol[j] != -1) matchRow[matchCol[j]] = j;
    }

    boolean[] visRow = new boolean[n];
    boolean[] visCol = new boolean[n];

    // filas no emparejadas: iniciar búsqueda desde ellas
    for (int i = 0; i < n; i++) {
        if (matchRow[i] == -1) {
            Stack<Integer> stack = new Stack<>();
            stack.push(i);
            visRow[i] = true;
            while (!stack.isEmpty()) {
                int r = stack.pop();
                for (int c = 0; c < n; c++) {
                    if (mat[r][c] == 0 && !visCol[c]) {
                        visCol[c] = true;
                        if (matchCol[c] != -1 && !visRow[matchCol[c]]) {
                            visRow[matchCol[c]] = true;
                            stack.push(matchCol[c]);
                        }
                    }
                }
            }
        }
    }

    boolean[] coverRows = new boolean[n];
    boolean[] coverCols = new boolean[n];
    for (int i = 0; i < n; i++) coverRows[i] = !visRow[i];
    for (int j = 0; j < n; j++) coverCols[j] = visCol[j];

    return new boolean[][]{coverRows, coverCols};
}

/**
 * Ajusta la matriz según las líneas: restar mínimo a no cubiertos; sumar mínimo en intersección.
 * Devuelve copia ajustada.
 * IMPORTANTE: si usas INF en algunas celdas, ignóralas (no usarlas para min).
 */
private int[][] ajustarSegunLineas(int[][] mat, boolean[] coverRows, boolean[] coverCols) {
    int n = mat.length;
    int INF = Integer.MAX_VALUE / 4;
    int min = Integer.MAX_VALUE;
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (!coverRows[i] && !coverCols[j]) {
                int val = mat[i][j];
                if (val < min && val < INF) min = val;
            }
        }
    }
    if (min == Integer.MAX_VALUE) return copiarMatriz(mat);

    int[][] copia = copiarMatriz(mat);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (!coverRows[i] && !coverCols[j]) {
                // no cubierto -> restar
                copia[i][j] = copia[i][j] - min;
            } else if (coverRows[i] && coverCols[j]) {
                // intersección -> sumar
                copia[i][j] = copia[i][j] + min;
            }
            // los demás (cubiertos una sola línea) quedan igual
        }
    }
    return copia;
}

/**
 * Función que itera: matching de ceros -> si no completo obtiene líneas mínimas -> ajusta -> guarda pasos -> repite.
 * Guarda la tabla con las líneas y la tabla ajustada en historialProcesos (usa copiar antes de modificar).
 */
private void aplicarTrazadoLineasYAjusteHastaAsignar() {
    int n = matrizTemporal.length;
    int iter = 0;
    final int MAX_ITER = 5 * n; // tope de seguridad

    while (iter < MAX_ITER) {
        iter++;
        // 1) matching en ceros
        int[] matchCol = maxMatchingEnCeros(matrizTemporal);
        int matchCount = 0;
        for (int j = 0; j < matchCol.length; j++) if (matchCol[j] != -1) matchCount++;

        // 2) obtener líneas mínimas (coverRows, coverCols)
        boolean[][] covers = obtenerMinLineasDesdeMatching(matrizTemporal, matchCol);
        boolean[] coverRows = covers[0];
        boolean[] coverCols = covers[1];

        // 3) Crear tabla visual que marca ceros y también dibuja líneas (marcas).
        // Hacemos una copia para no referenciar matrizTemporal directamente.
        int[][] copiaParaTabla = copiarMatriz(matrizTemporal);
        TablaProceso tpVis = new TablaProceso("Trazado líneas - iter " + iter,
                "Matching ceros: " + matchCount + " / " + n, copiaParaTabla);

        // marcar ceros para visual
        List<int[]> ceros = encontrarCeros(matrizTemporal);
        tpVis.marcarCeros(ceros);

        // marcar filas cubiertas (línea horizontal)
        for (int i = 0; i < coverRows.length; i++) {
            if (coverRows[i]) tpVis.marcarLineaHorizontal(i);
        }
        // marcar columnas cubiertas (línea vertical) - representado con barras en la celda
        for (int j = 0; j < coverCols.length; j++) {
            if (coverCols[j]) {
                for (int i = 0; i < tpVis.getDatosConMarcas().length; i++) {
                    tpVis.getDatosConMarcas()[i][j] = "|" + tpVis.getDatosConMarcas()[i][j] + "|";
                }
            }
        }

        // Guardamos la tabla de líneas marcadas
        historialProcesos.add(tpVis);

        if (matchCount == n) {
            // ya podemos asignar con ceros: terminamos bucle
            break;
        }

        // 4) ajustar matriz
        int[][] ajustada = ajustarSegunLineas(matrizTemporal, coverRows, coverCols);

        // 5) Guardar la matriz ajustada como paso (guardarPaso hará copia)
        guardarPaso("Ajuste por líneas - iter " + iter,
                "Se restó min fuera de líneas y se sumó en intersecciones", ajustada);

        // 6) sustituir matrizTemporal por la ajustada y repetir
        matrizTemporal = copiarMatriz(ajustada);
    }
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
