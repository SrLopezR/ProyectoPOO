package org.example.Proyecto.model;

import java.math.*;
import java.time.*;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentLocalDateCalculator;
import org.openxava.model.*;

import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "clientes")
public class Cliente extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idcliente")
    @DescriptionsList(descriptionProperties = "nombre, apellido")
    @Required
    private Cliente cliente;

    @Column(length = 100, nullable = false)
    @Required
    private String nombre;

    @Column(length = 100, nullable = false)
    @Required
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Required
    private TipoCliente tipoCliente;

    @Column(length = 15, nullable = false, unique = true)
    @Required
    private String telefonoPrincipal;

    @Column(length = 15)
    private String telefonoSecundario;

    @Column(length = 100, nullable = false, unique = true)
    @Required
    private String correo;

    @DefaultValueCalculator(CurrentLocalDateCalculator.class)
    @ReadOnly
    private LocalDate fechaRegistro;

    private LocalDate fechaNacimiento;

    @Column(length = 20, unique = true)
    private String identificacionTributaria;

    @Enumerated(EnumType.STRING)
    @Required
    private EstadoCliente estado;
}