// Inventario.java
package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "inventario")
public class Inventario extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_producto")
    @DescriptionsList(descriptionProperties = "nombre")
    @Required
    private Producto producto;

    @Column(nullable = false)
    @Required
    private Integer cantidad;

    @Column(name = "fecha_actualizacion", nullable = false)
    @Required
    private java.time.LocalDate fechaActualizacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Required
    private TipoMovimientoInventario tipoMovimiento;

    @Stereotype("MEMORY")
    private String observaciones;
}