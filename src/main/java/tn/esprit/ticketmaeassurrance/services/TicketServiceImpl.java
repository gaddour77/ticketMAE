package tn.esprit.ticketmaeassurrance.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.ticketmaeassurrance.entities.EtatTicket;
import tn.esprit.ticketmaeassurrance.entities.Ticket;
import tn.esprit.ticketmaeassurrance.entities.User;
import tn.esprit.ticketmaeassurrance.repositories.TicketRepository;
import tn.esprit.ticketmaeassurrance.repositories.UserRepository;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    @Override
    public List<Ticket> mytickets() {
        String mail = authentificationService.getCurrentUsername();
        List<Ticket> tickets = ticketRepository.findByEmploye_Email(mail);
        return null;
    }

    @Override
    public List<Ticket> getByEtatANDEMPLOYE(String etat) {
        String mail = authentificationService.getCurrentUsername();

        User user = userRepository.findByEmail(mail).orElse(null);
        System.out.println(mail);
        List<Ticket> tickets = ticketRepository.findByEtatAndEmploye(EtatTicket.valueOf(etat.toUpperCase()),user);
        if (tickets!=null){
            return  tickets;
        }
        return null;
    }

    @Override
    public Ticket affecter(Long idTicket) {
        Ticket ticket = ticketRepository.findById(idTicket).orElse(null);
        String mail = authentificationService.getCurrentUsername();
        User user = authentificationService.connected();
        System.out.println("user :"+ user.toString());
        if (user !=null && ticket != null){
            ticket.setEtat(EtatTicket.IN_PROGRESS);
            ticket.setItEmploye(user);
            ticket.setDateOuverture(new Date());
            return ticketRepository.save(ticket);
        }
        return null;
    }

    @Override
    public List<Ticket> getByEtat(String etat) {
        List<Ticket> tickets =ticketRepository.findByEtat(EtatTicket.valueOf(etat.toUpperCase()));
        return tickets;
    }

    @Override
    public Ticket desaffecter(Long idTicket) {
        Ticket ticket= ticketRepository.findById(idTicket).orElse(null);
        if(ticket!=null){
            ticket.setItEmploye(null);
            ticket.setEtat(EtatTicket.TO_DO);
            ticket.setDateOuverture(null);
            return ticketRepository.save(ticket);
        }
        return null;
    }

    @Override
    public List<Ticket> getDoneThisWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        Date oneWeekAgo = calendar.getTime();
        User user =authentificationService.connected();
        List<Ticket> tickets =ticketRepository.findByEtatAndEmployeAndDateFermetureAfter(EtatTicket.CLOSED,user,oneWeekAgo);
        return tickets;
    }
    public Ticket dragAndDrop(String previousList,String Currentlist,Long idTicket){
        Ticket ticket = ticketRepository.findById(idTicket).orElse(null);

        if(previousList.equals("cdk-drop-list-1")&&Currentlist.equals("cdk-drop-list-2")){
            ticket.setEtat(EtatTicket.CLOSED);
            ticket.setDateFermeture(new Date());
            return ticketRepository.save(ticket);
        } else if (previousList.equals("cdk-drop-list-0")&&Currentlist.equals("cdk-drop-list-1")) {
           return ticket =this.affecter(idTicket);
        } else if (previousList.equals("cdk-drop-list-1")&&Currentlist.equals("cdk-drop-list-0")) {
           return ticket = this.desaffecter(idTicket);
        }

        return null;
    }
}
