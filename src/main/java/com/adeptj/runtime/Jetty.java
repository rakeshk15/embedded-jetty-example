package com.adeptj.runtime;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.session.DefaultSessionIdManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletHandler;

public class Jetty {

    private Server server;

    public void start() throws Exception {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});
        server.setSessionIdManager(new DefaultSessionIdManager(server));
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(TimeServlet.class, "/time");
        servletHandler.setHandler(new SessionHandler());
        server.setHandler(servletHandler);
        server.start();
        server.join();
    }

    public void stop() throws Exception {
        System.out.println("Stopping Jetty!");
        server.stop();
    }

    public static void main(String[] args) throws Exception {
        Jetty jetty = new Jetty();
        jetty.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                jetty.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}
