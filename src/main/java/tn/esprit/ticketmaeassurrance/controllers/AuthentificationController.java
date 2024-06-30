package tn.esprit.ticketmaeassurrance.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ticketmaeassurrance.auth.AuthenticationRequest;
import tn.esprit.ticketmaeassurrance.auth.AuthenticationResponse;
import tn.esprit.ticketmaeassurrance.auth.RegistrationRequest;
import tn.esprit.ticketmaeassurrance.services.AuthentificationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthentificationController {
    private final AuthentificationService authentificationService;
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<AuthenticationResponse> register (@RequestBody RegistrationRequest request){
        log.info("hello world");
        return ResponseEntity.ok(authentificationService.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authentificationService.authenticate(request));
    }
}
