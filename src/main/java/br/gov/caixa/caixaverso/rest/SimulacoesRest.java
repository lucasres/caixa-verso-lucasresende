package br.gov.caixa.caixaverso.rest;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.rest.dto.SimulacaoRequestDTO;
import br.gov.caixa.caixaverso.services.CriarSimulacaoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("")
public class SimulacoesRest {
    @Inject
    JsonWebToken jwt;

    @Inject
    CriarSimulacaoService criarSimulacaoService;

    @GET
    @RolesAllowed({"User"})
    public Response listarSimulacoes() {
        return Response.status(Response.Status.OK)
            .entity("")
            .build();
    }

    @POST
    @Path("/simular-investimento")
    @RolesAllowed({"User"})
    public Response criarSimulacao(
        @Valid
        SimulacaoRequestDTO request
    ) throws RegraInvalidaException {
        return Response.status(Response.Status.CREATED)
            .entity(criarSimulacaoService.executar(request))
            .build();
    }
}
