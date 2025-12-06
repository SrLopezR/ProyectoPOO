package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.example.Proyecto.model.enums.CategoriaServicio;
import org.example.Proyecto.model.enums.EstadoServicio;
import org.example.Proyecto.model.enums.FrecuenciaServicio;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "servicios")
public class Servicio extends BaseEntity {

    @Column(name = "codigo_servicio", length = 20, nullable = false, unique = true)
    @Required
    private String codigoServicio;

    @Column(length = 100, nullable = false)
    @Required
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Required
    private CategoriaServicio categoria;

    @Column(nullable = false)
    @Stereotype("MEMORY")
    @Required
    private String descripcion;

    @Stereotype("MEMORY")
    private String procedimiento;

    @Column(name = "duracion_estimada", nullable = false)
    @Required
    private Integer duracionEstimada;

    @Enumerated(EnumType.STRING)
    @Column(name = "frecuencia_recomendada")
    private FrecuenciaServicio frecuenciaRecomendada;

    @Column(name = "precio_base", precision = 10, scale = 2, nullable = false)
    @Stereotype("DINERO")
    @Required
    private BigDecimal precioBase;

    @Column(name = "costo_estimado", precision = 10, scale = 2, nullable = false)
    @Stereotype("DINERO")
    @Required
    private BigDecimal costoEstimado;

    @Column(name = "requiere_productos")
    private Boolean requiereProductos = true;

    @Column(name = "productos_sugeridos")
    @Stereotype("MEMORY")
    private String productosSugeridos;

    @Enumerated(EnumType.STRING)
    @Required
    @Hidden
    private EstadoServicio estado;
}