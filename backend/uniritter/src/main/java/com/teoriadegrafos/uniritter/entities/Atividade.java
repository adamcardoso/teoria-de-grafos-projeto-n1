package com.teoriadegrafos.uniritter.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String identificador;
    private int duracaoEstimada;

    @ElementCollection
    private List<Long> dependencias;
}
