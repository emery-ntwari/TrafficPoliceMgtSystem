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
import rw.gov.trafficpolice.dao.ViolationTypeDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import rw.gov.trafficpolice.model.ViolationType;
import rw.gov.trafficpolice.service.ViolationTypeService;

public class ViolationTypeServiceImpl extends UnicastRemoteObject implements ViolationTypeService {
        ViolationTypeDao dao = new ViolationTypeDao();

    public ViolationTypeServiceImpl() throws RemoteException {
    }

    @Override
    public ViolationType saveViolationTypeRecord(ViolationType vt)
            throws RemoteException {
        return dao.saveViolationType(vt);
    }

    @Override
    public ViolationType updateViolationTypeRecord(ViolationType vt)
            throws RemoteException {
        return dao.updateViolationType(vt);
    }

    @Override
    public ViolationType deleteViolationTypeRecord(ViolationType vt)
            throws RemoteException {
        return dao.deleteViolationType(vt);
    }

    @Override
    public ViolationType findViolationTypeRecordById(ViolationType vt)
            throws RemoteException {
        return dao.findViolationTypeById(vt);
    }

    @Override
    public ViolationType findViolationTypeByCode(String code)
            throws RemoteException {
        return dao.findViolationTypeByCode(code);
    }

    @Override
    public List<ViolationType> findAllViolationTypeRecords()
            throws RemoteException {
        return dao.findAllViolationTypes();
    }
}
