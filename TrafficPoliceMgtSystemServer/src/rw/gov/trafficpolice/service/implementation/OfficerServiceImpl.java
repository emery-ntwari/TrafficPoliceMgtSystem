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
import rw.gov.trafficpolice.dao.OfficerDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import rw.gov.trafficpolice.model.Officer;
import rw.gov.trafficpolice.service.OfficerService;

public class OfficerServiceImpl extends UnicastRemoteObject implements OfficerService {
    
    OfficerDao dao = new OfficerDao();

    public OfficerServiceImpl() throws RemoteException {
    }

    @Override
    public Officer saveOfficerRecord(Officer officer)
            throws RemoteException {
        return dao.saveOfficer(officer);
    }

    @Override
    public Officer updateOfficerRecord(Officer officer)
            throws RemoteException {
        return dao.updateOfficer(officer);
    }

    @Override
    public Officer deleteOfficerRecord(Officer officer)
            throws RemoteException {
        return dao.deleteOfficer(officer);
    }

    @Override
    public Officer findOfficerRecordById(Officer officer)
            throws RemoteException {
        return dao.findOfficerById(officer);
    }

    @Override
    public Officer findOfficerByUsername(String username)
            throws RemoteException {
        return dao.findOfficerByUsername(username);
    }

    @Override
    public Officer login(String username, String password)
            throws RemoteException {
        return dao.login(username, password);
    }

    @Override
    public List<Officer> findAllOfficerRecords()
            throws RemoteException {
        return dao.findAllOfficers();
    }
}
