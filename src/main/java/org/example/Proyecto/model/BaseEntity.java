package org.example.Proyecto.model;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.Hidden;
import javax.persistence.*;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;
}