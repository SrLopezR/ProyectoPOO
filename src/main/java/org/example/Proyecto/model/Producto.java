package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.example.Proyecto.model.enums.CategoriaProducto;
import org.example.Proyecto.model.enums.EstadoProducto;
import org.openxava.annotations.*;
import javax.persistence.*;
import javax.ws.rs.DefaultValue;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "productos")
public class Producto extends BaseEntity {

    @Column(length = 100, nullable = false, unique = true)
    @Required
    private String nombre;

    @Column
    @Stereotype("MEMORY")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Required
    private CategoriaProducto categoria;

    @Column(precision = 10, scale = 2, nullable = false)
    @Stereotype("DINERO")
    @Required
    private BigDecimal precio;

    @Column(nullable = false)
    @Required
    private Integer stock;

    @Column(name = "stock_minimo", nullable = false)
    @Required
    private Integer stockMinimo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @DefaultValue("ACTIVO")
    @Hidden
    private EstadoProducto estado = EstadoProducto.ACTIVO;
}