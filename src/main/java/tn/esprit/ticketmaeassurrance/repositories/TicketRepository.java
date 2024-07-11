package tn.esprit.ticketmaeassurrance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ticketmaeassurrance.entities.EtatTicket;
import tn.esprit.ticketmaeassurrance.entities.Ticket;
import tn.esprit.ticketmaeassurrance.entities.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    List<Ticket> findByEmploye_Email(String email);
    List<Ticket> findByEtatAndEmploye(EtatTicket etatTicket, User user);
    List<Ticket> findByEtat(EtatTicket etatTicket);
    List<Ticket>  findByEtatAndEmployeAndDateFermetureAfter(EtatTicket etat,User user, Date dateFermeture);
}
