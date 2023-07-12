package com.dmdev;

import com.dmdev.entity.Payment;
import com.dmdev.entity.User;
import com.dmdev.entity.UserChat;
import com.dmdev.util.HibernateUtil;
import com.dmdev.util.TestDataImporter;
import jakarta.persistence.LockModeType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class HibernateRunner5 {
    public static void main(String[] args) {
        try ( SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            Session session2 = sessionFactory.openSession();

            TestDataImporter.importData(sessionFactory);
            session.beginTransaction();
            session2.beginTransaction();

//            только на чтение оптимизации работы бд и защиты от возможных изменений
//            можно прокидывать на сущность или добавлять в запрос через QueryHint
//            session.createNativeQuery("set transaction read only;").executeUpdate();
//            session.setDefaultReadOnly(true);

//            LockModeType.OPTIMISTIC_FORCE_INCREMENT - обновляет версию при чтении и любом обращении
//            LockModeType.OPTIMISTIC - обновляет версию при изменении записи, нужна версия.
//            LockModeType.PESSIMISTIC_READ - for share in postgres, читать доку бд
//            LockModeType.PESSIMISTIC_WRITE - for update in postgres, читать доку бд
            Payment payment = session.find(Payment.class, 1L, LockModeType.OPTIMISTIC);
            payment.setAmount(payment.getAmount() + 10);


            Payment payment2 = session2.find(Payment.class, 1L);
            payment2.setAmount(payment2.getAmount() + 20);

            session.doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    System.out.println(connection.getTransactionIsolation());
//                    connection.setAutoCommit(true);
                }
            });

            session2.getTransaction().commit();
            session.getTransaction().commit();
        }
    }
}