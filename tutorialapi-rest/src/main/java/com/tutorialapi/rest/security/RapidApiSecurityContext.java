package com.tutorialapi.rest.security;

import com.tutorialapi.model.RapidApiPrincipal;
import com.tutorialapi.model.Subscription;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;

public class RapidApiSecurityContext implements SecurityContext {
    private static final String AUTHENTICATION_SCHEME = "RapidApi";
    private final RapidApiPrincipal principal;

    public RapidApiSecurityContext(RapidApiPrincipal principal) {
        this.principal = principal;
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isUserInRole(String s) {
        return principal.getSubscription() == Subscription.from(s).orElse(null);
    }

    @Override
    public boolean isSecure() {
        return true;
    }

    @Override
    public String getAuthenticationScheme() {
        return AUTHENTICATION_SCHEME;
    }
}
