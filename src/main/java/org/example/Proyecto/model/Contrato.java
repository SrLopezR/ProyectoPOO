package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.example.Proyecto.model.enums.DiaServicio;
import org.example.Proyecto.model.enums.EstadoContrato;
import org.example.Proyecto.model.enums.FrecuenciaContrato;
import org.example.Proyecto.model.enums.TipoContratoServicio;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "contratos")
public class Contrato extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente")
    @DescriptionsList(descriptionProperties = "nombre, apellido")
    @Required
    private Cliente cliente;

    @Column(name = "numero_contrato", length = 50, nullable = false, unique = true)
    @Required
    private String numeroContrato;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contrato", nullable = false)
    @Required
    private TipoContratoServicio tipoContrato;

    @Column(name = "fecha_inicio", nullable = false)
    @Required
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    @Required
    private LocalDate fechaFin;

    @Column(name = "fecha_firma")
    private LocalDate fechaFirma;

    @Enumerated(EnumType.STRING)
    @Column(name = "frecuencia_servicio")
    private FrecuenciaContrato frecuenciaServicio;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_servicio")
    private DiaServicio diaServicio;

    @Column(name = "horario_preferido")
    private LocalTime horarioPreferido;

    @Column(name = "terminos_especiales")
    @Stereotype("MEMORY")
    private String terminosEspeciales;

    @Column(name = "limite_emergencias")
    private Integer limiteEmergencias = 0;

    @Column(precision = 5, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Required
    private EstadoContrato estado;

    @Column(name = "motivo_cancelacion")
    @Stereotype("MEMORY")
    private String motivoCancelacion;
}