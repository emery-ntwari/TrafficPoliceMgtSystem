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
import rw.gov.trafficpolice.model.Driver;

public interface DriverService extends Remote {
    Driver saveDriverRecord(Driver driver) throws RemoteException;
    Driver updateDriverRecord(Driver driver) throws RemoteException;
    Driver deleteDriverRecord(Driver driver) throws RemoteException;
    Driver findDriverRecordById(Driver driver) throws RemoteException;
    Driver findDriverByLicense(String licenseNumber) throws RemoteException;
    Driver findDriverByPlate(String vehiclePlate) throws RemoteException;
    List<Driver> findAllDriverRecords() throws RemoteException;
}
