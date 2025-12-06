package org.example.Proyecto.actions;

import org.example.Proyecto.model.enums.EstadoContrato;
import org.openxava.actions.*;
import org.openxava.jpa.XPersistence;
import org.example.Proyecto.model.*;
import javax.persistence.EntityManager;
import java.time.LocalDate;

public class RenovarContratoAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        EntityManager em = XPersistence.getManager();

        Long contratoId = Long.parseLong(getView().getValueString("id"));
        Contrato contratoOriginal = em.find(Contrato.class, contratoId);

        if (contratoOriginal == null) {
            addError("Contrato no encontrado");
            return;
        }

        // Validar que el contrato esté próximo a vencer
        if (contratoOriginal.getFechaFin().isAfter(LocalDate.now().plusMonths(1))) {
            addError("El contrato solo se puede renovar cuando falte menos de 1 mes para su vencimiento");
            return;
        }

        // Crear nuevo contrato como copia
        Contrato nuevoContrato = new Contrato();
        nuevoContrato.setCliente(contratoOriginal.getCliente());
        nuevoContrato.setNumeroContrato(generarNuevoNumero(contratoOriginal.getNumeroContrato()));
        nuevoContrato.setTipoContrato(contratoOriginal.getTipoContrato());
        nuevoContrato.setFechaInicio(contratoOriginal.getFechaFin().plusDays(1));
        nuevoContrato.setFechaFin(contratoOriginal.getFechaFin().plusYears(1)); // Renovar por 1 año
        nuevoContrato.setFrecuenciaServicio(contratoOriginal.getFrecuenciaServicio());
        nuevoContrato.setDiaServicio(contratoOriginal.getDiaServicio());
        nuevoContrato.setHorarioPreferido(contratoOriginal.getHorarioPreferido());
        nuevoContrato.setTerminosEspeciales(contratoOriginal.getTerminosEspeciales());
        nuevoContrato.setLimiteEmergencias(contratoOriginal.getLimiteEmergencias());
        nuevoContrato.setDescuento(contratoOriginal.getDescuento());
        nuevoContrato.setEstado(EstadoContrato.PENDIENTE);

        em.persist(nuevoContrato);

        // Marcar contrato original como RENOVADO
        contratoOriginal.setEstado(EstadoContrato.RENOVADO);
        contratoOriginal.setTerminosEspeciales(contratoOriginal.getTerminosEspeciales() +
                "\nRenovado el: " + LocalDate.now() + " con nuevo contrato: " + nuevoContrato.getNumeroContrato());

        addMessage("Contrato renovado exitosamente. Nuevo número: " + nuevoContrato.getNumeroContrato());
        getView().refresh();
    }

    private String generarNuevoNumero(String numeroAnterior) {
        // Extraer el número base y agregar -REN para renovación
        if (numeroAnterior.contains("-REN")) {
            int lastIndex = numeroAnterior.lastIndexOf("-REN");
            String base = numeroAnterior.substring(0, lastIndex);
            String suffix = numeroAnterior.substring(lastIndex + 4);
            int num = suffix.isEmpty() ? 1 : Integer.parseInt(suffix) + 1;
            return base + "-REN" + num;
        }
        return numeroAnterior + "-REN1";
    }
}