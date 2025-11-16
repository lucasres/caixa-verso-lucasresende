package br.gov.caixa.caixaverso.services.dto;

import java.util.List;

import br.gov.caixa.caixaverso.repository.model.ProdutoModel;

public record RecomendacaoDTO(
    String perfilCliente,
    List<String> riscosAceitos,
    List<ProdutoModel> produtos
) {
    // 
}
