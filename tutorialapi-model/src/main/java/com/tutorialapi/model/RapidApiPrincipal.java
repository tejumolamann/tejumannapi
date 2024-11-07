package com.tutorialapi.model;

import java.security.Principal;
import java.util.Objects;

public record RapidApiPrincipal(String user, Subscription subscription, String proxySecret) implements Principal {

    @Override
    public String getName() {
        return user;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RapidApiPrincipal that = (RapidApiPrincipal) obj;
        return Objects.equals(user, that.user) && Objects.equals(proxySecret, that.proxySecret) && subscription == that.subscription;
    }

    @Override
    public String toString() {
        return "RapidApiPrincipal{" +
                "user='" + user + '\'' +
                ", subscription=" + subscription + '\'' +
                ", proxySecret=" + proxySecret +
                '}';
    }
}
