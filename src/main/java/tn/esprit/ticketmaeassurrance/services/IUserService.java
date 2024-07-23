package tn.esprit.ticketmaeassurrance.services;

import tn.esprit.ticketmaeassurrance.entities.Ticket;
import tn.esprit.ticketmaeassurrance.entities.User;

import java.util.List;
import java.util.Set;

public interface IUserService {
    Set<Ticket> mytickets();
    List<User> gettAll();
}
