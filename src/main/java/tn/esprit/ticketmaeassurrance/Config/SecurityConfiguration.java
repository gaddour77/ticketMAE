package tn.esprit.ticketmaeassurrance.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import tn.esprit.ticketmaeassurrance.security.JwtFilter;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static tn.esprit.ticketmaeassurrance.entities.Role.IT;
import static tn.esprit.ticketmaeassurrance.entities.Role.ADMIN;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity

public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**","/api/v1/auth/forgot-password/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui/index.html#/**",
            "/swagger-ui.html",
            "/chat-socket/**",
            "/api/v1/auth/forgot-password/send-code",
            "api/reports/bannedUsers",
            "api/reports/unban"};
    private final JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http     .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
                    corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(Arrays.asList("*"));
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers("/video/**").permitAll()
                                .requestMatchers("/api/v1/auth").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/panne/**").permitAll()
                                .requestMatchers("/panne/getbytype").permitAll()
                                .requestMatchers("/Ticket/**").permitAll()
                                .requestMatchers("/Ticket/bytype").permitAll()
                                .requestMatchers("/Ticket/byetat").permitAll()
                                .requestMatchers("/chat-socket/**").permitAll()
                                .requestMatchers("/api/tutorials/**").permitAll()
                                .requestMatchers("/admin/machine/**").permitAll()
                                .requestMatchers("/admin/**").hasAnyAuthority(ADMIN.name())
                                .requestMatchers("/admin/pannesparmachine").hasAnyAuthority(ADMIN.name())
                                .requestMatchers("/admin/ticketstat").hasAnyAuthority(ADMIN.name())
                                .requestMatchers("/admin/ajouterpanne").hasAnyAuthority(ADMIN.name())
                                .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name())
                                .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name())
                                .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name())
                                .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name())
                                .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name())

                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )
        ;

        return http.build();
    }

}
