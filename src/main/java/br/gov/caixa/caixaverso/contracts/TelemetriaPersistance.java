package br.gov.caixa.caixaverso.contracts;

import java.time.LocalDate;
import java.util.List;

import br.gov.caixa.caixaverso.repository.dto.TelemetriaAgrupadasPorPath;
import br.gov.caixa.caixaverso.repository.model.TelemetriaModel;

public interface TelemetriaPersistance {
    public TelemetriaModel inserir(TelemetriaModel model);

    public List<TelemetriaAgrupadasPorPath> agruparPorPath(LocalDate inicio, LocalDate fim);
}
