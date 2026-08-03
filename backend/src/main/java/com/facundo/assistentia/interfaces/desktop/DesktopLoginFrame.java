package com.facundo.assistentia.interfaces.desktop;

import com.facundo.assistentia.application.auth.service.DesktopAuthenticationService;
import com.facundo.assistentia.application.auth.service.DesktopSession;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

public final class DesktopLoginFrame extends JFrame {

    private static final Color BACKGROUND = new Color(15, 23, 42);
    private static final Color SURFACE = new Color(17, 24, 39);
    private static final Color SURFACE_ALT = new Color(30, 41, 59);
    private static final Color BORDER = new Color(51, 65, 85);
    private static final Color TEXT = new Color(226, 232, 240);
    private static final Color MUTED = new Color(148, 163, 184);
    private static final Color ACCENT = new Color(56, 189, 248);

    private final ConfigurableApplicationContext applicationContext;
    private final DesktopAuthenticationService authenticationService;

    private DesktopLoginFrame(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.authenticationService = applicationContext.getBean(DesktopAuthenticationService.class);

        setTitle("AssistentIA - Acceso");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(880, 600));
        setSize(940, 650);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                closeApplication();
            }
        });

        setContentPane(createLayout());
    }

    public static void open(ConfigurableApplicationContext applicationContext) {
        DesktopLoginFrame frame = new DesktopLoginFrame(applicationContext);
        frame.setVisible(true);
    }

    private JPanel createLayout() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND);

        root.add(createBrandPanel(), BorderLayout.WEST);
        root.add(createAccessPanel(), BorderLayout.CENTER);
        return root;
    }

    private JPanel createBrandPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(360, 0));
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel brand = createLabel("ASSISTENTIA", 28, Font.BOLD, TEXT);
        brand.setBorder(BorderFactory.createEmptyBorder(72, 42, 4, 42));
        panel.add(brand);

        JLabel subtitle = createLabel("TEAM OPERATING SYSTEM", 11, Font.BOLD, ACCENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 42, 30, 42));
        panel.add(subtitle);

        JLabel explanation = createLabel(
                "Accede con tu cuenta personal para registrar activos y colaborar con tu equipo.",
                14,
                Font.PLAIN,
                MUTED
        );
        explanation.setAlignmentX(LEFT_ALIGNMENT);
        explanation.setBorder(BorderFactory.createEmptyBorder(0, 42, 0, 42));
        panel.add(explanation);

        panel.add(Box.createVerticalGlue());

        JLabel local = createLabel("DATOS LOCALES PROTEGIDOS", 10, Font.BOLD, ACCENT);
        local.setBorder(BorderFactory.createEmptyBorder(20, 42, 6, 42));
        panel.add(local);

        JLabel note = createLabel("El primer miembro creado se convierte en administrador.", 12, Font.PLAIN, MUTED);
        note.setBorder(BorderFactory.createEmptyBorder(0, 42, 42, 42));
        panel.add(note);
        return panel;
    }

    private JPanel createAccessPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setForeground(TEXT);
        tabs.setBackground(SURFACE);
        tabs.addTab("Ingresar", createLoginForm());
        tabs.addTab("Crear cuenta", createRegistrationForm());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(52, 64, 52, 64);
        panel.add(tabs, constraints);
        return panel;
    }

    private JPanel createLoginForm() {
        JPanel panel = createFormPanel();
        JTextField username = createTextField();
        JPasswordField password = createPasswordField();

        addFormTitle(panel, "Bienvenido", "Ingresa para abrir el espacio de trabajo del equipo.");
        addField(panel, "Usuario", username);
        addField(panel, "Contrasena", password);

        JButton signIn = createPrimaryButton("Ingresar");
        signIn.addActionListener(event -> signIn(username, password));
        addButton(panel, signIn);
        return panel;
    }

    private JPanel createRegistrationForm() {
        JPanel panel = createFormPanel();
        JTextField memberName = createTextField();
        JTextField username = createTextField();
        JPasswordField password = createPasswordField();
        JPasswordField confirmation = createPasswordField();

        addFormTitle(panel, "Crear cuenta", "Cada miembro usa sus propias credenciales para ingresar.");
        addField(panel, "Nombre del miembro", memberName);
        addField(panel, "Usuario", username);
        addField(panel, "Contrasena", password);
        addField(panel, "Repetir contrasena", confirmation);

        JButton createAccount = createPrimaryButton("Crear cuenta e ingresar");
        createAccount.addActionListener(event -> createAccount(memberName, username, password, confirmation));
        addButton(panel, createAccount);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(32, 34, 32, 34)
        ));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }

    private void addFormTitle(JPanel panel, String title, String description) {
        JLabel titleLabel = createLabel(title, 24, Font.BOLD, TEXT);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(titleLabel);

        JLabel descriptionLabel = createLabel(description, 13, Font.PLAIN, MUTED);
        descriptionLabel.setAlignmentX(LEFT_ALIGNMENT);
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 24, 0));
        panel.add(descriptionLabel);
    }

    private void addField(JPanel panel, String label, javax.swing.JComponent input) {
        JLabel fieldLabel = createLabel(label, 12, Font.BOLD, MUTED);
        fieldLabel.setAlignmentX(LEFT_ALIGNMENT);
        fieldLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 6, 0));
        panel.add(fieldLabel);

        input.setAlignmentX(LEFT_ALIGNMENT);
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        panel.add(input);
    }

    private void addButton(JPanel panel, JButton button) {
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setBorder(BorderFactory.createEmptyBorder(11, 16, 11, 16));
        panel.add(Box.createVerticalStrut(24));
        panel.add(button);
        panel.add(Box.createVerticalGlue());
    }

    private void signIn(JTextField username, JPasswordField password) {
        char[] rawPassword = password.getPassword();
        try {
            authenticationService.authenticate(username.getText(), new String(rawPassword))
                    .ifPresentOrElse(this::openWorkspace, () -> showError("Usuario o contrasena incorrectos."));
        } finally {
            Arrays.fill(rawPassword, '\0');
        }
    }

    private void createAccount(
            JTextField memberName,
            JTextField username,
            JPasswordField password,
            JPasswordField confirmation
    ) {
        char[] rawPassword = password.getPassword();
        char[] rawConfirmation = confirmation.getPassword();
        try {
            if (!Arrays.equals(rawPassword, rawConfirmation)) {
                showError("Las contrasenas no coinciden.");
                return;
            }

            DesktopSession session = authenticationService.register(
                    username.getText(),
                    memberName.getText(),
                    new String(rawPassword)
            );
            openWorkspace(session);
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        } finally {
            Arrays.fill(rawPassword, '\0');
            Arrays.fill(rawConfirmation, '\0');
        }
    }

    private void openWorkspace(DesktopSession session) {
        dispose();
        SwingUtilities.invokeLater(() -> DesktopDashboardFrame.open(applicationContext, session));
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setForeground(TEXT);
        textField.setCaretColor(TEXT);
        textField.setBackground(SURFACE_ALT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(9, 10, 9, 10)
        ));
        return textField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setForeground(TEXT);
        passwordField.setCaretColor(TEXT);
        passwordField.setBackground(SURFACE_ALT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(9, 10, 9, 10)
        ));
        return passwordField;
    }

    private JButton createPrimaryButton(String label) {
        JButton button = new JButton(label);
        button.setBackground(ACCENT);
        button.setForeground(BACKGROUND);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        return button;
    }

    private JLabel createLabel(String text, int size, int style, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", style, size));
        label.setForeground(color);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private void showError(String message) {
        javax.swing.JOptionPane.showMessageDialog(this, message, "AssistentIA", javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    private void closeApplication() {
        applicationContext.close();
        dispose();
        System.exit(0);
    }
}