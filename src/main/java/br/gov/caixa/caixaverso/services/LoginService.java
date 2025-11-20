package br.gov.caixa.caixaverso.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import org.mindrot.jbcrypt.BCrypt;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.repository.UsuariosRepository;
import br.gov.caixa.caixaverso.repository.model.UsuarioModel;
import br.gov.caixa.caixaverso.services.dto.LoginDTO;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LoginService {
    @Inject
    UsuariosRepository usuariosRepository;

    public LoginDTO executar(String cpf, String pass) throws RegraInvalidaException {
        UsuarioModel usuario = usuariosRepository.findUsuarioByCpf(cpf);

        if (usuario == null || !validarPassword(usuario, pass)) {
            throw new RegraInvalidaException("Usuário ou senha inválida");
        }

        String token = Jwt.issuer("https://example.com/issuer") 
                .upn(cpf) 
                .claim("clienteId", usuario.getCo_id())
                .expiresAt(28_800)
                .groups(new HashSet<>(Arrays.asList(usuario.getIc_perfil())))
                .sign();
        return new LoginDTO(token);
    }

        public boolean validarPassword(UsuarioModel usuario, String password) {
            return BCrypt.checkpw(password, usuario.getNo_password());
        }
    }
