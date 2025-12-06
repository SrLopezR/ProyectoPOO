package org.example.Proyecto.actions;

import org.example.Proyecto.model.enums.DiaServicio;
import org.example.Proyecto.model.enums.EstadoContrato;
import org.example.Proyecto.model.enums.EstadoProgramacionServicio;
import org.example.Proyecto.model.enums.FrecuenciaContrato;
import org.openxava.actions.*;
import org.openxava.jpa.XPersistence;
import org.example.Proyecto.model.*;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class GenerarProgramacionAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        EntityManager em = XPersistence.getManager();

        // Obtener el contrato actual
        Long contratoId = Long.parseLong(getView().getValueString("id"));
        Contrato contrato = em.find(Contrato.class, contratoId);

        if (contrato == null) {
            addError("Contrato no encontrado");
            return;
        }

        // Validar que el contrato esté activo
        if (contrato.getEstado() != EstadoContrato.ACTIVO) {
            addError("Solo se pueden generar programaciones para contratos activos");
            return;
        }

        // Generar programaciones basadas en frecuencia
        List<ProgramacionServicios> programaciones = generarProgramaciones(contrato);

        // Guardar todas las programaciones
        for (ProgramacionServicios ps : programaciones) {
            em.persist(ps);
        }

        addMessage(programaciones.size() + " servicios programados generados exitosamente");
    }

    private List<ProgramacionServicios> generarProgramaciones(Contrato contrato) {
        List<ProgramacionServicios> programaciones = new ArrayList<>();

        LocalDate fechaActual = contrato.getFechaInicio();
        LocalDate fechaFin = contrato.getFechaFin();

        while (!fechaActual.isAfter(fechaFin)) {
            if (esDiaValido(fechaActual, contrato.getDiaServicio())) {
                ProgramacionServicios ps = new ProgramacionServicios();
                ps.setContrato(contrato);
                ps.setEmpleado(obtenerEmpleadoDisponible()); // Deberías implementar esta lógica
                ps.setFechaServicio(fechaActual);
                ps.setHoraInicio(contrato.getHorarioPreferido() != null ?
                        contrato.getHorarioPreferido() : LocalTime.of(9, 0));
                ps.setEstado(EstadoProgramacionServicio.PROGRAMADO);
                ps.setDescripcion("Servicio programado para contrato " + contrato.getNumeroContrato());

                programaciones.add(ps);
            }

            // Avanzar según frecuencia
            fechaActual = siguienteFecha(fechaActual, contrato.getFrecuenciaServicio());
        }

        return programaciones;
    }

    private boolean esDiaValido(LocalDate fecha, DiaServicio diaServicio) {
        if (diaServicio == null) return true;

        DayOfWeek dayOfWeek = fecha.getDayOfWeek();
        return dayOfWeek.toString().equals(diaServicio.name());
    }

    private LocalDate siguienteFecha(LocalDate fecha, FrecuenciaContrato frecuencia) {
        if (frecuencia == null) return fecha.plusMonths(1);

        switch (frecuencia) {
            case DIARIA: return fecha.plusDays(1);
            case SEMANAL: return fecha.plusWeeks(1);
            case QUINCENAL: return fecha.plusWeeks(2);
            case MENSUAL: return fecha.plusMonths(1);
            default: return fecha.plusMonths(1);
        }
    }

    private Empleado obtenerEmpleadoDisponible() {
        // Implementar lógica para asignar empleado disponible
        // Por ahora, retorna null (deberás implementarlo)
        return null;
    }
}