package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.example.Proyecto.model.enums.*;
import org.openxava.annotations.*;

import javax.persistence.*;
import javax.ws.rs.DefaultValue;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "piscinas")
public class Piscinas extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ubicacion")
    @DescriptionsList(descriptionProperties = "alias, direccion, ciudad")
    @Required
    private Ubicacion ubicacion;

    @Column(name = "nombre_personalizado", length = 100)
    private String nombrePersonalizado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_piscina", nullable = false)
    @Required
    private TipoPiscinas tipoPiscina;

    @Enumerated(EnumType.STRING)
    @Column(name = "material_construccion", nullable = false)
    @Required
    private MaterialConstruccion materialConstruccion;

    @Enumerated(EnumType.STRING)
    @Required
    private FormaPiscina forma;

    @Column(precision = 6, scale = 2)
    private Double largo;

    @Column(precision = 6, scale = 2)
    private Double ancho;

    @Column(name = "profundidad_min", precision = 4, scale = 2)
    private Double profundidadMin;

    @Column(name = "profundidad_max", precision = 4, scale = 2)
    private Double profundidadMax;

    @Column(name = "capacidad_galones")
    private Integer capacidadGalones;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_filtro")
    private TipoFiltro tipoFiltro;

    @Column(name = "fecha_instalacion")
    private LocalDate fechaInstalacion;

    @Column(name = "ultimo_mantenimiento")
    private LocalDate ultimoMantenimiento;

    @Enumerated(EnumType.STRING)
    @DefaultValue("ACTIVO")
    @Hidden
    private EstadoPiscina estado = EstadoPiscina.ACTIVA;
}