package tn.esprit.ticketmaeassurrance.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ticketmaeassurrance.entities.Ticket;
import tn.esprit.ticketmaeassurrance.services.TicketServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Ticket")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class TicketController {
    private final TicketServiceImpl ticketService;
    @PostMapping("/addticket")
    public ResponseEntity<Ticket> addTicket(@RequestBody Ticket ticket){
        return ResponseEntity.ok(ticketService.addTicket(ticket));
    }
}
