package br.gov.caixa.caixaverso.repository;

import java.time.LocalDate;
import java.util.List;

import br.gov.caixa.caixaverso.contracts.TelemetriaPersistance;
import br.gov.caixa.caixaverso.repository.dto.TelemetriaAgrupadasPorPath;
import br.gov.caixa.caixaverso.repository.model.TelemetriaModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TelemetriaRepository implements PanacheRepository<TelemetriaModel>, TelemetriaPersistance {
    @Transactional
    public TelemetriaModel inserir(TelemetriaModel model) {
        getEntityManager().persist(model);
        return model;
    }

    public List<TelemetriaAgrupadasPorPath> agruparPorPath(LocalDate inicio, LocalDate fim) {
        Query query = getEntityManager().createNamedQuery("agruparPorPath", TelemetriaAgrupadasPorPath.class);
        query.setParameter("inicio", inicio);
        query.setParameter("fim", fim);
        return query.getResultList();
    }
}
