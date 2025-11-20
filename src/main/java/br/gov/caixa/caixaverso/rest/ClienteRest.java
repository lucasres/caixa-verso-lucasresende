package br.gov.caixa.caixaverso.rest;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.services.MotorDePerfilService;
import br.gov.caixa.caixaverso.services.dto.PerfilClienteDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
public class ClienteRest {
    @Inject
    MotorDePerfilService motorDePerfilService;

    @GET
    @Path("/perfil-risco/{clienteId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"User", "Admin"})
    @Operation(description = "Recupera o perfil do cliente.")
    @APIResponses(
        value = {
            @APIResponse(
                name = "Sucesso em recuperar o perfil do cliente",
                responseCode = "200",
                content = @Content(
                    schema = @Schema(implementation = PerfilClienteDTO.class)
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
    public Response getRiscoDoClient(
        @PathParam("clienteId") String clienteId
    ) throws RegraInvalidaException {
        if (!clienteId.matches("\\d+")) {
            throw new RegraInvalidaException("O clienteId deve conter apenas números.");
        }
        var perfil = motorDePerfilService.executar(Long.parseLong(clienteId));
        return Response.status(Response.Status.OK)
            .entity(perfil)
            .build();
    }
}
