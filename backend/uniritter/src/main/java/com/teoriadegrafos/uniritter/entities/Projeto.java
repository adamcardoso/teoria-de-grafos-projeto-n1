package com.teoriadegrafos.uniritter.entities;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Projeto {
    private String nome;
    private LocalDate dataInicioProjeto;
    private LocalDate dataFimProjeto;
    private List<Atividade> atividades = new ArrayList<>();
}
