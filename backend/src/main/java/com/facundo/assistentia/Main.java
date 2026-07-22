package com.facundo.assistentia;

import com.facundo.assistentia.interfaces.desktop.DesktopLoginFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.GraphicsEnvironment;

import javax.swing.SwingUtilities;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            AssistentiaBackendApplication.main(args);
            return;
        }

        System.setProperty("server.port", "0");
        System.setProperty("app.open-browser", "false");

        ConfigurableApplicationContext applicationContext = SpringApplication.run(AssistentiaBackendApplication.class, args);
        SwingUtilities.invokeLater(() -> DesktopLoginFrame.open(applicationContext));
    }
}
