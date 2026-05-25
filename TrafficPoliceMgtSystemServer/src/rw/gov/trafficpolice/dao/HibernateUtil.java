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

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            System.out.println("[Hibernate] Starting...");
            sessionFactory = new AnnotationConfiguration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
            System.out.println(
                "[Hibernate] Connected to database!");
            System.out.println(
                "[Hibernate] Tables created!");
        } catch (Throwable ex) {
            System.err.println(
                "[Hibernate] FAILED: " + ex.getMessage());
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}