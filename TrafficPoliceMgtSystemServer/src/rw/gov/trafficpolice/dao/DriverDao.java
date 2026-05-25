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
import rw.gov.trafficpolice.model.Driver;
import org.hibernate.*;

public class DriverDao {

    public Driver saveDriver(Driver driver) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.save(driver);
            tr.commit();
            ss.close();
            return driver;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Driver updateDriver(Driver driver) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.update(driver);
            tr.commit();
            ss.close();
            return driver;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Driver deleteDriver(Driver driver) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.delete(driver);
            tr.commit();
            ss.close();
            return driver;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Driver findDriverById(Driver driver) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Driver found = (Driver) ss.get(
                    Driver.class, driver.getId());
            ss.close();
            return found;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Driver findDriverByLicense(
            String licenseNumber) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Driver found = (Driver) ss.createQuery(
                "FROM Driver d WHERE "
                + "d.licenseNumber = :lic")
                .setParameter("lic", licenseNumber)
                .uniqueResult();
            ss.close();
            return found;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Driver findDriverByPlate(
            String vehiclePlate) {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            Driver found = (Driver) ss.createQuery(
                "FROM Driver d WHERE "
                + "d.vehiclePlate = :plate")
                .setParameter("plate", vehiclePlate)
                .uniqueResult();
            ss.close();
            return found;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Driver> findAllDrivers() {
        try {
            Session ss = HibernateUtil
                    .getSessionFactory().openSession();
            List<Driver> list = ss.createQuery(
                    "FROM Driver").list();
            ss.close();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}