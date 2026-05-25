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
import rw.gov.trafficpolice.model.Officer;

public class SessionManager {
    
    private static Officer currentOfficer;

    public static void setCurrentOfficer(Officer officer) {
        currentOfficer = officer;
    }

    public static Officer getCurrentOfficer() {
        return currentOfficer;
    }

    public static boolean isAdmin() {
        return currentOfficer != null &&
                currentOfficer.getRole().equals("ADMIN");
    }

    public static void logout() {
        currentOfficer = null;
    }
}
