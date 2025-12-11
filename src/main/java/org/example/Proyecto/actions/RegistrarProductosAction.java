package org.example.Proyecto.actions;

import org.openxava.actions.ViewBaseAction;

public class RegistrarProductosAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        System.out.println("=== BOTÓN Registrar Productos PRESIONADO ===");

        addMessage("Acción de registrar productos ejecutada");
        addMessage("Esta es una prueba. Funciona correctamente.");

        Object id = getView().getValue("id");
        if (id != null) {
            addMessage("ID del servicio: " + id);
        }

        getView().refresh();
    }
}