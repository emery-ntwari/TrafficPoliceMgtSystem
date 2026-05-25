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
import rw.gov.trafficpolice.model.ViolationType;
import org.hibernate.*;

public class ViolationTypeDao {

    public ViolationType saveViolationType(
            ViolationType vt) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.save(vt);
            tr.commit();
            ss.close();
            return vt;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ViolationType updateViolationType(
            ViolationType vt) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.update(vt);
            tr.commit();
            ss.close();
            return vt;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ViolationType deleteViolationType(
            ViolationType vt) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.delete(vt);
            tr.commit();
            ss.close();
            return vt;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ViolationType findViolationTypeById(
            ViolationType vt) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            ViolationType found =
                (ViolationType) ss.get(
                    ViolationType.class, vt.getId());
            ss.close();
            return found;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ViolationType findViolationTypeByCode(
            String code) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            ViolationType found =
                (ViolationType) ss.createQuery(
                "FROM ViolationType v "
                + "WHERE v.code = :code")
                .setParameter("code", code)
                .uniqueResult();
            ss.close();
            return found;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<ViolationType> findAllViolationTypes(){
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            List<ViolationType> list = ss.createQuery(
                    "FROM ViolationType").list();
            ss.close();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}