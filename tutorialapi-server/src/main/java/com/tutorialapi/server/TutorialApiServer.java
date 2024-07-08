package com.tutorialapi.server;

import com.tutorialapi.rest.ApiApplication;
import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.resource.URLResourceFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class TutorialApiServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TutorialApiServer.class);

    public static void main(String... args) throws Exception {

        HttpConfiguration httpsConfiguration =  new HttpConfiguration();
        httpsConfiguration.setSecureScheme(HttpScheme.HTTPS.asString());
        httpsConfiguration.setSecurePort(8443);
        httpsConfiguration.addCustomizer(new SecureRequestCustomizer());
        httpsConfiguration.setSendServerVersion(false);
        httpsConfiguration.setSendDateHeader(false);

        HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpsConfiguration);

        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath("tutorialapi-server/src/main/resources/certs/tutorialapi.p12");
        sslContextFactory.setKeyStoreType("PKCS12");
        sslContextFactory.setKeyStorePassword("changeit");
        sslContextFactory.setKeyManagerPassword("changeit");
        sslContextFactory.setTrustAll(true);

        SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString());

        Server server = new Server();

        ServerConnector httpsConnector = new ServerConnector(server, sslConnectionFactory, httpConnectionFactory);
        httpsConnector.setName("secure");
        httpsConnector.setPort(httpsConfiguration.getSecurePort());

        server.addConnector(httpsConnector);

        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        servletContextHandler.setContextPath("/");
        servletContextHandler.setBaseResource(
                new URLResourceFactory().newResource(
                        new File("tutorialapi-server/src/main/resources/www").toURI().toURL()
                )
        );
        servletContextHandler.addServlet(DefaultServlet.class, "/");

        server.setHandler(servletContextHandler);

        ServletHolder apiServletHolder = servletContextHandler.addServlet(ServletContainer.class, "/api/*");
        apiServletHolder.setInitParameter("jakarta.ws.rs.Application", ApiApplication.class.getName());

        LOGGER.info("Server starting");
        server.start();
        server.join();
    }
}
