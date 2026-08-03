package com.facundo.assistentia.interfaces.desktop;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

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
        } catch (Exception exception) {
            throw new IllegalStateException("No se pudo inicializar el aspecto visual del sistema.", exception);
        }
    }
}