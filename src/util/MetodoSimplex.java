package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetodoSimplex {

    public static class Resultado {

        public double[] x;
        public double z;
        public String estado;
        public List<double[][]> tablas;
        public List<int[]> basicas;
        public String[] headers;
        public List<int[]> pivotes;
    }

    public static Resultado maximizar(double[] c, double[][] A, double[] b) {
        int m = A.length, n = c.length;
        int rows = m + 1;
        int cols = n + m + 1;
        double[][] T = new double[rows][cols];

        // Restricciones + holguras
        for (int i = 0; i < m; i++) {
            System.arraycopy(A[i], 0, T[i + 1], 0, n);
            T[i + 1][n + i] = 1.0;
            T[i + 1][cols - 1] = b[i];
        }
        // Fila objetivo: -c
        for (int j = 0; j < n; j++) {
            T[0][j] = -c[j];
        }

        // Nombres columnas 
        String[] headers = new String[n + m + 1];
        for (int j = 0; j < n; j++) {
            headers[j] = "x" + (j + 1);
        }
        for (int j = 0; j < m; j++) {
            headers[n + j] = "x" + (n + j + 1);
        }
        headers[cols - 1] = "LD";

        // holguras
        int[] basicas = new int[m];
        for (int i = 0; i < m; i++) {
            basicas[i] = n + i;
        }

        List<double[][]> tablas = new ArrayList<>();
        List<int[]> bases = new ArrayList<>();
        List<int[]> pivotes = new ArrayList<>();
        tablas.add(copia(T));
        bases.add(basicas.clone());
        pivotes.add(new int[]{-1, -1});

        while (true) {
            int colIn = -1;
            double min = -1e-12;
            for (int j = 0; j < cols - 1; j++) {
                if (T[0][j] < min) {
                    min = T[0][j];
                    colIn = j;
                }
            }
            if (colIn == -1) {
                Resultado r = new Resultado();
                r.estado = "OPTIMO";
                r.tablas = tablas;
                r.basicas = bases;
                r.headers = headers;
                r.pivotes = pivotes;
                r.z = T[0][cols - 1];
                r.x = new double[n + m];
                for (int i = 1; i < rows; i++) {
                    int var = basicas[i - 1];
                    r.x[var] = T[i][cols - 1];
                }
                return r;
            }

            int rowOut = -1;
            double mejor = Double.POSITIVE_INFINITY;
            for (int i = 1; i < rows; i++) {
                double a = T[i][colIn];
                if (a > 1e-12) {
                    double ratio = T[i][cols - 1] / a;
                    if (ratio < mejor - 1e-12) {
                        mejor = ratio;
                        rowOut = i;
                    }
                }
            }
            if (rowOut == -1) {
                Resultado r = new Resultado();
                r.estado = "ILIMITADO";
                r.tablas = tablas;
                r.basicas = bases;
                r.headers = headers;
                r.pivotes = pivotes;
                r.x = new double[n + m];
                r.z = Double.POSITIVE_INFINITY;
                return r;
            }

            int[] pivote = new int[]{rowOut, colIn};
            pivote(T, rowOut, colIn);
            basicas[rowOut - 1] = colIn;

            tablas.add(copia(T));
            bases.add(basicas.clone());
            pivotes.add(pivote);
        }
    }

    private static void pivote(double[][] T, int rp, int cp) {
        int rows = T.length, cols = T[0].length;
        double p = T[rp][cp];
        for (int j = 0; j < cols; j++) {
            T[rp][j] /= p;
        }
        for (int i = 0; i < rows; i++) {
            if (i != rp) {
                double f = T[i][cp];
                if (Math.abs(f) > 1e-12) {
                    for (int j = 0; j < cols; j++) {
                        T[i][j] -= f * T[rp][j];
                    }
                }
            }
        }
    }

    private static double[][] copia(double[][] M) {
        double[][] C = new double[M.length][M[0].length];
        for (int i = 0; i < M.length; i++) {
            C[i] = Arrays.copyOf(M[i], M[i].length);
        }
        return C;
    }

    // Formato  iteraciones
    public static String formatearTabla(double[][] T, String[] headers, int[] basicas, int iter) {
        StringBuilder sb = new StringBuilder();
        sb.append("Iteración ").append(iter).append("\n");

        sb.append(String.format("%-18s%-12s%10s", "Variables básicas", "Ecuaciones", "Z"));
        for (String h : headers) {
            sb.append(String.format("%10s", h));
        }
        sb.append("\n");

        sb.append(repeat("-", 18 + 12 + 10 + 10 * headers.length)).append("\n");

        // fila Z
        sb.append(String.format("%-18s%-12s%10.2f", "Z", "0", 1.0));
        for (int j = 0; j < headers.length; j++) {
            sb.append(String.format("%10.2f", T[0][j]));
        }
        sb.append("   <- Z\n");

        // filas restricciones
        for (int i = 1; i < T.length; i++) {
            int varIndex = basicas[i - 1];
            String nombreBasica = headers[varIndex];
            sb.append(String.format("%-18s%-12s%10.2f", nombreBasica, String.valueOf(i), 0.0));
            for (int j = 0; j < headers.length; j++) {
                sb.append(String.format("%10.2f", T[i][j]));
            }
            sb.append("\n");
        }

        sb.append("\n");
        return sb.toString();
    }

    private static String repeat(String s, int n) {
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < n; i++) {
            r.append(s);
        }
        return r.toString();
    }

}
