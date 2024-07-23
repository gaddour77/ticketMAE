package tn.esprit.ticketmaeassurrance.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.ticketmaeassurrance.entities.Intervention;
import tn.esprit.ticketmaeassurrance.entities.Ticket;
import tn.esprit.ticketmaeassurrance.entities.User;
import tn.esprit.ticketmaeassurrance.repositories.InterventionRepository;
import tn.esprit.ticketmaeassurrance.repositories.TicketRepository;
import tn.esprit.ticketmaeassurrance.repositories.UserRepository;

import java.util.Date;

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
}
