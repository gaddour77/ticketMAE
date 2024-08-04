package tn.esprit.ticketmaeassurrance.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.ticketmaeassurrance.entities.User;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InterventionDto {
    private Long id;
    private String remarque;
    private String description;
    private Date affectation;
    private Date start;
    private Date end;
    private User user;
}
