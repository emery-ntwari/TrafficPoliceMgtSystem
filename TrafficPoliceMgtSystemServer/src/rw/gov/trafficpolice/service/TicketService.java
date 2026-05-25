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
import rw.gov.trafficpolice.model.ETicketStatus;
import rw.gov.trafficpolice.model.Ticket;

public interface TicketService extends Remote {
    Ticket saveTicketRecord(Ticket ticket) throws RemoteException;
    Ticket updateTicketRecord(Ticket ticket) throws RemoteException;
    Ticket deleteTicketRecord(Ticket ticket) throws RemoteException;
    Ticket findTicketRecordById(Ticket ticket) throws RemoteException;
    Ticket findTicketByNumber(String ticketNumber) throws RemoteException;
    List<Ticket> findTicketsByDriver(int driverId) throws RemoteException;
    List<Ticket> findTicketsByStatus(ETicketStatus status) throws RemoteException;
    List<Ticket> findOverdueTickets() throws RemoteException;
    List<Ticket> findAllTicketRecords() throws RemoteException;
    long countTicketsByDriver(int driverId) throws RemoteException;
}
