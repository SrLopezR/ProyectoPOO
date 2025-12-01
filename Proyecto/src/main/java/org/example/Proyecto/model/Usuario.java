// Usuario.java
package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import javax.persistence.*;
import javax.ws.rs.DefaultValue;

@Entity
@Getter
@Setter
@Table(name = "usuarios")
public class Usuario extends BaseEntity {

    @Column(length = 50, nullable = false, unique = true)
    @Required
    private String username;

    @Column(length = 100, nullable = false)
    @Stereotype("PASSWORD")
    @Required
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @DefaultValue("ACTIVO")
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;

    @Column(length = 100, nullable = false)
    @Required
    private String email;

    @Column(name = "fecha_creacion", nullable = false)
    @ReadOnly
    @DefaultValueCalculator(org.openxava.calculators.CurrentLocalDateCalculator.class)
    private java.time.LocalDate fechaCreacion;

    @Column(name = "ultimo_login")
    private java.time.LocalDateTime ultimoLogin;

    // Relación con Cliente
    @OneToOne(mappedBy = "usuario")
    private Cliente cliente;

    // Relación con Empleado
    @OneToOne(mappedBy = "usuario")
    private Empleado empleado;
}