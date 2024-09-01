module tutorialapi.rest {
    exports com.tutorialapi.rest;
    exports com.tutorialapi.rest.security;
    exports com.tutorialapi.rest.resource;

    requires jakarta.ws.rs;
    requires jersey.server;
    requires jersey.common;
    requires jersey.client;
    requires jersey.hk2;
    requires org.glassfish.jaxb.runtime;
    requires org.glassfish.hk2.api;
    requires org.slf4j;
    requires java.logging;
    requires tutorialapi.model;
}