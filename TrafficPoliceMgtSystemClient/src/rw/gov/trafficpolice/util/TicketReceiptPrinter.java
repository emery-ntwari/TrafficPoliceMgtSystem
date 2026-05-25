/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rw.gov.trafficpolice.util;
/**
 *
 * @author PC
 */

import java.awt.*;
import java.awt.print.*;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import rw.gov.trafficpolice.model.Ticket;


public class TicketReceiptPrinter implements Printable {

    private final Ticket ticket;
    private static final SimpleDateFormat sdf =
            new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat stf =
            new SimpleDateFormat("HH:mm:ss");

    public TicketReceiptPrinter(Ticket ticket) {
        this.ticket = ticket;
    }


    public static void printReceipt(Ticket ticket) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Traffic Ticket - "
                + ticket.getTicketNumber());

        PageFormat pf = job.defaultPage();
        Paper paper = new Paper();
        double width  = 148 * 2.83465;
        double height = 210 * 2.83465;
        paper.setSize(width, height);
        paper.setImageableArea(
                20, 20,
                width - 40, height - 40);
        pf.setPaper(paper);
        pf.setOrientation(PageFormat.PORTRAIT);

        job.setPrintable(
                new TicketReceiptPrinter(ticket),
                pf);

        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
                JOptionPane.showMessageDialog(
                    null,
                    "✅ Ticket receipt sent"
                    + " to printer!\n\n"
                    + "Ticket: "
                    + ticket.getTicketNumber(),
                    "Printed Successfully",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(
                    null,
                    "❌ Printing failed:\n"
                    + ex.getMessage(),
                    "Print Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public int print(Graphics graphics,
            PageFormat pageFormat,
            int pageIndex)
            throws PrinterException {

        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.translate(pageFormat.getImageableX(),
                pageFormat.getImageableY());

        int w = (int) pageFormat.getImageableWidth();
        int y = 0;

        g2.setColor(new Color(18, 50, 90));
        g2.fillRoundRect(0, y, w, 70, 10, 10);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font(
                "Arial", Font.BOLD, 14));
        drawCentered(g2, "RWANDA NATIONAL POLICE",
                w, y + 20);
        g2.setFont(new Font(
                "Arial", Font.PLAIN, 10));
        drawCentered(g2,
                "Traffic Management Department",
                w, y + 35);
        drawCentered(g2,
                "KN 4 Ave, Kigali, Rwanda",
                w, y + 48);
        drawCentered(g2,
                "Tel: +250 788 311 155",
                w, y + 60);
        y += 80;

        g2.setColor(new Color(220, 53, 69));
        g2.fillRoundRect(0, y, w, 30, 6, 6);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font(
                "Arial", Font.BOLD, 13));
        drawCentered(g2,
                "TRAFFIC VIOLATION NOTICE",
                w, y + 20);
        y += 38;

        g2.setColor(new Color(240, 245, 255));
        g2.fillRoundRect(0, y, w, 28, 6, 6);
        g2.setColor(new Color(18, 50, 90));
        g2.setFont(new Font(
                "Arial", Font.BOLD, 12));
        drawCentered(g2,
                "Ticket No: "
                + ticket.getTicketNumber(),
                w, y + 18);
        y += 36;

        g2.setColor(new Color(200, 210, 230));
        g2.drawLine(0, y, w, y);
        y += 10;

        g2.setColor(new Color(18, 50, 90));
        g2.setFont(new Font(
                "Arial", Font.BOLD, 11));
        g2.drawString("DRIVER INFORMATION", 0, y);
        y += 12;
        g2.setColor(new Color(200, 210, 230));
        g2.drawLine(0, y, w, y);
        y += 10;

        y = drawRow(g2, w, y,
                "Full Name:",
                ticket.getDriver().getFullName());
        y = drawRow(g2, w, y,
                "License No:",
                ticket.getDriver()
                    .getLicenseNumber());
        y = drawRow(g2, w, y,
                "National ID:",
                ticket.getDriver().getNationalId());
        y = drawRow(g2, w, y,
                "Vehicle Plate:",
                ticket.getDriver().getVehiclePlate());
        y = drawRow(g2, w, y,
                "Vehicle Model:",
                ticket.getDriver().getVehicleModel()
                != null
                ? ticket.getDriver().getVehicleModel()
                : "N/A");
        y += 5;

        g2.setColor(new Color(200, 210, 230));
        g2.drawLine(0, y, w, y);
        y += 8;
        g2.setColor(new Color(18, 50, 90));
        g2.setFont(new Font(
                "Arial", Font.BOLD, 11));
        g2.drawString("VIOLATION DETAILS", 0, y);
        y += 12;
        g2.setColor(new Color(200, 210, 230));
        g2.drawLine(0, y, w, y);
        y += 10;

        y = drawRow(g2, w, y,
                "Date:",
                sdf.format(ticket.getIssueDate()));
        y = drawRow(g2, w, y,
                "Location:",
                ticket.getLocation());
        y = drawRow(g2, w, y,
                "Issuing Officer:",
                ticket.getOfficer().getFullName());

        g2.setColor(new Color(80, 80, 80));
        g2.setFont(new Font(
                "Arial", Font.BOLD, 10));
        g2.drawString("Offences:", 0, y);
        y += 14;

        if (ticket.getViolationTypes() != null) {
            for (rw.gov.trafficpolice.model
                    .ViolationType vt
                    : ticket.getViolationTypes()) {
                g2.setColor(
                        new Color(100, 100, 100));
                g2.setFont(new Font(
                        "Arial", Font.PLAIN, 10));
                g2.drawString(
                    "  • " + vt.getName()
                    + " (RWF "
                    + String.format("%,.0f",
                        vt.getFineAmount())
                    + ")",
                    0, y);
                y += 14;
            }
        }
        y += 5;

        g2.setColor(new Color(240, 245, 255));
        g2.fillRoundRect(0, y, w, 50, 8, 8);
        g2.setColor(new Color(18, 50, 90));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, y, w, 50, 8, 8);

        g2.setFont(new Font(
                "Arial", Font.BOLD, 11));
        g2.drawString("TOTAL FINE AMOUNT:", 8,
                y + 18);
        g2.setColor(new Color(220, 53, 69));
        g2.setFont(new Font(
                "Arial", Font.BOLD, 18));
        drawCentered(g2,
                "RWF " + String.format("%,.0f",
                ticket.getTotalFineAmount()),
                w, y + 40);
        y += 60;

        g2.setColor(new Color(255, 240, 210));
        g2.fillRoundRect(0, y, w, 28, 6, 6);
        g2.setColor(new Color(180, 100, 0));
        g2.setFont(new Font(
                "Arial", Font.BOLD, 11));
        drawCentered(g2,
                "⚠  Payment Due by: "
                + sdf.format(ticket.getDueDate()),
                w, y + 18);
        y += 36;

        g2.setColor(new Color(50, 50, 50));
        g2.setFont(new Font(
                "Arial", Font.BOLD, 10));
        g2.drawString("Payment Options:", 0, y);
        y += 14;
        g2.setFont(new Font(
                "Arial", Font.PLAIN, 9));
        g2.setColor(new Color(80, 80, 80));
        g2.drawString(
            "• Bank: BK/KCB any branch", 5, y);
        y += 13;
        g2.drawString(
            "• Online: www.irembo.gov.rw", 5, y);
        y += 13;
        g2.drawString(
            "• Mobile Money: *182*8*1#", 5, y);
        y += 13;
        g2.drawString(
            "• Police Office: KN 4 Ave, Kigali",
            5, y);
        y += 18;

        g2.setColor(new Color(200, 210, 230));
        float[] dash = {4f};
        g2.setStroke(new BasicStroke(
            1, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,
            10f, dash, 0f));
        g2.drawLine(0, y, w, y);
        g2.setStroke(new BasicStroke(1));
        y += 10;

        g2.setColor(new Color(80, 80, 80));
        g2.setFont(new Font(
                "Arial", Font.PLAIN, 9));
        g2.drawString(
                "Officer: "
                + ticket.getOfficer().getFullName()
                + "  Badge: "
                + ticket.getOfficer()
                    .getBadgeNumber(),
                0, y);
        y += 14;
        g2.drawString(
                "Date: "
                + sdf.format(ticket.getIssueDate())
                + "     Signature: ____________",
                0, y);
        y += 20;

        g2.setColor(new Color(18, 50, 90));
        g2.fillRoundRect(0, y, w, 45, 8, 8);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font(
                "Arial", Font.PLAIN, 8));
        drawCentered(g2,
                "This is an official document"
                + " from Rwanda National Police.",
                w, y + 12);
        drawCentered(g2,
                "Failure to pay before due date"
                + " may result in legal action.",
                w, y + 24);
        drawCentered(g2,
                "info@police.gov.rw  |"
                + " www.police.gov.rw  |"
                + " Emergency: 113",
                w, y + 36);

        return PAGE_EXISTS;
    }

    private void drawCentered(Graphics2D g2,
            String text, int width, int y) {
        FontMetrics fm = g2.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        g2.drawString(text, Math.max(x, 0), y);
    }

    private int drawRow(Graphics2D g2,
            int width, int y,
            String label, String value) {
        g2.setColor(new Color(80, 80, 80));
        g2.setFont(new Font(
                "Arial", Font.BOLD, 10));
        g2.drawString(label, 0, y);

        g2.setColor(new Color(30, 30, 30));
        g2.setFont(new Font(
                "Arial", Font.PLAIN, 10));

        FontMetrics fm = g2.getFontMetrics();
        while (value.length() > 3
                && fm.stringWidth(label
                    + "  " + value) > width) {
            value = value.substring(
                    0, value.length() - 3) + "..";
        }
        g2.drawString(value, 100, y);
        return y + 14;
    }
}