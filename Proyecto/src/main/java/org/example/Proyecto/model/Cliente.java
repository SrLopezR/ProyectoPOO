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
@Table(name = "cliente")
public class Cliente extends BaseEntity {

    @Column(length=100, nullable=false)
    private String nombre;

    @Column(length=100, nullable=false)
    private String apellido;

    private TipoCliente tipoCliente;

    @Column(length=15, nullable=false, unique=true)
    private String telefonoPrincipal;

    @Column(length=15, nullable = false, unique =true)
    private String telefonoSecundario;

    @Column(length=100, nullable=false, unique=true)
    private String correo;

    @DefaultValueCalculator(CurrentLocalDateCalculator.class)
    @Hidden
    private LocalDate fechaRegistro;

    @DefaultValueCalculator(CurrentLocalDateCalculator.class)
    private LocalDate fechaNacimiento;

    @Column(length=20, unique=true)
    private String identificacionTribitaria;

    private EstadoCliente estado;
}
