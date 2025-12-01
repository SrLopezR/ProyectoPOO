// ProgramacionServicios.java
package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "programacion_servicios")
public class ProgramacionServicios extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_contrato")
    @DescriptionsList(descriptionProperties = "numeroContrato")
    @Required
    private Contrato contrato;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_empleado")
    @DescriptionsList(descriptionProperties = "nombre, apellido")
    @Required
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ubicacion")
    @DescriptionsList(descriptionProperties = "alias")
    @Required
    private Ubicacion ubicacion;

    @Column(name = "fecha_servicio", nullable = false)
    @Required
    private LocalDate fechaServicio;

    @Column(name = "hora_inicio", nullable = false)
    @Required
    private LocalTime horaInicio;

    @Column(name = "hora_fin")
    private LocalTime horaFin;

    @Column(length = 255)
    @Stereotype("MEMORY")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Required
    private EstadoProgramacionServicio estado;

    @Stereotype("MEMORY")
    private String observaciones;

    @Column(name = "es_recurrente")
    private Boolean esRecurrente = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "periodicidad")
    private Periodicidad periodicidad;

    @Column(name = "fecha_fin_recurrencia")
    private LocalDate fechaFinRecurrencia;

    @OneToMany(mappedBy = "servicio")
    @ListProperties("producto.nombre, cantidad, fechaUso")
    private java.util.List<UsoProductos> productosUtilizados;
}