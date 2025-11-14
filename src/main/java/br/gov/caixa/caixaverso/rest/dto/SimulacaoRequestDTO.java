package br.gov.caixa.caixaverso.rest.dto;

import org.hibernate.validator.constraints.Range;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SimulacaoRequestDTO(
    @NotNull
    Long clienteId,
    @NotNull
    @Range(min = 1, message = "O valor deve ser maior que 1")
    Double valor,
    @Range(min = 1, message = "O prazoMeses deve ser maior que 1")
    Integer prazoMeses,
    @NotBlank(message = "O tipoProduto deve ser um dos seguintes valores: CDB, Debenture ou Ações")
    String tipoProduto
) {
    // 
}
