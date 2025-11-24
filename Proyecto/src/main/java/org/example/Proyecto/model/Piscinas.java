package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "piscinas")
public class Piscinas extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idcliente")
    @DescriptionsList(descriptionProperties = "nombre, apellido")
    @Required
    private Cliente cliente;

    @Column(length = 100)
    private String nombrePersonalizado;

    @Enumerated(EnumType.STRING)
    @Required
    private TipoPiscinas tipoPiscina;

    @Enumerated(EnumType.STRING)
    @Required
    private MaterialConstruccion materialConstruccion;

    @Enumerated(EnumType.STRING)
    @Required
    private FormaPiscina forma;

    @Column(precision = 6, scale = 2)
    private Double largo;

    @Column(precision = 6, scale = 2)
    private Double ancho;

    @Column(precision = 4, scale = 2)
    private Double profundidadMin;

    @Column(precision = 4, scale = 2)
    private Double profundidadMax;

    private Integer capacidadGalones;

    @Enumerated(EnumType.STRING)
    private TipoFiltro tipoFiltro;

    private LocalDate fechaInstalacion;

    private LocalDate ultimoMantenimiento;

    @Enumerated(EnumType.STRING)
    @Required
    private EstadoPiscina estado;
}