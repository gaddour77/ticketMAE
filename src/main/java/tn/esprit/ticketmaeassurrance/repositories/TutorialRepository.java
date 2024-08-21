package tn.esprit.ticketmaeassurrance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ticketmaeassurrance.entities.Tutorial;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
}
