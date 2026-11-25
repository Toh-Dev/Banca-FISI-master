package com.bancamovil.ui;

import com.bancamovil.dao.AccountDAO;
import com.bancamovil.models.Account;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.Objects;

public class TransferPanel extends JPanel {
    private DashboardFrame dashboard;
    private Account cuentaOrigen;
    private AccountDAO accountDAO;
    
    private JTextField txtCuentaDestino, txtMonto, txtDescripcion;

    public TransferPanel(DashboardFrame dashboard, Account cuentaOrigen) {
        this.dashboard = dashboard;
        this.cuentaOrigen = cuentaOrigen;
        this.accountDAO = new AccountDAO();
        
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        JScrollPane scrollPane = new JScrollPane(createMainContent());
        scrollPane.setBorder(null);
        scrollPane.setBackground(UITheme.BG_DARK);
        scrollPane.getViewport().setBackground(UITheme.BG_DARK);
        scrollPane.getVerticalScrollBar().setUI(new UITheme.ModernScrollBarUI());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(UITheme.BG_DARK);
        mainPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        mainPanel.add(createHeader());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(createOriginAccountCard());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(createFormCard());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(createWarningPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(createButtonsPanel());
        mainPanel.add(Box.createVerticalGlue());

        return mainPanel;
    }
    
    private JPanel createHeader() {
        UITheme.RoundedPanel headerPanel = new UITheme.RoundedPanel(20, UITheme.BG_CARD, true, false);
        headerPanel.setLayout(new BorderLayout(20, 0));
        headerPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        JPanel iconContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, UITheme.PRIMARY,
                    getWidth(), getHeight(), UITheme.SECONDARY
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconContainer.setOpaque(false);
        iconContainer.setPreferredSize(new Dimension(56, 56));
        iconContainer.setLayout(new GridBagLayout());
        
        JLabel iconLabel = new JLabel("↔️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconContainer.add(iconLabel);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Transferir Fondos");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        
        JLabel subtitleLabel = new JLabel("Envía dinero a otras cuentas de forma segura");
        subtitleLabel.setFont(UITheme.FONT_SMALL);
        subtitleLabel.setForeground(UITheme.TEXT_SECONDARY);
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        textPanel.add(subtitleLabel);
        
        headerPanel.add(iconContainer, BorderLayout.WEST);
        headerPanel.add(textPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createOriginAccountCard() {
        UITheme.RoundedPanel card = new UITheme.RoundedPanel(16, UITheme.BG_CARD, true, false);
        card.setLayout(new BorderLayout(15, 0));
        card.setBorder(new EmptyBorder(20, 25, 20, 25));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(UITheme.INFO.getRed(), UITheme.INFO.getGreen(), UITheme.INFO.getBlue(), 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(48, 48));
        iconPanel.setLayout(new GridBagLayout());
        
        JLabel bankIcon = new JLabel("🏦");
        bankIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        iconPanel.add(bankIcon);
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel("Cuenta origen");
        lblTitle.setFont(UITheme.FONT_SMALL);
        lblTitle.setForeground(UITheme.TEXT_SECONDARY);
        
        JLabel lblCuentaNum = new JLabel(cuentaOrigen.getNumeroCuenta());
        lblCuentaNum.setFont(UITheme.FONT_SUBTITLE);
        lblCuentaNum.setForeground(UITheme.TEXT_PRIMARY);
        
        infoPanel.add(lblTitle);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        infoPanel.add(lblCuentaNum);
        
        JLabel lblSaldo = new JLabel(cuentaOrigen.getMoneda() + " " + cuentaOrigen.getSaldoFormateado());
        lblSaldo.setFont(UITheme.FONT_MONEY_SMALL);
        lblSaldo.setForeground(UITheme.ACCENT);
        
        card.add(iconPanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(lblSaldo, BorderLayout.EAST);
        
        addHoverEffect(card);
        
        return card;
    }
    
    private JPanel createFormCard() {
        UITheme.RoundedPanel formCard = new UITheme.RoundedPanel(20, UITheme.BG_CARD, true, false);
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(30, 35, 30, 35));
        formCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtCuentaDestino = UITheme.createRoundedTextField();
        txtMonto = UITheme.createRoundedTextField();
        txtDescripcion = UITheme.createRoundedTextField();

        JPanel destinoPanel = createInputGroup("Número de cuenta destino", txtCuentaDestino, "💳", "Ej: 191-12345678-9-01");
        JPanel montoPanel = createInputGroup("Monto a transferir", txtMonto, "💰", "Ingrese el monto en " + cuentaOrigen.getMoneda());
        JPanel descripcionPanel = createInputGroup("Concepto (opcional)", txtDescripcion, "📝", "Ej: Pago por servicios");
        
        formCard.add(destinoPanel);
        formCard.add(Box.createRigidArea(new Dimension(0, 22)));
        formCard.add(montoPanel);
        formCard.add(Box.createRigidArea(new Dimension(0, 22)));
        formCard.add(descripcionPanel);
        
        return formCard;
    }
    
    private JPanel createInputGroup(String labelText, JTextField field, String icon, String placeholder) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelPanel.setOpaque(false);
        labelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel iconLabel = new JLabel(icon + " ");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        
        JLabel label = new JLabel(labelText);
        label.setFont(UITheme.FONT_BODY_BOLD);
        label.setForeground(UITheme.TEXT_PRIMARY);
        
        labelPanel.add(iconLabel);
        labelPanel.add(label);
        
        field.setToolTipText(placeholder);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        panel.add(labelPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(field);
        
        return panel;
    }
    
    private JPanel createWarningPanel() {
        UITheme.RoundedPanel warningCard = new UITheme.RoundedPanel(14, new Color(UITheme.WARNING.getRed(), UITheme.WARNING.getGreen(), UITheme.WARNING.getBlue(), 20), false, false);
        warningCard.setLayout(new BorderLayout(15, 0));
        warningCard.setBorder(BorderFactory.createCompoundBorder(
            new UITheme.RoundedBorder(14, UITheme.WARNING, 1),
            new EmptyBorder(16, 20, 16, 20)
        ));
        warningCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        warningCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        JPanel iconContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(UITheme.WARNING.getRed(), UITheme.WARNING.getGreen(), UITheme.WARNING.getBlue(), 40));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconContainer.setOpaque(false);
        iconContainer.setPreferredSize(new Dimension(36, 36));
        iconContainer.setLayout(new GridBagLayout());
        
        JLabel warningIcon = new JLabel("⚠️");
        warningIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconContainer.add(warningIcon);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Aviso importante");
        titleLabel.setFont(UITheme.FONT_BODY_BOLD);
        titleLabel.setForeground(UITheme.WARNING);
        
        JLabel msgLabel = new JLabel("Verifica bien los datos antes de confirmar. Las transferencias son irreversibles.");
        msgLabel.setFont(UITheme.FONT_SMALL);
        msgLabel.setForeground(UITheme.TEXT_SECONDARY);
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        textPanel.add(msgLabel);
        
        warningCard.add(iconContainer, BorderLayout.WEST);
        warningCard.add(textPanel, BorderLayout.CENTER);
        
        return warningCard;
    }
    
    private JPanel createButtonsPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton btnTransferir = UITheme.createPrimaryButton("Confirmar Transferencia", "→");
        btnTransferir.setPreferredSize(new Dimension(220, 50));
        btnTransferir.addActionListener(e -> ejecutarTransferencia());
        
        JButton btnCancelar = UITheme.createSecondaryButton("Cancelar", "✕");
        btnCancelar.setPreferredSize(new Dimension(140, 50));
        btnCancelar.addActionListener(e -> dashboard.mostrarDashboardHome());
        
        buttonPanel.add(btnTransferir);
        buttonPanel.add(btnCancelar);
        
        return buttonPanel;
    }
    
    private void addHoverEffect(JPanel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                if (panel instanceof UITheme.RoundedPanel) {
                    ((UITheme.RoundedPanel) panel).setBackgroundColor(UITheme.BG_CARD_HOVER);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                if (panel instanceof UITheme.RoundedPanel) {
                    ((UITheme.RoundedPanel) panel).setBackgroundColor(UITheme.BG_CARD);
                }
            }
        });
    }
    
    // ══════════════════════════════════════════════════════════════════════════
    // LÓGICA DE BACKEND - NO MODIFICAR
    // ══════════════════════════════════════════════════════════════════════════
    
    private void ejecutarTransferencia() {
        try {
            BigDecimal monto = new BigDecimal(txtMonto.getText().trim());
            String numDestino = txtCuentaDestino.getText().trim();
            String descripcion = txtDescripcion.getText().trim().isEmpty() 
                ? "Transferencia a " + numDestino 
                : txtDescripcion.getText().trim();

            if (monto.compareTo(BigDecimal.ZERO) <= 0 || numDestino.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese una cuenta destino y un monto válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (monto.compareTo(cuentaOrigen.getSaldo()) > 0) {
                 JOptionPane.showMessageDialog(this, "Saldo insuficiente en la cuenta de origen.", "Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            
            // Validar que no se transfiera a sí mismo
            if (Objects.equals(cuentaOrigen.getNumeroCuenta(), numDestino)) {
                 JOptionPane.showMessageDialog(this, "No puedes transferir a la misma cuenta de origen.", "Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }

            // 1. Obtener Cuenta Destino
            Account cuentaDestino = accountDAO.obtenerPorNumero(numDestino);
            if (cuentaDestino == null) {
                 JOptionPane.showMessageDialog(this, "Error: Cuenta destino no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }

            // Confirmación con detalles
            String mensaje = String.format(
                "¿Confirmar transferencia?\n\n" +
                "De: %s\n" +
                "Para: %s\n" +
                "Monto: %s %.2f\n" +
                "Concepto: %s",
                cuentaOrigen.getNumeroCuenta(),
                numDestino,
                cuentaOrigen.getMoneda(),
                monto,
                descripcion
            );
            
            int confirmacion = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar Transferencia", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            // ✅ SOLUCIÓN: AccountDAO.transferir() ahora acepta la descripción
            // y registra la transacción internamente, NO duplicamos el registro
            boolean exito = accountDAO.transferir(
                cuentaOrigen.getId(), 
                cuentaDestino.getId(), 
                monto,
                descripcion  // ← Pasamos la descripción
            );

            if (exito) {
                JOptionPane.showMessageDialog(this, 
                    "¡Transferencia de " + cuentaOrigen.getMoneda() + " " + monto + " realizada exitosamente!", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                dashboard.actualizarDatos();    
                dashboard.mostrarDashboardHome();
            } else {
                JOptionPane.showMessageDialog(this, "Fallo al procesar la transferencia.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto numérico válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en el sistema: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
