/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 *
 * @author jhosu
 */
public class BotonEstilo {
    
    public static void aplicarHover(JButton boton,Color base, Color hover){
        
        boton.setBackground(base);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        boton.addMouseListener(new MouseListener() {
            
            public void mouseEntered(MouseEvent e){
                boton.setBackground(hover);
            }
            public void mouseExited(MouseEvent e) {
                boton.setBackground(base);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
            
        });
        
    }
    
    
    
}
