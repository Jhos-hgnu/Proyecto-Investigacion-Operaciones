/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import javax.swing.JPanel;
import modelo.ModeloPunto;
import modelo.ModeloVistaMetodoGrafico;

/**
 *
 * @author jhosu
 */
public class PanelGrafico extends JPanel {

    private List<ModeloVistaMetodoGrafico> restricciones;
    private List<ModeloPunto> factibles;

    public PanelGrafico(List<ModeloVistaMetodoGrafico> restricciones, List<ModeloPunto> factibles) {
        this.restricciones = restricciones;
        this.factibles = factibles;
    }
    
   
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        // Trasladar el origen al centro
        g2.translate(width / 2, height / 2);
        g2.scale(1, -1); // Invertir eje Y

        // Dibujar ejes
        g2.setColor(Color.BLACK);
        g2.drawLine(-width/2, 0, width/2, 0); // eje X
        g2.drawLine(0, -height/2, 0, height/2); // eje Y

        // Dibujar restricciones como líneas
        g2.setColor(Color.BLUE);
        for (ModeloVistaMetodoGrafico r : restricciones) {
            if (r.getB() != 0) {
                // y = (c - a*x) / b
                int x1 = -width/2;
                int y1 = (int)((r.getC() - r.getA() * x1) / r.getB());
                int x2 = width/2;
                int y2 = (int)((r.getC() - r.getA() * x2) / r.getB());
                g2.drawLine(x1, y1, x2, y2);
            }
        }

        // Dibujar puntos factibles
        g2.setColor(Color.RED);
        for (ModeloPunto p : factibles) {
            int px = (int) p.getX();
            int py = (int) p.getY();
            g2.fillOval(px-3, py-3, 6, 6);
        }
    }
}


/*
// Dibujar ejes
        g2.drawLine(50, getHeight() - 50, getWidth() - 50, getHeight() - 50); // eje X
        g2.drawLine(50, getHeight() - 50, 50, 50); // eje Y

        // Dibujar puntos factibles
        g2.setColor(Color.RED);
        for (double[] p : puntos) {
            int x = (int) (50 + p[0] * 20); // escalado
            int y = (int) (getHeight() - 50 - p[1] * 20);
            g2.fillOval(x - 3, y - 3, 6, 6);
        }




*/
