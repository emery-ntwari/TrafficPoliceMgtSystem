/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rw.gov.trafficpolice.service.implementation;

/**
 *
 * @author PC
 */
import rw.gov.trafficpolice.dao.DriverDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import rw.gov.trafficpolice.model.Driver;
import rw.gov.trafficpolice.service.DriverService;

public class DriverServiceImpl extends UnicastRemoteObject implements DriverService{
    
    DriverDao dao = new DriverDao();

    public DriverServiceImpl() throws RemoteException {
    }

    @Override
    public Driver saveDriverRecord(Driver driver)
            throws RemoteException {
        return dao.saveDriver(driver);
    }

    @Override
    public Driver updateDriverRecord(Driver driver)
            throws RemoteException {
        return dao.updateDriver(driver);
    }

    @Override
    public Driver deleteDriverRecord(Driver driver)
            throws RemoteException {
        return dao.deleteDriver(driver);
    }

    @Override
    public Driver findDriverRecordById(Driver driver)
            throws RemoteException {
        return dao.findDriverById(driver);
    }

    @Override
    public Driver findDriverByLicense(String licenseNumber)
            throws RemoteException {
        return dao.findDriverByLicense(licenseNumber);
    }

    @Override
    public Driver findDriverByPlate(String vehiclePlate)
            throws RemoteException {
        return dao.findDriverByPlate(vehiclePlate);
    }

    @Override
    public List<Driver> findAllDriverRecords()
            throws RemoteException {
        return dao.findAllDrivers();
    }
}
