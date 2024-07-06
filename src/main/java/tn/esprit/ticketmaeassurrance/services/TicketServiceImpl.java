package tn.esprit.ticketmaeassurrance.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.ticketmaeassurrance.entities.EtatTicket;
import tn.esprit.ticketmaeassurrance.entities.Ticket;
import tn.esprit.ticketmaeassurrance.entities.User;
import tn.esprit.ticketmaeassurrance.repositories.TicketRepository;
import tn.esprit.ticketmaeassurrance.repositories.UserRepository;

import java.time.LocalDate;
import java.util.Date;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements ITicketService{
    private final TicketRepository ticketRepository;
    private final AuthentificationService authentificationService;
    private final UserRepository userRepository;
    @Override
    public Ticket addTicket(Ticket ticket) {
        System.out.println("add ticket demarrer  ");
        String mail = authentificationService.getCurrentUsername();
        System.out.println("user mail: "+mail);
       // User user = userRepository.findByEmail(mail).orElse(null);
        User user = authentificationService.connected();
        if(user!=null){
            System.out.println("user connected : "+user.getEmail());
            ticket.setEmploye(user);
        }

        ticket.setEtat(EtatTicket.TO_DO);
        ticket.setDatePublication(new Date());
        return ticketRepository.save(ticket);
    }
}
