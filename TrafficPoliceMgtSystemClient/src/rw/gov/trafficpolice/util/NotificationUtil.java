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
import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class NotificationUtil {
        private static final String BROKER_URL =
            "tcp://localhost:61616";
    private static final String QUEUE_NAME =
            "TRAFFIC.NOTIFICATIONS";

    public static void sendNotification(
            String subject, String message) {
        Connection connection = null;
        try {
            ActiveMQConnectionFactory factory =
                    new ActiveMQConnectionFactory(BROKER_URL);
            connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(
                    false, Session.AUTO_ACKNOWLEDGE);
            Destination dest =
                    session.createQueue(QUEUE_NAME);
            MessageProducer producer =
                    session.createProducer(dest);
            producer.setDeliveryMode(
                    DeliveryMode.NON_PERSISTENT);

            String full = "[TRAFFIC] " + subject
                    + " | " + message;
            TextMessage msg =
                    session.createTextMessage(full);
            producer.send(msg);

            System.out.println("[ActiveMQ] Sent: " + full);
            session.close();

        } catch (Exception ex) {
            System.err.println("[ActiveMQ] Offline - "
                    + "notification skipped.");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ignored) {}
            }
        }
    }

    public static void notifyTicketIssued(
            String ticketNo, String driver, double amount) {
        sendNotification("TICKET ISSUED",
                "Ticket " + ticketNo + " | Driver: "
                + driver + " | Fine: RWF "
                + String.format("%.0f", amount));
    }

    public static void notifyPaymentReceived(
            String ticketNo, double amount) {
        sendNotification("PAYMENT RECEIVED",
                "Ticket " + ticketNo + " | RWF "
                + String.format("%.0f", amount));
    }

    public static void notifyTicketEscalated(
            String ticketNo, String driver) {
        sendNotification("TICKET ESCALATED",
                "Ticket " + ticketNo
                + " | Driver: " + driver
                + " escalated to legal action");
    }
}
