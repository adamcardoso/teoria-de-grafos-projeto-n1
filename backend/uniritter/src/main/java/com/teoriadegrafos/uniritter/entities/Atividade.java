package com.teoriadegrafos.uniritter.entities;

import com.teoriadegrafos.uniritter.entities.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private Integer diasDuracaoAtividade;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    private Integer idProjeto;
}
