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
import rw.gov.trafficpolice.model.Payment;
import org.hibernate.*;

public class PaymentDao {

    public Payment savePayment(Payment payment) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.save(payment);
            tr.commit();
            ss.close();
            return payment;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Payment updatePayment(Payment payment) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.update(payment);
            tr.commit();
            ss.close();
            return payment;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Payment deletePayment(Payment payment) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.delete(payment);
            tr.commit();
            ss.close();
            return payment;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Payment findPaymentById(Payment payment) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Payment found = (Payment) ss.get(
                    Payment.class, payment.getId());
            ss.close();
            return found;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Payment findPaymentByTicket(int ticketId) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Payment found = (Payment) ss.createQuery(
                "FROM Payment p WHERE "
                + "p.ticket.id = :ticketId")
                .setParameter("ticketId", ticketId)
                .uniqueResult();
            ss.close();
            return found;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public double getTotalCollectedFines() {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Double total = (Double) ss.createQuery(
                "SELECT SUM(p.amountPaid) FROM Payment p")
                .uniqueResult();
            ss.close();
            return total != null ? total : 0.0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0.0;
    }

    public List<Payment> findAllPayments() {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            List<Payment> list = ss.createQuery(
                    "FROM Payment").list();
            ss.close();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}