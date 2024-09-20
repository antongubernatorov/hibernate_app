package ru.gubern;

import org.slf4j.Logger;
import ru.gubern.entity.User;
import ru.gubern.util.HibernateUtil;
import org.slf4j.LoggerFactory;

public class HibernateRunner {

    private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) {
        var user = User.builder()
                .username("ivan@gmail.com")
                .lastname("Ivanov")
                .firstname("Ivan")
                .build();
        log.info("User created");
        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {

            try (var session1 = sessionFactory.openSession()) {

                session1.saveOrUpdate(user);

                session1.getTransaction().commit();
            }


            try (var session2 = sessionFactory.openSession()) {

                session2.getTransaction().commit();
            }

            log.warn("Session should be closed");

        }
    }
}
