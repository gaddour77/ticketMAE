package tn.esprit.ticketmaeassurrance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ticketmaeassurrance.entities.Ticket;
import tn.esprit.ticketmaeassurrance.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
   Optional<User> findByEmail(String email );

}
