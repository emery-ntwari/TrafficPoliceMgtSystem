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
import rw.gov.trafficpolice.model.Ticket;


public class SMSUtil {


    public static void sendTicketSMS(Ticket ticket) {
        try {
            String phone = formatPhone(
                ticket.getDriver().getPhoneNumber());
            String message = buildMessage(ticket);
            printToConsole(phone, message);

        } catch (Exception ex) {
            System.err.println(
                "[SMS] Error: " + ex.getMessage());
        }
    }


    public static String buildMessage(Ticket ticket) {
        SimpleDateFormat sdf =
                new SimpleDateFormat("dd/MM/yyyy");

        StringBuilder violations =
                new StringBuilder();
        if (ticket.getViolationTypes() != null
                && !ticket.getViolationTypes()
                    .isEmpty()) {
            ticket.getViolationTypes().forEach(
                vt -> violations
                    .append(vt.getName())
                    .append(", "));
            if (violations.length() > 2) {
                violations.setLength(
                    violations.length() - 2);
            }
        }

        return
            "RWANDA TRAFFIC POLICE\n"
            + "VIOLATION NOTICE\n"
            + "──────────────────\n"
            + "Dear "
            + ticket.getDriver().getFullName()
            + ",\n"
            + "A ticket has been issued.\n"
            + "Ticket : "
            + ticket.getTicketNumber() + "\n"
            + "Date   : "
            + sdf.format(ticket.getIssueDate()) + "\n"
            + "Place  : "
            + ticket.getLocation() + "\n"
            + "Car    : "
            + ticket.getDriver().getVehiclePlate()
            + "\n"
            + "Offence: "
            + violations.toString() + "\n"
            + "Fine   : RWF "
            + String.format("%,.0f",
                ticket.getTotalFineAmount()) + "\n"
            + "Pay by : "
            + sdf.format(ticket.getDueDate()) + "\n"
            + "Officer: "
            + ticket.getOfficer().getFullName() + "\n"
            + "Pay on time to avoid court action.\n"
            + "Rwanda National Police";
    }

 
    private static String formatPhone(String phone) {
        if (phone == null
                || phone.trim().isEmpty()) {
            return "+250780000000";
        }
        phone = phone.trim()
                     .replaceAll("[\\s-]", "");
        if (phone.startsWith("+250")) return phone;
        if (phone.startsWith("250"))
            return "+" + phone;
        if (phone.startsWith("07")
                || phone.startsWith("08"))
            return "+250" + phone.substring(1);
        return "+250" + phone;
    }


    private static void printToConsole(
            String phone, String message) {
        System.out.println(
            "\n╔═══════════════════════════╗");
        System.out.println(
            "║   📱 SMS NOTIFICATION     ║");
        System.out.println(
            "╠═══════════════════════════╣");
        System.out.println("║ TO : " + phone);
        System.out.println(
            "╠═══════════════════════════╣");
        System.out.println(message);
        System.out.println(
            "╚═══════════════════════════╝\n");
    }
}