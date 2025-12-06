package org.example.Proyecto.actions;

import org.openxava.actions.*;
import org.openxava.util.*;
import org.example.Proyecto.model.*;
import org.example.Proyecto.model.enums.*;
import javax.persistence.EntityManager;
import java.time.LocalDate;

public class CancelarServicioAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        // Pedir motivo de cancelación usando InputDialog
        String motivo = askForMotivo();
        if (motivo == null || motivo.trim().isEmpty()) {
            return; // Usuario canceló
        }

        EntityManager em = org.openxava.jpa.XPersistence.getManager();

        // Obtener el servicio
        Long servicioId = null;
        try {
            Object idObj = getView().getValue("id");
            if (idObj == null) {
                addError("No se puede obtener el ID del servicio");
                return;
            }
            servicioId = Long.parseLong(idObj.toString());
        } catch (Exception e) {
            addError("Error al obtener el ID del servicio");
            return;
        }

        ProgramacionServicios servicio = em.find(ProgramacionServicios.class, servicioId);

        if (servicio == null) {
            addError("Servicio no encontrado");
            return;
        }

        // Validar que no esté ya completado o cancelado
        if (servicio.getEstado() == EstadoProgramacionServicio.COMPLETADO) {
            addError("No se puede cancelar un servicio ya completado");
            return;
        }

        if (servicio.getEstado() == EstadoProgramacionServicio.CANCELADO) {
            addError("El servicio ya está cancelado");
            return;
        }

        // Actualizar servicio
        servicio.setEstado(EstadoProgramacionServicio.CANCELADO);
        String nuevasObservaciones = (servicio.getObservaciones() != null ?
                servicio.getObservaciones() + "\n" : "") +
                "Cancelado el: " + LocalDate.now() + " - Motivo: " + motivo;
        servicio.setObservaciones(nuevasObservaciones);

        // Registrar en observaciones del contrato si es necesario
        if (servicio.getContrato() != null) {
            String observacionContrato = "Servicio cancelado ID: " + servicio.getId() +
                    " Fecha: " + servicio.getFechaServicio() + " Motivo: " + motivo;
            String terminosActuales = servicio.getContrato().getTerminosEspeciales();
            servicio.getContrato().setTerminosEspeciales(
                    (terminosActuales != null ? terminosActuales + "\n" : "") + observacionContrato);
        }

        addMessage("Servicio cancelado exitosamente");
        getView().refresh(); // Refrescar la vista para mostrar cambios
    }

    private String askForMotivo() {
        // Usar InputDialog para pedir motivo
        try {
            return (String) javax.swing.JOptionPane.showInputDialog(
                    null,
                    "Ingrese el motivo de cancelación:",
                    "Motivo de Cancelación",
                    javax.swing.JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    ""
            );
        } catch (Exception e) {
            // Fallback simple
            System.out.println("Ingrese motivo de cancelación:");
            return "Cancelado por usuario"; // Valor por defecto
        }
    }
}