package br.gov.caixa.caixaverso.contracts;

import br.gov.caixa.caixaverso.repository.model.UsuarioModel;

public interface UsuarioPersistance {
    public UsuarioModel findUsuarioByCpf(String cpf);

    public UsuarioModel inserir(UsuarioModel model);

    public UsuarioModel findById(Long id);
}
