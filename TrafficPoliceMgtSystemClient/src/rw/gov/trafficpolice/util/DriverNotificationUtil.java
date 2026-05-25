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
import java.text.SimpleDateFormat;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JOptionPane;
import rw.gov.trafficpolice.model.Ticket;

public class DriverNotificationUtil {
    
    private static final String GMAIL_USER =
            "ntwariemery007@gmail.com";
    private static final String GMAIL_PASS =
            "ozoelbnxeahfzohy";

    private static final String RNP_HQ =
            "KN 4 Ave, Kigali, Rwanda";
    private static final String RNP_EMAIL =
            "info@police.gov.rw";
    private static final String RNP_PHONE =
            "+250 788 311 155";
    private static final String RNP_EMERGENCY =
            "113 / 112";
    private static final String RNP_WEBSITE =
            "www.police.gov.rw";
    private static final String RNP_TRAFFIC =
            "traffic@police.gov.rw";

    public static void notifyTicketIssued(
            Ticket ticket) {
        new Thread(() -> {
            try {
                String email =
                    ticket.getDriver().getEmail();
                if (email == null
                        || email.trim().isEmpty()) {
                    System.out.println(
                        "[NOTIFY] No email for driver: "
                        + ticket.getDriver()
                            .getFullName());
                    return;
                }

                String subject =
                    "🚨 Traffic Violation Notice - "
                    + ticket.getTicketNumber();

                String body = buildIssuedEmail(ticket);
                sendEmail(email, subject, body);

                System.out.println(
                    "[NOTIFY] ✅ Ticket email sent to: "
                    + email);

            } catch (Exception ex) {
                System.err.println(
                    "[NOTIFY] Error: "
                    + ex.getMessage());
            }
        }).start();
    }


    public static void notifyTicketOverdue(
            Ticket ticket) {
        new Thread(() -> {
            try {
                String email =
                    ticket.getDriver().getEmail();
                if (email == null
                        || email.trim().isEmpty())
                    return;

                String subject =
                    "⏰ OVERDUE: Ticket "
                    + ticket.getTicketNumber()
                    + " - Immediate Payment Required";

                String body = buildOverdueEmail(ticket);
                sendEmail(email, subject, body);

                System.out.println(
                    "[NOTIFY] ✅ Overdue reminder"
                    + " sent to: " + email);

            } catch (Exception ex) {
                System.err.println(
                    "[NOTIFY] Overdue error: "
                    + ex.getMessage());
            }
        }).start();
    }

    public static void notifyTicketEscalated(
            Ticket ticket) {
        new Thread(() -> {
            try {
                String email =
                    ticket.getDriver().getEmail();
                if (email == null
                        || email.trim().isEmpty())
                    return;

                String subject =
                    "🚨 URGENT: Ticket "
                    + ticket.getTicketNumber()
                    + " Escalated to Court Action";

                String body =
                    buildEscalatedEmail(ticket);
                sendEmail(email, subject, body);

                System.out.println(
                    "[NOTIFY] ✅ Escalation alert"
                    + " sent to: " + email);

            } catch (Exception ex) {
                System.err.println(
                    "[NOTIFY] Escalation error: "
                    + ex.getMessage());
            }
        }).start();
    }

    private static String buildIssuedEmail(
            Ticket ticket) {
        SimpleDateFormat sdf =
                new SimpleDateFormat("dd MMMM yyyy");

        StringBuilder violations =
                new StringBuilder();
        if (ticket.getViolationTypes() != null) {
            ticket.getViolationTypes().forEach(
                vt -> violations
                    .append("<li style='"
                    + "padding:4px 0;'>")
                    .append(vt.getName())
                    .append(" — RWF ")
                    .append(String.format("%,.0f",
                        vt.getFineAmount()))
                    .append("</li>"));
        }

        String qrData =
            "TICKET:" + ticket.getTicketNumber()
            + "|DRIVER:" + ticket.getDriver()
                .getFullName()
            + "|AMOUNT:RWF" + String.format("%.0f",
                ticket.getTotalFineAmount())
            + "|DUE:" + sdf.format(
                ticket.getDueDate())
            + "|PAY:bank.rw/traffic";

        String qrUrl =
            "https://chart.googleapis.com/chart"
            + "?chs=200x200&cht=qr&chl="
            + qrData.replace(" ", "+")
            + "&choe=UTF-8";

        return buildEmailTemplate(
            "🚨 TRAFFIC VIOLATION NOTICE",
            "#DC3545",
            ticket,
            sdf,
            violations.toString(),
            qrUrl,
            "<div style='background:#fff3f4;"
            + "border-left:4px solid #DC3545;"
            + "padding:15px;border-radius:6px;"
            + "margin-bottom:20px;'>"
            + "<p style='margin:0;color:#333;"
            + "font-size:15px;'>"
            + "Dear <b>"
            + ticket.getDriver().getFullName()
            + "</b>, a traffic violation ticket"
            + " has been issued against your"
            + " vehicle <b>"
            + ticket.getDriver().getVehiclePlate()
            + "</b>.</p>"
            + "</div>",
            "<div style='background:#e8f4fd;"
            + "border-radius:8px;padding:15px;"
            + "margin:15px 0;'>"
            + "<h4 style='color:#1F497D;margin:0"
            + " 0 10px;'>💳 Payment Instructions"
            + "</h4>"
            + "<p style='margin:4px 0;"
            + "font-size:13px;color:#555;'>"
            + "• Pay at any BK/KCB bank branch"
            + "</p>"
            + "<p style='margin:4px 0;"
            + "font-size:13px;color:#555;'>"
            + "• Online: www.irembo.gov.rw"
            + "</p>"
            + "<p style='margin:4px 0;"
            + "font-size:13px;color:#555;'>"
            + "• Mobile Money: *182*8*1#"
            + "</p>"
            + "<p style='margin:4px 0;"
            + "font-size:13px;color:#555;'>"
            + "• Scan QR code below to pay"
            + "</p>"
            + "</div>"
        );
    }

    private static String buildOverdueEmail(
            Ticket ticket) {
        SimpleDateFormat sdf =
                new SimpleDateFormat("dd MMMM yyyy");

        String qrData =
            "OVERDUE_TICKET:"
            + ticket.getTicketNumber()
            + "|AMOUNT:RWF"
            + String.format("%.0f",
                ticket.getTotalFineAmount())
            + "|PAY_NOW:irembo.gov.rw";

        String qrUrl =
            "https://chart.googleapis.com/chart"
            + "?chs=200x200&cht=qr&chl="
            + qrData.replace(" ", "+")
            + "&choe=UTF-8";

        StringBuilder violations =
                new StringBuilder();
        if (ticket.getViolationTypes() != null) {
            ticket.getViolationTypes().forEach(
                vt -> violations
                    .append("<li style='padding:4px;'>")
                    .append(vt.getName())
                    .append("</li>"));
        }

        return buildEmailTemplate(
            "⏰ OVERDUE PAYMENT REMINDER",
            "#FF8C00",
            ticket,
            sdf,
            violations.toString(),
            qrUrl,
            "<div style='background:#fff8e1;"
            + "border-left:4px solid #FF8C00;"
            + "padding:15px;border-radius:6px;"
            + "margin-bottom:20px;'>"
            + "<p style='margin:0;color:#333;"
            + "font-size:15px;'>"
            + "⚠️ Dear <b>"
            + ticket.getDriver().getFullName()
            + "</b>, your ticket <b>"
            + ticket.getTicketNumber()
            + "</b> is <b style='color:#DC3545;'>"
            + "OVERDUE</b>. "
            + "Please pay immediately to avoid"
            + " legal action.</p>"
            + "</div>",
            "<div style='background:#ffeeee;"
            + "border-radius:8px;padding:15px;"
            + "margin:15px 0;"
            + "border:1px solid #ffcccc;'>"
            + "<h4 style='color:#DC3545;margin:0"
            + " 0 10px;'>⚠️ Consequences of"
            + " Non-Payment</h4>"
            + "<p style='margin:4px 0;"
            + "font-size:13px;color:#555;'>"
            + "• Your case will be escalated"
            + " to court</p>"
            + "<p style='margin:4px 0;"
            + "font-size:13px;color:#555;'>"
            + "• Additional court fees apply</p>"
            + "<p style='margin:4px 0;"
            + "font-size:13px;color:#555;'>"
            + "• Your license may be suspended</p>"
            + "<p style='margin:4px 0;"
            + "font-size:13px;color:#555;'>"
            + "• Pay now at: irembo.gov.rw</p>"
            + "</div>"
        );
    }

    private static String buildEscalatedEmail(
            Ticket ticket) {
        SimpleDateFormat sdf =
                new SimpleDateFormat("dd MMMM yyyy");

        String qrData =
            "COURT_CASE:"
            + ticket.getTicketNumber()
            + "|DRIVER:"
            + ticket.getDriver().getFullName()
            + "|CONTACT:" + RNP_PHONE;

        String qrUrl =
            "https://chart.googleapis.com/chart"
            + "?chs=200x200&cht=qr&chl="
            + qrData.replace(" ", "+")
            + "&choe=UTF-8";

        StringBuilder violations =
                new StringBuilder();
        if (ticket.getViolationTypes() != null) {
            ticket.getViolationTypes().forEach(
                vt -> violations
                    .append("<li style='padding:4px;'>")
                    .append(vt.getName())
                    .append("</li>"));
        }

        return buildEmailTemplate(
            "🚨 URGENT: CASE ESCALATED TO COURT",
            "#8B0000",
            ticket,
            sdf,
            violations.toString(),
            qrUrl,
            "<div style='background:#ffe4e4;"
            + "border-left:4px solid #8B0000;"
            + "padding:15px;border-radius:6px;"
            + "margin-bottom:20px;'>"
            + "<p style='margin:0;color:#333;"
            + "font-size:15px;'>"
            + "🚨 Dear <b>"
            + ticket.getDriver().getFullName()
            + "</b>, your ticket <b>"
            + ticket.getTicketNumber()
            + "</b> has been <b style='"
            + "color:#8B0000;'>"
            + "ESCALATED TO LEGAL ACTION</b>."
            + " You are required to appear"
            + " before the court.</p>"
            + "</div>",
            "<div style='background:#fff0f0;"
            + "border-radius:8px;padding:15px;"
            + "margin:15px 0;"
            + "border:2px solid #8B0000;'>"
            + "<h4 style='color:#8B0000;margin:0"
            + " 0 10px;'>⚖️ Legal Action Details"
            + "</h4>"
            + "<p style='margin:4px 0;"
            + "font-size:13px;color:#555;'>"
            + "• Report to nearest police station"
            + "</p>"
            + "<p style='margin:4px 0;"
            + "font-size:13px;color:#555;'>"
            + "• Bring this notice with you</p>"
            + "<p style='margin:4px 0;"
            + "font-size:13px;color:#555;'>"
            + "• Contact our office immediately"
            + "</p>"
            + "<p style='margin:4px 0;"
            + "font-size:13px;color:#555;'>"
            + "• Phone: " + RNP_PHONE + "</p>"
            + "<p style='margin:4px 0;"
            + "font-size:13px;color:#555;'>"
            + "• Email: " + RNP_TRAFFIC + "</p>"
            + "</div>"
        );
    }

    private static String buildEmailTemplate(
            String title,
            String titleColor,
            Ticket ticket,
            SimpleDateFormat sdf,
            String violations,
            String qrUrl,
            String alertBox,
            String actionBox) {

        return
        "<div style='font-family:Segoe UI,Arial;"
        + "max-width:600px;margin:0 auto;"
        + "border-radius:12px;overflow:hidden;"
        + "border:1px solid #ddd;"
        + "box-shadow:0 2px 8px rgba(0,0,0,0.1);'>"

        + "<div style='background:" + titleColor
        + ";padding:20px;text-align:center;'>"
        + "<h2 style='color:white;margin:0;"
        + "font-size:18px;'>"
        + "🚔 RWANDA NATIONAL POLICE</h2>"
        + "<p style='color:rgba(255,255,255,0.85);"
        + "margin:4px 0 0;font-size:13px;'>"
        + "Traffic Management Department"
        + " — AUCA System</p>"
        + "<h3 style='color:white;margin:10px 0 0;"
        + "font-size:16px;border-top:"
        + "1px solid rgba(255,255,255,0.3);"
        + "padding-top:10px;'>"
        + title + "</h3>"
        + "</div>"

        + "<div style='padding:25px;"
        + "background:#ffffff;'>"
        + alertBox

        + "<table style='width:100%;"
        + "border-collapse:collapse;"
        + "margin:15px 0;"
        + "font-size:14px;'>"
        + "<tr style='background:#f0f5ff;'>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;'><b>Ticket No</b></td>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;color:#1F497D;"
        + "font-weight:bold;'>"
        + ticket.getTicketNumber() + "</td>"
        + "</tr>"
        + "<tr>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;'><b>Driver</b></td>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;'>"
        + ticket.getDriver().getFullName() + "</td>"
        + "</tr>"
        + "<tr style='background:#f0f5ff;'>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;'>"
        + "<b>Vehicle Plate</b></td>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;'>"
        + ticket.getDriver().getVehiclePlate()
        + "</td>"
        + "</tr>"
        + "<tr>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;'>"
        + "<b>Location</b></td>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;'>"
        + ticket.getLocation() + "</td>"
        + "</tr>"
        + "<tr style='background:#f0f5ff;'>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;'>"
        + "<b>Issue Date</b></td>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;'>"
        + sdf.format(ticket.getIssueDate()) + "</td>"
        + "</tr>"
        + "<tr>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;'>"
        + "<b>Due Date</b></td>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;"
        + "color:#DC3545;font-weight:bold;'>"
        + sdf.format(ticket.getDueDate()) + "</td>"
        + "</tr>"
        + "<tr style='background:#f0f5ff;'>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;'>"
        + "<b>Violations</b></td>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;'>"
        + "<ul style='margin:0;padding-left:18px;'>"
        + violations
        + "</ul></td>"
        + "</tr>"
        + "<tr>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;'>"
        + "<b>Total Fine</b></td>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;"
        + "color:#DC3545;font-size:18px;"
        + "font-weight:bold;'>"
        + "RWF " + String.format("%,.0f",
            ticket.getTotalFineAmount())
        + "</td>"
        + "</tr>"
        + "<tr style='background:#f0f5ff;'>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;'>"
        + "<b>Status</b></td>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;'>"
        + "<span style='background:"
        + getStatusColor(ticket.getStatus().name())
        + ";color:white;padding:3px 10px;"
        + "border-radius:20px;font-size:12px;'>"
        + ticket.getStatus().name()
        + "</span></td>"
        + "</tr>"
        + "<tr>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;'>"
        + "<b>Issued By</b></td>"
        + "<td style='padding:10px;"
        + "border:1px solid #dde;'>"
        + ticket.getOfficer().getFullName()
        + "</td>"
        + "</tr>"
        + "</table>"

        + actionBox

        + "<div style='text-align:center;"
        + "margin:20px 0;"
        + "padding:20px;"
        + "background:#f9f9f9;"
        + "border-radius:10px;"
        + "border:1px dashed #ccc;'>"
        + "<p style='color:#555;font-size:13px;"
        + "margin:0 0 10px;'>"
        + "📱 <b>Scan QR Code to Pay</b></p>"
        + "<img src='" + qrUrl + "'"
        + " width='180' height='180'"
        + " style='border:1px solid #ddd;"
        + "border-radius:8px;padding:5px;"
        + "background:white;'"
        + " alt='Payment QR Code'/>"
        + "<p style='color:#888;font-size:11px;"
        + "margin:10px 0 0;'>"
        + "Ticket: "
        + ticket.getTicketNumber()
        + " | Amount: RWF "
        + String.format("%,.0f",
            ticket.getTotalFineAmount())
        + "</p>"
        + "</div>"
        + "</div>"

        + "<div style='background:#1F497D;"
        + "padding:20px;color:white;'>"
        + "<h4 style='margin:0 0 12px;"
        + "color:#FFD700;font-size:14px;'>"
        + "🚔 Rwanda National Police — "
        + "Traffic Department</h4>"
        + "<table style='width:100%;"
        + "font-size:12px;color:rgba(255,255,255,0.9);'>"
        + "<tr>"
        + "<td style='padding:3px 10px 3px 0;'>"
        + "🏛️ <b>Headquarters:</b></td>"
        + "<td>" + RNP_HQ + "</td>"
        + "</tr>"
        + "<tr>"
        + "<td style='padding:3px 10px 3px 0;'>"
        + "📞 <b>Traffic Hotline:</b></td>"
        + "<td>" + RNP_PHONE + "</td>"
        + "</tr>"
        + "<tr>"
        + "<td style='padding:3px 10px 3px 0;'>"
        + "🚨 <b>Emergency:</b></td>"
        + "<td>" + RNP_EMERGENCY + "</td>"
        + "</tr>"
        + "<tr>"
        + "<td style='padding:3px 10px 3px 0;'>"
        + "📧 <b>General Email:</b></td>"
        + "<td>" + RNP_EMAIL + "</td>"
        + "</tr>"
        + "<tr>"
        + "<td style='padding:3px 10px 3px 0;'>"
        + "📧 <b>Traffic Email:</b></td>"
        + "<td>" + RNP_TRAFFIC + "</td>"
        + "</tr>"
        + "<tr>"
        + "<td style='padding:3px 10px 3px 0;'>"
        + "🌐 <b>Website:</b></td>"
        + "<td>" + RNP_WEBSITE + "</td>"
        + "</tr>"
        + "</table>"
        + "</div>"

        + "<div style='background:#f8f9fa;"
        + "padding:12px;text-align:center;"
        + "border-top:1px solid #eee;'>"
        + "<p style='color:#999;font-size:11px;"
        + "margin:0;'>"
        + "This is an official notice from"
        + " Rwanda National Police Traffic Dept."
        + "<br>System: Traffic Policy Management"
        + " System | AUCA | INSY 7312"
        + " | NTWALI EMERY | 27276"
        + "</p></div></div>";
    }


    private static void sendEmail(
            String toEmail,
            String subject,
            String htmlBody) throws Exception {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable",
                "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust",
                "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
props.put("mail.smtp.starttls.required", "true");
props.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getInstance(
            props,
            new Authenticator() {
                @Override
                protected PasswordAuthentication
                    getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        GMAIL_USER, GMAIL_PASS);
                }
            });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(
            GMAIL_USER,
            "Rwanda National Police - Traffic Dept"));
        msg.setRecipients(
            Message.RecipientType.TO,
            InternetAddress.parse(toEmail));
        msg.setSubject(subject);
        msg.setContent(htmlBody, "text/html");
        Transport.send(msg);
    }

    private static String getStatusColor(
            String status) {
        switch (status) {
            case "ISSUED":    return "#17a2b8";
            case "PAID":      return "#28a745";
            case "OVERDUE":   return "#FF8C00";
            case "ESCALATED": return "#DC3545";
            default:          return "#6c757d";
        }
    }
    
}
