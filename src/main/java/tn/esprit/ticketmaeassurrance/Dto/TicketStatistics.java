package tn.esprit.ticketmaeassurrance.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TicketStatistics {
    private int totalTickets;
    private double averageTicketTime;
    private int totalInterventions;
    private double totalInterventionTime;
    private double averageInterventionsPerTicket;


}
