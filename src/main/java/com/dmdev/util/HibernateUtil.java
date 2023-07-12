package com.dmdev.util;

import com.dmdev.interceptor.GlobalInterceptor;
import com.dmdev.listener.AuditTableListener;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        //
//        BlockingDeque<Connection> pool = new LinkedBlockingDeque<>();
//        pool.add(DriverManager.getConnection("db.url", "db.username", "db.password"));
//        Connection connection = pool.take();


        Configuration config = buildConfiguration();

//       by default
//        config.configure("/path/to/hibernate.cfg.xml");
        config.configure();

        SessionFactory sessionFactory = config.buildSessionFactory();
        registerListeners(sessionFactory);

        return sessionFactory;
    }

    private static void registerListeners(SessionFactory sessionFactory) {
        SessionFactoryImpl sessionFactoryImpl = sessionFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry listenerRegistry = sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);

        AuditTableListener auditTableListener = new AuditTableListener();
        listenerRegistry.appendListeners(EventType.PRE_INSERT, auditTableListener);
        listenerRegistry.appendListeners(EventType.PRE_DELETE, auditTableListener);
    }


    public static Configuration buildConfiguration() {
        Configuration config = new Configuration();
//        <mapping class="com.dmdev.entity.User"/>
//        config.addAnnotatedClass(User.class);

//        @Column(name = "birth_date")
//        config.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());

//        @Convert(converter = BirthdayConverter.class)
//        config.addAttributeConverter(new BirthdayConverter(), true);
//        @Converter(autoApply = true)

        config.setInterceptor(new GlobalInterceptor());

        config.registerTypeOverride(new JsonBinaryType(), new String[]{"jsonb"});
        return config;
    }
}
