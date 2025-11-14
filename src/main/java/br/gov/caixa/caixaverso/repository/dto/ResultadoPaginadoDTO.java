package br.gov.caixa.caixaverso.repository.dto;

import java.util.List;

public record ResultadoPaginadoDTO<T>(
    Long total,
    List<T> dados
) {
    // 
}
