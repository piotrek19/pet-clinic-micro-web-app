package net.dzioba.petclinicmicro.petclinicmicrowebapp.api.v1.service.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dzioba.petclinicmicro.common.model.PossibleVisitListDTO;
import net.dzioba.petclinicmicro.common.model.VisitDTO;
import net.dzioba.petclinicmicro.petclinicmicrowebapp.api.v1.service.VisitServiceClientSimulator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class VisitServiceClientSimulatorScheduled implements VisitServiceClientSimulator {


    //private final RestTemplate restTemplate;

    @Transactional
    @Scheduled(fixedRate = 3000) //run every 3 seconds
    @Override
    public PossibleVisitListDTO findPossibleVisitsForDate() {
        log.debug("Scheduled method findPossibleVisitsForDate started");

        return null;
    }

    @Transactional
    @Scheduled(fixedRate = 3000) //run every 3 seconds
    @Override
    public VisitDTO scheduleThisVisit() {
        log.debug("Scheduled method scheduleThisVisit started");

        return null;
    }

}
