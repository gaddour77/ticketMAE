package tn.esprit.ticketmaeassurrance.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.persistence.FetchType.EAGER;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails , Principal{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    @Email
    private String email;
    private String password;
    private int phone;
   @Enumerated(EnumType.STRING)
   private Role role;
   @OneToMany(mappedBy = "employe",cascade = CascadeType.ALL)
   private Set<Ticket> ticketDeclare ;

    @OneToMany(mappedBy = "user",cascade = CascadeType.MERGE)
    private List<Intervention> interventions ;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getName() {
        return email;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User orElse(Object o) {
        return null ;
    }
}
