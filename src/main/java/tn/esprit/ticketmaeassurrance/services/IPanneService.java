package tn.esprit.ticketmaeassurrance.services;

import tn.esprit.ticketmaeassurrance.entities.Panne;

import java.util.List;

public interface IPanneService {
    Panne addPanne(Panne panne);
    List<Panne> getAll();
    List<Panne> getDescriptionsParEnum(String type);
}
