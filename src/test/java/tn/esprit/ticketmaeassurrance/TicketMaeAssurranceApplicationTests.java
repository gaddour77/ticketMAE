package tn.esprit.ticketmaeassurrance;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.ticketmaeassurrance.entities.TypePanne;
import tn.esprit.ticketmaeassurrance.services.TicketServiceImpl;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AllArgsConstructor
class TicketMaeAssurranceApplicationTests {
     @Autowired
     private final TicketServiceImpl ticketService;


    @Test
    public void testCalculatePercentageTimeSpentPerPanneType() {
        Map<TypePanne, Double> result = ticketService.calculatePercentageTimeSpentPerPanneType();
        System.out.println(result);
        assertNotNull(result, "Result map should not be null");
        assertTrue(!result.isEmpty(), "Result map should not be empty");
        // Add additional assertions here based on expected results
    }
    @Test
    void contextLoads() {
    }

}
