package tn.esprit.ticketmaeassurrance.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;
    @Enumerated(EnumType.STRING)
    private EtatTicket etat;
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;
    private String description;
    private Date datePublication;
    private Date dateOuverture;
    private Date dateFermeture;
    private int estimation;
    @JsonIgnore
    @ManyToOne
    private User employe;
    @JsonIgnore
    @ManyToOne
    private User itEmploye;
    @ManyToOne(cascade=CascadeType.ALL)
    private Panne panne;



}
