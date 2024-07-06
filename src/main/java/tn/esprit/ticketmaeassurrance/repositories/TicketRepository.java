package tn.esprit.ticketmaeassurrance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ticketmaeassurrance.entities.Ticket;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
}
