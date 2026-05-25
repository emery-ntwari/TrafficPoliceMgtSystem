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
import rw.gov.trafficpolice.model.Officer;
import org.hibernate.*;

public class OfficerDao {

    public Officer saveOfficer(Officer officer) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.save(officer);
            tr.commit();
            ss.close();
            return officer;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Officer updateOfficer(Officer officer) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.update(officer);
            tr.commit();
            ss.close();
            return officer;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Officer deleteOfficer(Officer officer) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.delete(officer);
            tr.commit();
            ss.close();
            return officer;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Officer findOfficerById(Officer officer) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Officer found = (Officer) ss.get(
                    Officer.class, officer.getId());
            ss.close();
            return found;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Officer findOfficerByUsername(
            String username) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Officer found = (Officer) ss.createQuery(
                "FROM Officer o WHERE o.username = :u")
                .setParameter("u", username)
                .uniqueResult();
            ss.close();
            return found;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Officer login(String username,
            String password) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Officer found = (Officer) ss.createQuery(
                "FROM Officer o WHERE o.username = :u "
                + "AND o.password = :p "
                + "AND o.active = true")
                .setParameter("u", username)
                .setParameter("p", password)
                .uniqueResult();
            ss.close();
            return found;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Officer> findAllOfficers() {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            List<Officer> list = ss.createQuery(
                    "FROM Officer").list();
            ss.close();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}