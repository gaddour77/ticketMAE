package tn.esprit.ticketmaeassurrance.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static tn.esprit.ticketmaeassurrance.entities.Permission.*;

@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    EMPLOYE_READ,
                    EMPLOYE_UPDATE,
                    EMPLOYE_DELETE,
                    EMPLOYE_CREATE,
                    IT_READ,
                    IT_UPDATE,
                    IT_DELETE,
                    IT_CREATE
            )
    ),
    IT(
            Set.of(
                    IT_READ,
                    IT_UPDATE,
                    IT_DELETE,
                    IT_CREATE
            )
    );
    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
