// Pago.java
package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "pagos")
public class Pago extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_factura")
    @DescriptionsList(descriptionProperties = "numeroFactura")
    @Required
    private Factura factura;

    @Column(name = "fecha_pago", nullable = false)
    @Required
    private LocalDate fechaPago;

    @Column(precision = 10, scale = 2, nullable = false)
    @Stereotype("DINERO")
    @Required
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    @Required
    private MetodoPago metodoPago;

    @Column(length = 50)
    private String referencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Required
    private EstadoPago estado;

    @Stereotype("MEMORY")
    private String observaciones;
}