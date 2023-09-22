package com.teoriadegrafos.uniritter.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    private LocalDate dataDeInicioDaAtividade;
    private int duracaoEstimada;
    private LocalDate dataDeFimDaAtividade; // Add this field for end date

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> dependencias;
}
