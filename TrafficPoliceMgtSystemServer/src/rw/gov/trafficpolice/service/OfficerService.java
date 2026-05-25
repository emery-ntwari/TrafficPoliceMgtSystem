/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rw.gov.trafficpolice.service;

/**
 *
 * @author PC
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import rw.gov.trafficpolice.model.Officer;

public interface OfficerService extends Remote {
    
    Officer saveOfficerRecord(Officer officer) throws RemoteException;
    Officer updateOfficerRecord(Officer officer) throws RemoteException;
    Officer deleteOfficerRecord(Officer officer) throws RemoteException;
    Officer findOfficerRecordById(Officer officer) throws RemoteException;
    Officer findOfficerByUsername(String username) throws RemoteException;
    Officer login(String username, String password) throws RemoteException;
    List<Officer> findAllOfficerRecords() throws RemoteException;
}
