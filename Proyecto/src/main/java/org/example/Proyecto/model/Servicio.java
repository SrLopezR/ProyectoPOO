package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "servicios")
public class Servicio extends BaseEntity {

    @Column(length = 20, nullable = false, unique = true)
    private String codigoServicio;

    @Column(length = 100, nullable = false)
    private String nombre;

    private CategoriaServicio categoria;

    @Column(nullable = false)
    @Stereotype("MEMORY")
    private String descripcion;

    @Stereotype("MEMORY")
    private String procedimiento;

    @Column(nullable = false)
    private Integer duracionEstimada;

    private FrecuenciaServicio frecuenciaRecomendada;

    @Column(precision = 10, scale = 2, nullable = false)
    @Stereotype("DINERO")
    private BigDecimal precioBase;

    @Column(precision = 10, scale = 2, nullable = false)
    @Stereotype("DINERO")
    private BigDecimal costoEstimado;

    private Boolean requiereProductos = true;

    @Stereotype("MEMORY")
    private String productosSugeridos;

    private EstadoServicio estado;
}