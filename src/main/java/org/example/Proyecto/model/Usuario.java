package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.example.Proyecto.actions.ConectarUsuarioLoginAction;
import org.example.Proyecto.model.enums.EstadoUsuario;
import org.openxava.annotations.*;
import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.ws.rs.DefaultValue;

@Entity
@Getter
@Setter
@Table(name = "usuarios")
@Tab(
        properties = "username, email, estado, fechaCreacion"
)
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
    @OnChange(ConectarUsuarioLoginAction.class)
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;

    @Column(length = 100, nullable = false)
    @Required
    private String email;

    @Column(name = "fecha_creacion", nullable = false)
    @ReadOnly
    @DefaultValueCalculator(org.openxava.calculators.CurrentLocalDateCalculator.class)
    private java.time.LocalDate fechaCreacion;

    @Column(name = "ultimo_login")
    @Hidden
    private java.time.LocalDateTime ultimoLogin;

    @Column(name = "ultima_ip", length = 45)
    @Hidden
    private String ultimaIp;

    @OneToOne(mappedBy = "usuario")
    private Cliente cliente;

    @OneToOne(mappedBy = "usuario")
    private Empleado empleado;

    @AssertTrue(message = "Las contrasenas no coinciden")
    public boolean isPasswordMatch() {
        if (password == null && confirmarPassword == null) return true;
        if (password == null || confirmarPassword == null) return false;
        return password.equals(confirmarPassword);
    }

    @AssertTrue(message = "La contrasena debe tener al menos 8 caracteres")
    public boolean isPasswordStrong() {
        if (password == null || password.isEmpty()) return true;
        return password.length() >= 8;
    }
}