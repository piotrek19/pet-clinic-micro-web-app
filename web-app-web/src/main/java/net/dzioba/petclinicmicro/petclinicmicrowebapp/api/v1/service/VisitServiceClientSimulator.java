package net.dzioba.petclinicmicro.petclinicmicrowebapp.api.v1.service;

import net.dzioba.petclinicmicro.common.model.PossibleVisitListDTO;
import net.dzioba.petclinicmicro.common.model.VisitDTO;

public interface VisitServiceClientSimulator {

    PossibleVisitListDTO findPossibleVisitsForDate();

    VisitDTO scheduleThisVisit();
}
