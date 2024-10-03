package ru.gubern;


import org.junit.platform.commons.logging.LoggerFactory;
import ru.gubern.entity.User;
import ru.gubern.util.HibernateUtil;

import java.util.logging.Logger;

public class HibernateRunner {

    private static final Logger log = (Logger) LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) {
        /*var user = User.builder()
                .username("ivan@gmail.com")
                .lastname("Ivanov")
                .firstname("Ivan")
                .build();*/
        log.info("User created");
        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {

            try (var session1 = sessionFactory.openSession()) {

                /*session1.saveOrUpdate(user);*/

                session1.getTransaction().commit();
            }


            try (var session2 = sessionFactory.openSession()) {

                session2.getTransaction().commit();
            }

        }
    }
}
