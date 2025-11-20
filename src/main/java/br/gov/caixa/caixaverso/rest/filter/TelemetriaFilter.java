package br.gov.caixa.caixaverso.rest.filter;

import java.io.IOException;
import java.time.LocalDate;

import org.jboss.logging.Logger;

import br.gov.caixa.caixaverso.repository.TelemetriaRepository;
import br.gov.caixa.caixaverso.repository.model.TelemetriaModel;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TelemetriaFilter implements ContainerRequestFilter, ContainerResponseFilter {
    String START = "start-time";

    @Inject
    Logger logger = Logger.getLogger(TelemetriaFilter.class); 

    @Inject
    TelemetriaRepository telemetriaRepository;


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        requestContext.setProperty(START, System.nanoTime());
    }


    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        try {
            Long comeco = (Long) requestContext.getProperty(START);
            if (comeco != null) {
                long duracao = System.nanoTime() - comeco;
                double tempoMs = duracao / 1_000_000.0;
                
                TelemetriaModel model = new TelemetriaModel();
                model.setCo_path(requestContext.getUriInfo().getPath());
                model.setNu_tempo((int) tempoMs);
                model.setDt_criacao(LocalDate.now());
                inserirAsync(model);
            }
        } catch (Exception e) {
            logger.error("erro ao consultar telemetria",e);
        }
    }

    private void inserirAsync(TelemetriaModel model) {
        Thread.ofVirtual().start(() -> {
            telemetriaRepository.inserir(model);
        });
    }
}
