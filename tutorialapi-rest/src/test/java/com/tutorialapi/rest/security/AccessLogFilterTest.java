package com.tutorialapi.rest.security;

import com.tutorialapi.model.RapidApiPrincipal;
import com.tutorialapi.model.Subscription;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

class AccessLogFilterTest {
    @Test
    public void testFilterNoUser() {
        List<String> logList = new ArrayList<>();
        AccessLogFilter accessLogFilter = new AccessLogFilter(logList::add);

        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(uriInfo.getAbsolutePath()).thenReturn(URI.create("https://localhost/test"));
        ContainerRequestContext containerRequestContext = Mockito.mock(ContainerRequestContext.class);
        Mockito.when(containerRequestContext.getMethod()).thenReturn("GET");
        Mockito.when(containerRequestContext.getUriInfo()).thenReturn(uriInfo);
        accessLogFilter.filter(containerRequestContext);

        Assertions.assertEquals(1, logList.size());
        Assertions.assertEquals("? => GET /test", logList.get(0));
    }

    @Test
    public void testFilterWithUser() {
        List<String> logList = new ArrayList<>();
        AccessLogFilter accessLogFilter = new AccessLogFilter(logList::add);

        Principal principal = new RapidApiPrincipal("user", Subscription.BASIC, "proxy-secret");
        RapidApiSecurityContext rapidApiSecurityContext = Mockito.mock(RapidApiSecurityContext.class);
        Mockito.when(rapidApiSecurityContext.getUserPrincipal()).thenReturn(principal);
        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(uriInfo.getAbsolutePath()).thenReturn(URI.create("https://localhost/test"));
        ContainerRequestContext containerRequestContext = Mockito.mock(ContainerRequestContext.class);
        Mockito.when(containerRequestContext.getSecurityContext()).thenReturn(rapidApiSecurityContext);
        Mockito.when(containerRequestContext.getMethod()).thenReturn("GET");
        Mockito.when(containerRequestContext.getUriInfo()).thenReturn(uriInfo);
        accessLogFilter.filter(containerRequestContext);

        Assertions.assertEquals(1, logList.size());
        Assertions.assertEquals("user => GET /test", logList.get(0));
    }
}