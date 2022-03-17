package com.github.bombing;

import com.github.bombing.config.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @projectName: SMS-bombing
 * @className: App
 * @description:
 * @version: 1.0
 */
public class App {

    private static final String LOGGING_CONFIG_FILE_KEY = "java.util.logging.config.file";

    static {
        init();
    }

    private static void init() {
        String userHomePath = System.getProperty("user.home");
        if (userHomePath != null) {
            String logFilePath = userHomePath + File.separator + "logging.properties";
            File logFile = new File(logFilePath);
            if (logFile.exists()) {
                setProperty(LOGGING_CONFIG_FILE_KEY, logFilePath);
            } else {
                File tmpdir = new File(userHomePath);
                if (tmpdir.exists()) {
                    if (!logFile.exists()) {
                        InputStream is = App.class.getClassLoader().getResourceAsStream("logging.properties");
                        if (is == null) {
                            return;
                        }
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
                            reader.lines().forEach(l -> {
                                try {
                                    writer.write(l + "\n");
                                } catch (IOException ignored) {
                                }
                            });
                            writer.flush();
                            writer.close();
                            setProperty(LOGGING_CONFIG_FILE_KEY, logFilePath);
                        } catch (IOException ignored) {
                        }
                    }
                }
            }
        }
    }

    private static void setProperty(String key, String value) {
        System.setProperty(key, value);
    }

    public static void main(String[] args) {
        Config.initConfig(args);

        BombingExecutor executor = new BombingExecutor();
        executor.exec();
    }

}
