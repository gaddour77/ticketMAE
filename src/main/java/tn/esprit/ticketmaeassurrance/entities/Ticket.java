package tn.esprit.ticketmaeassurrance.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;
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
    @ManyToOne
    private Machine machine;

    @ManyToOne(cascade=CascadeType.MERGE)
    private Panne panne;
    @JsonIgnore
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<Intervention> interventions;
    public void addIntervention(Intervention intervention) {
        interventions.add(intervention);
    }


}
