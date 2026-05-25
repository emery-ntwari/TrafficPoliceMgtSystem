/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rw.gov.trafficpolice.view;

/**
 *
 * @author PC
 */
import rw.gov.trafficpolice.controller.ClientConnector;
import rw.gov.trafficpolice.controller.SessionManager;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import rw.gov.trafficpolice.model.Officer;
import rw.gov.trafficpolice.util.OTPUtil;
import rw.gov.trafficpolice.util.UITheme;

public class LoginForm extends JFrame {
        private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JTextField     txtOTP;
    private JButton        btnLogin;
    private JButton        btnVerifyOTP;
    private JButton        btnResendOTP;
    private JPanel         credPanel;
    private JPanel         otpPanel;
    private JLabel         lblOTPInfo;
    private Officer        pendingOfficer;

    public LoginForm() {
        initUI();
    }

    private void initUI() {
        setTitle("Traffic Policy Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.WHITE);

        JPanel header = new JPanel();
        header.setBackground(UITheme.PRIMARY);
        header.setLayout(new BoxLayout(header,
                BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory
                .createEmptyBorder(30, 20, 25, 20));

        JLabel lblIcon = new JLabel(
                "TRAFFIC POLICY SYSTEM",
                SwingConstants.CENTER);
        lblIcon.setFont(UITheme.FONT_TITLE);
        lblIcon.setForeground(UITheme.WHITE);
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel(
                "AUCA  |  NTWALI EMERY  |  27276",
                SwingConstants.CENTER);
        lblSub.setFont(UITheme.FONT_SMALL);
        lblSub.setForeground(new Color(180, 200, 230));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(lblIcon);
        header.add(Box.createVerticalStrut(8));
        header.add(lblSub);

        credPanel = new JPanel(new GridBagLayout());
        credPanel.setBackground(UITheme.WHITE);
        credPanel.setBorder(BorderFactory
                .createEmptyBorder(30, 40, 20, 40));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 0, 6, 0);
        g.gridx = 0;

        JLabel lblHead = new JLabel("Officer Login");
        lblHead.setFont(UITheme.FONT_SUBTITLE);
        lblHead.setForeground(UITheme.PRIMARY);
        g.gridy = 0;
        credPanel.add(lblHead, g);

        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(UITheme.FONT_LABEL);
        g.gridy = 1;
        credPanel.add(lblUser, g);

        txtUsername = new JTextField(20);
        UITheme.styleField(txtUsername);
        g.gridy = 2;
        credPanel.add(txtUsername, g);

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(UITheme.FONT_LABEL);
        g.gridy = 3;
        credPanel.add(lblPass, g);

        txtPassword = new JPasswordField(20);
        UITheme.styleField(txtPassword);
        g.gridy = 4;
        credPanel.add(txtPassword, g);

        btnLogin = new JButton("LOGIN");
        btnLogin.setPreferredSize(new Dimension(0, 42));
        UITheme.styleButton(btnLogin, UITheme.PRIMARY);
        btnLogin.setMaximumSize(new Dimension(
                Integer.MAX_VALUE, 42));
        g.gridy = 5;
        g.insets = new Insets(16, 0, 6, 0);
        credPanel.add(btnLogin, g);

        //OTP PANEL 
        otpPanel = new JPanel(new GridBagLayout());
        otpPanel.setBackground(UITheme.WHITE);
        otpPanel.setBorder(BorderFactory
                .createEmptyBorder(30, 40, 20, 40));
        otpPanel.setVisible(false);

        GridBagConstraints og = new GridBagConstraints();
        og.fill = GridBagConstraints.HORIZONTAL;
        og.insets = new Insets(6, 0, 6, 0);
        og.gridx = 0;

        JLabel lblOTPTitle = new JLabel("OTP Verification");
        lblOTPTitle.setFont(UITheme.FONT_SUBTITLE);
        lblOTPTitle.setForeground(UITheme.PRIMARY);
        og.gridy = 0;
        otpPanel.add(lblOTPTitle, og);

        lblOTPInfo = new JLabel(
                "Enter the 6-digit code.");
        lblOTPInfo.setFont(UITheme.FONT_SMALL);
        lblOTPInfo.setForeground(UITheme.TEXT_MUTED);
        og.gridy = 1;
        otpPanel.add(lblOTPInfo, og);

        JLabel lblOTPCode = new JLabel("OTP Code");
        lblOTPCode.setFont(UITheme.FONT_LABEL);
        og.gridy = 2;
        otpPanel.add(lblOTPCode, og);

        txtOTP = new JTextField(20);
        txtOTP.setFont(new Font("Monospaced",
                Font.BOLD, 22));
        txtOTP.setHorizontalAlignment(JTextField.CENTER);
        UITheme.styleField(txtOTP);
        og.gridy = 3;
        otpPanel.add(txtOTP, og);

        btnVerifyOTP = new JButton("VERIFY OTP");
        btnVerifyOTP.setPreferredSize(
                new Dimension(0, 42));
        UITheme.styleButton(btnVerifyOTP, UITheme.SUCCESS);
        og.gridy = 4;
        og.insets = new Insets(16, 0, 6, 0);
        otpPanel.add(btnVerifyOTP, og);

        btnResendOTP = new JButton("Resend OTP");
        btnResendOTP.setFont(UITheme.FONT_SMALL);
        btnResendOTP.setForeground(UITheme.PRIMARY);
        btnResendOTP.setBorderPainted(false);
        btnResendOTP.setContentAreaFilled(false);
        btnResendOTP.setCursor(Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR));
        og.gridy = 5;
        og.insets = new Insets(4, 0, 4, 0);
        otpPanel.add(btnResendOTP, og);

        JLabel footer = new JLabel(
                "INSY 7312  Java Programming  |  AUCA 2025-2026",
                SwingConstants.CENTER);
        footer.setFont(UITheme.FONT_SMALL);
        footer.setForeground(UITheme.TEXT_MUTED);
        footer.setBorder(BorderFactory
                .createEmptyBorder(10, 10, 14, 10));

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(UITheme.WHITE);
        center.add(credPanel, BorderLayout.NORTH);
        center.add(otpPanel,  BorderLayout.CENTER);

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        setContentPane(root);

        // ── EVENTS ───────────────────────────────────────
        btnLogin.addActionListener(
                e -> handleLogin());
        txtPassword.addActionListener(
                e -> handleLogin());
        btnVerifyOTP.addActionListener(
                e -> handleOTPVerification());
        btnResendOTP.addActionListener(
                e -> handleResendOTP());
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(
                txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Username and password are required.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Officer officer =
                ClientConnector.officerService
                    .login(username, password);

            if (officer == null) {
                JOptionPane.showMessageDialog(this,
                    "Invalid username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!officer.isActive()) {
                JOptionPane.showMessageDialog(this,
                    "Your account is deactivated.\n"
                    + "Contact an administrator.",
                    "Account Disabled",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

pendingOfficer = officer;
String otp = OTPUtil.generateAndSendOTP(
        officer.getEmail(),
        officer.getRole(),
        officer.getUsername());

if (officer.getRole()
        .equalsIgnoreCase("ADMIN")) {
    lblOTPInfo.setText(
        "📧 OTP sent to: "
        + officer.getEmail());
} else {
    lblOTPInfo.setText(
        "📱 OTP sent to phone: "
        + officer.getPhoneNumber());
}

            credPanel.setVisible(false);
            otpPanel.setVisible(true);
            txtOTP.requestFocus();

            JOptionPane.showMessageDialog(this,
                "DEV MODE — Your OTP is:\n\n"
                + "         " + otp + "\n\n"
                + "(In production this is emailed)",
                "OTP Sent",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Cannot connect to server.\n"
                + "Make sure the Server is running first!",
                "Connection Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleOTPVerification() {
        String entered = txtOTP.getText().trim();

        if (entered.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter the OTP code.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (entered.length() != 6) {
            JOptionPane.showMessageDialog(this,
                "OTP must be exactly 6 digits.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (OTPUtil.validateOTP(entered)) {
            OTPUtil.clearOTP();
            SessionManager.setCurrentOfficer(
                    pendingOfficer);
            JOptionPane.showMessageDialog(this,
                "Welcome, "
                + pendingOfficer.getFullName() + "!",
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE);
            new DashboardForm().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid or expired OTP.\n"
                + "Please try again.",
                "OTP Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

private void handleResendOTP() {
    if (pendingOfficer != null) {
        String otp = OTPUtil.generateAndSendOTP(
                pendingOfficer.getEmail(),
                pendingOfficer.getRole(),
                pendingOfficer.getUsername());
        System.out.println(
            "[OTP] Resent for: "
            + pendingOfficer.getUsername());
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (!ClientConnector.connect()) {
                JOptionPane.showMessageDialog(null,
                    "Cannot connect to server.\n"
                    + "Please start the Server first!",
                    "Connection Failed",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            new LoginForm().setVisible(true);
        });
    }
}
