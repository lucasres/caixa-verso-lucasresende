package br.gov.caixa.caixaverso.rest;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.logging.Logger;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.repository.model.ProdutoModel;
import br.gov.caixa.caixaverso.services.MotorDeRecomendacaoService;
import io.quarkus.security.UnauthorizedException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("")
public class ProdutoRest {
    @Inject
    JsonWebToken jwt;

    @Inject
    Logger logger = Logger.getLogger(ProdutoRest.class);

    @Inject
    MotorDeRecomendacaoService motorDeRecomendacaoService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"User", "Admin"})
    @Path("/produtos-recomendados/{perfil}")
    @Operation(description = "Recupera produtos recomendados.")
    @APIResponses(
        value = {
            @APIResponse(
                name = "Sucesso em recuperar o produtos recomendado",
                responseCode = "200",
                content = @Content(
                    schema = @Schema(implementation = ProdutoModel.class, type = SchemaType.ARRAY)
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
    public Response getDeProduto(
        @PathParam("perfil") String perfil,
        @Context SecurityContext ctx
    ) throws RegraInvalidaException {
        if (jwt.claim("clienteId") == null) {
            throw new UnauthorizedException();
        }

        var clienteId = Long.parseLong(jwt.getClaim("clienteId").toString());
        logger.info("perfil passado: " + perfil);
        logger.info("client id: " +  clienteId);
        Response.Status status = Response.Status.OK;
        var produtos = motorDeRecomendacaoService.executar(perfil, clienteId);

        return Response.status(status)
            .entity(produtos)
            .build();

    }
}
