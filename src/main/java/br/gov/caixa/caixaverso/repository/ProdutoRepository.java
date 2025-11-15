package br.gov.caixa.caixaverso.repository;

import java.util.List;

import br.gov.caixa.caixaverso.repository.model.ProdutoModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped 
public class ProdutoRepository implements PanacheRepository<ProdutoModel> {
    public ProdutoModel findByTipo(String tipo) {
        var query = getEntityManager().createNamedQuery("findByTipo", ProdutoModel.class);
        query.setParameter("tipo", tipo);
        return query.getSingleResultOrNull();
    }

    public List<ProdutoModel> findAllByRisco(String risco) {
        var query = getEntityManager().createNamedQuery("findAllByRisco", ProdutoModel.class);
        query.setParameter("risco", risco);
        return query.getResultList();
    }
}
