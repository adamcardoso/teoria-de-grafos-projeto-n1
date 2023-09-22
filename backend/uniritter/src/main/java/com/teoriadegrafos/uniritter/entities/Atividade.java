package com.teoriadegrafos.uniritter.entities;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Atividade {
    private Integer id;
    private String nome;
    private Integer diasDuracaoAtividade;
    private StatusEnum status;
    private List<Atividade> atividadesDependentes = new ArrayList<>();
    private List<Atividade> atividadesLiberadas = new ArrayList<>();
}
