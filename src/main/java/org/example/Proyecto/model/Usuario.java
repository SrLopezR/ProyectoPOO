package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
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

    @Transient
    @Stereotype("PASSWORD")
    @Required
    private String confirmarPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @DefaultValue("ACTIVO")
    @Hidden
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

    @AssertTrue(message = "Las contraseñas no coinciden")
    public boolean isPasswordMatch() {
        // Si ambos campos son nulos, no hay contraseña para validar
        if (password == null && confirmarPassword == null) {
            return true; // Permitir que la validación @Required maneje esto
        }

        // Si uno es nulo y el otro no, no coinciden
        if (password == null || confirmarPassword == null) {
            return false;
        }

        // Comparar las contraseñas
        return password.equals(confirmarPassword);
    }

    @AssertTrue(message = "La contraseña debe contener al menos una letra mayúscula, una minúscula y un número")
    public boolean isPasswordStrong() {
        if (password == null || password.isEmpty()) {
            return true; // Dejar que @Required maneje el campo vacío
        }

        // Verificar longitud mínima
        if (password.length() < 8) {
            return false;
        }

        // Verificar que tenga al menos una mayúscula
        boolean hasUpper = false;
        // Verificar que tenga al menos una minúscula
        boolean hasLower = false;
        // Verificar que tenga al menos un número
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isDigit(c)) hasDigit = true;
        }

        return hasUpper && hasLower && hasDigit;
    }

}