package com.tutorialapi.rest.resource;

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
        return new ResourceConfig(TestResource.class);
    }

    @Test
    void test() {
        Response response = target("/test").request().get();

        Assertions.assertEquals(200, response.getStatus());
    }
}
