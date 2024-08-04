package tn.esprit.ticketmaeassurrance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.ticketmaeassurrance.entities.EtatTicket;
import tn.esprit.ticketmaeassurrance.entities.Ticket;
import tn.esprit.ticketmaeassurrance.entities.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    List<Ticket> findByEmploye_Email(String email);
   // List<Ticket> findByEtatAndItEmploye(EtatTicket etatTicket, User user);
    List<Ticket> findByEtat(EtatTicket etatTicket);
    //List<Ticket>  findByEtatAndItEmployeAndDateFermetureAfter(EtatTicket etat,User user, Date dateFermeture);
    List<Ticket> findByEtatAndEmploye(EtatTicket etatTicket, User user);
 @Query("SELECT t FROM Ticket t JOIN t.interventions i WHERE i.user = :user AND i.affectation IS NOT NULL AND i.start is  NULL ")
 List<Ticket> findTicketsByUserAndAffectationDate(@Param("user") User user);
    @Query("SELECT t FROM Ticket t JOIN t.interventions i WHERE i.user = :user AND i.start IS NOT NULL AND i.end IS  NULL")
    List<Ticket> findTicketsInProgress(@Param("user") User user);
    @Query("SELECT t FROM Ticket t JOIN t.interventions i WHERE i.user = :user AND  i.end IS NOT NULL")
    List<Ticket> findTicketsDone(@Param("user") User user);

}

