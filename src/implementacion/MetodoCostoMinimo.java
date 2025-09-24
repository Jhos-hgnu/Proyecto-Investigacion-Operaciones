/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package implementacion;

import java.util.Arrays;
import javax.swing.JTable;
import modelo.ModeloCostoMinimo;

/**
 *
 * @author jhosu
 */
public class MetodoCostoMinimo {

    int[][] costos;       // matriz de costos [suministro][destino]
    int[] oferta;         // vector de oferta
    int[] demanda;        // vector de demanda
    int[][] asignaciones;
    public boolean balanceado;

    public MetodoCostoMinimo(JTable tablaCostos, JTable tablaOferta, JTable tablaDemanda) {
        this.oferta = extraerVectorDesdeTabla(tablaOferta, "oferta");
        this.demanda = extraerVectorDesdeTabla(tablaDemanda, "demanda");
//        this.costos = extraerMatrizDesdeTabla(tablaCostos, oferta.length, demanda.length);
        this.costos = extraerMatrizDesdeTablaConEncabezado(tablaCostos);
        balancear();
        asignaciones = new int[oferta.length][demanda.length];

        if (costos.length != oferta.length || costos[0].length != demanda.length) {
            System.out.println("La matriz esta mal papu");

        }

    }

    public int[] extraerVectorDesdeTabla(JTable tabla, String tipo) {

        int[] vector = new int[tabla.getRowCount()];
        for (int i = 0; i < vector.length; i++) {
            Object valor = tabla.getValueAt(i, 0);
            if (valor == null || valor.toString().trim().isEmpty()) {
                throw new IllegalArgumentException("La tabla de " + tipo + "");
            }
            try {
                vector[i] = Integer.parseInt(valor.toString().trim());
            } catch (Exception e) {
                throw new IllegalArgumentException("La tabla de " + tipo + "");
            }

        }

        return vector;
    }

//    public int[][] extraerMatrizDesdeTabla(JTable tabla, int filas, int columnas) {
//
//        int[][] matriz = new int[filas][columnas - 1];
//        for (int i = 0; i < filas; i++) {
//            for (int j = 1; j < columnas; j++) {
//                Object valor = tabla.getValueAt(i, j);
//                if (valor == null || valor.toString().trim().isEmpty()) {
//                    throw new IllegalArgumentException("La tabla de costos");
//                }
//                try {
//                    matriz[i][j - 1] = Integer.parseInt(valor.toString().trim());
//                } catch (NumberFormatException e) {
//                    throw new IllegalArgumentException("La tabla de costos contiene valores no numéricos");
//                }
//
//            }
//        }
//        return matriz;
//
//    }
    
    //Probando metodo alternativo
    public int[][] extraerMatrizDesdeTablaConEncabezado(JTable tabla) {
    int filas = tabla.getRowCount();
    int columnas = tabla.getColumnCount();
    
        System.out.println(filas);
        System.out.println(columnas);

    // Ignorar la primera columna (encabezado de fila)
    int[][] matriz = new int[filas][columnas - 1];

    for (int i = 0; i < filas; i++) {
        for (int j = 1; j < columnas; j++) { // empieza en 1
            Object valor = tabla.getValueAt(i, j);
            if (valor == null || valor.toString().trim().isEmpty()) {
                throw new IllegalArgumentException("La tabla de costos tiene celdas vacías en [" + (i + 1) + "," + j + "]");
            }
            try {
                matriz[i][j - 1] = Integer.parseInt(valor.toString().trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("La tabla de costos tiene valores no numéricos en [" + (i + 1) + "," + j + "]");
            }
        }
    }

    return matriz;
}
    

    public void balancear() {
        //Validar si el método se encuentra baleanceado
        int totalOferta = Arrays.stream(oferta).sum();
        int totalDemanda = Arrays.stream(demanda).sum();

        if (totalOferta == totalDemanda) {
            balanceado = true;
            System.out.println(balanceado);
            return;
        }

        balanceado = false;
        if (totalOferta > totalDemanda) {
            int[] nuevaDemanda = Arrays.copyOf(demanda, demanda.length + 1);
            nuevaDemanda[nuevaDemanda.length - 1] = totalOferta - totalDemanda;
            int[][] nuevosCostos = new int[oferta.length][nuevaDemanda.length];

            for (int i = 0; i < oferta.length; i++) {
                System.arraycopy(costos[i], 0, nuevosCostos[i], 0, demanda.length);
                nuevosCostos[i][nuevaDemanda.length - 1] = 0;
            }
            demanda = nuevaDemanda;
            costos = nuevosCostos;
        } else {
            //Agregar suministro ficticio
            int[] nuevaOferta = Arrays.copyOf(oferta, oferta.length + 1);
            nuevaOferta[nuevaOferta.length - 1] = totalDemanda - totalOferta;
            int[][] nuevosCostos = new int[nuevaOferta.length][demanda.length];
            for (int i = 0; i < oferta.length; i++) {
                System.arraycopy(costos[i], 0, nuevosCostos[i], 0, costos[i].length);
                //System.arraycopy(costos[i], 0, nuevosCostos[i], 0, demanda.length);
            }
            for (int j = 0; j < demanda.length; j++) {
                nuevosCostos[nuevaOferta.length - 1][j] = 0;
            }
            oferta = nuevaOferta;
            costos = nuevosCostos;

        }
    }

    public void resolverCostoMinimo() {

        boolean[] filaSatisfecha = new boolean[oferta.length];
        boolean[] columnaSatisfecha = new boolean[demanda.length];

        System.out.println("Oferta: " + oferta.length);
        System.out.println("Demanda: " + demanda.length);
        System.out.println("Costos: " + costos.length + " x " + costos[0].length);

        while (!todoAsignado(filaSatisfecha, columnaSatisfecha)) {
            System.out.println("Hola desde Resolver Costo Min");

            int[] pos = buscarMenorCosto(filaSatisfecha, columnaSatisfecha);
            int i = pos[0];
            int j = pos[1];

            int cantidad = Math.min(oferta[i], demanda[j]);
            asignaciones[i][j] = cantidad;
            oferta[i] -= cantidad;
            demanda[j] -= cantidad;

            if (oferta[i] == 0) {
                filaSatisfecha[i] = true;
            }
            if (demanda[j] == 0) {
                columnaSatisfecha[j] = true;
            }
        }

    }

    public boolean todoAsignado(boolean[] filas, boolean[] columnas) {
        for (boolean f : filas) {
            if (!f) {
                return false;
            }
        }
        for (boolean c : columnas) {
            if (!c) {
                return false;
            }
        }
        return true;
    }

    public int[] buscarMenorCosto(boolean[] filas, boolean[] columnas) {
        int min = Integer.MAX_VALUE;
        int[] pos = {-1, -1};
        for (int i = 0; i < costos.length; i++) {
            if (filas[i]) {
                continue;
            }
            for (int j = 0; j < costos[0].length; j++) {
                if (columnas[j]) {
                    continue;
                }
                if (costos[i][j] < min) {
                    min = costos[i][j];
                    pos[0] = i;
                    pos[1] = j;
                }
            }

        }
        return pos;
    }

    public int getCostoTotal() {
        int total = 0;
        for (int i = 0; i < asignaciones.length; i++) {
            for (int j = 0; j < asignaciones[0].length; j++) {
                total += asignaciones[i][j] * costos[i][j];
            }
        }
        return total;
    }

    public int[][] getAsignaciones() {
        return asignaciones;
    }

    public boolean estaBalanceado() {
        return balanceado;
    }

}
