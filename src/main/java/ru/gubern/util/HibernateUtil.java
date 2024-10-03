package ru.gubern.util;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import ru.gubern.converter.BirthdayConverter;
import ru.gubern.entity.Audit;
import ru.gubern.entity.Revision;
import ru.gubern.entity.User;
import ru.gubern.interceptor.GlobalInterceptor;
import ru.gubern.listener.AuditTableListener;

@UtilityClass
public class HibernateUtil {
    
    public static SessionFactory buildSessionFactory() {
        var configuration = buildConfiguration();
        configuration.configure();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());

        var sessionFactory = configuration.buildSessionFactory();
        var sessionFactoryIml = sessionFactory.unwrap(SessionFactoryImpl.class);
        var service = sessionFactoryIml.getServiceRegistry().getService(EventListenerRegistry.class);

        var auditTableListener = new AuditTableListener();
        service.appendListeners(EventType.PRE_INSERT, auditTableListener);
        service.appendListeners(EventType.PRE_DELETE, auditTableListener);
        return sessionFactory;
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Audit.class);
        configuration.addAttributeConverter(BirthdayConverter.class);
        configuration.addAnnotatedClass(Revision.class);
        configuration.setInterceptor(new GlobalInterceptor());
        configuration.registerTypeOverride(new JsonBinaryType());
        return configuration;
    }
}
