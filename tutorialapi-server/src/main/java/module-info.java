module tutorialapi.server {
    requires jersey.container.servlet.core;
    requires org.eclipse.jetty.ee10.servlet;
    requires org.eclipse.jetty.http;
    requires org.eclipse.jetty.server;
    requires org.eclipse.jetty.util;
    requires org.slf4j;
    requires tutorialapi.rest;
    requires typesafe.config;
}