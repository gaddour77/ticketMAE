package tn.esprit.ticketmaeassurrance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ticketmaeassurrance.entities.Panne;
import tn.esprit.ticketmaeassurrance.entities.TypePanne;

import java.util.List;

public interface PanneRepository extends JpaRepository<Panne,Long> {
    List<Panne> findByTypePanne(TypePanne panne);
}
