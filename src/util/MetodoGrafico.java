package util;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.ModeloPunto;
import modelo.ModeloVistaMetodoGrafico;

public class MetodoGrafico {

    public static List<ModeloVistaMetodoGrafico> obtenerRestricciones(JTable tablaRestricciones) {
        List<ModeloVistaMetodoGrafico> restricciones = new ArrayList<>();

        int filas = tablaRestricciones.getRowCount();

        for (int i = 0; i < filas; i++) {

            try {
                double a = Double.parseDouble(tablaRestricciones.getValueAt(i, 0).toString());
                double b = Double.parseDouble(tablaRestricciones.getValueAt(i, 1).toString());
                String operador = tablaRestricciones.getValueAt(i, 2).toString();
                double c = Double.parseDouble(tablaRestricciones.getValueAt(i, 3).toString());

                ModeloVistaMetodoGrafico restriccion = new ModeloVistaMetodoGrafico(a, b, operador, c);
                restricciones.add(restriccion);

            } catch (Exception e) {
            }

        }
        return restricciones;
    }

    public static List<ModeloPunto> interseccionesConEjes(ModeloVistaMetodoGrafico r) {
        List<ModeloPunto> puntos = new ArrayList<>();

        if (r.getA() != 0) {
            double x = r.getC() / r.getA();
            puntos.add(new ModeloPunto(x, 0));
        }

        if (r.getB() != 0) {
            double y = r.getC() / r.getB();
            puntos.add(new ModeloPunto(0, y));
        }
        return puntos;
    }

    public static ModeloPunto interseccionEntreRestricciones(ModeloVistaMetodoGrafico r1, ModeloVistaMetodoGrafico r2) {
        double a1 = r1.getA(), b1 = r1.getB(), c1 = r1.getC();
        double a2 = r2.getA(), b2 = r2.getB(), c2 = r2.getC();

        double determinante = a1 * b2 - a2 * b1;

        if (determinante == 0) {
            return null; // Son paralelas o coinciden, no hay intersección única
        }

        double x = (c1 * b2 - c2 * b1) / determinante;
        double y = (a1 * c2 - a2 * c1) / determinante;

        return new ModeloPunto(x, y);
    }

    public static boolean cumpleRestriccion(ModeloPunto p, ModeloVistaMetodoGrafico r) {
        double lhs = r.getA() * p.getX() + r.getB() * p.getY();
        double rhs = r.getC();

        switch (r.getOperador()) {
            case "<=":
                return lhs <= rhs + 1e-6; // tolerancia por decimales
            case ">=":
                return lhs >= rhs - 1e-6;
            case "=":
                return Math.abs(lhs - rhs) < 1e-6;
            default:
                return false;
        }
    }

    public static boolean esFactible(ModeloPunto p, List<ModeloVistaMetodoGrafico> restricciones) {
        for (ModeloVistaMetodoGrafico r : restricciones) {
            if (!cumpleRestriccion(p, r)) {
                return false;
            }
        }
        return true;
    }
    
    public static double evaluarFuncionObjetivo(ModeloPunto p, double cx, double cy) {
    return cx * p.getX() + cy * p.getY();
}
    
    public static ModeloPunto encontrarOptimo(List<ModeloPunto> factibles, double cx, double cy, boolean maximizar) {
    ModeloPunto mejor = null;
    double mejorValor = maximizar ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

    for (ModeloPunto p : factibles) {
        double valor = evaluarFuncionObjetivo(p, cx, cy);

        if ((maximizar && valor > mejorValor) || (!maximizar && valor < mejorValor)) {
            mejorValor = valor;
            mejor = p;
        }
    }

    System.out.println("Óptimo en " + mejor + " con Z = " + mejorValor);
    return mejor;
}

    

}

//    public static double[] interseccionEjeX(ModeloVistaMetodoGrafico r){
//        if (r.getA() == 0) return null; //No cruza con X
//        double x = r.getC() / r.getA();
//        return new double[]{x,0};
//    }
//    
//    public static double[] interseccionEjeY(ModeloVistaMetodoGrafico r){
//        if(r.getB() == 0) return null;
//        double y = r.getC() / r.getB();
//        return new double[]{0,y};
//    }

