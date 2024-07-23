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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Intervention implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String remarque;
    private String description;
    private Date affectation;
    private Date start;
    private Date end;
    @JsonIgnore
    @ManyToOne
    private User user;
    @Override
    public String toString(){
        return "id : "+this.id+" start date : "+start;
    }

}
