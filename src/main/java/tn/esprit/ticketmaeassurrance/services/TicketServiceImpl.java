package tn.esprit.ticketmaeassurrance.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.ticketmaeassurrance.entities.EtatTicket;
import tn.esprit.ticketmaeassurrance.entities.Intervention;
import tn.esprit.ticketmaeassurrance.entities.Ticket;
import tn.esprit.ticketmaeassurrance.entities.User;
import tn.esprit.ticketmaeassurrance.repositories.InterventionRepository;
import tn.esprit.ticketmaeassurrance.repositories.TicketRepository;
import tn.esprit.ticketmaeassurrance.repositories.UserRepository;

import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements ITicketService{
    private final TicketRepository ticketRepository;
    private final AuthentificationService authentificationService;
    private final UserRepository userRepository;
    private final InterventionServiceImpl interventionService;
    private final InterventionRepository interventionRepository;
    @Override
    public Ticket addTicket(Ticket ticket) {
        User user = authentificationService.connected();
        if(user!=null){
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
        return tickets;
    }

    @Override
    public List<Ticket> getByEtatANDEMPLOYE(String etat) {
        String mail = authentificationService.getCurrentUsername();

        User user = userRepository.findByEmail(mail).orElse(null);
        List<Ticket> tickets = new ArrayList<Ticket>();
        if(user.getRole().equals("IT")){
            tickets = ticketRepository.findTicketsInProgress(user);
        } else if (user.getRole().equals("EMPLOYE")) {
            tickets = ticketRepository.findByEtatAndEmploye(EtatTicket.valueOf(etat.toUpperCase()),user);
        }else {
            tickets =ticketRepository.findByEtat(EtatTicket.valueOf(etat.toUpperCase()));
        }
        System.out.println(mail);

        if (tickets!=null){
            return  tickets;
        }
        return null;
    }

    @Override
    public Ticket affecter(Long idTicket) {
        Ticket ticket = ticketRepository.findById(idTicket).orElse(null);
        User user = authentificationService.connected();

        if (user !=null && ticket != null){
            ticket.setEtat(EtatTicket.IN_PROGRESS);
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
            //ticket.setItEmploye(null);
            ticket.setEtat(EtatTicket.TO_DO);
           // ticket.setDateOuverture(null);
            return ticketRepository.save(ticket);
        }
        return null;
    }


    public Ticket dragAndDrop(String previousList,String Currentlist,Long idTicket,Intervention intervention){
        Ticket ticket = ticketRepository.findById(idTicket).orElse(null);
       User user = authentificationService.connected();
        if(previousList.equals("cdk-drop-list-1")&&Currentlist.equals("cdk-drop-list-2")){
            Intervention i = this.findInterventionForUser(ticket,user);
            i.setEnd(new Date());
            i.setDescription(intervention.getDescription());
            interventionRepository.save(i);
            //tester s'il a des autres interventions ouvertes
            if (this.interventionsClosed(ticket)== false) {
                ticket.setEtat(EtatTicket.CLOSED);
                ticket.setDateFermeture(new Date());
            }
            return ticketRepository.save(ticket);
        } else if (previousList.equals("cdk-drop-list-3")&&Currentlist.equals("cdk-drop-list-1")) {
            if(intervention!=null){
              intervention.setStart(new Date());
              intervention.setUser(user);
              interventionRepository.save(intervention);
              ticket.addIntervention(intervention);
            }
            return this.affecter(idTicket);
        } else if (previousList.equals("cdk-drop-list-1")&&Currentlist.equals("cdk-drop-list-3")) {
            if(intervention!=null){
                Intervention intervention1= this.findInterventionForUser(ticket,user);
                intervention1.setDescription(intervention.getDescription());
                intervention1.setEnd(new Date());
                interventionRepository.save(intervention1);
            }
            if (this.interventionsClosed(ticket)== false){
                ticket.setEtat(EtatTicket.TO_DO);
            }
            return  ticketRepository.save(ticket);
        }else if (previousList.equals("cdk-drop-list-3")&&Currentlist.equals("cdk-drop-list-2")){
            if(intervention!=null){
                intervention.setUser(user);
                intervention.setEnd(new Date());
                interventionRepository.save(intervention);
                ticket.addIntervention(intervention);
            }
            ticket.setEtat(EtatTicket.CLOSED);
            ticket.setDateFermeture(new Date());
            return ticketRepository.save(ticket);
        }else if (previousList.equals("cdk-drop-list-2")&&Currentlist.equals("cdk-drop-list-1")){
            ticket.setDateFermeture(null);
            ticket.setEtat(EtatTicket.IN_PROGRESS);
            if(intervention!=null){
                intervention.setStart(new Date());
                ticket.addIntervention(intervention);
            }
            return  ticketRepository.save(ticket);
        }else if (previousList.equals("cdk-drop-list-2")&&Currentlist.equals("cdk-drop-list-3")){
            ticket.setDateFermeture(null);
            if(this.interventionsClosed(ticket)==false){
                ticket.setEtat(EtatTicket.TO_DO);
            }else {}

            return  ticketRepository.save(ticket);
        }else if (previousList.equals("cdk-drop-list-0")&&Currentlist.equals("cdk-drop-list-1")){
            Intervention intervention1= this.findInterventionAffectedForUser(ticket,user);
            if(intervention!=null){
                intervention1.setRemarque(intervention.getRemarque());
                intervention1.setStart(new Date());
            }
            return ticketRepository.save(ticket);
        }else if (previousList.equals("cdk-drop-list-0")&&Currentlist.equals("cdk-drop-list-2")){
            Intervention intervention1= this.findInterventionAffectedForUser(ticket,user);
            if(intervention!=null){
                intervention1.setRemarque(intervention.getDescription());
                intervention1.setEnd(new Date());
            }
            return ticketRepository.save(ticket);
        }
        return null;
    }

    public boolean interventionsClosed(Ticket ticket){
        return ticket.getInterventions().stream()
                .anyMatch(intervention -> intervention.getEnd() == null);
    }
    public boolean myInterventionsClosed(Ticket ticket,User user){
        return ticket.getInterventions().stream()
                .anyMatch(intervention -> intervention.getUser().equals(user) &&  intervention.getStart() !=null && intervention.getEnd() ==null);
    }
    public Intervention findInterventionForUser(Ticket ticket, User user) {
        return ticket.getInterventions().stream()
                .filter(intervention ->
                        intervention.getUser().equals(user) &&
                                intervention.getStart() != null &&
                                intervention.getEnd() == null
                )
                .findFirst().orElse(null);
    }
    //trouver les intervention affecte a cet user
    public Intervention findInterventionAffectedForUser(Ticket ticket, User user) {
        return ticket.getInterventions().stream()
                .filter(intervention ->
                        intervention.getUser().equals(user) &&
                                intervention.getAffectation() != null
                )
                .findFirst().orElse(null);
    }
    //affecter une intervention a un autre user
    public Intervention affecter(Intervention intervention,Long idTicket,Long idUser){
        User user =authentificationService.connected();
        Ticket ticket = ticketRepository.findById(idTicket).orElse(null);
        User user1= userRepository.findById(idUser).orElse(null);
       if(user != null && user1 != null && ticket != null && intervention!=null){
         intervention.setUser(user1);
     intervention.setAffectation(new Date());
     intervention.setRemarque(" affecté par "+user.getFirstName()+" : "+intervention.getRemarque());
     interventionRepository.save(intervention);
         ticket.addIntervention(intervention);
         ticketRepository.save(ticket);
     return intervention;
     }

        return  null;
    }
    //regler l'affichage de it board
    @Override
    public List<Ticket> getDoneThisWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        Date oneWeekAgo = calendar.getTime();
        User user =authentificationService.connected();
        List<Ticket> tickets =ticketRepository.findTicketsDone(user);
        // List<Ticket> tickets =new ArrayList<>() ;
        return tickets;
    }
    public List<Ticket> affectedTicket(){
        User user = authentificationService.connected();
        List<Ticket> tickets = ticketRepository.findTicketsByUserAndAffectationDate(user);
        if(tickets!=null && user!=null){
            return tickets;
        }
        return null;
    }
    public  List<Ticket> inProgress(){
        User user =authentificationService.connected();
        List<Ticket> tickets = ticketRepository.findTicketsInProgress(user);
        return tickets;
    }
    public List<Intervention> interventions(Long idTicket){
        Ticket ticket = ticketRepository.findById(idTicket).orElse(null);
        List<Intervention> interventions = ticket.getInterventions();
        if(interventions != null){
            return interventions;
        }
        return null;
    }
}
