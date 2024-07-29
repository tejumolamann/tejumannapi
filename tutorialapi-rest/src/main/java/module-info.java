module tutorialapi.rest {
    exports com.tutorialapi.rest;
    exports com.tutorialapi.rest.resource;
    requires jakarta.ws.rs;
    requires jersey.server;
    requires jersey.common;
    requires org.glassfish.hk2.api;
    requires org.slf4j;
    requires java.logging;

    opens com.tutorialapi.rest to jersey.server;
    opens com.tutorialapi.rest.resource to jersey.server;
}