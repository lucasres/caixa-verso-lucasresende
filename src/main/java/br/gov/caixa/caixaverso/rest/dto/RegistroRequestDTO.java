package br.gov.caixa.caixaverso.rest.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record RegistroRequestDTO(
    @NotEmpty(message = "O cpf não pode ser vazio")
    String cpf,
    @NotEmpty(message = "A senha não pode ser vazia")
    String password,
    @NotEmpty(message = "O nome não pode ser vazio")
    String nome,
    @Pattern(regexp = "^(User|Admin)$", message = "O perfil deve ser 'User' ou 'Admin'")
    String perfil
) {
    // 
}
