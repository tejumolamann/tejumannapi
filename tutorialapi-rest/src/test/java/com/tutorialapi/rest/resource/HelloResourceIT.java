package com.tutorialapi.rest.resource;

import com.tutorialapi.model.Subscription;
import com.tutorialapi.rest.security.AccessLogFilter;
import com.tutorialapi.rest.security.CorsFilter;
import com.tutorialapi.rest.security.SecurityFilter;
import com.tutorialapi.rest.security.SecurityHeader;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.logging.LogManager;

class HelloResourceIT extends JerseyTest {

    static {
        LogManager.getLogManager().reset();
    }

    @Override
    protected Application configure() {

        return new ResourceConfig(
                HelloResource.class,
                AccessLogFilter.class,
                CorsFilter.class,
                SecurityFilter.class
        );
    }

    @Test
    void testNoSecurityHeaders() {
        Response response = target("/test").request().get();

        Assertions.assertEquals(401, response.getStatus());
        Assertions.assertEquals("", response.readEntity(String.class));
    }

    @Test
    void testOnlyProxySecretHeader() {
        Response response = target("/test").request().header(SecurityHeader.RAPID_API_PROXY_SECRET.getHeader(), "proxy-secret" ).get();

        Assertions.assertEquals(401, response.getStatus());
        Assertions.assertEquals("", response.readEntity(String.class));
    }

    @Test
    void testOnlyProxySecretAndUserHeaders() {
        Response response = target("/test").request()
                .header(SecurityHeader.RAPID_API_PROXY_SECRET.getHeader(), "proxy-secret" )
                .header(SecurityHeader.RAPID_API_USER.getHeader(), "user" )
                .get();

        Assertions.assertEquals(401, response.getStatus());
        Assertions.assertEquals("", response.readEntity(String.class));
    }

    @Test
    void testInvalidSubscription() {
        Response response = target("/test").request()
                .header(SecurityHeader.RAPID_API_PROXY_SECRET.getHeader(), "proxy-secret" )
                .header(SecurityHeader.RAPID_API_USER.getHeader(), "user" )
                .header(SecurityHeader.RAPID_API_SUBSCRIPTION.getHeader(), "INVALID" )
                .get();

        Assertions.assertEquals(401, response.getStatus());
        Assertions.assertEquals("", response.readEntity(String.class));
    }

    @Test
    void testValidHeaders() {
        Response response = target("/test").request()
                .header(SecurityHeader.RAPID_API_PROXY_SECRET.getHeader(), "proxy-secret" )
                .header(SecurityHeader.RAPID_API_USER.getHeader(), "user" )
                .header(SecurityHeader.RAPID_API_SUBSCRIPTION.getHeader(), Subscription.BASIC.name())
                .get();

        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals("Hello", response.readEntity(String.class));

        Assertions.assertEquals("*", response.getHeaderString("Access-Control-Allow-Origin"));
        Assertions.assertEquals("DELETE, HEAD, GET, OPTIONS, PATCH, POST, PUT", response.getHeaderString("Access-Control-Allow-Methods"));
    }
}
