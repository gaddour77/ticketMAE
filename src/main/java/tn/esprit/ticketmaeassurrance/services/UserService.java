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
    private final TicketServiceImpl ticketService;
    private final InterventionServiceImpl interventionService;
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
    public User upDateUser(User user){
        User currentuser = userRepository.findById(user.getIdUser()).orElse(null);
        if(currentuser!=null){
            currentuser.setEmail(user.getEmail());
            currentuser.setFirstName(user.getFirstName());
            currentuser.setLastName(user.getLastName());
            currentuser.setPhone(user.getPhone());
            currentuser.setRole(user.getRole());
            //currentuser.setPassword(user.getPassword());
        return userRepository.save(currentuser);
        }
        return null;
    }
    public List<User> allusers(){
        return userRepository.findAll();
    }
    public String delete(Long id ){
        User user = userRepository.findById(id).orElse(null);
        if(user.getIdUser()!=3 || user.getEmail()!="kaouther.kanzari@mae.tn"){
            userRepository.delete(user);
            return "le compte de "+ user.getFirstName()+" "+user.getLastName()+" est supprimé";
        }
        return " impossible de supprimer ce compte";
    }
    public User bann(Long id){
        User user = userRepository.findById(id).orElse(null);
        if (user!= null && user.getEmail()!="kaouther.kanzari@mae.tn"){
            user.setBann(!user.isBann());
           return userRepository.save(user);

        }
        return null;
    }
}
