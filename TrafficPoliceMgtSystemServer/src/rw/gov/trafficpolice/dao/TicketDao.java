/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rw.gov.trafficpolice.dao;
/**
 *
 * @author PC
 */

import java.util.Collections;
import java.util.List;
import rw.gov.trafficpolice.model.ETicketStatus;
import rw.gov.trafficpolice.model.Ticket;
import org.hibernate.*;

public class TicketDao {

    public Ticket saveTicket(Ticket ticket) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.save(ticket);
            tr.commit();
            ss.close();
            return ticket;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Ticket updateTicket(Ticket ticket) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.update(ticket);
            tr.commit();
            ss.close();
            return ticket;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Ticket deleteTicket(Ticket ticket) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.delete(ticket);
            tr.commit();
            ss.close();
            return ticket;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Ticket findTicketById(Ticket ticket) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Ticket found = (Ticket) ss.get(
                    Ticket.class, ticket.getId());
            ss.close();
            return found;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Ticket findTicketByNumber(
            String ticketNumber) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Ticket found = (Ticket) ss.createQuery(
                "FROM Ticket t WHERE "
                + "t.ticketNumber = :tnum")
                .setParameter("tnum", ticketNumber)
                .uniqueResult();
            ss.close();
            return found;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Ticket> findTicketsByStatus(
            ETicketStatus status) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            List<Ticket> list = ss.createQuery(
                "FROM Ticket t WHERE t.status = :s")
                .setParameter("s", status)
                .list();
            ss.close();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public List<Ticket> findTicketsByDriver(
            int driverId) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            List<Ticket> list = ss.createQuery(
                "FROM Ticket t WHERE "
                + "t.driver.id = :driverId")
                .setParameter("driverId", driverId)
                .list();
            ss.close();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public List<Ticket> findOverdueTickets() {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            List<Ticket> list = ss.createQuery(
                "FROM Ticket t WHERE "
                + "t.status = 'ISSUED' "
                + "AND t.dueDate < CURRENT_DATE")
                .list();
            ss.close();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public List<Ticket> findAllTickets() {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            List<Ticket> list = ss.createQuery(
                    "FROM Ticket").list();
            ss.close();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public long countTicketsByDriver(int driverId) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Long count = (Long) ss.createQuery(
                "SELECT COUNT(t) FROM Ticket t "
                + "WHERE t.driver.id = :driverId")
                .setParameter("driverId", driverId)
                .uniqueResult();
            ss.close();
            return count != null ? count : 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}