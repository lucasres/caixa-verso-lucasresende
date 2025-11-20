package br.gov.caixa.caixaverso.rest;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.services.MotorDePerfilService;
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
