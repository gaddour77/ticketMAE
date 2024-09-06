package tn.esprit.ticketmaeassurrance.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ticketmaeassurrance.Dto.ITEmployeeInterventionsDTO;
import tn.esprit.ticketmaeassurrance.Dto.TicketStatistics;
import tn.esprit.ticketmaeassurrance.entities.*;
import tn.esprit.ticketmaeassurrance.services.*;

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
    private final PanneServiceImpl panneService;
    private final MachineService machineService;
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
    @GetMapping("/gettallusers")
    public List<User> allUsers(){
        return userService.allusers();
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
    @PutMapping("/updateuser")
    public User upDateUser(@RequestBody User user){
        return userService.upDateUser(user);
    }
    @PostMapping("/ajouterpanne")
    public ResponseEntity<Panne> addPanne(@RequestBody  Panne panne){
        return ResponseEntity.ok(panneService.addPanne(panne));
    }
    @GetMapping("/timespentperpannetype")
    public Map<TypePanne, Double> getTimeSpentPerPanneType() {
        return ticketService.calculatePercentageTimeSpentPerPanneType();
    }
    @GetMapping("/findinterventionbyuser/{id}")
    public List<Intervention> interventionsById(@PathVariable Long id){
        return interventionService.interventionsById(id);
    }
    @PutMapping("/updateticket")
    public Ticket updateticket(
           @RequestParam("idTicket") Long idTicket,@RequestParam("description") String description, @RequestParam("etat")String etat){
        return ticketService.updateticket(idTicket,description,etat);
    }
    @GetMapping("/ticketdeclared/{id}")
    public List<Ticket> ticketsdeclared(@PathVariable Long id){
        return ticketService.ticketsdeclared(id);
    }
    @PutMapping("/affecterintervention")
    public Intervention affecterintervention(@RequestBody(required = false) Intervention intervention, @RequestParam("idTicket")Long idTicket, @RequestParam("idUser")Long idUser){
        return ticketService.affecterintervention(intervention,idTicket,idUser);
    }
    @PostMapping("/machine/addmachine")
    public Machine saveMachine(@RequestBody Machine machine){
        return machineService.saveMachine(machine);
    }
    @GetMapping("/machine/findall")
    public List<Machine> getall(){
        return machineService.getall();
    }
    @GetMapping("/pannesparmachine")
    public Map<String, Long> getPannesParMachineCetteAnnee(){
        return ticketService.getPannesParMachineCetteAnnee();
    }
    @PutMapping("/bann/{id}")
    public User delete(@PathVariable Long id){
        return userService.bann(id);
    }
}
