package br.gov.caixa.caixaverso.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
    @NotBlank(message = "cpf não pode ser branco ou vazio.")
    @Pattern(regexp = "\\d{11}", message = "cpf não pode ser branco ou vazio.")
    String cpf,
    @NotBlank(message = "password não pode ser branco ou vazio.")
    @Size(min = 8, max = 8, message = "A senha deve ser de 8 dígitos")
    String password
){
    // 
}
