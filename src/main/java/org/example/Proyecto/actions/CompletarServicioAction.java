package org.example.Proyecto.actions;

import org.openxava.actions.*;
import org.openxava.jpa.XPersistence;
import org.example.Proyecto.model.*;
import org.example.Proyecto.model.enums.*;
import javax.persistence.EntityManager;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CompletarServicioAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        EntityManager em = XPersistence.getManager();

        Long servicioId = Long.parseLong(getView().getValueString("id"));
        ProgramacionServicios servicio = em.find(ProgramacionServicios.class, servicioId);

        if (servicio == null) {
            addError("Servicio no encontrado");
            return;
        }

        if (servicio.getEstado() == EstadoProgramacionServicio.COMPLETADO) {
            addError("El servicio ya está completado");
            return;
        }

        if (servicio.getEstado() == EstadoProgramacionServicio.CANCELADO) {
            addError("No se puede completar un servicio cancelado");
            return;
        }

        LocalDate hoy = LocalDate.now();
        DateTimeFormatter fc = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = hoy.format(fc);
        servicio.setEstado(EstadoProgramacionServicio.COMPLETADO);
        servicio.setHoraFin(LocalTime.now());
        servicio.setObservaciones("\nCompletado el: " + fechaFormateada + " a las " + LocalTime.now().truncatedTo(ChronoUnit.MINUTES));

        actualizarInventario(servicio);

        if (Boolean.TRUE.equals(servicio.getEsRecurrente())) {
            programarProximoRecurrente(servicio);
        }

        addMessage("Servicio marcado como completado exitosamente");
        getView().refresh();
    }

    private void actualizarInventario(ProgramacionServicios servicio) {
        if (servicio.getProductosUtilizados() != null && !servicio.getProductosUtilizados().isEmpty()) {
            EntityManager em = XPersistence.getManager();

            for (UsoProductos uso : servicio.getProductosUtilizados()) {
                Inventario movimiento = new Inventario();
                movimiento.setProducto(uso.getProducto());
                movimiento.setCantidad(-uso.getCantidad());
                movimiento.setFechaActualizacion(LocalDate.now());
                movimiento.setTipoMovimiento(TipoMovimientoInventario.SALIDA_SERVICIO);
                movimiento.setObservaciones("Uso en servicio ID: " + servicio.getId());

                em.persist(movimiento);

                Producto producto = uso.getProducto();
                producto.setStock(producto.getStock() - uso.getCantidad());
            }
        }
    }

    private void programarProximoRecurrente(ProgramacionServicios servicio) {
        if (servicio.getPeriodicidad() != null &&
                (servicio.getFechaFinRecurrencia() == null ||
                        LocalDate.now().isBefore(servicio.getFechaFinRecurrencia()))) {

            ProgramacionServicios proximo = new ProgramacionServicios();
            proximo.setContrato(servicio.getContrato());
            proximo.setEmpleado(servicio.getEmpleado());
            proximo.setUbicacion(servicio.getUbicacion());

            LocalDate proximaFecha = calcularProximaFecha(
                    servicio.getFechaServicio(),
                    servicio.getPeriodicidad()
            );

            proximo.setFechaServicio(proximaFecha);
            proximo.setHoraInicio(servicio.getHoraInicio());
            proximo.setEstado(EstadoProgramacionServicio.PROGRAMADO);
            proximo.setDescripcion(servicio.getDescripcion());
            proximo.setEsRecurrente(true);
            proximo.setPeriodicidad(servicio.getPeriodicidad());
            proximo.setFechaFinRecurrencia(servicio.getFechaFinRecurrencia());

            XPersistence.getManager().persist(proximo);
        }
    }

    private LocalDate calcularProximaFecha(LocalDate fecha, Periodicidad periodicidad) {
        switch (periodicidad) {
            case DIARIA: return fecha.plusDays(1);
            case SEMANAL: return fecha.plusWeeks(1);
            case QUINCENAL: return fecha.plusWeeks(2);
            case MENSUAL: return fecha.plusMonths(1);
            case BIMESTRAL: return fecha.plusMonths(2);
            case TRIMESTRAL: return fecha.plusMonths(3);
            case SEMESTRAL: return fecha.plusMonths(6);
            case ANUAL: return fecha.plusYears(1);
            default: return fecha.plusDays(1);
        }
    }
}