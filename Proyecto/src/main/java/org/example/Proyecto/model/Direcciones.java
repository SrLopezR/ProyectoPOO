package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "direcciones")
public class Direcciones extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idcliente")
    @DescriptionsList(descriptionProperties = "nombre, apellido")
    @ReadOnly
    private Cliente cliente;

    @Column(length = 50, nullable = false)
    private String alias;

    @Column(nullable = false)
    @Stereotype("MEMORY")
    private String direccion;

    @Column(length = 100, nullable = false)
    private String ciudad;

    @Column(length = 100, nullable = false)
    private String departamento;

    @Column(name = "codigoPostal", length = 10)
    private String codigoPostal;

    @Stereotype("MEMORY")
    private String referencia;

    @Column(precision = 10, scale = 8)
    private Double latitud;

    @Column(precision = 11, scale = 8)
    private Double longitud;

    @Column(name = "esPrincipal")
    private Boolean esPrincipal = false;

    @Column(name = "instruccionesAcceso")
    @Stereotype("MEMORY")
    private String instruccionesAcceso;
}