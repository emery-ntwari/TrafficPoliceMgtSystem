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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JOptionPane;


public class OTPUtil {

    // OTP Storage 
    private static String generatedOTP = null;
    private static long   otpTimestamp = 0;
    private static final long VALIDITY =
            5 * 60 * 1000; // 5 minutes

    private static final String GMAIL_USER =
            "ntwariemery007@gmail.com";
    private static final String GMAIL_PASS =
            "ozoelbnxeahfzohy";
    private static final String ADMIN_EMAIL =
            "ntwariemery007@gmail.com";

    private static final String[][] OFFICER_PHONES
            = {
        {"officer1", "+250784476801"},
        {"officer2", "+250790846109"}
    };


public static String generateAndSendOTP(
        String email,
        String role,
        String username) {

    // Generate 6-digit OTP
    Random rand = new Random();
    int code = 100000 + rand.nextInt(900000);
    generatedOTP = String.valueOf(code);
    otpTimestamp = System.currentTimeMillis();

    System.out.println(
        "[OTP] Generated: " + generatedOTP
        + " | User: " + username
        + " | Role: " + role);

    if ("ADMIN".equalsIgnoreCase(role)) {
        sendEmailOTP(ADMIN_EMAIL,
                generatedOTP, username, role);
    } else {
        sendEmailOTP(email,
                generatedOTP, username, role);
        String phone = getPhone(username);
        trySMSInBackground(phone, generatedOTP);
    }

    return generatedOTP;
}

private static void sendEmailOTP(
        String toEmail, String otp,
        String username, String role) {
    try {
        System.out.println(
            "[OTP] Sending email to: " + toEmail);

Properties props = new Properties();
props.put("mail.smtp.auth", "true");
props.put("mail.smtp.starttls.enable", "true");
props.put("mail.smtp.host", "smtp.gmail.com");
props.put("mail.smtp.port", "587");
props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
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
            "Traffic Policy System - AUCA"));
        msg.setRecipients(
            Message.RecipientType.TO,
            InternetAddress.parse(toEmail));
        msg.setSubject(
            "🔐 Your Login OTP - "
            + "Traffic Policy System");

        String roleColor =
            "ADMIN".equalsIgnoreCase(role)
            ? "#1F497D" : "#28a745";
        String roleLabel =
            "ADMIN".equalsIgnoreCase(role)
            ? "Administrator" : "Traffic Officer";

        String html =
            "<div style='"
            + "font-family:Segoe UI,Arial;"
            + "max-width:520px;"
            + "margin:0 auto;"
            + "border-radius:12px;"
            + "overflow:hidden;"
            + "border:1px solid #ddd;'>"

            + "<div style='"
            + "background:" + roleColor + ";"
            + "padding:25px;"
            + "text-align:center;'>"
            + "<h2 style='"
            + "color:white;margin:0;'>"
            + "🚦 TRAFFIC POLICY SYSTEM"
            + "</h2>"
            + "<p style='"
            + "color:#d0e8ff;margin:5px 0 0;'>"
            + "Rwanda National Police — AUCA"
            + "</p></div>"

            + "<div style='"
            + "padding:35px;"
            + "text-align:center;"
            + "background:#fff;'>"

            + "<p style='"
            + "font-size:15px;color:#333;'>"
            + "Hello <b>" + username + "</b>,"
            + "<br>Role: <b>" + roleLabel
            + "</b></p>"

            + "<p style='color:#555;'>"
            + "Your One-Time Password:</p>"

            + "<div style='"
            + "background:#EEF4FF;"
            + "border:2px dashed "
            + roleColor + ";"
            + "border-radius:12px;"
            + "padding:25px;"
            + "margin:20px 0;'>"
            + "<h1 style='"
            + "font-size:54px;"
            + "letter-spacing:14px;"
            + "color:" + roleColor + ";"
            + "margin:0;'>"
            + otp
            + "</h1>"
            + "<p style='"
            + "color:#888;font-size:13px;"
            + "margin:10px 0 0;'>"
            + "⏰ Expires in 5 minutes"
            + "</p></div>"

            + "<div style='"
            + "background:#fff3cd;"
            + "border-radius:8px;"
            + "padding:12px;'>"
            + "<p style='"
            + "color:#856404;margin:0;"
            + "font-size:13px;'>"
            + "⚠️ Do not share this OTP"
            + " with anyone."
            + "</p></div></div>"

            + "<div style='"
            + "background:#f8f9fa;"
            + "padding:12px;"
            + "text-align:center;'>"
            + "<p style='"
            + "color:#aaa;font-size:11px;"
            + "margin:0;'>"
            + "Traffic Policy Management System"
            + " | AUCA | INSY 7312"
            + " | NTWALI EMERY | 27276"
            + "</p></div></div>";

        msg.setContent(html, "text/html");
        Transport.send(msg);

        System.out.println(
            "[OTP] ✅ Email sent to: " + toEmail);

        JOptionPane.showMessageDialog(null,
            "📧 OTP sent to your email!\n\n"
            + "Email : " + toEmail + "\n"
            + "User  : " + username + "\n"
            + "Role  : " + roleLabel + "\n\n"
            + "✅ Check your email inbox\n"
            + "   for the 6-digit code.\n\n"
            + "⏰ Expires in 5 minutes.",
            "OTP Sent via Email ✅",
            JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception ex) {
        System.err.println(
            "[OTP] Email error: "
            + ex.getMessage());
        ex.printStackTrace();

        // Show OTP directly as fallback
        JOptionPane.showMessageDialog(null,
            "⚠️ Email could not be sent.\n\n"
            + "User  : " + username + "\n\n"
            + "Your OTP is:\n\n"
            + "     " + otp + "\n\n"
            + "⏰ Expires in 5 minutes.",
            "Your OTP Code 🔐",
            JOptionPane.WARNING_MESSAGE);
    }
}
//
private static void trySMSInBackground(
        String phone, String otp) {
    new Thread(() -> {
        try {
            String message =
                "TRAFFIC SYSTEM OTP: "
                + otp
                + " Valid 5mins. AUCA/RNP";

            URL url = new URL(
                "https://textbelt.com/text");
            HttpURLConnection conn =
                (HttpURLConnection)
                url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);

            String params =
                "phone=" + phone
                + "&message="
                + java.net.URLEncoder.encode(
                    message, "UTF-8")
                + "&key=textbelt";

            try (OutputStream os =
                    conn.getOutputStream()) {
                os.write(params.getBytes("UTF-8"));
            }

            int code = conn.getResponseCode();
            BufferedReader br = new BufferedReader(
                new InputStreamReader(
                    conn.getInputStream()));
            StringBuilder resp =
                    new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                resp.append(line);
            }
            br.close();

            if (resp.toString()
                    .contains("\"success\":true")) {
                System.out.println(
                    "[OTP] ✅ SMS also sent to: "
                    + phone);
            } else {
                System.out.println(
                    "[OTP] SMS not available"
                    + " for Rwanda - email sent instead");
            }
        } catch (Exception ex) {
            System.err.println(
                "[OTP] SMS background error: "
                + ex.getMessage());
        }
    }).start();
}


    private static void sendSMSOTP(
            String phone, String otp,
            String username) {
        try {
            System.out.println(
                "[OTP] Sending SMS to: " + phone);

            String message =
                "TRAFFIC POLICY SYSTEM\n"
                + "Your OTP: " + otp + "\n"
                + "Valid 5 mins.\n"
                + "Do not share.\n"
                + "Rwanda National Police - AUCA";

            URL url = new URL(
                "https://textbelt.com/text");
            HttpURLConnection conn =
                (HttpURLConnection)
                url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            String params =
                "phone=" + phone
                + "&message="
                + java.net.URLEncoder.encode(
                    message, "UTF-8")
                + "&key=textbelt";

            try (OutputStream os =
                    conn.getOutputStream()) {
                os.write(params.getBytes("UTF-8"));
                os.flush();
            }

            int code = conn.getResponseCode();
            BufferedReader br = new BufferedReader(
                new InputStreamReader(
                    conn.getInputStream()));
            StringBuilder resp =
                    new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                resp.append(line);
            }
            br.close();

            System.out.println(
                "[OTP] SMS Response: " + resp);

            if (resp.toString()
                    .contains("\"success\":true")) {
                JOptionPane.showMessageDialog(null,
                    "📱 OTP sent to phone!\n\n"
                    + "Phone  : " + phone + "\n"
                    + "Officer: " + username + "\n\n"
                    + "✅ Check your SMS messages.\n"
                    + "⏰ Expires in 5 minutes.",
                    "OTP Sent via SMS ✅",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                    "📱 SMS limit reached today.\n\n"
                    + "Officer : " + username + "\n"
                    + "Phone   : " + phone + "\n\n"
                    + "Your OTP is:\n\n"
                    + "     " + otp + "\n\n"
                    + "⏰ Expires in 5 minutes.",
                    "Your OTP Code 🔐",
                    JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            System.err.println(
                "[OTP] SMS error: "
                + ex.getMessage());
            JOptionPane.showMessageDialog(null,
                "📱 SMS unavailable.\n\n"
                + "Officer : " + username + "\n\n"
                + "Your OTP is:\n\n"
                + "     " + otp + "\n\n"
                + "⏰ Expires in 5 minutes.",
                "Your OTP Code 🔐",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private static String getPhone(String username) {
        for (String[] entry : OFFICER_PHONES) {
            if (entry[0].equalsIgnoreCase(username)) {
                return entry[1];
            }
        }
        return "+250784476801";
    }

    public static boolean validateOTP(
            String entered) {
        if (generatedOTP == null
                || entered == null) return false;
        long elapsed =
            System.currentTimeMillis() - otpTimestamp;
        if (elapsed > VALIDITY) {
            generatedOTP = null;
            return false;
        }
        return generatedOTP.equals(entered.trim());
    }

    public static void clearOTP() {
        generatedOTP = null;
        otpTimestamp = 0;
    }
}