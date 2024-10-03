package ru.gubern;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;
import ru.gubern.dao.CompanyRepository;
import ru.gubern.dao.PaymentRepository;
import ru.gubern.dao.UserRepository;
import ru.gubern.mapper.CompanyReadMapper;
import ru.gubern.mapper.UserCreateMapper;
import ru.gubern.mapper.UserReadMapper;
import ru.gubern.service.UserService;
import ru.gubern.util.HibernateUtil;

import java.lang.reflect.Proxy;
import java.util.logging.Logger;

public class HibernateRunner {

    private static final Logger log = (Logger) LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {
            var session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
            session.beginTransaction();

            var companyRepository = new CompanyRepository(session);

            var companyReadMapper = new CompanyReadMapper();
            var userMapper = new UserReadMapper(companyReadMapper);

            var userCreateMapper = new UserCreateMapper(companyRepository);

            var userRepository = new UserRepository(session);
            var paymentRepository = new PaymentRepository(session);

            var userService = new UserService(userRepository, userMapper, userCreateMapper);

            paymentRepository.findById(1L).ifPresent(System.out::println);

            session.getTransaction().commit();
        }
    }
}
