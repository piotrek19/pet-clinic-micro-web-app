package net.dzioba.petclinicmicro.petclinicmicrowebapp.api.v1.service.scheduled;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dzioba.petclinicmicro.common.model.*;
import net.dzioba.petclinicmicro.petclinicmicrowebapp.api.v1.service.VisitServiceClient;
import net.dzioba.petclinicmicro.petclinicmicrowebapp.domain.RoomReservationStart;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Setter
@ConfigurationProperties(prefix = "net.dzioba.petclinicmicro")
@Service
public class VisitServiceClientScheduled implements VisitServiceClient {

    private static final Integer DAYS_OFFSET_MAX = 11;
    private static final RoomReservationStart[] ROOM_RESERVATION_STARTS = RoomReservationStart.values();

    private String clinicManagerAppHost;
    private String clinicManagerApiV1Visits;

    private final RestTemplate restTemplate;

    public VisitServiceClientScheduled(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Transactional
    @Scheduled(fixedRate = 1000) //run every 1 seconds
    @Override
    public void findPossibleVisitsForDate() {
        log.debug("Scheduled method findPossibleVisitsForDate started");

        String visitDate = LocalDate.now().plusDays(new Random().nextInt(DAYS_OFFSET_MAX)).toString();
        String url = UriComponentsBuilder.fromUriString(clinicManagerAppHost + clinicManagerApiV1Visits)
                .queryParam("visitDate", visitDate).toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        log.debug("Sending GET request to: " + url);
        ResponseEntity<PossibleVisitListDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), ParameterizedTypeReference.forType(PossibleVisitListDTO.class));
        log.debug("RESPONSE STATUS: " + responseEntity.getStatusCodeValue());
        log.debug("RESPONSE BODY: " + responseEntity.getBody());
    }

    @Transactional
    @Scheduled(fixedRate = 1000) //run every 1 seconds
    @Override
    public void scheduleThisVisit() {
        log.debug("Scheduled method scheduleThisVisit started");

        LocalDate visitDateRoullete = LocalDate.now().plusDays(new Random().nextInt(DAYS_OFFSET_MAX));
        RoomReservationStart roomReservationStartRoulette = ROOM_RESERVATION_STARTS[new Random().nextInt(ROOM_RESERVATION_STARTS.length)];
        Long petRoulette = new Random().nextInt(2) == 0 ? 1L : 2L;
        Long vetRoulette =  new Random().nextInt(2) == 0 ? 1L : 2L;

        PetShorterDTO pet = PetShorterDTO.builder().id(petRoulette).build();
        OwnerShorterDTO owner = OwnerShorterDTO.builder().id(petRoulette).build();
        VetShortDTO vet = VetShortDTO.builder().id(vetRoulette).build();
        RoomReservationShortDTO roomReservation = RoomReservationShortDTO.builder().room(RoomShortDTO.builder().id(vetRoulette).build()).build();

        VisitDTO visitDTO = VisitDTO.builder().dateTime(LocalDateTime.of(visitDateRoullete, roomReservationStartRoulette.getTime()))
                .description("Client Simulator here").owner(owner).pet(pet)
                .roomReservation(roomReservation).vet(vet).build();

        String url = clinicManagerAppHost + clinicManagerApiV1Visits;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        try {
            log.debug("Sending POST request to: " + url);
            ResponseEntity<VisitDTO> responseEntity = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(visitDTO, headers), ParameterizedTypeReference.forType(VisitDTO.class));
            log.debug("RESPONSE STATUS: " + responseEntity.getStatusCodeValue());
            log.debug("RESPONSE BODY: " + responseEntity.getBody());
        } catch (HttpClientErrorException|NullPointerException exception){
            log.debug(exception.toString());
        }
    }

}

