package com.movieticket.config;

import jakarta.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Resolves where the XML data files live and seeds them on first run.
 *
 * Priority:
 *   1. -Dapp.data.dir=...            (explicit override, used by the embedded server)
 *   2. &lt;project&gt;/runtime-data   (default working folder for local runs)
 *   3. &lt;project&gt;/data
 *   4. WEB-INF/data                 (used when deployed as a WAR in Tomcat)
 */
public final class AppConfig {

    private AppConfig() {}

    public static File dataDir(ServletContext ctx) {
        File dir = resolve(ctx);
        seed(dir);
        return dir;
    }

    private static File resolve(ServletContext ctx) {
        // 1. explicit override always wins
        String prop = System.getProperty("app.data.dir");
        if (prop != null && !prop.isBlank()) return new File(prop);

        String userDir = System.getProperty("user.dir");

        // 2/3. local working folders
        File runtime = new File(userDir, "runtime-data");
        if (runtime.exists()) return runtime;
        File data = new File(userDir, "data");
        if (data.exists()) return data;

        // 4. deployed inside a WAR
        if (ctx != null) {
            String real = ctx.getRealPath("/WEB-INF/data");
            if (real != null && new File(real).exists()) return new File(real);
        }

        // default: create the local working folder
        return runtime;
    }

    /** Copies the sample XML files into the data dir the first time it is used. */
    private static void seed(File dir) {
        if (!dir.exists() && !dir.mkdirs()) return;
        String[] files = {"movies.xml", "shows.xml", "bookings.xml", "users.xml"};
        for (String f : files) {
            File target = new File(dir, f);
            if (target.exists()) continue;
            File src = findSeed(f);
            if (src == null) continue;
            try {
                Files.copy(src.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ignored) { }
        }
    }

    private static File findSeed(String name) {
        String[] candidates = {
                "src/main/resources/data/" + name,
                "src/main/webapp/WEB-INF/data/" + name
        };
        for (String c : candidates) {
            File f = new File(c);
            if (f.exists()) return f;
        }
        return null;
    }
}
