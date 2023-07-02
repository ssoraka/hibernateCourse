package com.dmdev;

import com.dmdev.entity.User;
import com.dmdev.entity.UserChat;
import com.dmdev.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;

import java.util.List;
import java.util.Map;

public class HibernateRunner4 {
    public static void main(String[] args) {
        try ( SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();

            session.beginTransaction();

//            включение @FetchProfile только для getById
//            session.enableFetchProfile("withCompanyAndPayments");
//            session.get(User.class, 1l);


            RootGraph<User> userGraph = session.createEntityGraph(User.class);
            userGraph.addAttributeNodes("company", "userChats");
            SubGraph<UserChat> chats = userGraph.addSubgraph("userChats", UserChat.class);
            chats.addAttributeNodes("chat");

            Map<String, Object> properties = Map.of(
                    GraphSemantic.LOAD.getJakartaHintName(), userGraph // аналог session.getEntityGraph("withCompanyAndChats")
            );
            User user = session.find(User.class, 1l, properties);
            System.out.println(user.getCompany().getName());
            System.out.println(user.getUserChats().size());

            List<User> users = session.createQuery("select u from User u join fetch u.payments ", User.class)
                    .setHint(GraphSemantic.LOAD.getJakartaHintName(), session.getEntityGraph("withCompanyAndChats"))
                    .list();

//            List<User> users = session.createQuery("select u from User u join fetch u.payments ", User.class)
//                    .list();
//            users.forEach(user -> System.out.println(user.getPayments().size()));

            session.getTransaction().commit();
        }
    }
}