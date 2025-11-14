package br.gov.caixa.caixaverso.rest;

import br.gov.caixa.caixaverso.rest.dto.LoginRequestDTO;
import br.gov.caixa.caixaverso.services.LoginService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/auth")
public class AuthRest {

    @Inject
    LoginService loginService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response login(
        @Valid
        LoginRequestDTO request
    ) {
        Response.Status status = Response.Status.BAD_REQUEST;

        Object retornoApi;
        try {
            retornoApi = loginService.executar(request.cpf(), request.password());
        } catch (Exception e) {
            retornoApi = "invalido";
            throw e;
        }

        return Response.status(status)
            .entity(retornoApi)
            .build();
    }
}
