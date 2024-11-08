package tn.esprit.ticketmaeassurrance.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.ticketmaeassurrance.entities.Ticket;
import tn.esprit.ticketmaeassurrance.entities.User;
import tn.esprit.ticketmaeassurrance.services.AuthentificationService;
import tn.esprit.ticketmaeassurrance.services.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;
    private final AuthentificationService authentificationService;
    @GetMapping("/mytickets")
    public Set<Ticket> mytickets(){
        return userService.mytickets();
    }
    @GetMapping("/itemploye")
    public List<User> getAllIt(){
        return userService.gettAll();
    }
    @GetMapping("/currentuser")
    public User currentUser(){
        return authentificationService.connected();
    }
}
