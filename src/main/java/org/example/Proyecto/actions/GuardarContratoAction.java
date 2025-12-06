package org.example.Proyecto.actions;

import org.example.Proyecto.model.enums.EstadoContrato;
import org.openxava.actions.*;
import org.openxava.util.Messages;
import org.openxava.util.Is;

import java.time.LocalDate;

public class GuardarContratoAction extends SaveAction {

    @Override
    public void execute() throws Exception {
        Messages errors = validate();
        if (errors.contains()) {
            addErrors(errors);
            return;
        }

        // Validaciones específicas
        LocalDate fechaInicio = (LocalDate) getView().getValue("fechaInicio");
        LocalDate fechaFin = (LocalDate) getView().getValue("fechaFin");
        EstadoContrato estado = (EstadoContrato) getView().getValue("estado");

        if (fechaFin != null && fechaInicio != null && fechaFin.isBefore(fechaInicio)) {
            addError("La fecha de fin debe ser posterior a la fecha de inicio");
            return;
        }


        // Generar número de contrato si está vacío
        String numeroContrato = (String) getView().getValue("numeroContrato");
        if (Is.emptyString(numeroContrato)) {
            String nuevoNumero = "CTR-" + System.currentTimeMillis();
            getView().setValue("numeroContrato", nuevoNumero);
        }

        super.execute();

        // Actualizar historial
        addMessage("Contrato guardado exitosamente. Número: " + getView().getValueString("numeroContrato"));
    }

    private Messages validate() {
        Messages errors = new Messages();

        // Validaciones básicas
        if (getView().getValue("cliente") == null) {
            errors.add("El cliente es requerido");
        }

        if (getView().getValue("tipoContrato") == null) {
            errors.add("El tipo de contrato es requerido");
        }

        return errors;
    }
}