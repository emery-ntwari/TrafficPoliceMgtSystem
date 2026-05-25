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
import rw.gov.trafficpolice.dao.PaymentDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import rw.gov.trafficpolice.model.Payment;
import rw.gov.trafficpolice.service.PaymentService;

public class PaymentServiceImpl extends UnicastRemoteObject implements PaymentService {
        PaymentDao dao = new PaymentDao();

    public PaymentServiceImpl() throws RemoteException {
    }

    @Override
    public Payment savePaymentRecord(Payment payment)
            throws RemoteException {
        return dao.savePayment(payment);
    }

    @Override
    public Payment updatePaymentRecord(Payment payment)
            throws RemoteException {
        return dao.updatePayment(payment);
    }

    @Override
    public Payment deletePaymentRecord(Payment payment)
            throws RemoteException {
        return dao.deletePayment(payment);
    }

    @Override
    public Payment findPaymentRecordById(Payment payment)
            throws RemoteException {
        return dao.findPaymentById(payment);
    }

    @Override
    public Payment findPaymentByTicket(int ticketId)
            throws RemoteException {
        return dao.findPaymentByTicket(ticketId);
    }

    @Override
    public double getTotalCollectedFines()
            throws RemoteException {
        return dao.getTotalCollectedFines();
    }

    @Override
    public List<Payment> findAllPaymentRecords()
            throws RemoteException {
        return dao.findAllPayments();
    }
}
