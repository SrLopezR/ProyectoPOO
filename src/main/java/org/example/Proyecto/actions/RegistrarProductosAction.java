package org.example.Proyecto.actions;

import org.openxava.actions.ViewBaseAction;

public class RegistrarProductosAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        System.out.println("=== BOTÓN Registrar Productos PRESIONADO ===");

        // Solo muestra un mensaje
        addMessage("📦 Acción de registrar productos ejecutada");
        addMessage("Esta es una prueba. Funciona correctamente.");

        // Muestra el ID del servicio actual (si existe)
        Object id = getView().getValue("id");
        if (id != null) {
            addMessage("ID del servicio: " + id);
        }

        getView().refresh();
    }
}