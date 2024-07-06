package tn.esprit.ticketmaeassurrance.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
   EMPLOYE_READ("AGENT:read"),
    EMPLOYE_UPDATE("AGENT:update"),
    EMPLOYE_CREATE("AGENT:create"),
    EMPLOYE_DELETE("AGENT:delete"),
    IT_READ("CLIENT:read"),
    IT_UPDATE("CLIENT:update"),
    IT_CREATE("CLIENT:create"),
    IT_DELETE("CLIENT:delete"),


    ;

    @Getter
    private final String permission;
}
