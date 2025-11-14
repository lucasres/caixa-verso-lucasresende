package br.gov.caixa.caixaverso.services;

import org.mindrot.jbcrypt.BCrypt;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.repository.UsuariosRepository;
import br.gov.caixa.caixaverso.repository.model.UsuarioModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RegistroService {
    @Inject
    UsuariosRepository usuariosRepository;

    public UsuarioModel executar(String cpf, String pass, String nome) throws RegraInvalidaException {
        UsuarioModel usuarioExistente = usuariosRepository.findUsuarioByCpf(cpf);

        if (usuarioExistente != null) {
            throw new RegraInvalidaException("CPF '" +  cpf  + "' já cadastrado");
        }

        UsuarioModel model = new UsuarioModel(); 
        model.setCo_cpf(cpf);
        model.setNo_password(BCrypt.hashpw(pass, BCrypt.gensalt()));
        model.setNo_nome(nome);

        usuariosRepository.inserir(model);
        return model;
    }
}
