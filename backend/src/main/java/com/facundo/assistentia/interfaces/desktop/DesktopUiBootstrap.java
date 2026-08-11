package com.facundo.assistentia.interfaces.desktop;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import java.awt.Color;

public final class DesktopUiBootstrap {

    private DesktopUiBootstrap() {
    }

    public static void configure() {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Button.focusPainted", Boolean.FALSE);
            UIManager.put("Button.borderPainted", Boolean.TRUE);
            UIManager.put("Button.contentAreaFilled", Boolean.TRUE);
            UIManager.put("Button.background", new Color(30, 41, 59));
            UIManager.put("Button.foreground", new Color(226, 232, 240));
        } catch (Exception exception) {
            throw new IllegalStateException("No se pudo inicializar el aspecto visual del sistema.", exception);
        }
    }
}