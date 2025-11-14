package br.gov.caixa.caixaverso.services;

import java.util.Arrays;
import java.util.HashSet;

import br.gov.caixa.caixaverso.services.dto.LoginDTO;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoginService {
    public LoginDTO executar(String cpf, String pass) {
        String token = Jwt.issuer("https://example.com/issuer") 
                .upn(cpf) 
                .groups(new HashSet<>(Arrays.asList("User", "Admin"))) 
                .sign();
        return new LoginDTO(token);
    }
}
