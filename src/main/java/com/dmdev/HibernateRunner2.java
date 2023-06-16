package com.dmdev;

import com.dmdev.entity.Birthday;
import com.dmdev.entity.PersonalInfo;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.util.HibernateUtil;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.SQLException;
import java.time.LocalDate;

public class HibernateRunner2 {
    public static void main(String[] args) throws SQLException, InterruptedException {

        //transient
        User user = User.builder()
                .username("ivan4@mail.ru")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Ivan")
                        .lastname("Ivanov")
                        .build())
                .birthDate(new Birthday(LocalDate.of(2000, 1, 19)))
                .marriageDate(LocalDate.of(2000, 1, 19))
                .role(Role.ADMIN)
                .info("""
                            {
                            "name": "Ivan",
                            "age": 19
                            }
                            """)
                .build();

        try ( SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {

            try (Session session1 = sessionFactory.openSession()) {
                session1.beginTransaction();

                //persist через select и insert или update
                session1.merge(user);

                session1.getTransaction().commit();
            }

            try (Session session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                user.getPersonalInfo().setFirstname("Test");
//                обновляет объект с бд
                session2.refresh(user);

                session2.getTransaction().commit();
            }

            try (Session session3 = sessionFactory.openSession()) {
                session3.beginTransaction();

//                в этой сессии объект user находится в состоянии transient

//                transient->persist через select
//                persist->removed
                session3.remove(user);


                session3.getTransaction().commit();
            }

        }
    }
}
