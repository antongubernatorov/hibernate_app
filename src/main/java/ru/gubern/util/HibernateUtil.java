package ru.gubern.util;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import ru.gubern.converter.BirthdayConverter;
import ru.gubern.entity.User;

@UtilityClass
public class HibernateUtil {
    
    public static SessionFactory buildSessionFactory() {
        var configuration = buildConfiguration();
        configuration.configure();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());

        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(User.class);
        configuration.addAttributeConverter(BirthdayConverter.class);
        configuration.registerTypeOverride(new JsonBinaryType());
        return configuration;
    }
}
