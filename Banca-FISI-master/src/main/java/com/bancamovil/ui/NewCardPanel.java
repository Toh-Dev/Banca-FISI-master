package com.bancamovil.ui;

import com.bancamovil.dao.AccountDAO;
import com.bancamovil.dao.CardDAO;
import com.bancamovil.models.Account;
import com.bancamovil.models.Card;
import com.bancamovil.models.User;
import com.bancamovil.utils.CardUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.math.BigDecimal;
import java.util.List;

public class NewCardPanel extends JPanel {
    private DashboardFrame dashboard;
    private User user;
    private AccountDAO accountDAO;
    private CardDAO cardDAO;

    private UITheme.RoundedComboBox<String> comboTipoTarjeta;
    private UITheme.RoundedComboBox<String> comboRed;
    private JComboBox<Account> comboCuentas;
    private JCheckBox checkVincularCuenta;
    private JTextField txtLimiteCredito;
    private JPanel limitePanel;
    private JPanel cuentaPanel;
    
    private JPanel cardPreviewPanel;
    private JLabel previewCardNumber;
    private JLabel previewCardHolder;
    private JLabel previewCardExpiry;
    private JLabel previewCardType;
    private JLabel previewCardNetwork;

    public NewCardPanel(DashboardFrame dashboard, User user) {
        this.dashboard = dashboard;
        this.user = user;
        this.accountDAO = new AccountDAO();
        this.cardDAO = new CardDAO();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        JScrollPane scrollPane = new JScrollPane(createMainContent());
        scrollPane.setBorder(null);
        scrollPane.setBackground(UITheme.BG_DARK);
        scrollPane.getViewport().setBackground(UITheme.BG_DARK);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(UITheme.BG_DARK);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        mainPanel.add(createHeader());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(createInfoBox());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(createContentPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(createButtonPanel());
        mainPanel.add(Box.createVerticalGlue());

        return mainPanel;
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel iconLabel = new JLabel("💳");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Solicitar Nueva Tarjeta");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Genera tu tarjeta virtual al instante");
        subtitleLabel.setFont(UITheme.FONT_BODY);
        subtitleLabel.setForeground(UITheme.TEXT_SECONDARY);

        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        textPanel.add(subtitleLabel);

        headerPanel.add(iconLabel);
        headerPanel.add(textPanel);

        return headerPanel;
    }

    private JPanel createInfoBox() {
        UITheme.RoundedPanel infoBox = new UITheme.RoundedPanel(16, new Color(34, 197, 94, 15), false);
        infoBox.setLayout(new BorderLayout());
        infoBox.setBorder(BorderFactory.createCompoundBorder(
                new UITheme.RoundedBorder(16, UITheme.SUCCESS, 1),
                new EmptyBorder(16, 20, 16, 20)
        ));
        infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel iconInfo = new JLabel("ℹ️");
        iconInfo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconInfo.setBorder(new EmptyBorder(0, 0, 0, 15));

        JLabel infoText = new JLabel("<html>" +
                "<span style='color: #22C55E'>✓</span> La tarjeta será generada instantáneamente<br>" +
                "<span style='color: #22C55E'>✓</span> Tarjetas de débito deben estar vinculadas a una cuenta<br>" +
                "<span style='color: #22C55E'>✓</span> Tarjetas de crédito pueden ser independientes</html>");
        infoText.setFont(UITheme.FONT_BODY);
        infoText.setForeground(UITheme.SUCCESS);

        infoBox.add(iconInfo, BorderLayout.WEST);
        infoBox.add(infoText, BorderLayout.CENTER);

        return infoBox;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 25);
        gbc.weightx = 0.55;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        contentPanel.add(createFormCard(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.45;
        gbc.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(createCardPreview(), gbc);

        return contentPanel;
    }

    private JPanel createFormCard() {
        UITheme.RoundedPanel formCard = new UITheme.RoundedPanel(20, UITheme.BG_CARD, true);
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel formTitle = new JLabel("Configuración de la Tarjeta");
        formTitle.setFont(UITheme.FONT_SUBTITLE);
        formTitle.setForeground(UITheme.TEXT_PRIMARY);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        formCard.add(formTitle);
        formCard.add(Box.createRigidArea(new Dimension(0, 25)));

        String[] tiposTarjeta = {"💳  Débito", "💎  Crédito"};
        comboTipoTarjeta = new UITheme.RoundedComboBox<>(tiposTarjeta);
        comboTipoTarjeta.addActionListener(e -> {
            actualizarFormulario();
            updateCardPreview();
        });

        String[] redesTarjeta = {"VISA", "MASTERCARD"};
        comboRed = new UITheme.RoundedComboBox<>(redesTarjeta);
        comboRed.addActionListener(e -> updateCardPreview());

        List<Account> cuentas = accountDAO.obtenerCuentasPorUsuario(user.getId());
        comboCuentas = new UITheme.RoundedComboBox<>();
        for (Account cuenta : cuentas) {
            comboCuentas.addItem(cuenta);
        }

        checkVincularCuenta = new JCheckBox("Vincular a una cuenta existente");
        checkVincularCuenta.setFont(UITheme.FONT_BODY);
        checkVincularCuenta.setForeground(UITheme.TEXT_PRIMARY);
        checkVincularCuenta.setOpaque(false);
        checkVincularCuenta.setFocusPainted(false);
        checkVincularCuenta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkVincularCuenta.setVisible(false);
        checkVincularCuenta.addActionListener(e -> {
            comboCuentas.setEnabled(checkVincularCuenta.isSelected());
            cuentaPanel.setVisible(checkVincularCuenta.isSelected());
        });

        txtLimiteCredito = UITheme.createRoundedTextField();
        txtLimiteCredito.setVisible(false);

        JPanel tipoPanel = createInputPanel("Tipo de Tarjeta", comboTipoTarjeta);
        JPanel redPanel = createInputPanel("Red de Pago", comboRed);
        cuentaPanel = createInputPanel("Cuenta a Vincular", comboCuentas);
        limitePanel = createInputPanel("Límite de Crédito (opcional)", txtLimiteCredito);

        formCard.add(tipoPanel);
        formCard.add(Box.createRigidArea(new Dimension(0, 20)));
        formCard.add(redPanel);
        formCard.add(Box.createRigidArea(new Dimension(0, 20)));
        formCard.add(checkVincularCuenta);
        formCard.add(Box.createRigidArea(new Dimension(0, 15)));
        formCard.add(cuentaPanel);
        formCard.add(Box.createRigidArea(new Dimension(0, 20)));
        formCard.add(limitePanel);

        actualizarFormulario();
        
        return formCard;
    }

    private JPanel createCardPreview() {
        UITheme.RoundedPanel previewContainer = new UITheme.RoundedPanel(20, UITheme.BG_CARD, true);
        previewContainer.setLayout(new BoxLayout(previewContainer, BoxLayout.Y_AXIS));
        previewContainer.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel previewTitle = new JLabel("Vista Previa de tu Tarjeta");
        previewTitle.setFont(UITheme.FONT_SUBTITLE);
        previewTitle.setForeground(UITheme.TEXT_PRIMARY);
        previewTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        previewContainer.add(previewTitle);
        previewContainer.add(Box.createRigidArea(new Dimension(0, 25)));

        cardPreviewPanel = createVirtualCard();
        cardPreviewPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        previewContainer.add(cardPreviewPanel);

        previewContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel disclaimer = new JLabel("<html><center>Esta es una vista previa. Los datos reales<br>serán generados al confirmar la solicitud.</center></html>");
        disclaimer.setFont(UITheme.FONT_SMALL);
        disclaimer.setForeground(UITheme.TEXT_MUTED);
        disclaimer.setAlignmentX(Component.LEFT_ALIGNMENT);
        disclaimer.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel disclaimerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        disclaimerPanel.setOpaque(false);
        disclaimerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        disclaimerPanel.add(disclaimer);
        previewContainer.add(disclaimerPanel);

        return previewContainer;
    }

    private JPanel createVirtualCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                int w = getWidth();
                int h = getHeight();

                String tipo = (String) comboTipoTarjeta.getSelectedItem();
                boolean esDebito = tipo != null && tipo.contains("Débito");

                GradientPaint gradient;
                if (esDebito) {
                    gradient = new GradientPaint(0, 0, new Color(59, 130, 246), w, h, new Color(37, 99, 235));
                } else {
                    gradient = new GradientPaint(0, 0, new Color(139, 92, 246), w, h, new Color(109, 40, 217));
                }

                g2.setPaint(gradient);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 24, 24));

                GradientPaint shine = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 40),
                    0, h / 3, new Color(255, 255, 255, 0)
                );
                g2.setPaint(shine);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h / 3, 24, 24));

                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillOval(w - 120, -40, 200, 200);
                g2.fillOval(w - 80, h - 60, 150, 150);

                g2.dispose();
            }
        };
        card.setLayout(null);
        card.setPreferredSize(new Dimension(340, 200));
        card.setMinimumSize(new Dimension(340, 200));
        card.setMaximumSize(new Dimension(340, 200));
        card.setOpaque(false);

        JLabel chipLabel = new JLabel("▣");
        chipLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        chipLabel.setForeground(new Color(255, 215, 0, 200));
        chipLabel.setBounds(25, 45, 50, 40);
        card.add(chipLabel);

        previewCardNetwork = new JLabel("VISA");
        previewCardNetwork.setFont(new Font("Segoe UI", Font.BOLD, 22));
        previewCardNetwork.setForeground(Color.WHITE);
        previewCardNetwork.setBounds(270, 20, 60, 30);
        card.add(previewCardNetwork);

        previewCardNumber = new JLabel("•••• •••• •••• ••••");
        previewCardNumber.setFont(new Font("Consolas", Font.BOLD, 20));
        previewCardNumber.setForeground(Color.WHITE);
        previewCardNumber.setBounds(25, 95, 280, 30);
        card.add(previewCardNumber);

        JLabel holderLabel = new JLabel("TITULAR");
        holderLabel.setFont(UITheme.FONT_CAPTION);
        holderLabel.setForeground(new Color(255, 255, 255, 150));
        holderLabel.setBounds(25, 135, 100, 15);
        card.add(holderLabel);

        previewCardHolder = new JLabel(user.getNombreCompleto().toUpperCase());
        previewCardHolder.setFont(new Font("Segoe UI", Font.BOLD, 13));
        previewCardHolder.setForeground(Color.WHITE);
        previewCardHolder.setBounds(25, 150, 180, 20);
        card.add(previewCardHolder);

        JLabel expiryLabel = new JLabel("VÁLIDA HASTA");
        expiryLabel.setFont(UITheme.FONT_CAPTION);
        expiryLabel.setForeground(new Color(255, 255, 255, 150));
        expiryLabel.setBounds(220, 135, 100, 15);
        card.add(expiryLabel);

        previewCardExpiry = new JLabel("••/••");
        previewCardExpiry.setFont(new Font("Segoe UI", Font.BOLD, 13));
        previewCardExpiry.setForeground(Color.WHITE);
        previewCardExpiry.setBounds(220, 150, 60, 20);
        card.add(previewCardExpiry);

        previewCardType = new JLabel("DÉBITO");
        previewCardType.setFont(new Font("Segoe UI", Font.BOLD, 11));
        previewCardType.setForeground(new Color(255, 255, 255, 180));
        previewCardType.setBounds(25, 175, 100, 15);
        card.add(previewCardType);

        return card;
    }

    private void updateCardPreview() {
        String tipo = (String) comboTipoTarjeta.getSelectedItem();
        String red = (String) comboRed.getSelectedItem();

        if (tipo != null) {
            previewCardType.setText(tipo.contains("Débito") ? "DÉBITO" : "CRÉDITO");
        }
        if (red != null) {
            previewCardNetwork.setText(red);
        }

        cardPreviewPanel.repaint();
    }

    private JPanel createInputPanel(String label, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_BODY_BOLD);
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (field instanceof JComboBox) {
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            field.setPreferredSize(new Dimension(300, 48));
        } else {
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        }

        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(field);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnSolicitar = UITheme.createPrimaryButton("Generar Tarjeta", "✓");
        btnSolicitar.setPreferredSize(new Dimension(200, 50));
        btnSolicitar.addActionListener(e -> solicitarTarjeta());

        JButton btnCancelar = UITheme.createSecondaryButton("Cancelar", "✕");
        btnCancelar.setPreferredSize(new Dimension(140, 50));
        btnCancelar.addActionListener(e -> dashboard.mostrarDashboardHome());

        buttonPanel.add(btnSolicitar);
        buttonPanel.add(btnCancelar);

        return buttonPanel;
    }

    // ════════════════════════════════════════════════════════════════════════
    // LÓGICA DE BACKEND - SIN MODIFICACIONES
    // ════════════════════════════════════════════════════════════════════════

    private void solicitarTarjeta() {
        try {
            String tipoSeleccionado = (String) comboTipoTarjeta.getSelectedItem();
            String tipoTarjeta = tipoSeleccionado.contains("Débito") ? "DEBITO" : "CREDITO";
            String red = (String) comboRed.getSelectedItem();
            boolean esDebito = "DEBITO".equals(tipoTarjeta);

            if (esDebito && comboCuentas.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar una cuenta para vincular la tarjeta de débito.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!esDebito && checkVincularCuenta.isSelected() && comboCuentas.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar una cuenta para vincular.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String mensaje = "¿Confirma la solicitud de una tarjeta de " + tipoTarjeta + " " + red + "?";
            int confirmacion = JOptionPane.showConfirmDialog(this, mensaje,
                    "Confirmar Solicitud", JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) return;

            Card nuevaTarjeta = new Card();

            String numeroTarjeta;
            do {
                numeroTarjeta = CardUtils.generarNumeroTarjeta(red, null);
            } while (cardDAO.existeNumeroTarjeta(numeroTarjeta));

            nuevaTarjeta.setNumeroTarjeta(numeroTarjeta);
            nuevaTarjeta.setTipoTarjeta(Card.TipoTarjeta.valueOf(tipoTarjeta));
            nuevaTarjeta.setNombreTitular(user.getNombreCompleto().toUpperCase());
            nuevaTarjeta.setFechaVencimiento(CardUtils.generarFechaVencimiento());
            nuevaTarjeta.setCvv(CardUtils.generarCVV());
            nuevaTarjeta.setActiva(true);

            if (esDebito || checkVincularCuenta.isSelected()) {
                Account cuentaSeleccionada = (Account) comboCuentas.getSelectedItem();
                nuevaTarjeta.setCuentaId(cuentaSeleccionada.getId());
            } else {
                nuevaTarjeta.setCuentaId(null);
            }

            if (!esDebito) {
                String limiteStr = txtLimiteCredito.getText().trim();
                if (!limiteStr.isEmpty()) {
                    nuevaTarjeta.setLimiteCredito(new BigDecimal(limiteStr));
                } else {
                    nuevaTarjeta.setLimiteCredito(new BigDecimal("5000.00"));
                }
            } else {
                nuevaTarjeta.setLimiteCredito(null);
            }

            if (cardDAO.crearTarjeta(nuevaTarjeta)) {
                String mensajeExito = String.format(
                        "¡Tarjeta generada exitosamente!\n\n" +
                                "Tipo: %s %s\n" +
                                "Número: %s\n" +
                                "Titular: %s\n" +
                                "Vencimiento: %s\n" +
                                "CVV: %s",
                        tipoTarjeta, red,
                        CardUtils.formatearNumeroTarjeta(numeroTarjeta),
                        nuevaTarjeta.getNombreTitular(),
                        nuevaTarjeta.getFechaFormateada(),
                        nuevaTarjeta.getCvv()
                );

                JOptionPane.showMessageDialog(this, mensajeExito,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                dashboard.mostrarDashboardHome();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al generar la tarjeta. Intente nuevamente.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El límite de crédito debe ser un número válido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void actualizarFormulario() {
        String tipoSeleccionado = (String) comboTipoTarjeta.getSelectedItem();
        boolean esDebito = tipoSeleccionado != null && tipoSeleccionado.contains("Débito");

        checkVincularCuenta.setVisible(!esDebito);
        limitePanel.setVisible(!esDebito);

        if (esDebito) {
            comboCuentas.setEnabled(true);
            cuentaPanel.setVisible(true);
            checkVincularCuenta.setSelected(true);
        } else {
            cuentaPanel.setVisible(checkVincularCuenta.isSelected());
            comboCuentas.setEnabled(checkVincularCuenta.isSelected());
        }

        revalidate();
        repaint();
    }
}
