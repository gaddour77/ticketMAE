package tn.esprit.ticketmaeassurrance.services;


import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.ticketmaeassurrance.auth.AuthenticationRequest;
import tn.esprit.ticketmaeassurrance.auth.AuthenticationResponse;
import tn.esprit.ticketmaeassurrance.auth.RegistrationRequest;
import tn.esprit.ticketmaeassurrance.entities.Token;
import tn.esprit.ticketmaeassurrance.entities.TokenType;
import tn.esprit.ticketmaeassurrance.entities.User;
import tn.esprit.ticketmaeassurrance.repositories.TokenRepository;
import tn.esprit.ticketmaeassurrance.repositories.UserRepository;
import tn.esprit.ticketmaeassurrance.security.JwtService;

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
        // Prepend "+216" to the phone number if it doesn't already start with it
      /*  String phoneNumber = request.getPhoneNumber();
        if (phoneNumber != null && !phoneNumber.startsWith("+216")) {
            phoneNumber = "+216" + phoneNumber;
        }*/

         /* var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                // Use the modified phone number
                .build();

        */
var user = User.builder()
        .firstName(request.getFirstname())
        .lastName(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        // Use the modified phone number
        .build();

        var savedUser = userRepository.save(user);
       // mailSender.sendMail(user.getEmail(),"your account of mae assurance ","hello");
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
         /*   if (user.isBanned()) {
                return AuthenticationResponse.builder()
                        .errorMessage("You are banned. Please contact support for assistance.")
                        .build();
            }*/

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
}
