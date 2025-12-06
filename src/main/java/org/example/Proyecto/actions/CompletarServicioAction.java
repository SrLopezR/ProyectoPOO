package org.example.Proyecto.actions;

import org.openxava.actions.*;
import org.openxava.jpa.XPersistence;
import org.example.Proyecto.model.*;
import org.example.Proyecto.model.enums.*;
import javax.persistence.EntityManager;
import java.time.LocalTime;
import java.time.LocalDate;

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

        // Validar estado actual
        if (servicio.getEstado() == EstadoProgramacionServicio.COMPLETADO) {
            addError("El servicio ya está completado");
            return;
        }

        if (servicio.getEstado() == EstadoProgramacionServicio.CANCELADO) {
            addError("No se puede completar un servicio cancelado");
            return;
        }

        // Actualizar servicio
        servicio.setEstado(EstadoProgramacionServicio.COMPLETADO);
        servicio.setHoraFin(LocalTime.now());
        servicio.setObservaciones(servicio.getObservaciones() +
                "\nCompletado el: " + LocalDate.now() + " a las " + LocalTime.now());

        // Actualizar inventario si se usaron productos
        actualizarInventario(servicio);

        // Si es recurrente, programar el próximo
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
                // Crear registro de inventario
                Inventario movimiento = new Inventario();
                movimiento.setProducto(uso.getProducto());
                movimiento.setCantidad(-uso.getCantidad()); // Salida negativa
                movimiento.setFechaActualizacion(LocalDate.now());
                movimiento.setTipoMovimiento(TipoMovimientoInventario.SALIDA_SERVICIO);
                movimiento.setObservaciones("Uso en servicio ID: " + servicio.getId());

                em.persist(movimiento);

                // Actualizar stock del producto
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

            // Calcular próxima fecha según periodicidad
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