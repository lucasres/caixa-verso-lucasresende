package br.gov.caixa.caixaverso.rest;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.repository.TelemetriaRepository;
import br.gov.caixa.caixaverso.rest.dto.TelemetriaResponseDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
public class TelemetriaRest {
    @Inject
    TelemetriaRepository telemetriaRepository;

    @RolesAllowed({"Admin"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/telemetria")
    @Operation(description = "Listagem de dados de telemtria.")
    @APIResponses(
        value = {
            @APIResponse(
                name = "Retorna da listagem",
                responseCode = "200",
                content = @Content(
                    schema = @Schema(implementation = TelemetriaResponseDTO.class)
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
    public Response listaTelemetria(
        @QueryParam("inicio") String inicio,
        @QueryParam("fim") String fim
    ) throws RegraInvalidaException {
        LocalDate inicioParsed;
        LocalDate fimParsed;

        try {
            inicioParsed = inicio != null ? LocalDate.parse(inicio) : LocalDate.now();
            fimParsed = fim != null ? LocalDate.parse(fim) : LocalDate.now();
        } catch (DateTimeParseException e) {
            throw new RegraInvalidaException("valor do inicio ou fim inválido: Padrão esperado YYYY-mm-dd");
        }

        var telemetrias = telemetriaRepository.agruparPorPath(inicioParsed, fimParsed);
        var periodo = Map.of("inicio", inicioParsed.toString(), "fim", fimParsed.toString());

        return Response.status(Response.Status.OK)
            .entity(new TelemetriaResponseDTO(telemetrias, periodo))
            .build();
    }
}
