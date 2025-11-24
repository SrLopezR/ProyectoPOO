package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
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
    @JoinColumn(name = "idcliente")
    @DescriptionsList(descriptionProperties = "nombre, apellido, identificacionTributaria")
    @ReadOnly
    private Cliente cliente;

    @Column(length = 50, nullable = false, unique = true)
    private String numeroContrato;

    private TipoContratoServicio tipoContrato;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    private LocalDate fechaFirma;

    private FrecuenciaContrato frecuenciaServicio;

    private DiaServicio diaServicio;

    private LocalTime horarioPreferido;

    @Stereotype("MEMORY")
    private String terminosEspeciales;

    private Integer limiteEmergencias = 0;

    @Column(precision = 5, scale = 2)
    @Stereotype("PORCENTAJE")
    private BigDecimal descuento = BigDecimal.ZERO;

    private EstadoContrato estado;

    @Stereotype("MEMORY")
    private String motivoCancelacion;
}