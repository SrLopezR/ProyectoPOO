package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.example.Proyecto.model.enums.EstadoCliente;
import org.example.Proyecto.model.enums.TipoCliente;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentLocalDateCalculator;

import javax.persistence.*;
import javax.ws.rs.DefaultValue;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "clientes")
@Views({
        @View(name = "simple", members = "nombre, apellido, telefonoPrincipal, correo"),
        @View(name = "completo", members = "nombre, apellido, tipoCliente, telefonoPrincipal, telefonoSecundario, correo, fechaRegistro, fechaNacimiento, identificacionTributaria, estado, usuario")
})
public class Cliente extends BaseEntity {

    @Column(length = 100, nullable = false)
    @Required
    private String nombre;

    @Column(length = 100, nullable = false)
    @Required
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente", nullable = false)
    @Required
    private TipoCliente tipoCliente;

    @Column(name = "telefono_principal", length = 15, nullable = false, unique = true)
    @Required
    private String telefonoPrincipal;

    @Column(name = "telefono_secundario", length = 15)
    private String telefonoSecundario;

    @Column(length = 100, nullable = false, unique = true)
    @Required
    private String correo;

    @DefaultValueCalculator(CurrentLocalDateCalculator.class)
    @ReadOnly
    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "identificacion_tributaria", length = 20, unique = true)
    private String identificacionTributaria;

    @Enumerated(EnumType.STRING)
    @DefaultValue ("ACTIVO")
    private EstadoCliente estado = EstadoCliente.ACTIVO;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", unique = true)
    @DescriptionsList(descriptionProperties = "username")
    @Required
    private Usuario usuario;

    @OneToMany(mappedBy = "cliente")
    @ReadOnly
    @ListProperties("alias, direccion, ciudad, esPrincipal")
    private java.util.List<Ubicacion> ubicaciones;

    @OneToMany(mappedBy = "cliente")
    @ReadOnly
    @ListProperties("numeroContrato, tipoContrato, estado, fechaInicio, fechaFin")
    private java.util.List<Contrato> contratos;

    //@OneToMany(mappedBy = "cliente")
    //@ReadOnly
    //@ListProperties("nombrePersonalizado, tipoPiscina, estado")
    //private java.util.List<Piscinas> piscinas;
}