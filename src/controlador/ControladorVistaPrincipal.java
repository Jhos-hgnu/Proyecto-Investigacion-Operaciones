package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.ModeloVistaPrincipal;
import vista.VistaModeloAsignacion;
import vista.VistaModeloTransporte;
import vista.VistaProgramacionLineal;

public class ControladorVistaPrincipal implements ActionListener{
    
    ModeloVistaPrincipal modelo;

    public ControladorVistaPrincipal(ModeloVistaPrincipal modelo) {
        this.modelo = modelo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == modelo.getVistaPrincipal().btnProgramacionLineal){
            VistaProgramacionLineal vistaPLineal = new VistaProgramacionLineal();
            vistaPLineal.setVisible(true);
            
            modelo.getVistaPrincipal().dispose();
        } else if (e.getSource() == modelo.getVistaPrincipal().btnMetodoTransporte){
            VistaModeloTransporte vistaMTransporte = new VistaModeloTransporte();
            vistaMTransporte.setVisible(true);
            
            modelo.getVistaPrincipal().dispose();
        } else if (e.getSource() == modelo.getVistaPrincipal().btnModeloAsignacion){
            VistaModeloAsignacion vistaAsignacion = new VistaModeloAsignacion();
            vistaAsignacion.setVisible(true);
            
            modelo.getVistaPrincipal().dispose();
        }
        
        
        
    }
    
    
}
