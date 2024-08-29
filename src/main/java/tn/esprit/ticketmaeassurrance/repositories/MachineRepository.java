package tn.esprit.ticketmaeassurrance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ticketmaeassurrance.entities.Machine;

public interface MachineRepository extends JpaRepository<Machine,Long> {

}
