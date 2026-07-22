package com.facundo.assistentia.interfaces.desktop;

import com.facundo.assistentia.application.asset.service.AssetHoldingView;
import com.facundo.assistentia.application.asset.service.AssetWorkspaceService;
import com.facundo.assistentia.application.auth.service.DesktopSession;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DesktopDashboardFrame extends JFrame {

    private static final Color BACKGROUND = new Color(15, 23, 42);
    private static final Color SURFACE = new Color(17, 24, 39);
    private static final Color SURFACE_ALT = new Color(30, 41, 59);
    private static final Color BORDER = new Color(51, 65, 85);
    private static final Color TEXT = new Color(226, 232, 240);
    private static final Color MUTED = new Color(148, 163, 184);
    private static final Color ACCENT = new Color(56, 189, 248);
        private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    private final ConfigurableApplicationContext applicationContext;
        private final DesktopSession session;
        private final AssetWorkspaceService assetWorkspaceService;
    private final CardLayout contentLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(contentLayout);
    private final JLabel pageTitle = new JLabel("Dashboard");
    private final JLabel pageDescription = new JLabel("Tu espacio de trabajo para hoy");
    private final DefaultTableModel prospectsModel = new DefaultTableModel(
            new Object[]{"Nombre", "Estado", "Ultimo contacto"}, 0
    );
    private final DefaultTableModel sharedAssetsModel = new DefaultTableModel(
            new Object[]{"Activo", "Cantidad", "Propietario", "Usuario", "Actualizado"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final javax.swing.DefaultListModel<String> tasksModel = new javax.swing.DefaultListModel<>();

    private DesktopDashboardFrame(ConfigurableApplicationContext applicationContext, DesktopSession session) {
        this.applicationContext = applicationContext;
        this.session = session;
        this.assetWorkspaceService = applicationContext.getBean(AssetWorkspaceService.class);

        setTitle("AssistentIA - " + session.displayName());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(1024, 680));
        setSize(1280, 780);
        setLocationByPlatform(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                closeApplication();
            }
        });

        setContentPane(createLayout());
    }

    public static void open(ConfigurableApplicationContext applicationContext, DesktopSession session) {
        DesktopDashboardFrame frame = new DesktopDashboardFrame(applicationContext, session);
        frame.setVisible(true);
    }

    private JPanel createLayout() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createSidebar(), createWorkspace());
        splitPane.setDividerSize(1);
        splitPane.setDividerLocation(240);
        splitPane.setEnabled(false);
        root.add(splitPane, BorderLayout.CENTER);

        return root;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SURFACE);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel brand = createLabel("ASSISTENTIA", 20, Font.BOLD, TEXT);
        brand.setBorder(BorderFactory.createEmptyBorder(28, 24, 2, 24));
        sidebar.add(brand);

        JLabel tagline = createLabel("TEAM OPERATING SYSTEM", 10, Font.BOLD, ACCENT);
        tagline.setBorder(BorderFactory.createEmptyBorder(0, 24, 28, 24));
        sidebar.add(tagline);

        sidebar.add(createNavigationButton("Dashboard", "dashboard", "Tu espacio de trabajo para hoy"));
        sidebar.add(createNavigationButton("Prospectos", "prospects", "Seguimiento comercial del equipo"));
        sidebar.add(createNavigationButton("Tareas", "tasks", "Prioridades y pendientes"));
        sidebar.add(createNavigationButton("Activos", "assets", "Activos personales visibles para todo el equipo"));
        sidebar.add(createNavigationButton("Reuniones", "meetings", "Agenda y acuerdos"));
        sidebar.add(createNavigationButton("Equipo", "team", "Actividad de la red"));

        sidebar.add(Box.createVerticalGlue());

        JLabel status = createLabel("SESION ACTIVA", 10, Font.BOLD, ACCENT);
        status.setBorder(BorderFactory.createEmptyBorder(16, 24, 4, 24));
        sidebar.add(status);

        JLabel account = createLabel(session.displayName() + "  @" + session.username(), 12, Font.PLAIN, MUTED);
        account.setBorder(BorderFactory.createEmptyBorder(0, 24, 12, 24));
        sidebar.add(account);

        JButton logout = createSecondaryButton("Cerrar sesion");
        logout.setAlignmentX(LEFT_ALIGNMENT);
        logout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        logout.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        logout.addActionListener(event -> logout());
        sidebar.add(logout);
        sidebar.add(Box.createVerticalStrut(18));

        return sidebar;
    }

    private JPanel createWorkspace() {
        JPanel workspace = new JPanel(new BorderLayout());
        workspace.setBackground(BACKGROUND);

        JPanel header = new JPanel();
        header.setBackground(BACKGROUND);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(28, 34, 18, 34));

        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        pageTitle.setForeground(TEXT);
        header.add(pageTitle);

        pageDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pageDescription.setForeground(MUTED);
        pageDescription.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        header.add(pageDescription);

        contentPanel.setBackground(BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 34, 34, 34));
        contentPanel.add(createDashboard(), "dashboard");
        contentPanel.add(createProspects(), "prospects");
        contentPanel.add(createTasks(), "tasks");
        contentPanel.add(createAssets(), "assets");
        contentPanel.add(createMeetings(), "meetings");
        contentPanel.add(createTeam(), "team");

        workspace.add(header, BorderLayout.NORTH);
        workspace.add(contentPanel, BorderLayout.CENTER);
        return workspace;
    }

    private JPanel createDashboard() {
        JPanel dashboard = new JPanel(new BorderLayout(0, 18));
        dashboard.setBackground(BACKGROUND);

        JPanel metrics = new JPanel(new GridLayout(1, 4, 14, 0));
        metrics.setBackground(BACKGROUND);
        metrics.add(createMetricCard("PROSPECTOS", "0", "Sin seguimiento pendiente"));
        metrics.add(createMetricCard("TAREAS", "0", "Para completar hoy"));
        metrics.add(createMetricCard("REUNIONES", "0", "Proximas 7 dias"));
        metrics.add(createMetricCard("ACTIVOS", String.valueOf(assetWorkspaceService.getCatalogNames().size()), "Nombres en el catalogo"));
        dashboard.add(metrics, BorderLayout.NORTH);

        JPanel lower = new JPanel(new GridLayout(1, 2, 14, 0));
        lower.setBackground(BACKGROUND);
        lower.add(createInfoPanel("Prioridades de hoy", new String[]{
                "Organiza tus prospectos.",
                "Registra tus proximas reuniones.",
                "Define las tareas del equipo."
        }));
        lower.add(createInfoPanel("Actividad reciente", new String[]{
                "La aplicacion de escritorio esta lista.",
                "Los datos se conectaran al equipo en los siguientes modulos."
        }));
        dashboard.add(lower, BorderLayout.CENTER);

        return dashboard;
    }

    private JPanel createProspects() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BACKGROUND);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(SURFACE);
        form.setBorder(createPanelBorder());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(16, 16, 16, 8);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        JTextField name = createTextField("Nombre del prospecto");
        JComboBox<String> status = new JComboBox<>(new String[]{"Frio", "Tibio", "Caliente", "Presentado"});
        styleComboBox(status);
        JButton add = createPrimaryButton("Agregar prospecto");
        add.addActionListener(event -> {
            if (!name.getText().isBlank()) {
                prospectsModel.addRow(new Object[]{name.getText().trim(), status.getSelectedItem(), "Ahora"});
                name.setText("");
            }
        });

        constraints.gridx = 0;
        form.add(name, constraints);
        constraints.gridx = 1;
        constraints.weightx = 0.45;
        form.add(status, constraints);
        constraints.gridx = 2;
        constraints.weightx = 0;
        constraints.insets = new Insets(16, 8, 16, 16);
        form.add(add, constraints);

        JTable table = new JTable(prospectsModel);
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(TEXT);
        table.setBackground(SURFACE);
        table.setGridColor(BORDER);
        table.getTableHeader().setBackground(SURFACE_ALT);
        table.getTableHeader().setForeground(MUTED);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(createPanelBorder());
        scrollPane.getViewport().setBackground(SURFACE);

        panel.add(form, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAssets() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BACKGROUND);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(SURFACE);
        form.setBorder(createPanelBorder());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(16, 16, 16, 8);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.6;

        JComboBox<String> assetName = new JComboBox<>(assetWorkspaceService.getCatalogNames().toArray(String[]::new));
        styleComboBox(assetName);
        JTextField quantity = createTextField("Cantidad personal");
        JButton save = createPrimaryButton("Guardar mi activo");
        JButton refresh = createSecondaryButton("Actualizar lista");

        save.addActionListener(event -> saveAssetHolding(assetName, quantity));
        refresh.addActionListener(event -> refreshSharedAssets());

        constraints.gridx = 0;
        form.add(assetName, constraints);
        constraints.gridx = 1;
        constraints.weightx = 0.4;
        form.add(quantity, constraints);
        constraints.gridx = 2;
        constraints.weightx = 0;
        form.add(save, constraints);
        constraints.gridx = 3;
        constraints.insets = new Insets(16, 8, 16, 16);
        form.add(refresh, constraints);

        JTable table = new JTable(sharedAssetsModel);
        styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(160);
        table.getColumnModel().getColumn(1).setPreferredWidth(110);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(140);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(createPanelBorder());
        scrollPane.getViewport().setBackground(SURFACE);

        panel.add(form, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        refreshSharedAssets();
        return panel;
    }

    private JPanel createTasks() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BACKGROUND);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(SURFACE);
        form.setBorder(createPanelBorder());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(16, 16, 16, 8);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        JTextField title = createTextField("Nueva tarea");
        JComboBox<String> priority = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});
        styleComboBox(priority);
        JButton add = createPrimaryButton("Agregar tarea");
        add.addActionListener(event -> {
            if (!title.getText().isBlank()) {
                tasksModel.addElement("[" + priority.getSelectedItem() + "] " + title.getText().trim());
                title.setText("");
            }
        });

        constraints.gridx = 0;
        form.add(title, constraints);
        constraints.gridx = 1;
        constraints.weightx = 0.35;
        form.add(priority, constraints);
        constraints.gridx = 2;
        constraints.weightx = 0;
        constraints.insets = new Insets(16, 8, 16, 16);
        form.add(add, constraints);

        JList<String> list = new JList<>(tasksModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setBackground(SURFACE);
        list.setForeground(TEXT);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        list.setFixedCellHeight(38);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(createPanelBorder());
        scrollPane.getViewport().setBackground(SURFACE);

        panel.add(form, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMeetings() {
        return createInfoPanel("Reuniones", new String[]{
                "No hay reuniones programadas.",
                "Este modulo conectara participantes, acuerdos, tareas y resumenes con IA."
        });
    }

    private JPanel createTeam() {
        return createInfoPanel("Equipo", new String[]{
                "Tu red se mostrara aqui cuando registremos usuarios y patrocinadores.",
                "El primer miembro activo es el administrador local."
        });
    }

    private JPanel createMetricCard(String title, String value, String description) {
        JPanel card = new JPanel();
        card.setBackground(SURFACE);
        card.setBorder(createPanelBorder());
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel titleLabel = createLabel(title, 11, Font.BOLD, MUTED);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(18, 18, 8, 18));
        card.add(titleLabel);

        JLabel valueLabel = createLabel(value, 30, Font.BOLD, TEXT);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 18, 4, 18));
        card.add(valueLabel);

        JLabel descriptionLabel = createLabel(description, 12, Font.PLAIN, MUTED);
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(0, 18, 18, 18));
        card.add(descriptionLabel);
        return card;
    }

    private JPanel createInfoPanel(String title, String[] items) {
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE);
        panel.setBorder(createPanelBorder());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = createLabel(title, 16, Font.BOLD, TEXT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 12, 20));
        panel.add(titleLabel);

        for (String item : items) {
            JLabel itemLabel = createLabel(item, 13, Font.PLAIN, MUTED);
            itemLabel.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 20));
            panel.add(itemLabel);
        }

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JButton createNavigationButton(String title, String page, String description) {
        JButton button = new JButton(title);
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setBackground(SURFACE);
        button.setForeground(MUTED);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.addActionListener(event -> showPage(page, title, description));
        return button;
    }

    private JButton createPrimaryButton(String label) {
        JButton button = new JButton(label);
        button.setBackground(ACCENT);
        button.setForeground(BACKGROUND);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        button.setFocusPainted(false);
        return button;
    }

    private JButton createSecondaryButton(String label) {
        JButton button = new JButton(label);
        button.setBackground(SURFACE_ALT);
        button.setForeground(TEXT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createLineBorder(BORDER));
        button.setFocusPainted(false);
        return button;
    }

    private JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.setToolTipText(placeholder);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setForeground(TEXT);
        textField.setCaretColor(TEXT);
        textField.setBackground(SURFACE_ALT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(9, 10, 9, 10)
        ));
        return textField;
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setForeground(TEXT);
        comboBox.setBackground(SURFACE_ALT);
        comboBox.setBorder(BorderFactory.createLineBorder(BORDER));
    }

    private void styleTable(JTable table) {
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(TEXT);
        table.setBackground(SURFACE);
        table.setGridColor(BORDER);
        table.getTableHeader().setBackground(SURFACE_ALT);
        table.getTableHeader().setForeground(MUTED);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
    }

    private JLabel createLabel(String text, int size, int style, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", style, size));
        label.setForeground(color);
        return label;
    }

    private javax.swing.border.Border createPanelBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        );
    }

    private void showPage(String page, String title, String description) {
        pageTitle.setText(title);
        pageDescription.setText(description);
        if ("assets".equals(page)) {
            refreshSharedAssets();
        }
        contentLayout.show(contentPanel, page);
    }

    private void saveAssetHolding(JComboBox<String> assetName, JTextField quantityInput) {
        try {
            String normalizedQuantity = quantityInput.getText().trim().replace(',', '.');
            BigDecimal quantity = new BigDecimal(normalizedQuantity);
            AssetHoldingView savedHolding = assetWorkspaceService.saveHolding(
                    session,
                    (String) assetName.getSelectedItem(),
                    quantity
            );
            quantityInput.setText("");
            refreshSharedAssets();
            JOptionPane.showMessageDialog(
                    this,
                    savedHolding.assetName() + " actualizado para " + savedHolding.ownerName() + ".",
                    "Activo guardado",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (NumberFormatException exception) {
            showError("Ingresa una cantidad valida. Puedes usar decimales, por ejemplo: 12.5");
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void refreshSharedAssets() {
        sharedAssetsModel.setRowCount(0);
        for (AssetHoldingView holding : assetWorkspaceService.getSharedHoldings()) {
            sharedAssetsModel.addRow(new Object[]{
                    holding.assetName(),
                    holding.quantity().stripTrailingZeros().toPlainString(),
                    holding.ownerName(),
                    "@" + holding.ownerUsername(),
                    DATE_TIME_FORMAT.format(holding.updatedAt())
            });
        }
    }

    private void logout() {
        dispose();
        SwingUtilities.invokeLater(() -> DesktopLoginFrame.open(applicationContext));
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "AssistentIA", JOptionPane.ERROR_MESSAGE);
    }

    private void closeApplication() {
        applicationContext.close();
        dispose();
        System.exit(0);
    }
}