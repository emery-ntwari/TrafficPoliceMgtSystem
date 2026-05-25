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
import rw.gov.trafficpolice.model.ViolationType;

public interface ViolationTypeService extends Remote {
    ViolationType saveViolationTypeRecord(ViolationType vt) throws RemoteException;
    ViolationType updateViolationTypeRecord(ViolationType vt) throws RemoteException;
    ViolationType deleteViolationTypeRecord(ViolationType vt) throws RemoteException;
    ViolationType findViolationTypeRecordById(ViolationType vt) throws RemoteException;
    ViolationType findViolationTypeByCode(String code) throws RemoteException;
    List<ViolationType> findAllViolationTypeRecords() throws RemoteException;
}
