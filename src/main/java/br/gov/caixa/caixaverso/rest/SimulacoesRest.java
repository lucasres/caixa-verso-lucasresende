package br.gov.caixa.caixaverso.rest;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.repository.SimulacaoRepository;
import br.gov.caixa.caixaverso.repository.dto.ResultadoPaginadoDTO;
import br.gov.caixa.caixaverso.repository.dto.SimulacoesAgrupadasPorDiaDTO;
import br.gov.caixa.caixaverso.rest.dto.RegistroRequestDTO;
import br.gov.caixa.caixaverso.rest.dto.SimulacaoRequestDTO;
import br.gov.caixa.caixaverso.services.CriarSimulacaoService;
import br.gov.caixa.caixaverso.services.dto.SimulacaoDTO;
import br.gov.caixa.caixaverso.utils.examples.SimulacaoExemplo;
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
@Tag(name = "Simulacoes")
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
    @RolesAllowed({"User", "Admin"})
    @Operation(description = "Lista as simulações por dia.")
    @APIResponses(
        value = {
            @APIResponse(
                name = "Sucesso em recuperar as simulações",
                responseCode = "200",
                content = @Content(
                    schema = @Schema(implementation = SimulacoesAgrupadasPorDiaDTO.class, type = SchemaType.ARRAY)
                )
            ),
            @APIResponse(
                name = "Regra inválida",
                responseCode = "400",
                description = "Algum dado fornecido não estava válido",
                content = @Content(
                    schema = @Schema(implementation = RegraInvalidaException.class)
                )
            )
        }
    )
    public Response listarSimulacoesPorDia() throws RegraInvalidaException {
        return Response.status(Response.Status.OK)
            .entity(simulacaoRepository.agruparPorDia())
            .build();
    }

    @GET
    @Path("/simulacoes")
    @RolesAllowed({"User", "Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Lista todas as simulações.")
    @APIResponses(
        value = {
            @APIResponse(
                name = "Sucesso em recuperar as simulações",
                responseCode = "200",
                content = @Content(
                    schema = @Schema(implementation = ResultadoPaginadoDTO.class)
                )
            ),
            @APIResponse(
                name = "Regra inválida",
                responseCode = "400",
                description = "Algum dado fornecido não estava válido",
                content = @Content(
                    schema = @Schema(implementation = RegraInvalidaException.class)
                )
            )
        }
    )
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
    @RolesAllowed({"User", "Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Cria uma nova simulação de investimento.")
        @RequestBody(
        required = true,
        content = @Content(
            schema = @Schema(implementation = RegistroRequestDTO.class, required = true),
            examples = @ExampleObject(
              name = "Payload de criação",
              value = SimulacaoExemplo.CRIAR_OK
            )
        )
    )
    @APIResponses(
        value = {
            @APIResponse(
                name = "Retorna da simulação",
                responseCode = "200",
                content = @Content(
                    schema = @Schema(implementation = SimulacaoDTO.class)
                )
            ),
            @APIResponse(
                name = "Regra inválida",
                responseCode = "400",
                description = "Algum dado fornecido não estava válido",
                content = @Content(
                    schema = @Schema(implementation = RegraInvalidaException.class)
                )
            )
        }
    )
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
    @RolesAllowed({"User", "Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Listar simulações de um cliente")
    @APIResponses(
        value = {
            @APIResponse(
                name = "Retorna da listagem",
                responseCode = "200",
                content = @Content(
                    schema = @Schema(implementation = ResultadoPaginadoDTO.class)
                )
            ),
            @APIResponse(
                name = "Regra inválida",
                responseCode = "400",
                description = "Algum dado fornecido não estava válido",
                content = @Content(
                    schema = @Schema(implementation = RegraInvalidaException.class)
                )
            )
        }
    )
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

        return Response.status(Response.Status.OK)
            .entity(simulacaoRepository.listarByClienteIdPaginado(Long.parseLong(clienteId), pagina, quantidade))
            .build();
    }
}
