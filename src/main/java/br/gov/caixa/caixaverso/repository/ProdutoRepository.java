package br.gov.caixa.caixaverso.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.gov.caixa.caixaverso.contracts.ProdutoPersistance;
import br.gov.caixa.caixaverso.repository.model.ProdutoModel;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped 
public class ProdutoRepository implements PanacheRepository<ProdutoModel>, ProdutoPersistance {
    @Override
    public ProdutoModel findByTipo(String tipo) {
        var query = getEntityManager().createNamedQuery("findByTipo", ProdutoModel.class);
        query.setParameter("tipo", tipo);
        return query.getSingleResultOrNull();
    }

    @Override
    public Set<ProdutoModel> findByRiscos(List<String> riscos) {
        var query = getEntityManager().createNamedQuery("findAllByRiscos", ProdutoModel.class);
        query.setParameter("riscos", riscos);
        var produtos = query.getResultList();
        return new HashSet<>(produtos);
    }

    @Override
    public PanacheQuery<ProdutoModel> findAll() {
        return ProdutoModel.findAll();
    }

    @Override
    public List<ProdutoModel> listAll() {
        return ProdutoModel.listAll();
    }
}
