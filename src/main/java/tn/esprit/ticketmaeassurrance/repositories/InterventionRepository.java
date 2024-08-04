package tn.esprit.ticketmaeassurrance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.ticketmaeassurrance.Dto.ITEmployeeInterventionsDTO;
import tn.esprit.ticketmaeassurrance.entities.Intervention;
import tn.esprit.ticketmaeassurrance.entities.User;

import java.util.List;

public interface InterventionRepository extends JpaRepository<Intervention,Long> {
    @Query("SELECT i FROM Intervention i WHERE i.user IS NOT NULL")
    List<Intervention> findAllWithEmploye();
}
