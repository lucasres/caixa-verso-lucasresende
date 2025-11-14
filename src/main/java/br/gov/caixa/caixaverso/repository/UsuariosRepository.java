package br.gov.caixa.caixaverso.repository;

import br.gov.caixa.caixaverso.repository.model.UsuarioModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UsuariosRepository implements PanacheRepository<UsuarioModel> {
    public UsuarioModel findUsuarioByCpf(String cpf) {
        var query = getEntityManager().createNamedQuery("findUsuarioByCpf", UsuarioModel.class);
        query.setParameter("cpf", cpf);
        UsuarioModel usuario = query.getSingleResultOrNull();
        return usuario;
    }

    @Transactional
    public UsuarioModel inserir(UsuarioModel model) {
        getEntityManager().persist(model);
        return model;
    }
}
