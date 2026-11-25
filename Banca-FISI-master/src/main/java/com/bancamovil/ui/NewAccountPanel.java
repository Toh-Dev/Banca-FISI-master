package com.bancamovil.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.bancamovil.dao.AccountDAO;
import com.bancamovil.models.Account;
import com.bancamovil.models.User;

public class NewAccountPanel extends JPanel {
    private DashboardFrame dashboard;
    private User user;
    private AccountDAO accountDAO;

    private String selectedTipoCuenta = "AHORROS";
    private String selectedMoneda = "PEN";
    
    private AccountTypeCard cardAhorros;
    private AccountTypeCard cardCorriente;
    private AccountTypeCard cardCTS;
    private UITheme.RoundedComboBox<String> comboMoneda;

    public NewAccountPanel(DashboardFrame dashboard, User user) {
        this.dashboard = dashboard;
        this.user = user;
        this.accountDAO = new AccountDAO();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setOpaque(false);
        mainContainer.setBorder(new EmptyBorder(40, 50, 40, 50));

        mainContainer.add(createHeader());
        mainContainer.add(Box.createRigidArea(new Dimension(0, 30)));
        mainContainer.add(createUserInfoCard());
        mainContainer.add(Box.createRigidArea(new Dimension(0, 25)));
        mainContainer.add(createInfoBanner());
        mainContainer.add(Box.createRigidArea(new Dimension(0, 35)));
        mainContainer.add(createAccountTypeSection());
        mainContainer.add(Box.createRigidArea(new Dimension(0, 30)));
        mainContainer.add(createCurrencySection());
        mainContainer.add(Box.createRigidArea(new Dimension(0, 40)));
        mainContainer.add(createButtonPanel());
        mainContainer.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(mainContainer);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        // Usamos BoxLayout horizontal para alinear icono y texto perfectamente
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 1. Contenedor del Icono (con fondo sutil para ordenarlo)
        JPanel iconContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Círculo de fondo muy sutil
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        iconContainer.setPreferredSize(new Dimension(72, 72));
        iconContainer.setMaximumSize(new Dimension(72, 72));
        iconContainer.setOpaque(false);
        iconContainer.setLayout(new GridBagLayout()); // Centra el emoji en el círculo

        JLabel iconLabel = new JLabel("🏦");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        iconContainer.add(iconLabel);

        // 2. Panel de Texto
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Crear Nueva Cuenta");
        // Aseguramos que use una fuente grande si FONT_TITLE_LARGE no existe en tu UITheme actual
        try {
            titleLabel.setFont(UITheme.FONT_TITLE_LARGE);
        } catch (Exception e) {
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        }
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Selecciona el tipo de cuenta que deseas abrir");
        subtitleLabel.setFont(UITheme.FONT_BODY);
        subtitleLabel.setForeground(UITheme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        textPanel.add(subtitleLabel);

        // 3. Alineación Vertical (El truco para que no se vea desordenado)
        iconContainer.setAlignmentY(Component.CENTER_ALIGNMENT);
        textPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        headerPanel.add(iconContainer);
        headerPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Espacio entre icono y texto
        headerPanel.add(textPanel);

        return headerPanel;
    }

    private UITheme.RoundedPanel createUserInfoCard() {
        UITheme.RoundedPanel card = new UITheme.RoundedPanel(16, UITheme.BG_CARD, true);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 25, 20, 25));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel avatarPanel = UITheme.createAvatar(
            user.getNombreCompleto().substring(0, 2).toUpperCase(), 
            50, 
            UITheme.PRIMARY
        );

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(0, 15, 0, 0));

        JLabel lblTitle = new JLabel("Titular de la cuenta");
        lblTitle.setFont(UITheme.FONT_SMALL);
        lblTitle.setForeground(UITheme.TEXT_SECONDARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNombre = new JLabel(user.getNombreCompleto());
        lblNombre.setFont(UITheme.FONT_HEADING);
        lblNombre.setForeground(UITheme.TEXT_PRIMARY);
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDni = new JLabel("DNI: " + user.getDni());
        lblDni.setFont(UITheme.FONT_BODY);
        lblDni.setForeground(UITheme.TEXT_MUTED);
        lblDni.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(lblTitle);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        infoPanel.add(lblNombre);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        infoPanel.add(lblDni);

        // Usamos BoxLayout aquí también para mejor alineación
        JPanel leftContainer = new JPanel();
        leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.X_AXIS));
        leftContainer.setOpaque(false);
        
        avatarPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        infoPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        leftContainer.add(avatarPanel);
        leftContainer.add(infoPanel);

        card.add(leftContainer, BorderLayout.WEST);

        return card;
    }

    private UITheme.RoundedPanel createInfoBanner() {
        UITheme.RoundedPanel banner = new UITheme.RoundedPanel(12, new Color(34, 197, 94, 15), false);
        banner.setLayout(new BorderLayout());
        banner.setBorder(BorderFactory.createCompoundBorder(
            new UITheme.RoundedBorder(12, UITheme.SUCCESS, 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel iconInfo = new JLabel("ℹ️");
        iconInfo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

        JLabel infoText = new JLabel("<html><b>Información:</b> La cuenta se creará con saldo inicial de 0.00. " +
                "El número de cuenta se generará automáticamente.</html>");
        infoText.setFont(UITheme.FONT_BODY);
        infoText.setForeground(UITheme.SUCCESS);
        infoText.setBorder(new EmptyBorder(0, 12, 0, 0));

        banner.add(iconInfo, BorderLayout.WEST);
        banner.add(infoText, BorderLayout.CENTER);

        return banner;
    }

    private JPanel createAccountTypeSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("Selecciona el tipo de cuenta");
        sectionTitle.setFont(UITheme.FONT_SUBTITLE);
        sectionTitle.setForeground(UITheme.TEXT_PRIMARY);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        cardAhorros = new AccountTypeCard(
            "💰", "AHORROS", 
            "Ideal para ahorro personal",
            "• Sin costo de mantenimiento\n• Intereses mensuales\n• Retiros ilimitados",
            UITheme.ACCENT,
            true
        );
        
        cardCorriente = new AccountTypeCard(
            "💳", "CORRIENTE", 
            "Para movimientos frecuentes",
            "• Chequera disponible\n• Sobregiros permitidos\n• Operaciones ilimitadas",
            UITheme.PRIMARY,
            false
        );
        
        cardCTS = new AccountTypeCard(
            "🏦", "CTS", 
            "Compensación por tiempo de servicio",
            "• Depósitos del empleador\n• Retiros según ley\n• Mayor rentabilidad",
            UITheme.ACCENT_ORANGE,
            false
        );

        cardAhorros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectAccountType("AHORROS");
            }
        });

        cardCorriente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectAccountType("CORRIENTE");
            }
        });

        cardCTS.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectAccountType("CTS");
            }
        });

        cardsPanel.add(cardAhorros);
        cardsPanel.add(cardCorriente);
        cardsPanel.add(cardCTS);

        section.add(sectionTitle);
        section.add(Box.createRigidArea(new Dimension(0, 20)));
        section.add(cardsPanel);

        return section;
    }

    private void selectAccountType(String tipo) {
        selectedTipoCuenta = tipo;
        cardAhorros.setSelected(tipo.equals("AHORROS"));
        cardCorriente.setSelected(tipo.equals("CORRIENTE"));
        cardCTS.setSelected(tipo.equals("CTS"));
    }

    private JPanel createCurrencySection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("Selecciona la moneda");
        sectionTitle.setFont(UITheme.FONT_SUBTITLE);
        sectionTitle.setForeground(UITheme.TEXT_PRIMARY);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        UITheme.RoundedPanel currencyCard = new UITheme.RoundedPanel(16, UITheme.BG_CARD, false);
        currencyCard.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        currencyCard.setBorder(new EmptyBorder(10, 20, 10, 20));
        currencyCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        currencyCard.setMaximumSize(new Dimension(400, 70));

        JLabel currencyIcon = new JLabel("💱");
        currencyIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        String[] monedas = {"PEN", "USD"};
        comboMoneda = new UITheme.RoundedComboBox<>(monedas);
        comboMoneda.setPreferredSize(new Dimension(200, 45));
        comboMoneda.addActionListener(e -> selectedMoneda = (String) comboMoneda.getSelectedItem());

        currencyCard.add(currencyIcon);
        currencyCard.add(comboMoneda);

        section.add(sectionTitle);
        section.add(Box.createRigidArea(new Dimension(0, 15)));
        section.add(currencyCard);

        return section;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCrear = UITheme.createPrimaryButton("Crear Cuenta", "✓");
        btnCrear.setPreferredSize(new Dimension(200, 50));
        btnCrear.addActionListener(e -> crearCuenta());

        JButton btnCancelar = UITheme.createSecondaryButton("Cancelar", "✕");
        btnCancelar.setPreferredSize(new Dimension(160, 50));
        btnCancelar.addActionListener(e -> dashboard.mostrarDashboardHome());

        buttonPanel.add(btnCrear);
        buttonPanel.add(btnCancelar);

        return buttonPanel;
    }

    private void crearCuenta() {
        try {
            String tipoCuenta = selectedTipoCuenta;
            String moneda = selectedMoneda;

            String mensaje = String.format(
                    "¿Confirma la creación de una cuenta de tipo %s en %s?\n\n" +
                            "Titular: %s\n" +
                            "Saldo inicial: %s 0.00",
                    tipoCuenta, moneda,
                    user.getNombreCompleto(),
                    moneda.equals("PEN") ? "S/." : "$"
            );

            int confirmacion = JOptionPane.showConfirmDialog(this, mensaje,
                    "Confirmar Creación de Cuenta", JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) return;

            String numeroCuenta;
            do {
                numeroCuenta = generarNumeroCuenta();
            } while (accountDAO.obtenerPorNumero(numeroCuenta) != null);

            Account nuevaCuenta = new Account();
            nuevaCuenta.setUsuarioId(user.getId());
            nuevaCuenta.setNumeroCuenta(numeroCuenta);
            nuevaCuenta.setTipoCuenta(Account.TipoCuenta.valueOf(tipoCuenta));
            nuevaCuenta.setSaldo(BigDecimal.ZERO);
            nuevaCuenta.setMoneda(Account.Moneda.valueOf(moneda));

            if (accountDAO.crearCuenta(nuevaCuenta)) {
                String mensajeExito = String.format(
                        "¡Cuenta creada exitosamente!\n\n" +
                                "Número de cuenta: %s\n" +
                                "Tipo: %s\n" +
                                "Moneda: %s\n" +
                                "Saldo inicial: %s 0.00",
                        numeroCuenta, tipoCuenta, moneda,
                        moneda.equals("PEN") ? "S/." : "$"
                );

                JOptionPane.showMessageDialog(this, mensajeExito,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                dashboard.actualizarDatos();
                dashboard.mostrarDashboardHome();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al crear la cuenta. Intente nuevamente.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String generarNumeroCuenta() {
        Random random = new Random();
        String banco = "191";
        String cuenta = String.format("%08d", random.nextInt(100000000));
        String verificador = String.valueOf(random.nextInt(10));

        String tipo = "01";
        if ("CORRIENTE".equals(selectedTipoCuenta)) tipo = "02";
        else if ("CTS".equals(selectedTipoCuenta)) tipo = "03";

        return banco + "-" + cuenta + "-" + verificador + "-" + tipo;
    }

    private class AccountTypeCard extends UITheme.RoundedPanel {
        private String icon;
        private String tipoCuenta;
        private String description;
        private String features;
        private Color accentColor;
        private boolean selected;

        public AccountTypeCard(String icon, String tipoCuenta, String description, String features, Color accentColor, boolean selected) {
            super(20, UITheme.BG_CARD, true, selected);
            this.icon = icon;
            this.tipoCuenta = tipoCuenta;
            this.description = description;
            this.features = features;
            this.accentColor = accentColor;
            this.selected = selected;

            if (selected) {
                setGlowColor(accentColor);
            }

            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(20, 20, 20, 20));

            buildContent();

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!AccountTypeCard.this.selected) {
                        setBackgroundColor(UITheme.BG_CARD_HOVER);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!AccountTypeCard.this.selected) {
                        setBackgroundColor(UITheme.BG_CARD);
                    }
                }
            });
        }

        private void buildContent() {
            removeAll();

            JLabel iconLabel = new JLabel(icon);
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
            iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel titleLabel = new JLabel(tipoCuenta);
            titleLabel.setFont(UITheme.FONT_HEADING);
            titleLabel.setForeground(selected ? accentColor : UITheme.TEXT_PRIMARY);
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel descLabel = new JLabel(description);
            descLabel.setFont(UITheme.FONT_SMALL);
            descLabel.setForeground(UITheme.TEXT_SECONDARY);
            descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel featuresLabel = new JLabel("<html>" + features.replace("\n", "<br>") + "</html>");
            featuresLabel.setFont(UITheme.FONT_CAPTION);
            featuresLabel.setForeground(UITheme.TEXT_MUTED);
            featuresLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            add(iconLabel);
            add(Box.createRigidArea(new Dimension(0, 12)));
            add(titleLabel);
            add(Box.createRigidArea(new Dimension(0, 5)));
            add(descLabel);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(featuresLabel);
            add(Box.createVerticalGlue());

            if (selected) {
                JLabel checkLabel = new JLabel("✓ Seleccionado");
                checkLabel.setFont(UITheme.FONT_SMALL_BOLD);
                checkLabel.setForeground(accentColor);
                checkLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                add(Box.createRigidArea(new Dimension(0, 10)));
                add(checkLabel);
            }

            revalidate();
            repaint();
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            setBackgroundColor(selected ? new Color(
                accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 20
            ) : UITheme.BG_CARD);
            
            if (selected) {
                setGlowColor(accentColor);
            }
            
            buildContent();
        }
    }
}  

