package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.example.Proyecto.model.enums.*;
import org.openxava.annotations.*;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.ws.rs.DefaultValue;
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

    @Column(name = "numero_identificacion", length = 20, nullable = false, unique = true)
    @Required
    @Pattern(regexp = "^\\d{3}-\\d{6}-\\d{4}[A-Z]$",
            message = "Formato de cédula nicaragüense inválido. Use: 000-000000-0000A")
    private String numeroIdentificacion;

    @Enumerated(EnumType.STRING)
    @Required
    private CargoEmpleado cargo;

    @Enumerated(EnumType.STRING)
    @Required
    private EspecialidadEmpleado especialidad;

    @Column(name = "telefono_contacto", length = 15, nullable = false)
    @Required
    private String telefonoContacto;

    @Column(name = "correo_corporativo", length = 100, unique = true)
    @Pattern(regexp = ".+@PoolNic\\.com$",
            message = "El correo corporativo debe terminar en @PoolNic.com")
    private String correoCorporativo;

    @Column(name = "fecha_contratacion", nullable = false)
    @Required
    private LocalDate fechaContratacion;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "salario_base", precision = 10, scale = 2, nullable = false)
    @Stereotype("DINERO")
    @Required
    private BigDecimal salarioBase;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contrato", nullable = false)
    @Required
    private TipoContrato tipoContrato;

    @Enumerated(EnumType.STRING)
    @Hidden
    @DefaultValue("ACTIVO")
    private EstadoEmpleado estado = EstadoEmpleado.ACTIVO;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", unique = true)
    @DescriptionsList(descriptionProperties = "username")
    private Usuario usuario;
}