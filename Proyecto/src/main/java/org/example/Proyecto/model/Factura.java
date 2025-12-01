// Factura.java
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
@Table(name = "facturas")
public class Factura extends BaseEntity {

    @Column(length = 20, nullable = false, unique = true)
    @Required
    private String numeroFactura;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente")
    @DescriptionsList(descriptionProperties = "nombre, apellido")
    @Required
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_contrato")
    @DescriptionsList(descriptionProperties = "numeroContrato")
    @Required
    private Contrato contrato;

    @Column(name = "fecha_emision", nullable = false)
    @Required
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento", nullable = false)
    @Required
    private LocalDate fechaVencimiento;

    @Column(precision = 10, scale = 2, nullable = false)
    @Stereotype("DINERO")
    @Required
    private BigDecimal subtotal;

    @Column(name = "impuestos", precision = 10, scale = 2, nullable = false)
    @Stereotype("DINERO")
    @Required
    private BigDecimal impuestos;

    @Column(precision = 10, scale = 2, nullable = false)
    @Stereotype("DINERO")
    @Required
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Required
    private EstadoFactura estado;

    @OneToMany(mappedBy = "factura")
    @ListProperties("producto.nombre, cantidad, precioUnitario, total")
    private java.util.List<DetalleFactura> detalles;

    @OneToMany(mappedBy = "factura")
    @ListProperties("fechaPago, monto, metodoPago")
    private java.util.List<Pago> pagos;
}