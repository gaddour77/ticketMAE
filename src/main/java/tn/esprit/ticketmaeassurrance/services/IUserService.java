package tn.esprit.ticketmaeassurrance.services;

import tn.esprit.ticketmaeassurrance.entities.Ticket;

import java.util.List;
import java.util.Set;

public interface IUserService {
    Set<Ticket> mytickets();
}
