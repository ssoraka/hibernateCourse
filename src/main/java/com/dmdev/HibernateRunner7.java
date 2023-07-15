package com.dmdev;

import com.dmdev.entity.Payment;
import com.dmdev.entity.User;
import com.dmdev.interceptor.GlobalInterceptor;
import com.dmdev.util.HibernateUtil;
import com.dmdev.util.TestDataImporter;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

@Slf4j
public class HibernateRunner7 {

    @Transactional
    public static void main(String[] args) {
        try ( SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {

            TestDataImporter.importData(sessionFactory);

            User user = null;
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                user = session.find(User.class, 1L);
                user.getCompany().getName();
                user.getUserChats().size();
                var user1 = session.find(User.class, 1L);

                var payment = session.createQuery("select p from Payment p where p.receiver.id = :userId", Payment.class)
                        .setParameter("userId", 1L)
                        .setCacheable(true)
                        .getResultList();

                System.out.println(sessionFactory.getStatistics().getCacheRegionStatistics("Users"));
                session.getTransaction().commit();
            }


            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                var user2 = session.find(User.class, 1L);
                user2.getCompany().getName();
                user2.getUserChats().size();

                var payment = session.createQuery("select p from Payment p where p.receiver.id = :userId", Payment.class)
                        .setParameter("userId", 1L)
                        .setCacheable(true)
                        .getResultList();

                System.out.println(sessionFactory.getStatistics().getCacheRegionStatistics("Users"));

                session.getTransaction().commit();
            }
        }
    }
}