package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "direcciones")
public class Direcciones extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente")
    @DescriptionsList(descriptionProperties = "nombre, apellido")
    @Required
    @NoSearch
    private Cliente cliente;

    @Column(length = 50, nullable = false)
    @Required
    private String alias;

    @Column(nullable = false)
    @Stereotype("MEMORY")
    @Required
    private String direccion;

    @Column(length = 100, nullable = false)
    @Required
    private String ciudad;

    @Column(length = 100, nullable = false)
    @Required
    private String departamento;

    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    @Stereotype("MEMORY")
    private String referencia;

    @Column(precision = 10, scale = 8)
    private Double latitud;

    @Column(precision = 11, scale = 8)
    private Double longitud;

    @Column(name = "es_principal")
    private Boolean esPrincipal = false;

    @Column(name = "instrucciones_acceso")
    @Stereotype("MEMORY")
    private String instruccionesAcceso;

    @OneToMany(mappedBy = "direccion")
    @ReadOnly
    @ListProperties("nombrePersonalizado, tipoPiscina, estado")
    private java.util.List<Piscinas> piscinas;
}