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
import rw.gov.trafficpolice.service.OfficerService;
import rw.gov.trafficpolice.service.DriverService;
import rw.gov.trafficpolice.service.ViolationTypeService;
import rw.gov.trafficpolice.service.TicketService;
import rw.gov.trafficpolice.service.PaymentService;

public class ClientConnector {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 5027;

    public static OfficerService officerService;
    public static DriverService driverService;
    public static ViolationTypeService
            violationTypeService;
    public static TicketService ticketService;
    public static PaymentService paymentService;

    public static boolean connect() {
        try {
            System.out.println(
                "[CLIENT] Connecting to server at "
                + HOST + ":" + PORT + "...");

            Registry registry =
                LocateRegistry.getRegistry(HOST, PORT);

            System.out.println(
                "[CLIENT] Registry found! "
                + "Looking up services...");

            officerService = (OfficerService)
                registry.lookup("officer-service");
            System.out.println(
                "[CLIENT] officer-service OK");

            driverService = (DriverService)
                registry.lookup("driver-service");
            System.out.println(
                "[CLIENT] driver-service OK");

            violationTypeService =
                (ViolationTypeService) registry.lookup(
                "violation-type-service");
            System.out.println(
                "[CLIENT] violation-type-service OK");

            ticketService = (TicketService)
                registry.lookup("ticket-service");
            System.out.println(
                "[CLIENT] ticket-service OK");

            paymentService = (PaymentService)
                registry.lookup("payment-service");
            System.out.println(
                "[CLIENT] payment-service OK");

            System.out.println(
                "[CLIENT] All services connected!");
            return true;

        } catch (Exception ex) {
            System.err.println(
                "[CLIENT] FULL ERROR: "
                + ex.getClass().getName()
                + " - " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
}