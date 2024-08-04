package tn.esprit.ticketmaeassurrance.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.ticketmaeassurrance.Dto.ITEmployeeInterventionsDTO;
import tn.esprit.ticketmaeassurrance.entities.Intervention;
import tn.esprit.ticketmaeassurrance.entities.Ticket;
import tn.esprit.ticketmaeassurrance.entities.User;
import tn.esprit.ticketmaeassurrance.repositories.InterventionRepository;
import tn.esprit.ticketmaeassurrance.repositories.TicketRepository;
import tn.esprit.ticketmaeassurrance.repositories.UserRepository;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InterventionServiceImpl implements IInterventionService{
    private final InterventionRepository interventionRepository;
    private final TicketRepository ticketRepository;
    private final AuthentificationService authentificationService;
    private final UserRepository userRepository;
    @Override
    public Intervention addIntervention(Intervention intervention,Long id) {
        User user = authentificationService.connected();
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if(user!=null && ticket!=null){
            intervention.setUser(user);
            intervention.setStart(new Date());
            return interventionRepository.save(intervention);
        }
        return null;
    }

    @Override
    public Intervention affecterIntervention(Intervention intervention, Long idTicket,Long idUser) {
        User user = userRepository.findById(idUser).orElse(null) ;
        Ticket ticket = ticketRepository.findById(idTicket).orElse(null);
        if(user!=null && ticket!=null){
            intervention.setUser(user);
            //intervention.setTicket(ticket);
            intervention.setAffectation(new Date());
            return interventionRepository.save(intervention);
        }
        return null;
    }
    public List<Intervention> findAll(){
        return interventionRepository.findAll();
    }
    public List<ITEmployeeInterventionsDTO> getITEmployeesOrderedByInterventionCount() {
        List<Intervention> interventions = interventionRepository.findAllWithEmploye();

        Map<User, List<Intervention>> interventionsByEmploye = interventions.stream()
                .collect(Collectors.groupingBy(Intervention::getUser));

        return interventionsByEmploye.entrySet().stream()
                .map(entry -> {
                    User employe = entry.getKey();
                    List<Intervention> employeInterventions = entry.getValue();
                    long interventionCount = employeInterventions.size();
                    double averageTimeSpent = employeInterventions.stream()
                            .filter(i -> i.getEnd() != null && i.getStart() != null)
                            .mapToLong(i -> Duration.between(i.getStart().toInstant(), i.getEnd().toInstant()).toHours())
                            .average()
                            .orElse(0.0);

                    return new ITEmployeeInterventionsDTO(
                            employe.getIdUser(),
                            employe.getFirstName() + " " + employe.getLastName(),
                            interventionCount,
                            averageTimeSpent
                    );
                })
                .sorted((e1, e2) -> Long.compare(e2.getInterventionCount(), e1.getInterventionCount()))
                .collect(Collectors.toList());
    }
}
