package tn.esprit.ticketmaeassurrance.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ITEmployeeInterventionsDTO {
    private Long employeeId;
    private String employeeName;
    private Long interventionCount;
    private Double averageTimeSpent;




}
