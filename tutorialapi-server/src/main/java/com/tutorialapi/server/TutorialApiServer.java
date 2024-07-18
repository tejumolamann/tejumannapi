package com.tutorialapi.server;

import com.tutorialapi.rest.ApiApplication;
import com.tutorialapi.server.config.ConfigKey;
import com.tutorialapi.server.config.SystemKey;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class TutorialApiServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TutorialApiServer.class);

    private static final String ROOT_CONTEXT = "/";
    private static final String API_PATTERN = "api/*";
    private static final String APPLICATION_INIT_PARAM_KEY = "jakarta.ws.rs.Application";

    private static Server createJettyServer (int port, Config config) throws MalformedURLException {
        HttpConfiguration httpsConfiguration =  new HttpConfiguration();
        httpsConfiguration.setSecureScheme(HttpScheme.HTTPS.asString());
        httpsConfiguration.setSecurePort(port);
        httpsConfiguration.addCustomizer(new SecureRequestCustomizer());
        httpsConfiguration.setSendServerVersion(false);
        httpsConfiguration.setSendDateHeader(false);

        HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpsConfiguration);

        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(config.getString(ConfigKey.SERVER_KEYSTORE_FILE.getKey()));
        sslContextFactory.setKeyStoreType(config.getString(ConfigKey.SERVER_KEYSTORE_TYPE.getKey()));
        sslContextFactory.setKeyStorePassword(config.getString(ConfigKey.SERVER_KEYSTORE_PASSWORD.getKey()));
        sslContextFactory.setKeyManagerPassword(config.getString(ConfigKey.SERVER_KEYSTORE_PASSWORD.getKey()));
        sslContextFactory.setTrustAll(true);

        SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString());

        Server server = new Server();

        ServerConnector httpsConnector = new ServerConnector(server, sslConnectionFactory, httpConnectionFactory);
        httpsConnector.setPort(httpsConfiguration.getSecurePort());

        server.addConnector(httpsConnector);

        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        servletContextHandler.setContextPath("/");
        servletContextHandler.setBaseResource(
                new URLResourceFactory().newResource(
                        new File(config.getString(ConfigKey.SERVER_WEB_CONTENT.getKey())).toURI().toURL()
                )
        );
        servletContextHandler.addServlet(DefaultServlet.class, ROOT_CONTEXT);

        server.setHandler(servletContextHandler);

        ServletHolder apiServletHolder = servletContextHandler.addServlet(ServletContainer.class, API_PATTERN);
        apiServletHolder.setInitParameter(APPLICATION_INIT_PARAM_KEY, ApiApplication.class.getName());

        return server;
    }

    public static void main(String... args) throws Exception {
        int port = Integer.parseInt(
                Optional.ofNullable(
                        System.getProperty(
                                SystemKey.PORT.getKey()
                        )
                ).orElse(
                        SystemKey.PORT.getDefaultValue()
                )
        );

        String mode = Optional.ofNullable(
                System.getProperty(
                        SystemKey.MODE.getKey()
                )
        ).orElse(SystemKey.MODE.getDefaultValue());

        String url = String.format("https://raw.githubusercontent.com/tejumolamann/tejumannapi/main/system-%s.properties", mode);
        Config config = ConfigFactory.parseURL(new URL(url));

        Server server = createJettyServer(port, config);

        LOGGER.info("Server starting on port: {}", port);
        server.start();
        server.join();
    }
}
