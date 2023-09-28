package com.teoriadegrafos.uniritter.entities.enums;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum StatusEnum {
    ATIVA("A"),
    INATIVA("I"),
    DEPENDENTE("D"),
    CONCLUIDA("C");

    private final String valor;

    public String getValor() {
        return valor;
    }

    public static StatusEnum fromValor(final String valor) {
        return Arrays.stream(values())
                .filter(situacao -> situacao.getValor().equalsIgnoreCase(valor))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(valor));
    }
}
