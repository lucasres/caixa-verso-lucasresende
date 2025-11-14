package br.gov.caixa.caixaverso.repository.dto;

public record TelemetriaAgrupadasPorPath(
    Long quantidadeChamadas,
    String nome,
    Double mediaTempoRespostaMs
) {
    
}
