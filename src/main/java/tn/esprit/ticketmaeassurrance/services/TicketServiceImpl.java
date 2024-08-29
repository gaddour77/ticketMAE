package tn.esprit.ticketmaeassurrance.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;
import tn.esprit.ticketmaeassurrance.Dto.InterventionDto;
import tn.esprit.ticketmaeassurrance.Dto.TicketStatistics;
import tn.esprit.ticketmaeassurrance.entities.*;
import tn.esprit.ticketmaeassurrance.repositories.InterventionRepository;
import tn.esprit.ticketmaeassurrance.repositories.TicketRepository;
import tn.esprit.ticketmaeassurrance.repositories.UserRepository;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service

@Slf4j
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
       // String mail = authentificationService.getCurrentUsername();

        User user = authentificationService.connected();
        List<Ticket> tickets = new ArrayList<Ticket>();
        if(user.getRole().equals("IT")){
            tickets = ticketRepository.findTicketsInProgress(user);
        } else if (user.getRole().equals("EMPLOYE")) {
            tickets = ticketRepository.findByEtatAndEmploye(EtatTicket.valueOf(etat.toUpperCase()),user);
        }else {
            tickets =ticketRepository.findByEtat(EtatTicket.valueOf(etat.toUpperCase()));
        }
        System.out.println("user name : "+user.getName());

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
                intervention1.setRemarque(intervention1.getRemarque()+ System.lineSeparator()+"    mon remarque personnel :  "+intervention.getRemarque());
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
    public List<InterventionDto> dtos(Long idTicket){
        Ticket ticket = ticketRepository.findById(idTicket).orElse(null);
        List<Intervention> interventions = ticket.getInterventions();
        if(interventions != null){
            //return interventions;
        }
        return null;
    }
    public InterventionDto mapToDTO(Intervention intervention) {
        InterventionDto dto = new InterventionDto();
        dto.setId(intervention.getId());
        dto.setRemarque(intervention.getRemarque());
        dto.setDescription(intervention.getDescription());
        dto.setAffectation(intervention.getAffectation());
        dto.setStart(intervention.getStart());
        dto.setEnd(intervention.getEnd());

       dto.setUser(intervention.getUser());

        return dto;
    }

    public List<InterventionDto> mapToDTOList(List<Intervention> interventions) {
        if (interventions == null) {
            return Collections.emptyList();
        }
        return interventions.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    public List<InterventionDto> getInterventionsDto(Long idTicket) {
        Ticket ticket = ticketRepository.findById(idTicket).orElse(null);
        if (ticket != null) {
            List<Intervention> interventions = ticket.getInterventions();
            return this.mapToDTOList(interventions);
        }
        return Collections.emptyList(); // Retourne une liste vide si le ticket est null
    }
    public List<Ticket> findAll(){
        return ticketRepository.findAll();
    }
    public double calculateAverageClosureTime() {
        List<Ticket> closedTickets = ticketRepository.findByEtat(EtatTicket.CLOSED);

        if (closedTickets.isEmpty()) {
            return 0;
        }

        long totalDuration = 0;
        for (Ticket ticket : closedTickets) {
            long duration = ticket.getDateFermeture().getTime() - ticket.getDateOuverture().getTime();
            totalDuration += duration;
        }

        long averageDuration =  totalDuration / closedTickets.size();
        return TimeUnit.MILLISECONDS.toHours(averageDuration); // convert to hours or any desired unit
    }
    public Map<String, Double> calculateAverageClosureTimePerDay() {
        List<Ticket> closedTickets = ticketRepository.findByEtat(EtatTicket.CLOSED);

        if (closedTickets.isEmpty()) {
            return Collections.emptyMap();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, List<Ticket>> ticketsByDay = closedTickets.stream()
                .collect(Collectors.groupingBy(ticket -> sdf.format(ticket.getDateOuverture())));
        //test de premier map
        System.out.println("tickets per day :" + ticketsByDay.toString());
        Map<String, Double> averageClosureTimePerDay = new HashMap<>();
        for (Map.Entry<String, List<Ticket>> entry : ticketsByDay.entrySet()) {
            String day = entry.getKey();
            List<Ticket> tickets = entry.getValue();
            long totalDuration = 0;
            for (Ticket ticket : tickets) {
                long duration = ticket.getDateFermeture().getTime() - ticket.getDateOuverture().getTime();
                totalDuration += duration;
            }
            double averageDuration = (double) totalDuration / tickets.size();
            double averageDurationInHours = TimeUnit.MILLISECONDS.toHours((long) averageDuration) +
                    ((double) (TimeUnit.MILLISECONDS.toMinutes((long) averageDuration) % 60) / 60);
            averageClosureTimePerDay.put(day, averageDurationInHours);
        }

        return averageClosureTimePerDay;
    }
    public Map<String, Double> calculateAverageInterventionTimePerDay(Long userId) {
        // Fetch interventions for the specific user
        User user  = userRepository.findById(userId).orElse(null);
        List<Intervention> interventions = user.getInterventions();

        if (interventions.isEmpty()) {
            return Collections.emptyMap();
        }
         List<Intervention> i2 = interventions.stream().filter(intervention -> intervention.getStart()!=null).toList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // Group interventions by date
        Map<String, List<Intervention>> interventionsByDay = i2.stream()
                .collect(Collectors.groupingBy(intervention -> sdf.format(intervention.getStart())));

        // Print the grouped interventions for debugging
        System.out.println("interventions per day :" + interventionsByDay.toString());

        // Calculate average intervention duration per day
        Map<String, Double> averageInterventionTimePerDay = new HashMap<>();
        for (Map.Entry<String, List<Intervention>> entry : interventionsByDay.entrySet()) {
            String day = entry.getKey();
            List<Intervention> dailyInterventions = entry.getValue();
            long totalDuration = 0;

            for (Intervention intervention : dailyInterventions) {
                // Ensure start and end are not null
                if (intervention.getStart() != null && intervention.getEnd() != null) {
                    long duration = intervention.getEnd().getTime() - intervention.getStart().getTime();
                    totalDuration += duration;
                }
            }

            if (!dailyInterventions.isEmpty()) {
                double averageDuration = (double) totalDuration / dailyInterventions.size();
                double averageDurationInHours = TimeUnit.MILLISECONDS.toMinutes((long) averageDuration);
                averageInterventionTimePerDay.put(day, averageDurationInHours);
            }
        }

        return averageInterventionTimePerDay;
    }
    public TicketStatistics getTicketStatistics() {
        List<Ticket> tickets = ticketRepository.findAll();
        List<Ticket> openTickets = ticketRepository.findByEtat(EtatTicket.CLOSED);
        List<Intervention> interventions = interventionRepository.findAll();

        int totalTickets = tickets.size();

        double averageTicketTime = tickets.stream()
                .mapToDouble(ticket -> {
                    if (ticket.getDateOuverture() != null && ticket.getDateFermeture() != null) {
                        LocalDateTime dateOuverture = toLocalDateTime(ticket.getDateOuverture());
                        LocalDateTime dateFermeture = toLocalDateTime(ticket.getDateFermeture());
                        return Duration.between(dateOuverture, dateFermeture).toMinutes();
                    } else {
                        return 0;
                    }
                })
                .average()
                .orElse(0.0);

        int totalInterventions = interventions.size();
        double totalInterventionTime = interventions.stream()
                .mapToDouble(intervention -> {
                    if (intervention.getStart() != null && intervention.getEnd() != null) {
                        LocalDateTime start = toLocalDateTime(intervention.getStart());
                        LocalDateTime end = toLocalDateTime(intervention.getEnd());
                        return Duration.between(start, end).toMinutes();
                    } else {
                        return 0;
                    }
                })
                .average()
                .orElse(0.0);

        double averageInterventionsPerTicket = openTickets.stream()
                .mapToInt(ticket -> ticket.getInterventions().size())
                .average()
                .orElse(0.0);

        TicketStatistics stats = new TicketStatistics(totalTickets, averageTicketTime, totalInterventions, totalInterventionTime, averageInterventionsPerTicket);
        log.info("stats : {}", stats);
        return stats;
    }

    private LocalDateTime toLocalDateTime(java.util.Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private LocalDateTime toLocalDateTime(java.sql.Timestamp timestamp) {
        return timestamp.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    public Map<TypePanne, Double> calculatePercentageTimeSpentPerPanneType() {
        List<Ticket> tickets = ticketRepository.findByEtat(EtatTicket.CLOSED);
        Logger logger = Logger.getLogger(TicketServiceImpl.class.getName());

        Map<TypePanne, Long> timeSpentPerPanneType = new HashMap<>();

        for (Ticket ticket : tickets) {
            if (ticket.getTicketType() == TicketType.PANNE) {
                TypePanne typePanne = ticket.getPanne().getTypePanne();
                long timeSpent = ticket.getDateFermeture().getTime() - ticket.getDateOuverture().getTime();
                timeSpentPerPanneType.put(typePanne, timeSpentPerPanneType.getOrDefault(typePanne, 0L) + timeSpent);
            }
        }

        long totalTimeSpent = timeSpentPerPanneType.values().stream().mapToLong(Long::longValue).sum();
        Map<TypePanne, Double> percentageTimeSpentPerPanneType = new HashMap<>();

        for (Map.Entry<TypePanne, Long> entry : timeSpentPerPanneType.entrySet()) {
            double percentage = (totalTimeSpent > 0) ? ((double) entry.getValue() / totalTimeSpent * 100) : 0;
            percentageTimeSpentPerPanneType.put(entry.getKey(), percentage);
        }

        logger.info("Percentage time spent per panne type: " + percentageTimeSpentPerPanneType);
        return percentageTimeSpentPerPanneType;
    }
    public List<Ticket> myitTicket(Long idUser){
        User user = userRepository.findById(idUser).orElse(null);
        return  ticketRepository.finditTickets(user);
    }
  public Ticket updateticket(Long id,String description,String etat){
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        User user =authentificationService.connected();
        String adminName =user.getFirstName()+" "+user.getLastName();
        if (ticket!=null){
             List<Intervention> interventions = ticket.getInterventions();
            if(EtatTicket.valueOf(etat)==EtatTicket.TO_DO){
                ticket.setDateOuverture(null);
                ticket.setDateFermeture(null);
            } else if (EtatTicket.valueOf(etat)==EtatTicket.IN_PROGRESS) {
                ticket.setDateFermeture(null);
            } else if (EtatTicket.valueOf(etat)==EtatTicket.CLOSED &&ticket.getEtat()!=EtatTicket.CLOSED) {
                ticket.setDateFermeture(new Date());
                for (Intervention i : interventions){
                    if(i.getEnd()==null && i.getStart()!=null){
                        i.setDescription("fermé par l'admin : "+adminName);
                        i.setEnd(new Date());
                    } else if (i.getStart()==null ) {
                        i.setRemarque("fermé par l'admin : "+adminName);
                        i.setDescription("fermé par l'admin : "+adminName);
                        i.setStart(new Date());
                        i.setEnd(new Date());
                    }
                    interventionRepository.save(i);
                }
            }
            ticket.setEtat(EtatTicket.valueOf(etat));
            ticket.setDescription(description);
            return ticketRepository.save(ticket);
        }
        return null;
  }
  public List<Ticket> ticketsdeclared(Long id){
        User user = userRepository.findById(id).orElse(null);
        if(user!=null){
            return user.getTicketDeclare().stream().toList();
        }
        return null;
  }
  public Intervention affecterintervention(Intervention intervention,Long idTicket,Long idUser){
        User user =authentificationService.connected();
        User user1 = userRepository.findById(idUser).orElse(null);
  Ticket ticket =ticketRepository.findById(idTicket).orElse(null);
        if(intervention!=null && ticket!=null && user1 !=null){
            if (ticket.getEtat()!=EtatTicket.IN_PROGRESS){
                ticket.setEtat(EtatTicket.IN_PROGRESS);
                ticket.setDateFermeture(null);
                if (ticket.getEtat()!=EtatTicket.TO_DO){
                    ticket.setDateOuverture(new Date());
                }

            }
            intervention.setUser(user1);
            intervention.setAffectation(new Date());
            intervention.setRemarque(" affecté par "+user.getFirstName()+" : "+intervention.getRemarque());
            interventionRepository.save(intervention);
            ticket.addIntervention(intervention);
            ticketRepository.save(ticket);
            return intervention;

        }
        return null;
  }
    public Map<String, Long> getPannesParMachineCetteAnnee() {
        Date startOfYear = new Date(); // Vous devez ajuster cette date pour le début de l'année en cours
        startOfYear.setMonth(0); // Janvier
        startOfYear.setDate(1); // 1er jour du mois

        Date endOfYear = new Date(); // Vous devez ajuster cette date pour la fin de l'année en cours
        endOfYear.setMonth(11); // Décembre
        endOfYear.setDate(31); // Dernier jour du mois

        List<Ticket> tickets = ticketRepository.findByDatePublicationBetween(startOfYear, endOfYear);

        return tickets.stream() .filter(ticket -> ticket.getTicketType() == TicketType.PANNE && ticket.getMachine() != null  )
                .collect(Collectors.groupingBy(ticket -> ticket.getMachine().getName(), Collectors.counting()));
    }
}
