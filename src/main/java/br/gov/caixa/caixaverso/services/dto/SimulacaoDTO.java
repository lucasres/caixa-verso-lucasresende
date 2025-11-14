package br.gov.caixa.caixaverso.services.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.caixa.caixaverso.repository.model.ProdutoModel;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SimulacaoDTO(
    ProdutoModel produtoValidado,
    String dataSimulacao,
    @JsonIgnore
    List<Double> progressao,
    @JsonIgnore
    Integer prazo
) {

    @JsonProperty("resultadoSimulacao")
    public Map<String, Object> resultadoSimulacao() {
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("valorFinal", progressao.getLast());
        resultado.put("rentabilidadeEfetiva", produtoValidado.getNu_rentabilidade());
        resultado.put("prazoMeses", prazo);
        resultado.put("progressao", progressao);

        return resultado;
    }
}
