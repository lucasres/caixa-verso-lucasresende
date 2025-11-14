package br.gov.caixa.caixaverso.rest.dto;

public record RegistroRequestDTO(
    String cpf,
    String password,
    String nome
) {
    // 
}
