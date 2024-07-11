package tn.esprit.ticketmaeassurrance.services;

import tn.esprit.ticketmaeassurrance.entities.Ticket;

import java.util.List;

public interface ITicketService {
    Ticket addTicket(Ticket ticket);
    List<Ticket> mytickets();
    List<Ticket> getByEtatANDEMPLOYE(String etat);
    Ticket affecter(Long idTicket);
    List<Ticket> getByEtat(String etat);
    Ticket desaffecter(Long idTicket);
    List<Ticket> getDoneThisWeek();
}
