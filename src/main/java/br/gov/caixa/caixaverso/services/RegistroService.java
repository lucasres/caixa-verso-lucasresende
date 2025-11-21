package br.gov.caixa.caixaverso.services;

import java.util.Arrays;
import java.util.HashSet;

import org.mindrot.jbcrypt.BCrypt;

import br.gov.caixa.caixaverso.contracts.UsuarioPersistance;
import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.repository.model.UsuarioModel;
import br.gov.caixa.caixaverso.services.dto.LoginDTO;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RegistroService {
    @Inject
    UsuarioPersistance usuarioPersistance;

    public LoginDTO executar(String cpf, String pass, String nome) throws RegraInvalidaException {
        UsuarioModel usuarioExistente = usuarioPersistance.findUsuarioByCpf(cpf);

        if (usuarioExistente != null) {
            throw new RegraInvalidaException("CPF '" +  cpf  + "' já cadastrado");
        }

        UsuarioModel model = new UsuarioModel(); 
        model.setCo_cpf(cpf);
        model.setNo_password(BCrypt.hashpw(pass, BCrypt.gensalt()));
        model.setNo_nome(nome);

        usuarioPersistance.inserir(model);

        
        String token = Jwt.issuer("https://example.com/issuer") 
                .upn(cpf) 
                .claim("clienteId", model.getCo_id()) 
                .groups(new HashSet<>(Arrays.asList("User")))
                .sign();

        return new LoginDTO(token);
    }
}
