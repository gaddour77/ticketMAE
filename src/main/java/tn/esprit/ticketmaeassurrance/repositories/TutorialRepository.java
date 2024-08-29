package tn.esprit.ticketmaeassurrance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ticketmaeassurrance.entities.Tutorial;
import tn.esprit.ticketmaeassurrance.entities.TuturialAccess;

import java.util.List;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
    List<Tutorial> findByAccess(TuturialAccess access);
}
