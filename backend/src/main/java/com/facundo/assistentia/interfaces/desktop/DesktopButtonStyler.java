package com.facundo.assistentia.interfaces.desktop;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

public final class DesktopButtonStyler {

    private DesktopButtonStyler() {
    }

    public static void stylePrimary(JButton button, Color background, Color foreground, int fontSize) {
        configure(button, background, foreground, fontSize, BorderFactory.createEmptyBorder(10, 14, 10, 14));
    }

    public static void styleSecondary(JButton button, Color background, Color foreground, Color borderColor, int fontSize) {
        configure(button, background, foreground, fontSize, BorderFactory.createLineBorder(borderColor));
    }

    public static void styleNavigation(JButton button, Color background, Color foreground, int fontSize) {
        button.setAlignmentX(javax.swing.JComponent.LEFT_ALIGNMENT);
        button.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        configure(button, background, foreground, fontSize, BorderFactory.createEmptyBorder(12, 24, 12, 24));
    }

    private static void configure(JButton button, Color background, Color foreground, int fontSize, javax.swing.border.Border border) {
        button.setUI(new BasicButtonUI());
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setBorder(border);
        button.putClientProperty("JComponent.roundRect", Boolean.TRUE);
        UIManager.put("Button.select", background.darker());
    }
}