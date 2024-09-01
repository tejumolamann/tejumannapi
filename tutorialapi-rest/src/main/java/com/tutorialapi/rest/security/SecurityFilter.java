package com.tutorialapi.rest.security;

import com.tutorialapi.model.RapidApiPrincipal;
import com.tutorialapi.model.Subscription;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Provider
public class SecurityFilter implements ContainerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);

    private Optional<String> getHeader (ContainerRequestContext context, String headerName) {
        return Stream.of(context.getHeaders())
                .filter(Objects::nonNull)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .filter(entry -> entry.getKey().equalsIgnoreCase(headerName))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .findFirst();
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext)  {
        Optional<String> proxySecret = getHeader(containerRequestContext, SecurityHeader.RAPID_API_PROXY_SECRET.getHeader());
        Optional<String> user = getHeader(containerRequestContext, SecurityHeader.RAPID_API_USER.getHeader());
        Optional<Subscription> subscription = getHeader(
                containerRequestContext, SecurityHeader.RAPID_API_SUBSCRIPTION.getHeader()
        ).flatMap(Subscription::from);

        String notAuthorizedExceptionMessage = "Missing or invalid security header: ";

        if (proxySecret.isEmpty()) {
            throw new NotAuthorizedException(notAuthorizedExceptionMessage + SecurityHeader.RAPID_API_PROXY_SECRET.getHeader());
        }

        if (user.isEmpty()) {
            throw new NotAuthorizedException(notAuthorizedExceptionMessage + SecurityHeader.RAPID_API_USER.getHeader());
        }

        if (subscription.isEmpty()) {
            throw new NotAuthorizedException(notAuthorizedExceptionMessage + SecurityHeader.RAPID_API_SUBSCRIPTION.getHeader());
        }

        RapidApiPrincipal principal = new RapidApiPrincipal(user.get(), subscription.get(), proxySecret.get());
        LOGGER.info("User Principal: {}", principal);
        containerRequestContext.setSecurityContext(new RapidApiSecurityContext(principal));
    }
}
