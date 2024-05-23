package com.tutorialapi.server;

import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.server.HttpConfiguration;

public class TutorialApiServer {
    public static void main(String... args) {
        System.out.println("Hello World");

        HttpConfiguration httpsConfiguration =  new HttpConfiguration();
        httpsConfiguration.setSecureScheme(HttpScheme.HTTPS.asString());
    }
}
