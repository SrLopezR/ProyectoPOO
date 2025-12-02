package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "productos")
public class Producto extends BaseEntity {

    @Column(length = 100, nullable = false, unique = true)
    @Required
    private String nombre;

    @Column(length = 255)
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
    private Integer stockMinimo = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Required
    private EstadoProducto estado;
}