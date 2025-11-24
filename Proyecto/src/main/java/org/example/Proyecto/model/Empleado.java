package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "empleados")
public class Empleado extends BaseEntity {
    @Column(length = 100, nullable = false)
    @Required
    private String nombre;

    @Column(length = 100, nullable = false)
    @Required
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Required
    private TipoIdentificacion tipoIdentificacion;

    @Column(length = 20, nullable = false, unique = true)
    @Required
    private String numeroIdentificacion;

    @Enumerated(EnumType.STRING)
    @Required
    private CargoEmpleado cargo;

    @Enumerated(EnumType.STRING)
    @Required
    private EspecialidadEmpleado especialidad;

    @Column(length = 15, nullable = false)
    @Required
    private String telefonoContacto;

    @Column(length = 100, unique = true)
    private String correoCorporativo;

    @Column(nullable = false)
    @Required
    private LocalDate fechaContratacion;

    private LocalDate fechaNacimiento;

    @Column(precision = 10, scale = 2, nullable = false)
    @Stereotype("DINERO")
    @Required
    private BigDecimal salarioBase;

    @Enumerated(EnumType.STRING)
    @Required
    private TipoContrato tipoContrato;

    @Enumerated(EnumType.STRING)
    @Required
    private EstadoEmpleado estado;
}