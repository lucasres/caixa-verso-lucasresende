package br.gov.caixa.caixaverso.rest;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.rest.dto.LoginRequestDTO;
import br.gov.caixa.caixaverso.rest.dto.RegistroRequestDTO;
import br.gov.caixa.caixaverso.services.LoginService;
import br.gov.caixa.caixaverso.services.RegistroService;
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

    @Inject
    RegistroService registroService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response login(
        @Valid
        LoginRequestDTO request
    ) throws RegraInvalidaException {
        Response.Status status = Response.Status.OK;

        Object retornoApi = loginService.executar(request.cpf(), request.password());

        return Response.status(status)
            .entity(retornoApi)
            .build();
    }

    @POST
    @Path("/cadastro")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response cadastro(
        @Valid
        RegistroRequestDTO request
    ) throws RegraInvalidaException {
        Response.Status status = Response.Status.OK;

        var usuario = registroService.executar(request.cpf(), request.password(), request.nome());

        return Response.status(status)
            .entity(usuario)
            .build();
    }
}
