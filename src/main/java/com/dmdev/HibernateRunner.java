package com.dmdev;

import com.dmdev.converter.BirthdayConverter;
import com.dmdev.entity.Birthday;
import com.dmdev.entity.PersonalInfo;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.util.HibernateUtil;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
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
        try (   //аналог threadPool
                SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
                //аналог connection
                Session session = sessionFactory.openSession();
        ) {

            User newUser = User.builder()
                    .username("ivan2@mail.ru")
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

            Transaction transaction = session.beginTransaction();
            try {
                session.persist(newUser);
//                User user1 = session.get(User.class, newUser.getId());
//                нет запроса из-за кеша 1 уровня
//                User user2 = session.get(User.class, newUser.getId());

//                все ниже переводит сущность в состояние detached
//                удаление из кеша определенной сущности
//                session.evict(user);
//                session.detach(user); //по jpa
//                очистка всего кеша
//                session.clear();
//                закрытие сессии
//                session.close();

                newUser.getPersonalInfo().setLastname("Petrov");
//                грязная сессия, в конце произойдет update для синхронизации сущности с бд
                System.out.println("session.isDirty() = " + session.isDirty());

//                все изменения залиты в бд
                session.flush();
                System.out.println("session.isDirty() = " + session.isDirty());

                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }

        }
    }
}
