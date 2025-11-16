package br.gov.caixa.caixaverso.rest.mapping;

import java.util.Map;

import io.quarkus.security.UnauthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider 
public class UnathorizedExceptionMapping implements ExceptionMapper<UnauthorizedException> {
    @Override
    public Response toResponse(UnauthorizedException exception) {

        return Response.status(Response.Status.UNAUTHORIZED)
            .entity(Map.of("mensagem", "Primeiro faça a autenticação para acessar esse endpoint."))
            .build();
    }
}
