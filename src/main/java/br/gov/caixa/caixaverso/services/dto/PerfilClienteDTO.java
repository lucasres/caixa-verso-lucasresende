package br.gov.caixa.caixaverso.services.dto;

public record PerfilClienteDTO(
    Long clienteId,
    String perfil,
    Integer pontuacao,
    String descricao
) {
    // 
}
