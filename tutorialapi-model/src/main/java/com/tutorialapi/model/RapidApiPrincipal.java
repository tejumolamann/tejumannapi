package com.tutorialapi.model;

import java.security.Principal;
import java.util.Objects;

public class RapidApiPrincipal implements Principal {
    private final String user;
    private final Subscription subscription;
    private final String proxySecret;

    public RapidApiPrincipal(String user, Subscription subscription, String proxySecret) {
        this.user = user;
        this.subscription = subscription;
        this.proxySecret = proxySecret;
    }

    public String getProxySecret() {
        return proxySecret;
    }

    public String getUser() {
        return user;
    }

    public Subscription getSubscription() {
        return subscription;
    }

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
    public int hashCode() {
        return Objects.hash(user, subscription, proxySecret);
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
