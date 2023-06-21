package com.dmdev;

import com.dmdev.entity.*;
import com.dmdev.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.SQLException;
import java.time.LocalDate;

public class HibernateRunner3 {
    public static void main(String[] args) throws SQLException, InterruptedException {

        Company company = Company.builder()
                .name("Amazon")
                .build();

        User user = null;
//        User user = User.builder()
//                .username("ivan@mail.ru")
//                .personalInfo(PersonalInfo.builder()
//                        .firstname("Ivan")
//                        .lastname("Ivanov")
//                        .build())
//                .birthDate(new Birthday(LocalDate.of(2000, 1, 19)))
//                .marriageDate(LocalDate.of(2000, 1, 19))
//                .role(Role.ADMIN)
//                .info("""
//                            {
//                            "name": "Ivan",
//                            "age": 19
//                            }
//                            """)
//                .company(company)
//                .build();

        try ( SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {

            try (Session session1 = sessionFactory.openSession()) {
                session1.beginTransaction();

//                session1.persist(company);
//                session1.persist(user);
//                User user1 = session1.get(User.class, 1l);
//                session1.evict(user1);

                Company company1 = session1.get(Company.class, 1l);

                session1.getTransaction().commit();
            }
        }
    }
}
