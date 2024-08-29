package tn.esprit.ticketmaeassurrance.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.ticketmaeassurrance.entities.Machine;
import tn.esprit.ticketmaeassurrance.repositories.MachineRepository;

import java.util.List;

@Service
@AllArgsConstructor

public class MachineService {
    private final MachineRepository machineRepository;
    public Machine saveMachine(Machine machine){
        if (machine!=null){
            return machineRepository.save(machine);
        }
        return null;
    }
    public void deleteMachine(Long id){
        Machine machine = machineRepository.findById(id).orElse(null);
        if (machine!=null){
             machineRepository.delete(machine);
        }

    }
    public List<Machine> getall(){
        return machineRepository.findAll();
    }
}
