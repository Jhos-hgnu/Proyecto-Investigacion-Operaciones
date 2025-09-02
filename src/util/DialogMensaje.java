package util;

import javax.swing.JOptionPane;

public class DialogMensaje {

    public static void mostrarMensaje(int tipo) {

        if (tipo == 1) {
            JOptionPane.showInternalMessageDialog(null, "Por favor debe de ingresar todos los datos ", "ERROR \"DATOS VACIOS\"", JOptionPane.WARNING_MESSAGE);

        } else if (tipo == 2) {
            JOptionPane.showInternalMessageDialog(null, "No hay ninguna fila seleccionada", "ERROR \"DATOS NO SELECCIONADOS\"", JOptionPane.WARNING_MESSAGE);

        }

    }

}
