package br.gov.caixa.caixaverso.repository.dto;

import java.time.LocalDate;

public record SimulacoesAgrupadasPorDiaDTO(
    String nome,
    Long quantidadeSimulacoes,
    LocalDate data,
    Double mediaValorFinal
) {
    
}
