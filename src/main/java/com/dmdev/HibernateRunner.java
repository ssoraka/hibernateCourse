package com.dmdev;

import com.dmdev.converter.BirthdayConverter;
import com.dmdev.entity.Birthday;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class HibernateRunner {
    public static void main(String[] args) throws SQLException, InterruptedException {

//
//        BlockingDeque<Connection> pool = new LinkedBlockingDeque<>();
//        pool.add(DriverManager.getConnection("db.url", "db.username", "db.password"));
//        Connection connection = pool.take();


        Configuration config = new Configuration();
//        <mapping class="com.dmdev.entity.User"/>
//        config.addAnnotatedClass(User.class);

//        @Column(name = "birth_date")
//        config.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());

//        @Convert(converter = BirthdayConverter.class)
//        config.addAttributeConverter(new BirthdayConverter(), true);
//        @Converter(autoApply = true)

//       by default
//        config.configure("/path/to/hibernate.cfg.xml");
        config.configure();


        try (   //аналог threadPool
                SessionFactory sessionFactory = config.buildSessionFactory();
                //аналог connection
                Session session = sessionFactory.openSession();
        ) {

            User user = User.builder()
                    .username("ivan1@mail.ru")
                    .firstname("Ivan")
                    .lastname("Ivanov")
                    .birthDate(new Birthday(LocalDate.of(2000, 1, 19)))
                    .marriageDate(LocalDate.of(2000, 1, 19))
                    .role(Role.ADMIN)
                    .build();

            Transaction transaction = session.beginTransaction();
            try {
                session.persist(user);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }

        }
    }
}
