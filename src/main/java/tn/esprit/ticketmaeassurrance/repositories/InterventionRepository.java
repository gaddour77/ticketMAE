package tn.esprit.ticketmaeassurrance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.ticketmaeassurrance.Dto.ITEmployeeInterventionsDTO;
import tn.esprit.ticketmaeassurrance.entities.Intervention;
import tn.esprit.ticketmaeassurrance.entities.User;

import java.util.Date;
import java.util.List;

public interface InterventionRepository extends JpaRepository<Intervention,Long> {
    @Query("SELECT i FROM Intervention i WHERE i.user IS NOT NULL")
    List<Intervention> findAllWithEmploye();
    List<Intervention> findByUser(User user);
    List<Intervention> findByUserAndStart(User user , Date date);
    @Query("SELECT " +
            "SUM(CASE WHEN i.affectation IS NOT NULL AND i.start IS NULL THEN 1 ELSE 0 END) AS affectedCount, " +
            "SUM(CASE WHEN i.start IS NOT NULL AND i.end IS NULL THEN 1 ELSE 0 END) AS doingCount, " +
            "SUM(CASE WHEN i.end IS NOT NULL THEN 1 ELSE 0 END) AS doneCount " +
            "FROM Intervention i " +
            "WHERE i.user.idUser = :userId AND " +
            "MONTH(i.affectation) = MONTH(CURRENT_DATE) AND " +
            "YEAR(i.affectation) = YEAR(CURRENT_DATE)")
    List<Object[]> getInterventionCountsForUserThisMonth(@Param("userId") Long userId);
}
