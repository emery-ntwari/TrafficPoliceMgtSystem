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
import rw.gov.trafficpolice.model.*;
import rw.gov.trafficpolice.util.UITheme;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import javax.swing.*;


public class StatisticsForm extends JFrame {

    private Map<String, Integer> violationCounts
            = new LinkedHashMap<>();
    private Map<String, Integer> statusCounts
            = new LinkedHashMap<>();
    private Map<String, Double> officerFines
            = new LinkedHashMap<>();

    public StatisticsForm() {
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("Statistics & Analytics");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(
                new BorderLayout(10, 10));
        root.setBackground(
                new Color(240, 242, 248));
        root.setBorder(BorderFactory
                .createEmptyBorder(15, 15, 15, 15));

        
        JPanel header = new JPanel(
                new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp =
                    new GradientPaint(
                        0, 0,
                        new Color(18, 50, 90),
                        getWidth(), 0,
                        new Color(31, 73, 125));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0,
                    getWidth(), getHeight(),
                    12, 12);
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(
                new Dimension(0, 60));
        header.setBorder(BorderFactory
                .createEmptyBorder(12, 20, 12, 20));

        JLabel lblTitle = new JLabel(
                "📊  Statistics & Analytics Dashboard");
        lblTitle.setFont(new Font(
                "Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);

        JButton btnRefresh = new JButton(
                "↻  Refresh");
        btnRefresh.setBackground(
                new Color(46, 139, 87));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font(
                "Segoe UI", Font.BOLD, 12));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setPreferredSize(
                new Dimension(110, 36));
        btnRefresh.setCursor(
                Cursor.getPredefinedCursor(
                    Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(
                e -> loadData());

        header.add(lblTitle, BorderLayout.WEST);
        header.add(btnRefresh, BorderLayout.EAST);

       
        JPanel chartsPanel = new JPanel(
                new GridLayout(2, 2, 15, 15));
        chartsPanel.setOpaque(false);

      
        JPanel violationChart = new JPanel(
                new BorderLayout()) {
            @Override
            protected void paintComponent(
                    Graphics g) {
                super.paintComponent(g);
                drawBarChart(g, violationCounts,
                    "Violations by Type",
                    new Color(31, 73, 125));
            }
        };
        violationChart.setBackground(Color.WHITE);
        violationChart.setBorder(
                BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    UITheme.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(
                    10, 10, 10, 10)));

      
        JPanel statusChart = new JPanel(
                new BorderLayout()) {
            @Override
            protected void paintComponent(
                    Graphics g) {
                super.paintComponent(g);
                drawPieChart(g, statusCounts,
                    "Ticket Status Distribution");
            }
        };
        statusChart.setBackground(Color.WHITE);
        statusChart.setBorder(
                BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    UITheme.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(
                    10, 10, 10, 10)));

       
        JPanel officerChart = new JPanel(
                new BorderLayout()) {
            @Override
            protected void paintComponent(
                    Graphics g) {
                super.paintComponent(g);
                drawBarChart(g,
                    convertDoubleToInt(officerFines),
                    "Fines Collected by Officer",
                    new Color(40, 167, 69));
            }
        };
        officerChart.setBackground(Color.WHITE);
        officerChart.setBorder(
                BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    UITheme.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(
                    10, 10, 10, 10)));

      
        JPanel summaryPanel = buildSummaryPanel();

        chartsPanel.add(violationChart);
        chartsPanel.add(statusChart);
        chartsPanel.add(officerChart);
        chartsPanel.add(summaryPanel);

        root.add(header,      BorderLayout.NORTH);
        root.add(chartsPanel, BorderLayout.CENTER);
        setContentPane(root);
    }


    // DRAW BAR CHART

    private void drawBarChart(Graphics g,
            Map<String, Integer> data,
            String title, Color barColor) {

        if (data == null || data.isEmpty()) {
            drawNoData(g, title);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int padding = 50;
        int topPad  = 40;

       
        g2.setFont(new Font(
                "Segoe UI", Font.BOLD, 13));
        g2.setColor(new Color(31, 73, 125));
        g2.drawString(title, padding, 25);


        int maxVal = data.values().stream()
                .mapToInt(Integer::intValue)
                .max().orElse(1);

        int chartW = w - padding * 2;
        int chartH = h - topPad - padding;
        int barW   = Math.max(
                chartW / data.size() - 10, 20);
        int x = padding;

   
        g2.setColor(new Color(230, 235, 245));
        for (int i = 0; i <= 5; i++) {
            int y = topPad + chartH
                    - (i * chartH / 5);
            g2.drawLine(padding, y,
                    w - padding, y);
            g2.setColor(new Color(150, 150, 150));
            g2.setFont(new Font(
                    "Segoe UI", Font.PLAIN, 9));
            g2.drawString(
                String.valueOf(maxVal * i / 5),
                5, y + 4);
            g2.setColor(new Color(230, 235, 245));
        }

      
        String[] keys = data.keySet()
                .toArray(new String[0]);
        Color[] colors = {
            new Color(31, 73, 125),
            new Color(40, 167, 69),
            new Color(220, 53, 69),
            new Color(255, 165, 0),
            new Color(148, 0, 211),
            new Color(0, 128, 128),
            new Color(255, 99, 71),
            new Color(70, 130, 180)
        };

        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            int val = data.get(key);
            int barH = (int)((double) val
                    / maxVal * chartH);
            int barX = x + 5;
            int barY = topPad + chartH - barH;

          
            Color c = colors[i % colors.length];
            GradientPaint gp = new GradientPaint(
                barX, barY,
                c.brighter(),
                barX, barY + barH, c);
            g2.setPaint(gp);
            g2.fillRoundRect(barX, barY,
                    barW, barH, 6, 6);

            
            g2.setColor(new Color(50, 50, 50));
            g2.setFont(new Font(
                    "Segoe UI", Font.BOLD, 10));
            g2.drawString(String.valueOf(val),
                barX + barW/2 - 5, barY - 4);

           
            g2.setFont(new Font(
                    "Segoe UI", Font.PLAIN, 9));
            g2.setColor(new Color(80, 80, 80));
            String label = key.length() > 8
                ? key.substring(0, 7) + ".."
                : key;
            g2.drawString(label,
                barX + barW/2
                    - g2.getFontMetrics()
                        .stringWidth(label)/2,
                topPad + chartH + 15);

            x += barW + 10;
        }
    }

    //  DRAW PIE CHART
    private void drawPieChart(Graphics g,
            Map<String, Integer> data,
            String title) {

        if (data == null || data.isEmpty()) {
            drawNoData(g, title);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Title
        g2.setFont(new Font(
                "Segoe UI", Font.BOLD, 13));
        g2.setColor(new Color(31, 73, 125));
        g2.drawString(title, 15, 25);

        int total = data.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        if (total == 0) {
            drawNoData(g, title); return;
        }

        int size   = Math.min(w, h) - 100;
        int cx     = w / 2 - 40;
        int cy     = h / 2 + 10;
        int radius = size / 2;

        Color[] colors = {
            new Color(17, 99, 178),
            new Color(40, 167, 69),
            new Color(220, 53, 69),
            new Color(255, 165, 0)
        };

        String[] keys = data.keySet()
                .toArray(new String[0]);
        double startAngle = 0;

        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            int val = data.get(key);
            double angle =
                (double) val / total * 360;

            g2.setColor(colors[i % colors.length]);
            g2.fill(new Arc2D.Double(
                cx - radius, cy - radius,
                radius * 2, radius * 2,
                startAngle, angle,
                Arc2D.PIE));

            
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.draw(new Arc2D.Double(
                cx - radius, cy - radius,
                radius * 2, radius * 2,
                startAngle, angle,
                Arc2D.PIE));

          
            int ly = 40 + i * 22;
            g2.setColor(colors[i % colors.length]);
            g2.fillRoundRect(w - 120, ly - 12,
                    14, 14, 4, 4);
            g2.setColor(new Color(50, 50, 50));
            g2.setFont(new Font(
                    "Segoe UI", Font.PLAIN, 11));
            int pct = (int)(
                (double) val / total * 100);
            g2.drawString(
                key + " " + pct + "%",
                w - 102, ly);

            startAngle += angle;
        }
    }

    
    //  BUILD SUMMARY STATS PANEL
    private JPanel buildSummaryPanel() {
        JPanel panel = new JPanel(
                new GridLayout(4, 1, 8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    UITheme.BORDER_COLOR),
                BorderFactory.createEmptyBorder(
                    15, 15, 15, 15)));

        JLabel title = new JLabel(
                "📈  Quick Summary");
        title.setFont(new Font(
                "Segoe UI", Font.BOLD, 13));
        title.setForeground(
                new Color(31, 73, 125));

        panel.add(title);

        try {
            List<Ticket> all = ClientConnector
                .ticketService.findAllTicketRecords();

            int issued = 0, paid = 0,
                overdue = 0, escalated = 0;
            double totalFines = 0, collectedFines = 0;

            for (Ticket t : all) {
                switch (t.getStatus()) {
                    case ISSUED:
                        issued++; break;
                    case PAID:
                        paid++;
                        collectedFines +=
                            t.getTotalFineAmount();
                        break;
                    case OVERDUE:
                        overdue++; break;
                    case ESCALATED:
                        escalated++; break;
                }
                totalFines += t.getTotalFineAmount();
            }

            panel.add(makeSummaryRow(
                "💰 Total Fines Issued:",
                "RWF " + String.format(
                    "%,.0f", totalFines),
                new Color(31, 73, 125)));
            panel.add(makeSummaryRow(
                "✅ Total Collected:",
                "RWF " + String.format(
                    "%,.0f", collectedFines),
                new Color(40, 167, 69)));
            panel.add(makeSummaryRow(
                "📊 Collection Rate:",
                totalFines > 0
                ? String.format("%.1f%%",
                    collectedFines/totalFines*100)
                : "0%",
                new Color(148, 0, 211)));

        } catch (Exception ex) {
            panel.add(new JLabel("Loading..."));
        }

        return panel;
    }

    private JPanel makeSummaryRow(
            String label, String value,
            Color color) {
        JPanel row = new JPanel(
                new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory
                .createEmptyBorder(5, 5, 5, 5));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font(
                "Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(80, 80, 80));

        JLabel val = new JLabel(value);
        val.setFont(new Font(
                "Segoe UI", Font.BOLD, 13));
        val.setForeground(color);

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);
        return row;
    }

    private void drawNoData(Graphics g,
            String title) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font(
                "Segoe UI", Font.BOLD, 13));
        g2.setColor(new Color(31, 73, 125));
        g2.drawString(title, 15, 25);
        g2.setColor(new Color(150, 150, 150));
        g2.setFont(new Font(
                "Segoe UI", Font.PLAIN, 12));
        g2.drawString("No data available yet.",
            getWidth()/2 - 60,
            getHeight()/2);
    }

    private Map<String, Integer>
            convertDoubleToInt(
            Map<String, Double> map) {
        Map<String, Integer> result =
                new LinkedHashMap<>();
        map.forEach((k, v) ->
            result.put(k, (int)(v / 1000)));
        return result;
    }

    //  LOAD DATA FROM SERVER
    private void loadData() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    // Load violation counts
                    violationCounts.clear();
                    List<Ticket> tickets =
                        ClientConnector.ticketService
                        .findAllTicketRecords();

                    for (Ticket t : tickets) {
                        if (t.getViolationTypes()
                                != null) {
                            t.getViolationTypes()
                                .forEach(vt ->
                                violationCounts.merge(
                                    vt.getName()
                                    .length() > 10
                                    ? vt.getName()
                                        .substring(0, 9)
                                        + ".."
                                    : vt.getName(),
                                    1, Integer::sum));
                        }
                    }

                    statusCounts.clear();
                    statusCounts.put("ISSUED",
                        (int) tickets.stream()
                        .filter(t -> t.getStatus()
                            == ETicketStatus.ISSUED)
                        .count());
                    statusCounts.put("PAID",
                        (int) tickets.stream()
                        .filter(t -> t.getStatus()
                            == ETicketStatus.PAID)
                        .count());
                    statusCounts.put("OVERDUE",
                        (int) tickets.stream()
                        .filter(t -> t.getStatus()
                            == ETicketStatus.OVERDUE)
                        .count());
                    statusCounts.put("ESCALATED",
                        (int) tickets.stream()
                        .filter(t -> t.getStatus()
                            == ETicketStatus.ESCALATED)
                        .count());

                    officerFines.clear();
                    for (Ticket t : tickets) {
                        if (t.getStatus()
                                == ETicketStatus.PAID
                                && t.getOfficer()
                                    != null) {
                            officerFines.merge(
                                t.getOfficer()
                                .getFullName()
                                .split(" ")[0],
                                t.getTotalFineAmount(),
                                Double::sum);
                        }
                    }

                    System.out.println(
                        "[STATS] Data loaded!");

                } catch (Exception ex) {
                    System.err.println(
                        "[STATS] Error: "
                        + ex.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                repaint();
            }
        }.execute();
    }
}