package com.tutorialapi.rest.resource;

import com.tutorialapi.rest.security.AccessLogFilter;
import com.tutorialapi.rest.security.CorsFilter;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.logging.LogManager;

class TestResourceTest extends JerseyTest {

    static {
        LogManager.getLogManager().reset();
    }

    @Override
    protected Application configure() {

        return new ResourceConfig(
                TestResource.class,
                AccessLogFilter.class,
                CorsFilter.class
        );
    }

    @Test
    void test() {
        Response response = target("/test").request().get();

        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals("Hello", response.readEntity(String.class));

        Assertions.assertEquals("*", response.getHeaderString("Access-Control-Allow-Origin"));
        Assertions.assertEquals("DELETE, HEAD, GET, OPTIONS, PATCH, POST, PUT", response.getHeaderString("Access-Control-Allow-Methods"));
    }
}
