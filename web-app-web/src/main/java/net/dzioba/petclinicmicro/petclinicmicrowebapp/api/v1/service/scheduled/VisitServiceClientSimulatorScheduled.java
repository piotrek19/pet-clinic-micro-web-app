package net.dzioba.petclinicmicro.petclinicmicrowebapp.api.v1.service.scheduled;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dzioba.petclinicmicro.common.model.PossibleVisitListDTO;
import net.dzioba.petclinicmicro.petclinicmicrowebapp.api.v1.service.VisitServiceClientSimulator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Random;

@Slf4j
@Setter
@ConfigurationProperties(prefix = "net.dzioba.petclinicmicro")
@Service
public class VisitServiceClientSimulatorScheduled implements VisitServiceClientSimulator {

    private final Integer maxDayOffset = 11;

    private String clinicManagerAppHost;
    private String clinicManagerApiV1Visits;

    private final RestTemplate restTemplate;

    public VisitServiceClientSimulatorScheduled(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Transactional
    @Scheduled(fixedRate = 3000) //run every 3 seconds
    @Override
    public void findPossibleVisitsForDate() {
        log.debug("Scheduled method findPossibleVisitsForDate started");


        String visitDate = LocalDate.now().plusDays(new Random().nextInt(maxDayOffset)).toString();

        String url = UriComponentsBuilder.fromUriString(clinicManagerAppHost + clinicManagerApiV1Visits)
                .queryParam("visitDate", visitDate).toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        log.debug("Sending GET request to: " + url);
        ResponseEntity<PossibleVisitListDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), ParameterizedTypeReference.forType(PossibleVisitListDTO.class));
        log.debug("Response received: \n" + responseEntity.getBody());
    }

    @Transactional
    @Scheduled(fixedRate = 3000) //run every 3 seconds
    @Override
    public void scheduleThisVisit() {
        log.debug("Scheduled method scheduleThisVisit started");

    }

}

