/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Madelin
 */


public class MetodoEsquinaNoroeste {

    // ==== CLASES AUXILIARES ====
    public static class Paso {
        public int i, j;     // índices (oferta y demanda)
        public int asign;    // cantidad asignada
        public int costo;    // costo unitario

        public Paso(int i, int j, int asign, int costo) {
            this.i = i;
            this.j = j;
            this.asign = asign;
            this.costo = costo;
        }
    }

    public static class Resultado {
        public List<Paso> pasos = new ArrayList<>();
        public int costoTotal = 0;
    }

    // MÉTODO PRINCIPAL 
    public static Resultado resolver(int[][] costos, int[] oferta, int[] demanda) {
        int m = oferta.length;
        int n = demanda.length;

        int[] s = Arrays.copyOf(oferta, m);
        int[] d = Arrays.copyOf(demanda, n);

        Resultado R = new Resultado();

        int i = 0, j = 0;
        while (i < m && j < n) {
            int asign = Math.min(s[i], d[j]);
            R.pasos.add(new Paso(i, j, asign, costos[i][j]));
            R.costoTotal += asign * costos[i][j];

            s[i] -= asign;
            d[j] -= asign;

            if (s[i] == 0) i++;
            else if (d[j] == 0) j++;
        }

        return R;
    }

    // ==== FORMATP DEL RESULTADO ====
    public static String formatearDetalle(Resultado R) {
        StringBuilder sb = new StringBuilder();
        java.util.Locale us = java.util.Locale.US; 

        for (Paso p : R.pasos) {
            int subtotal = p.asign * p.costo;
            sb.append(String.format(us, "S%d -> %d x %d = %,d%n",
                    p.i + 1, p.asign, p.costo, subtotal));
        }

        sb.append(String.format(us, "%nTotal = %,d", R.costoTotal));
        return sb.toString();
    }
}

