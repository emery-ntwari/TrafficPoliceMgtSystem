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
import rw.gov.trafficpolice.model.Payment;

public interface PaymentService extends Remote {
    Payment savePaymentRecord(Payment payment) throws RemoteException;
    Payment updatePaymentRecord(Payment payment) throws RemoteException;
    Payment deletePaymentRecord(Payment payment) throws RemoteException;
    Payment findPaymentRecordById(Payment payment) throws RemoteException;
    Payment findPaymentByTicket(int ticketId) throws RemoteException;
    double getTotalCollectedFines() throws RemoteException;
    List<Payment> findAllPaymentRecords() throws RemoteException;
}
