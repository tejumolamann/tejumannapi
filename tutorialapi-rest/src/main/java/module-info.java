module tutorialapi.rest {
    exports com.tutorialapi.rest;
    requires jakarta.ws.rs;
    requires jersey.server;
    requires jersey.common;
    requires org.glassfish.hk2.api;
    requires org.slf4j;

    opens com.tutorialapi.rest to jersey.server;
}