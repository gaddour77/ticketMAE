package tn.esprit.ticketmaeassurrance.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.ticketmaeassurrance.Config.ApplicationAuditAware;
import tn.esprit.ticketmaeassurrance.auth.AuthenticationRequest;
import tn.esprit.ticketmaeassurrance.auth.AuthenticationResponse;
import tn.esprit.ticketmaeassurrance.auth.RegistrationRequest;
import tn.esprit.ticketmaeassurrance.entities.Token;
import tn.esprit.ticketmaeassurrance.entities.TokenType;
import tn.esprit.ticketmaeassurrance.entities.User;
import tn.esprit.ticketmaeassurrance.repositories.TokenRepository;
import tn.esprit.ticketmaeassurrance.repositories.UserRepository;
import tn.esprit.ticketmaeassurrance.security.JwtService;
import tn.esprit.ticketmaeassurrance.security.PasswordGenerator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AuthentificationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EMailSender mailSender;

    public AuthenticationResponse register(RegistrationRequest request) {
        //password generé automatique pour l'envoi
       // String generatedPassword = PasswordGenerator.generatePassword();
         // this.mailSender.sendMail(request.getEmail(),"your credentiels in mae ticketing plateforme","bonjour "+request.getLastname()+"votre mot de passe de notre plateforme est : "+request.getPassword());
        var user = User.builder()
        .firstName(request.getFirstname())
        .lastName(request.getLastname())
        .email(request.getEmail()).role(request.getRole())
        .password(passwordEncoder.encode(request.getPassword()))
        .build();

        var savedUser = userRepository.save(user);
        this.mailSender.sendMail(request.getEmail(),"your credentiels in mae ticketing plateforme","bonjour "+request.getLastname()+"votre mot de passe de notre plateforme est : "+request.getPassword());

        //a configurer ki iji si moez
        //mailSender.sendMail(user.getEmail(),"votre compte a été crée  ","votre mot de passe actuelle est "+generatedPassword+"s'il vous plait changer votre mot de passe ");
        var jwtToken = jwtService.generateToken((UserDetails) user);
        var refreshToken = jwtService.generateRefreshToken((UserDetails) user);
       // profileServices.createProfileForUser(savedUser);
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow();

            // Check if the user is banned
         /*   if (user.isBann()) {
                return AuthenticationResponse.builder()
                        .errorMessage("You are banned. Please contact support for assistance.")
                        .build();
            }*/
            //System.out.println(user.getEmail());

            var jwtToken = jwtService.generateToken((UserDetails) user);
            var refreshToken = jwtService.generateRefreshToken((UserDetails) user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (DisabledException e) {
            return AuthenticationResponse.builder()
                    .errorMessage("User is banned")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return AuthenticationResponse.builder()
                    .errorMessage("Authentication failed. Please check your credentials.")
                    .build();
        }
    }
    private void saveUserToken(tn.esprit.ticketmaeassurrance.entities.User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(Math.toIntExact(user.getIdUser()));
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, (UserDetails) user)) {
                var accessToken = jwtService.generateToken((UserDetails) user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .token(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
    private String generateValidationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Generate 6-digit code
        return String.valueOf(code);
    }
    public void sendValidation(String mail){
      User user = userRepository.findByEmail(mail).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " +mail));
      if(user!=null){
          String validation = generateValidationCode();
          user.setValidationCode(validation);
          user.setValidationCodeTimestamp(LocalDateTime.now());
          userRepository.save(user);
          mailSender.sendMail(user.getEmail(),"Validation Code to reset password","your validation code is : "+validation+" the code is valid only for 10 minutes");
      }
    }
    public boolean isCodeValid(String mail, String submittedCode) {
        User user = userRepository.findByEmail(mail).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " +mail));
        if(user!=null){
            String userCode = user.getValidationCode();
            LocalDateTime codeTimestamp = user.getValidationCodeTimestamp();
            return userCode != null && userCode.equals(submittedCode) &&
                    codeTimestamp != null && codeTimestamp.isAfter(LocalDateTime.now().minusMinutes(10)); // 10-minute validity
        }
       return false;
    }
    public User resetPassword(String mail , String password){
        User user = userRepository.findByEmail(mail).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " +mail));
         if (user !=null){
             user.setPassword(passwordEncoder.encode(password));
             user.setValidationCodeTimestamp(null);
             user.setValidationCode(null);
             return userRepository.save(user);
         }
        return null;
    }
    public User connected(){

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

        String mail = loggedInUser.getName().toString();

        User user = userRepository.findByEmail(mail)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        return user;
    }
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        } else {
            return "AnonymousUser"; // or handle as per your application logic
        }
    }
}
