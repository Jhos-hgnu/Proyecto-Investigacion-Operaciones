package modelo;

import vista.VistaMetodoGrafico;

public class ModeloVistaMetodoGrafico {
    
    VistaMetodoGrafico vistaMetodoGrafico;
    
    private double a;
    private double b;
    private String operador;
    private double c;

    public ModeloVistaMetodoGrafico(double a, double b, String operador, double c) {
     
        this.a = a;
        this.b = b;
        this.operador = operador;
        this.c = c;
    }
    
    public ModeloVistaMetodoGrafico() {
    }

    public ModeloVistaMetodoGrafico(VistaMetodoGrafico vistaMetodoGrafico) {
        this.vistaMetodoGrafico = vistaMetodoGrafico;
    }

    public VistaMetodoGrafico getVistaMetodoGrafico() {
        return vistaMetodoGrafico;
    }

    public void setVistaMetodoGrafico(VistaMetodoGrafico vistaMetodoGrafico) {
        this.vistaMetodoGrafico = vistaMetodoGrafico;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }
    
    @Override
    public String toString(){
        return a + "x + " + b + "y " + operador + " " + c;
    }
    
    
}
