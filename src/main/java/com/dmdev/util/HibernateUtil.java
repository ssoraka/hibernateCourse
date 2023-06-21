package com.dmdev.util;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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
        return config.buildSessionFactory();
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

        config.registerTypeOverride(new JsonBinaryType(), new String[]{"jsonb"});
        return config;
    }
}
