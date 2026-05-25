/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rw.gov.trafficpolice.controller;

/**
 *
 * @author PC
 */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import rw.gov.trafficpolice.service.implementation.*;

public class Server {
        public static void main(String[] args) {
        try {
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");

            Registry registry = LocateRegistry.createRegistry(5027);  // qn4.3000-6000

            registry.rebind("officer-service",
                    new OfficerServiceImpl());
            registry.rebind("driver-service",
                    new DriverServiceImpl());
            registry.rebind("violation-type-service",
                    new ViolationTypeServiceImpl());
            registry.rebind("ticket-service",
                    new TicketServiceImpl());
            registry.rebind("payment-service",
                    new PaymentServiceImpl());

            System.out.println("================================");
            System.out.println(" Traffic Policy Server STARTED");
            System.out.println(" Port : 5027");
            System.out.println(" Student ID : 27276");
            System.out.println(" Waiting for clients...");
            System.out.println("================================");

        } catch (Exception ex) {
            System.err.println("Server error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
