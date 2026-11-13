package com.movieticket.server;

import java.io.File;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

/**
 * Runs the web app on an embedded Tomcat for quick testing.
 * Usage: java -cp "out;lib/*" com.movieticket.server.EmbeddedServer
 */
public class EmbeddedServer {

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getProperty("server.port", "8080"));
        String dataDir = System.getProperty("app.data.dir");
        if (dataDir != null) {
            File dir = new File(dataDir);
            if (!dir.exists()) dir.mkdirs();
        }

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.setBaseDir(new File(System.getProperty("java.io.tmpdir"), "movieticket-tomcat").getAbsolutePath());
        // Create the HTTP connector before starting the embedded server.
        tomcat.getConnector();

        File webapp = new File("src/main/webapp");
        if (!webapp.exists()) {
            throw new IllegalStateException("src/main/webapp not found - run from project folder");
        }

        Context ctx = tomcat.addWebapp("", webapp.getAbsolutePath());
        ctx.setReloadable(false);

        tomcat.start();
        System.out.println("========================================================");
        System.out.println("  MOVIE TICKET BOOKING SYSTEM - RUNNING");
        System.out.println("  Open:   http://localhost:" + port + "/");
        System.out.println("  Data:   " + (dataDir != null ? dataDir : "WEB-INF/data"));
        System.out.println("  Stop:   press Ctrl+C in this window");
        System.out.println("========================================================");
        tomcat.getServer().await();
    }
}
