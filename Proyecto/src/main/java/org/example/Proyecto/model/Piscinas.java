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
    @JoinColumn(name = "iddireccion")
    @DescriptionsList(descriptionProperties = "alias, direccion, ciudad")
    @ReadOnly
    private Direcciones direccion;

    @Column(length = 100)
    private String nombrePersonalizado;

    private TipoPiscinas tipoPiscina;

    private MaterialConstruccion materialConstruccion;

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

    private TipoFiltro tipoFiltro;

    private LocalDate fechaInstalacion;

    private LocalDate ultimoMantenimiento;

    private EstadoPiscina estado;
}