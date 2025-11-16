package br.gov.caixa.caixaverso.rest;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
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
    @RolesAllowed({"User"})
    @Path("/produtos-recomendados/{perfil}")
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
