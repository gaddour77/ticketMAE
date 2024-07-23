package tn.esprit.ticketmaeassurrance.controllers;

import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ticketmaeassurrance.entities.Intervention;
import tn.esprit.ticketmaeassurrance.entities.Ticket;
import tn.esprit.ticketmaeassurrance.services.TicketServiceImpl;

import java.util.List;

@RestController

@RequestMapping("/Ticket")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor

public class TicketController {
    private final TicketServiceImpl ticketService;
    @PostMapping("/addticket")
    public ResponseEntity<Ticket> addTicket(@RequestBody Ticket ticket){
        return ResponseEntity.ok(ticketService.addTicket(ticket));
    }
    @GetMapping("/byetatandemploye")
    public List<Ticket> getByEtat(@RequestParam String etat){
        return ticketService.getByEtatANDEMPLOYE(etat);
    }
    @GetMapping("/byetat")
    public List<Ticket> etat(@RequestParam String etat){
        return ticketService.getByEtat(etat);
    }
    @PutMapping("/affecter/{idticket}")
    public Ticket affecter(@PathVariable Long idticket){
        return ticketService.affecter(idticket);
    }
    @PutMapping("/desaffecter/{idticket}")
    public Ticket desaffecter(@PathVariable Long idticket){
        return ticketService.desaffecter(idticket);
    }
    @GetMapping("/donethisweek")
    public  List<Ticket> getDonethisWeek(){
        return ticketService.getDoneThisWeek();
    }
    @PutMapping("/drag-drop")
    public Ticket dragAndDropTicket(@RequestParam("previousList") String previousList,
                                    @RequestParam("currentList") String currentList,
                                    @RequestParam("idTicket") Long idTicket,
                                    @RequestBody(required = false) Intervention intervention) {
        return ticketService.dragAndDrop(previousList, currentList, idTicket,intervention);
    }
    @GetMapping("/affectedtickets")
    public List<Ticket> affectedTickets(){
        return ticketService.affectedTicket();
    }
    @GetMapping("/inprogress")
    public List<Ticket> inProgress(){
        return ticketService.inProgress();
    }
    @PutMapping("/affecterintervention")
    public Intervention affecter( @RequestParam("idTicket") Long idTicket,
                                  @RequestParam("idUser") Long idUser,
                                  @RequestBody(required = false) Intervention intervention){
        return ticketService.affecter(intervention,idTicket,idUser);
    }
    @GetMapping("/interventions/{id}")
    public List<Intervention> interventions(@PathVariable("id") Long id){
        return ticketService.interventions(id);
    }
}
