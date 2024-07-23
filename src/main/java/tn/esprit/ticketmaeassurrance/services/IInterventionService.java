package tn.esprit.ticketmaeassurrance.services;

import tn.esprit.ticketmaeassurrance.entities.Intervention;

public interface IInterventionService {
    Intervention addIntervention(Intervention intervention,Long id);
    Intervention affecterIntervention(Intervention intervention,Long idticket,Long isUser);
}
