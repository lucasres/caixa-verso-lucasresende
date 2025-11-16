package br.gov.caixa.caixaverso.rest;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.repository.SimulacaoRepository;
import br.gov.caixa.caixaverso.rest.dto.SimulacaoRequestDTO;
import br.gov.caixa.caixaverso.services.CriarSimulacaoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
public class SimulacoesRest {
    @Inject
    JsonWebToken jwt;

    @Inject
    CriarSimulacaoService criarSimulacaoService;

    @Inject
    SimulacaoRepository simulacaoRepository;

    @GET
    @Path("/simulacoes/por-produto-dia")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"User"})
    public Response listarSimulacoesPorDia() throws RegraInvalidaException {
        return Response.status(Response.Status.OK)
            .entity(simulacaoRepository.agruparPorDia())
            .build();
    }

    @GET
    @Path("/simulacoes")
    @RolesAllowed({"User"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarSimulacoes(
        @QueryParam("pagina") Integer pagina,
        @QueryParam("quantidade") Integer quantidade
    ) throws RegraInvalidaException {
        if (pagina == null) {
            pagina = 0;
        }

        if (quantidade == null) {
            quantidade = 10;
        }

        if (pagina < 0) {
            throw new RegraInvalidaException("O valor da página não pode ser menor que zero");
        }

        if (quantidade <= 0) {
            throw new RegraInvalidaException("O valor da quantidade não pode ser menor ou igual a zero");
        }

        return Response.status(Response.Status.OK)
            .entity(simulacaoRepository.listar(pagina, quantidade))
            .build();
    }

    @POST
    @Path("/simular-investimento")
    @RolesAllowed({"User"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response criarSimulacao(
        @Valid
        SimulacaoRequestDTO request
    ) throws RegraInvalidaException {
        return Response.status(Response.Status.CREATED)
            .entity(criarSimulacaoService.executar(request))
            .build();
    }

    @GET
    @Path("/investimentos/{clienteId}")
    @RolesAllowed({"User"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarSimulacoesCliente(
        @PathParam("clienteId") String clienteId,
        @QueryParam("pagina") Integer pagina,
        @QueryParam("quantidade") Integer quantidade
    ) throws RegraInvalidaException {
        if (!clienteId.matches("\\d+")) {
            throw new RegraInvalidaException("O clienteId deve conter apenas números.");
        }

        if (pagina == null) {
            pagina = 0;
        }

        if (quantidade == null) {
            quantidade = 10;
        }

        if (pagina < 0) {
            throw new RegraInvalidaException("O valor da página não pode ser menor que zero");
        }

        if (quantidade <= 0) {
            throw new RegraInvalidaException("O valor da quantidade não pode ser menor ou igual a zero");
        }

        return Response.status(Response.Status.CREATED)
            .entity(simulacaoRepository.listarByClienteIdPaginado(Long.parseLong(clienteId), pagina, quantidade))
            .build();
    }
}
