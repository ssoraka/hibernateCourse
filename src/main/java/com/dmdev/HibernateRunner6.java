package com.dmdev;

import com.dmdev.entity.Payment;
import com.dmdev.interceptor.GlobalInterceptor;
import com.dmdev.util.HibernateUtil;
import com.dmdev.util.TestDataImporter;
import jakarta.persistence.LockModeType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.SQLException;

public class HibernateRunner6 {
    public static void main(String[] args) {
        try ( SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory
                    .withOptions()
                    .interceptor(new GlobalInterceptor())
                    .openSession();

            TestDataImporter.importData(sessionFactory);
            session.beginTransaction();

            Payment payment = session.find(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);

            session.getTransaction().commit();
        }
    }
}