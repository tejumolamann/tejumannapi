package com.tutorialapi.rest.resource;

import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.LogManager;

class TestResourceTest extends JerseyTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestResourceTest.class);

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
        Assertions.assertEquals("Hello", response.readEntity(String.class));

        LOGGER.info("{}", response.getHeaders());
    }
}
