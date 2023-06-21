package com.dmdev;

import com.dmdev.entity.*;
import com.dmdev.util.HibernateTestUtil;
import com.dmdev.util.HibernateUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class HibernateRunnerTest {

    @Test
    void checkH2() {
        try(var sessionFactory = HibernateTestUtil.buildSessionFactory();
            var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var company = Company.builder()
                    .name("Google")
                    .build();


            session.persist(company);

            session.getTransaction().commit();
        }
    }

    @Test
    void orderedBy() {
        try(var sessionFactory = HibernateUtil.buildSessionFactory();
            var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var company = session.get(Company.class, 2l);
            company.getUsers().forEach((k, v) -> System.out.println(v));

            session.getTransaction().commit();
        }
    }

    @Test
    void localeInfo() {
        try(var sessionFactory = HibernateUtil.buildSessionFactory();
            var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var company = session.get(Company.class, 2l);
            company.getLocales().add(LocaleInfo.of("ru", "описание на русском"));
            company.getLocales().add(LocaleInfo.of("en", "english description"));

            session.getTransaction().commit();
        }
    }

    @Test
    void checkManyToMany() {
        try(var sessionFactory = HibernateUtil.buildSessionFactory();
            var session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, 6l);
            Chat chat = session.get(Chat.class, 1l);

            var userChat = UserChat.builder()
                    .createdAt(Instant.now())
                    .createdBy(user.getUsername())
                    .build();

            userChat.setUser(user);
            userChat.setChat(chat);
            session.persist(userChat);

//            User user = session.get(User.class, 6l);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOneToOne() {
        try(var sessionFactory = HibernateUtil.buildSessionFactory();
            var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var user = User.builder()
                    .username("test3@mail.ru")
                    .build();

            var profile = Profile.builder()
                    .language("ru")
                    .street("Lenina")
                    .build();

            profile.setUser(user);

            session.persist(user);

//            User user = session.get(User.class, 6l);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkLazyInitialisation() {
        Company company = null;
        Company companyRef = null;
        try(var sessionFactory = HibernateUtil.buildSessionFactory();
            var session = sessionFactory.openSession()) {
            session.beginTransaction();

            company = session.get(Company.class, 2l);
            companyRef = session.getReference(Company.class, 2l); //возвращает прокси, не делая запрос

            session.getTransaction().commit();
        }

        var users = company.getUsers();
        System.out.println(users.size()); // LazyInitializationException

        var users1 = companyRef.getUsers(); // LazyInitializationException
    }
    @Test
    void deleteCompany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 3l);
        session.remove(company);

        session.getTransaction().commit();
    }

    @Test
    void addUserToNewCompany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();


        var company = Company.builder()
                .name("Facebook")
                .build();

        var user = User.builder()
                .username("sveta@mail.ru")
                .build();

        company.addUser(user);

        session.persist(company);

        session.getTransaction().commit();
    }

    @Test
    void oneToMany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var company = session.get(Company.class, 1l);
        System.out.println(company.getUsers());

        session.getTransaction().commit();
    }

    @Test
    void checkReflectionApi() throws SQLException, IllegalAccessException {
        User user = User.builder()
                .username("ivan@mail.ru")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Ivan")
                        .lastname("Ivanov")
                        .build())
                .birthDate(new Birthday(LocalDate.of(2000, 1, 19)))
                .marriageDate(LocalDate.of(2000, 1, 19))
                .build();

        String sql = """
                insert
                into
                %s
                (%s)
                values
                (%s);
                """;

        String tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(a -> a.schema() + "." + a.name())
                .orElse(user.getClass().getName());

        Field[] fields = user.getClass().getDeclaredFields();

        String columnNames = Arrays.stream(fields)
                .map(field -> Optional.ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(Collectors.joining(", "));

        String columnValues = Arrays.stream(fields)
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        System.out.println(sql.formatted(tableName, columnNames, columnValues));

        Connection connection = null;
        PreparedStatement preparedStatement = connection.prepareStatement(sql.formatted(tableName, columnNames, columnValues));
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            preparedStatement.setObject(i + 1, field.get(user));
        }
    }
}