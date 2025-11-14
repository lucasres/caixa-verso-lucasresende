package br.gov.caixa.caixaverso.rest.dto;

import java.util.List;
import java.util.Map;

import br.gov.caixa.caixaverso.repository.dto.TelemetriaAgrupadasPorPath;

public record TelemetriaResponseDTO(
    List<TelemetriaAgrupadasPorPath> servicos,
    Map<String, String> periodo
) {

}
