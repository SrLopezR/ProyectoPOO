package org.example.Proyecto.actions;

import org.openxava.actions.ViewBaseAction;

public class CancelarServicioAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        System.out.println("=== Botón de CancelarServicioAction presionado ===");

        Object id = getView().getValue("id");
        System.out.println("ID del servicio: " + id);

        addMessage("Acción de cancelar servicio ejecutada");
        addMessage("Esta es solo una prueba. ID del servicio: " + id);

        getView().refresh();
    }
}