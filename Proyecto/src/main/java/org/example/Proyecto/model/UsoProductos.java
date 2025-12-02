package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "uso_productos")
public class UsoProductos extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_servicio")
    @DescriptionsList(descriptionProperties = "descripcion")
    @Required
    private ProgramacionServicios servicio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_producto")
    @DescriptionsList(descriptionProperties = "nombre")
    @Required
    private Producto producto;

    @Column(nullable = false)
    @Required
    private Integer cantidad;

    @Column(name = "fecha_uso", nullable = false)
    @Required
    private java.time.LocalDate fechaUso;

    @Stereotype("MEMORY")
    private String observaciones;
}