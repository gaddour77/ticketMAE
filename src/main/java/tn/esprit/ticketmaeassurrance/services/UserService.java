package tn.esprit.ticketmaeassurrance.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.ticketmaeassurrance.entities.Role;
import tn.esprit.ticketmaeassurrance.entities.Ticket;
import tn.esprit.ticketmaeassurrance.entities.User;
import tn.esprit.ticketmaeassurrance.repositories.UserRepository;

import java.net.Proxy;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final AuthentificationService authentificationService;
    @Override
    public Set<Ticket> mytickets() {
        String mail = authentificationService.getCurrentUsername();
        User user = userRepository.findByEmail(mail).orElse(null);
        if (user!=null){
            Set<Ticket> myticket = user.getTicketDeclare();
            return myticket;
        }
        return null;
    }

    @Override
    public List<User> gettAll() {
        List<User> users = userRepository.findAllByRole(Role.IT);
        return users;
    }

}
