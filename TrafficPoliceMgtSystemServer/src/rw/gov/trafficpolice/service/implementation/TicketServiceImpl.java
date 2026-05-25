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
import rw.gov.trafficpolice.dao.TicketDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import rw.gov.trafficpolice.model.ETicketStatus;
import rw.gov.trafficpolice.model.Ticket;
import rw.gov.trafficpolice.service.TicketService;

public class TicketServiceImpl extends UnicastRemoteObject implements TicketService {
        TicketDao dao = new TicketDao();

    public TicketServiceImpl() throws RemoteException {
    }

    @Override
    public Ticket saveTicketRecord(Ticket ticket)
            throws RemoteException {
        return dao.saveTicket(ticket);
    }

    @Override
    public Ticket updateTicketRecord(Ticket ticket)
            throws RemoteException {
        return dao.updateTicket(ticket);
    }

    @Override
    public Ticket deleteTicketRecord(Ticket ticket)
            throws RemoteException {
        return dao.deleteTicket(ticket);
    }

    @Override
    public Ticket findTicketRecordById(Ticket ticket)
            throws RemoteException {
        return dao.findTicketById(ticket);
    }

    @Override
    public Ticket findTicketByNumber(String ticketNumber)
            throws RemoteException {
        return dao.findTicketByNumber(ticketNumber);
    }

    @Override
    public List<Ticket> findTicketsByDriver(int driverId)
            throws RemoteException {
        return dao.findTicketsByDriver(driverId);
    }

    @Override
    public List<Ticket> findTicketsByStatus(ETicketStatus status)
            throws RemoteException {
        return dao.findTicketsByStatus(status);
    }

    @Override
    public List<Ticket> findOverdueTickets()
            throws RemoteException {
        return dao.findOverdueTickets();
    }

    @Override
    public List<Ticket> findAllTicketRecords()
            throws RemoteException {
        return dao.findAllTickets();
    }

    @Override
    public long countTicketsByDriver(int driverId)
            throws RemoteException {
        return dao.countTicketsByDriver(driverId);
    }
}
