package br.gov.caixa.caixaverso.rest;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

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

    @RolesAllowed({"User"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/telemetria")
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
