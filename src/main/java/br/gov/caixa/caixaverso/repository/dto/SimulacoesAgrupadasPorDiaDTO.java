package br.gov.caixa.caixaverso.repository.dto;

import java.time.LocalDate;

public record SimulacoesAgrupadasPorDiaDTO(
    String de_produto,
    Long quantidadeSimulacoes,
    LocalDate data,
    Double mediaValorFinal
) {
    
}
