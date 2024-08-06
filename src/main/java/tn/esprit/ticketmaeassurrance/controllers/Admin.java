package tn.esprit.ticketmaeassurrance.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ticketmaeassurrance.Dto.ITEmployeeInterventionsDTO;
import tn.esprit.ticketmaeassurrance.Dto.TicketStatistics;
import tn.esprit.ticketmaeassurrance.entities.Intervention;
import tn.esprit.ticketmaeassurrance.entities.Ticket;
import tn.esprit.ticketmaeassurrance.entities.User;
import tn.esprit.ticketmaeassurrance.services.InterventionServiceImpl;
import tn.esprit.ticketmaeassurrance.services.TicketServiceImpl;
import tn.esprit.ticketmaeassurrance.services.UserService;

import java.util.List;
import java.util.Map;

@RestController

@RequestMapping("/admin")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class Admin {
    private final TicketServiceImpl ticketService;
    private final InterventionServiceImpl interventionService;
    private final UserService userService;
    @GetMapping("/alltickets")
    public List<Ticket> findAll(){return ticketService.findAll();}
    @GetMapping("/byetat")
    public List<Ticket> etat(@RequestParam String etat){
        return ticketService.getByEtat(etat);
    }
    @GetMapping("/intervention")
    public List<Intervention> allIntervention(){return interventionService.findAll();}
    @GetMapping("/it")
    public List<User> itEmploye(){return userService.gettAll();}
    @GetMapping("/closedtickettime")
    public Map<String, Double> getAverageClosureTimePerDay() {
        return ticketService.calculateAverageClosureTimePerDay();
    }
    @GetMapping("/userstat/{id}")
    public Map<String, Double> getAverageInterventionPerDay(@PathVariable Long id) {
        return ticketService.calculateAverageInterventionTimePerDay(id);
    }
    @GetMapping("/stat")
    public TicketStatistics getTicketStatistics() {
        return ticketService.getTicketStatistics();
    }
    @GetMapping("/it-employees")
    public List<ITEmployeeInterventionsDTO> getITEmployeesOrderedByInterventionCount() {
        return interventionService.getITEmployeesOrderedByInterventionCount();
    }
    @GetMapping("/itmonthstat/{userId}")
    public Map<String, Long> getInterventionCountsForUserThisMonth(@PathVariable Long userId) {
        return interventionService.getInterventionCountsForUserThisMonth(userId);
    }

}
