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
import rw.gov.trafficpolice.model.ETicketStatus;
import rw.gov.trafficpolice.util.UITheme;
import java.awt.*;
import java.util.List;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import rw.gov.trafficpolice.util.DriverNotificationUtil;

public class DashboardForm extends JFrame {

    private JLabel lblTotalTickets;
    private JLabel lblPaidTickets;
    private JLabel lblOverdueTickets;
    private JLabel lblTotalFines;

    public DashboardForm() {
        initUI();
        loadStats();
    }

    private void initUI() {
        setTitle("Dashboard — Traffic Policy System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 680);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(
                new BorderLayout(0, 0));
        root.setBackground(new Color(240, 242, 248));

        JPanel topBar = new JPanel(
                new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0,
                    new Color(18, 50, 90),
                    getWidth(), 0,
                    new Color(31, 73, 125));
                g2.setPaint(gp);
                g2.fillRect(0, 0,
                    getWidth(), getHeight());
            }
        };
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory
                .createEmptyBorder(0, 0, 0, 0));
        topBar.setPreferredSize(new Dimension(0, 70));

        JPanel leftTop = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 15, 12));
        leftTop.setOpaque(false);

        JPanel badgePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 200, 0));
                g2.fillOval(2, 2, 42, 42);
                g2.setColor(new Color(220, 160, 0));
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(2, 2, 42, 42);
                g2.setColor(new Color(18, 50, 90));
                g2.setFont(new Font("Segoe UI",
                    Font.BOLD, 22));
                FontMetrics fm = g2.getFontMetrics();
                String star = "★";
                int x = (46 - fm.stringWidth(star))/2;
                g2.drawString(star, x, 30);
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(46, 46);
            }
        };
        badgePanel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(
                titlePanel, BoxLayout.Y_AXIS));

        JLabel lblSysTitle = new JLabel(
                "TRAFFIC POLICY MANAGEMENT SYSTEM");
        lblSysTitle.setFont(new Font(
                "Segoe UI", Font.BOLD, 16));
        lblSysTitle.setForeground(Color.WHITE);

        JLabel lblSubTitle = new JLabel(
                "Rwanda National Police  •  "
                + "AUCA  •  INSY 7312");
        lblSubTitle.setFont(new Font(
                "Segoe UI", Font.PLAIN, 11));
        lblSubTitle.setForeground(
                new Color(180, 200, 230));

        titlePanel.add(lblSysTitle);
        titlePanel.add(lblSubTitle);

        leftTop.add(badgePanel);
        leftTop.add(titlePanel);

        // Right side - User + Buttons
        JPanel rightTop = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 17));
        rightTop.setOpaque(false);

        JLabel lblUser = new JLabel(
                "👮  "
                + SessionManager.getCurrentOfficer()
                    .getFullName()
                + "  [" + SessionManager
                    .getCurrentOfficer().getRole()
                + "]");
        lblUser.setFont(new Font(
                "Segoe UI", Font.PLAIN, 12));
        lblUser.setForeground(Color.WHITE);

        JButton btnRefresh = makeTopButton(
                "↻  Refresh", new Color(46, 139, 87));
        JButton btnLogout = makeTopButton(
                "⏻  Logout", new Color(180, 40, 40));

 btnRefresh.addActionListener(e -> {
    lblTotalTickets.setText("...");
    lblPaidTickets.setText("...");
    lblOverdueTickets.setText("...");
    lblTotalFines.setText("...");
    loadStats();
    JOptionPane.showMessageDialog(
        this,
        "✅ Dashboard refreshed!\n\n"
        + "Overdue tickets have been\n"
        + "checked and updated.",
        "Refreshed",
        JOptionPane.INFORMATION_MESSAGE);
});

        btnLogout.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(
                this, "Are you sure you want"
                + " to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                SessionManager.logout();
                new LoginForm().setVisible(true);
                dispose();
            }
        });

        rightTop.add(lblUser);
        rightTop.add(btnRefresh);
        rightTop.add(btnLogout);

        topBar.add(leftTop,  BorderLayout.WEST);
        topBar.add(rightTop, BorderLayout.EAST);

        JPanel heroBanner = new JPanel(
                new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint bg = new GradientPaint(
                    0, 0, new Color(31, 73, 125),
                    getWidth(), getHeight(),
                    new Color(10, 35, 70));
                g2.setPaint(bg);
                g2.fillRect(0, 0,
                    getWidth(), getHeight());

                g2.setColor(new Color(255,255,255,15));
                g2.fillOval(-30, -30, 200, 200);
                g2.fillOval(getWidth()-120,
                    -40, 200, 200);
                g2.setColor(new Color(255,255,255,8));
                g2.fillOval(getWidth()/2 - 100,
                    -60, 250, 250);

                g2.setColor(new Color(255,255,255,20));
                g2.setStroke(new BasicStroke(4,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10, new float[]{20, 15}, 0));
                g2.drawLine(0, getHeight()/2,
                    getWidth(), getHeight()/2);

                int lx = getWidth() - 80;
                int ly = 15;
                g2.setColor(new Color(0,0,0,40));
                g2.fillRoundRect(lx, ly,
                    35, 90, 10, 10);
                g2.setColor(new Color(220,53,69,180));
                g2.fillOval(lx+5, ly+5, 25, 25);
                g2.setColor(new Color(255,165,0,100));
                g2.fillOval(lx+5, ly+33, 25, 25);
                g2.setColor(new Color(40,167,69,100));
                g2.fillOval(lx+5, ly+61, 25, 25);
                g2.setColor(new Color(255,255,255,25));
                g2.fillRoundRect(50, 25, 120, 50,
                    15, 15);
                g2.fillRoundRect(75, 10, 70, 30,
                    10, 10);
                g2.setColor(new Color(0,0,0,40));
                g2.fillOval(60, 68, 30, 30);
                g2.fillOval(130, 68, 30, 30);
                // Lights on car
                g2.setColor(new Color(220,53,69,200));
                g2.fillRect(80, 8, 15, 6);
                g2.setColor(new Color(0,100,255,200));
                g2.fillRect(100, 8, 15, 6);
            }
        };
        heroBanner.setOpaque(false);
        heroBanner.setPreferredSize(
                new Dimension(0, 115));

        JPanel heroText = new JPanel();
        heroText.setOpaque(false);
        heroText.setLayout(new BoxLayout(
                heroText, BoxLayout.Y_AXIS));
        heroText.setBorder(BorderFactory
                .createEmptyBorder(18, 220, 10, 10));

        JLabel lblHero = new JLabel(
                "Welcome, "
                + SessionManager.getCurrentOfficer()
                    .getFullName() + "!");
        lblHero.setFont(new Font(
                "Segoe UI", Font.BOLD, 22));
        lblHero.setForeground(Color.WHITE);
        lblHero.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblHeroSub = new JLabel(
                "🚦  Manage traffic violations,"
                + " fines, and enforcement"
                + " from one place.");
        lblHeroSub.setFont(new Font(
                "Segoe UI", Font.PLAIN, 13));
        lblHeroSub.setForeground(
                new Color(180, 210, 240));
        lblHeroSub.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        JLabel lblDate = new JLabel(
                "📅  " + java.time.LocalDate.now()
                .format(java.time.format
                .DateTimeFormatter
                .ofPattern("EEEE, MMMM dd yyyy")));
        lblDate.setFont(new Font(
                "Segoe UI", Font.PLAIN, 11));
        lblDate.setForeground(
                new Color(150, 180, 220));
        lblDate.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        heroText.add(lblHero);
        heroText.add(Box.createVerticalStrut(4));
        heroText.add(lblHeroSub);
        heroText.add(Box.createVerticalStrut(4));
        heroText.add(lblDate);
        heroBanner.add(heroText, BorderLayout.CENTER);

        lblTotalTickets   = new JLabel("...",
                SwingConstants.CENTER);
        lblPaidTickets    = new JLabel("...",
                SwingConstants.CENTER);
        lblOverdueTickets = new JLabel("...",
                SwingConstants.CENTER);
        lblTotalFines     = new JLabel("...",
                SwingConstants.CENTER);

        JPanel statsPanel = new JPanel(
                new GridLayout(1, 4, 12, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory
                .createEmptyBorder(15, 15, 8, 15));

        statsPanel.add(makeStatCard(
            "🎫  Total Tickets",
            lblTotalTickets,
            new Color(31, 73, 125),
            new Color(220, 235, 255)));
        statsPanel.add(makeStatCard(
            "✅  Paid Tickets",
            lblPaidTickets,
            new Color(40, 167, 69),
            new Color(220, 255, 230)));
        statsPanel.add(makeStatCard(
            "⏰  Overdue Tickets",
            lblOverdueTickets,
            new Color(220, 53, 69),
            new Color(255, 220, 225)));
        statsPanel.add(makeStatCard(
            "💰  Fines Collected",
            lblTotalFines,
            new Color(180, 100, 0),
            new Color(255, 240, 210)));

        JPanel navPanel = new JPanel(
                new GridLayout(3, 3, 12, 12));
        navPanel.setOpaque(false);
        navPanel.setBorder(BorderFactory
                .createEmptyBorder(8, 15, 15, 15));

        navPanel.add(makeNavCard(
            "🚗", "DRIVERS",
            "Register & manage driver records",
            new Color(31, 73, 125),
            new Color(214, 228, 255),
            () -> new DriverForm().setVisible(true)));

        navPanel.add(makeNavCard(
            "🎫", "TICKETS",
            "Issue & track traffic tickets",
            new Color(148, 0, 211),
            new Color(240, 220, 255),
            () -> new TicketForm().setVisible(true)));

        navPanel.add(makeNavCard(
            "💳", "PAYMENTS",
            "Process & record fine payments",
            new Color(40, 167, 69),
            new Color(220, 255, 230),
            () -> new PaymentForm().setVisible(true)));

        navPanel.add(makeNavCard(
            "⚠️", "VIOLATION TYPES",
            "Configure violation categories",
            new Color(180, 100, 0),
            new Color(255, 240, 210),
            () -> new ViolationTypeForm()
                .setVisible(true)));

        navPanel.add(makeNavCard(
            "👮", "OFFICERS",
            "Manage officer accounts & roles",
            new Color(18, 50, 90),
            new Color(210, 225, 255),
            () -> new OfficerForm()
                .setVisible(true)));

        navPanel.add(makeNavCard(
            "📊", "REPORTS",
            "Generate & export PDF / Excel",
            new Color(180, 40, 40),
            new Color(255, 220, 220),
            () -> new ReportForm()
                .setVisible(true)));
        navPanel.add(makeNavCard(
    "📊", "STATISTICS",
    "Live charts & analytics",
    new Color(0, 128, 128),
    new Color(210, 245, 245),
    () -> new StatisticsForm()
        .setVisible(true)));

        JPanel footer = new JPanel(
                new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(31, 73, 125));
                g2.fillRect(0, 0,
                    getWidth(), getHeight());
            }
        };
        footer.setOpaque(false);
        footer.setPreferredSize(new Dimension(0, 30));

        JLabel lblFooter = new JLabel(
            "  🚔  Traffic Policy Management System"
            + "  |  AUCA  |  INSY 7312"
            + " Java Programming"
            + "  |  Student ID: 27276"
            + "  |  NTWALI EMERY",
            SwingConstants.CENTER);
        lblFooter.setFont(new Font(
                "Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(Color.WHITE);
        footer.add(lblFooter, BorderLayout.CENTER);

        JPanel headerSection = new JPanel(
                new BorderLayout());
        headerSection.setOpaque(false);
        headerSection.add(topBar,    BorderLayout.NORTH);
        headerSection.add(heroBanner,BorderLayout.CENTER);

        JPanel centerSection = new JPanel(
                new BorderLayout());
        centerSection.setOpaque(false);
        centerSection.add(statsPanel,BorderLayout.NORTH);
        centerSection.add(navPanel,  BorderLayout.CENTER);

        root.add(headerSection,  BorderLayout.NORTH);
        root.add(centerSection,  BorderLayout.CENTER);
        root.add(footer,         BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JButton makeTopButton(
            String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI",
                Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(110, 34));
        btn.setBorder(BorderFactory
                .createEmptyBorder(5, 10, 5, 10));
        return btn;
    }

    private JPanel makeStatCard(String title,
            JLabel valueLabel,
            Color accent, Color bg) {
        JPanel card = new JPanel(
                new BorderLayout(0, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0,
                    getWidth(), getHeight(), 15, 15);
                // Left accent bar
                g2.setColor(accent);
                g2.fillRoundRect(0, 0,
                    6, getHeight(), 6, 6);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory
                .createEmptyBorder(15, 18, 15, 15));

        JLabel lbl = new JLabel(title,
                SwingConstants.LEFT);
        lbl.setFont(new Font("Segoe UI",
                Font.BOLD, 12));
        lbl.setForeground(accent);

        valueLabel.setFont(new Font(
                "Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(accent);
        valueLabel.setHorizontalAlignment(
                SwingConstants.LEFT);

        JLabel lblDesc = new JLabel(
                "View details →");
        lblDesc.setFont(new Font(
                "Segoe UI", Font.PLAIN, 10));
        lblDesc.setForeground(
                new Color(accent.getRed(),
                accent.getGreen(),
                accent.getBlue(), 150));

        JPanel bottom = new JPanel(
                new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(valueLabel, BorderLayout.CENTER);
        bottom.add(lblDesc,    BorderLayout.SOUTH);

        card.add(lbl,    BorderLayout.NORTH);
        card.add(bottom, BorderLayout.CENTER);
        return card;
    }

    private JPanel makeNavCard(
            String icon, String title,
            String subtitle,
            Color accent, Color bg,
            Runnable action) {

        JPanel card = new JPanel(
                new BorderLayout(0, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0,
                    getWidth(), getHeight(), 15, 15);
                // Top accent bar
                g2.setColor(accent);
                g2.fillRoundRect(0, 0,
                    getWidth(), 5, 5, 5);
            }
        };
        card.setBackground(Color.WHITE);
        card.setOpaque(false);
        card.setBorder(BorderFactory
                .createEmptyBorder(18, 18, 18, 18));
        card.setCursor(Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR));

        JPanel iconCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillOval(0, 0,
                    getWidth()-1, getHeight()-1);
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(50, 50);
            }
        };
        iconCircle.setOpaque(false);
        iconCircle.setLayout(
                new GridBagLayout());

        JLabel lblIcon = new JLabel(icon,
                SwingConstants.CENTER);
        lblIcon.setFont(new Font(
                "Segoe UI Emoji", Font.PLAIN, 22));
        iconCircle.add(lblIcon);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font(
                "Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(accent);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font(
                "Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(
                new Color(100, 100, 120));

        JLabel lblArrow = new JLabel(
                "Open →");
        lblArrow.setFont(new Font(
                "Segoe UI", Font.BOLD, 11));
        lblArrow.setForeground(accent);

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(
                textPanel, BoxLayout.Y_AXIS));
        textPanel.add(lblTitle);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(lblSub);
        textPanel.add(Box.createVerticalStrut(6));
        textPanel.add(lblArrow);

        JPanel topRow = new JPanel(
                new FlowLayout(
                    FlowLayout.LEFT, 12, 0));
        topRow.setOpaque(false);
        topRow.add(iconCircle);
        topRow.add(textPanel);

        card.add(topRow, BorderLayout.CENTER);

        card.addMouseListener(
            new java.awt.event.MouseAdapter() {
                Color original = Color.WHITE;
                @Override
                public void mouseClicked(
                        java.awt.event.MouseEvent e) {
                    action.run();
                }
                @Override
                public void mouseEntered(
                        java.awt.event.MouseEvent e) {
                    card.setBackground(bg);
                    card.repaint();
                }
                @Override
                public void mouseExited(
                        java.awt.event.MouseEvent e) {
                    card.setBackground(original);
                    card.repaint();
                }
            });

        return card;
    }

    private void loadStats() {
    new SwingWorker<int[], Void>() {
        int total   = 0;
        int paid    = 0;
        int overdue = 0;
        double fines = 0;

        @Override
        protected int[] doInBackground() {
            try {
                markOverdueTickets();

                total = ClientConnector
                    .ticketService
                    .findAllTicketRecords()
                    .size();

                paid = ClientConnector
                    .ticketService
                    .findTicketsByStatus(
                        ETicketStatus.PAID)
                    .size();

                overdue = ClientConnector
                    .ticketService
                    .findTicketsByStatus(
                        ETicketStatus.OVERDUE)
                    .size();

                fines = ClientConnector
                    .paymentService
                    .getTotalCollectedFines();

                // Send overdue reminders
                List<rw.gov.trafficpolice.model
                    .Ticket> overdueList =
                    ClientConnector.ticketService
                    .findTicketsByStatus(
                        ETicketStatus.OVERDUE);

                for (rw.gov.trafficpolice.model
                        .Ticket ot : overdueList) {
                    DriverNotificationUtil
                        .notifyTicketOverdue(ot);
                }

                System.out.println(
                    "[DASHBOARD] Stats loaded: "
                    + "Total=" + total
                    + " Paid=" + paid
                    + " Overdue=" + overdue
                    + " Fines=" + fines);

            } catch (Exception ex) {
                System.err.println(
                    "[DASHBOARD] Stats error: "
                    + ex.getMessage());
            }
            return new int[]{0};
        }

        @Override
        protected void done() {
            SwingUtilities.invokeLater(() -> {
                lblTotalTickets.setText(
                    String.valueOf(total));
                lblPaidTickets.setText(
                    String.valueOf(paid));
                lblOverdueTickets.setText(
                    String.valueOf(overdue));
                lblTotalFines.setText("RWF "
                    + String.format(
                        "%,.0f", fines));
            });
        }
    }.execute();
}

private void markOverdueTickets() {
    try {
        List<rw.gov.trafficpolice.model.Ticket>
            issued = ClientConnector.ticketService
            .findTicketsByStatus(
                ETicketStatus.ISSUED);

        java.util.Date today = new java.util.Date();

        for (rw.gov.trafficpolice.model
                .Ticket t : issued) {
            if (t.getDueDate() != null
                    && t.getDueDate().before(today)) {
                t.setStatus(ETicketStatus.OVERDUE);
                ClientConnector.ticketService
                    .updateTicketRecord(t);
                System.out.println(
                    "[DASHBOARD] Marked overdue: "
                    + t.getTicketNumber());
            }
        }
    } catch (Exception ex) {
        System.err.println(
            "[DASHBOARD] Mark overdue error: "
            + ex.getMessage());
    }
}
}