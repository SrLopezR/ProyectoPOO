package org.example.Proyecto.actions;

import org.openxava.actions.ViewBaseAction;

public class CancelarServicioAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        // Solo para probar que el botón funciona
        System.out.println("=== Botón de CancelarServicioAction presionado ===");

        // Obtener ID del servicio actual
        Object id = getView().getValue("id");
        System.out.println("ID del servicio: " + id);

        // Mostrar mensaje simple
        addMessage("⚠️ Acción de cancelar servicio ejecutada");
        addMessage("Esta es solo una prueba. ID del servicio: " + id);

        // Refrescar vista
        getView().refresh();
    }
}