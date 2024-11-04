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

    @Test
    void contextLoads() {
    }

}
