package ru.gubern;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;
import ru.gubern.entity.Company;
import ru.gubern.entity.Profile;
import ru.gubern.entity.User;
import ru.gubern.util.HibernateUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

    @Test
    void checkHql(){
        try(var sessionFactory = HibernateUtil.buildSessionFactory();
            var session = sessionFactory.openSession();){
            session.beginTransaction();

            //jpql / hql
            var list = session.createQuery(
                    "select u from User u where u.personalInfo.firstname = :firstname",
                    User.class)
                    .setParameter("firstname", "Ivan").list();
            session.getTransaction().commit();
        }
    }

    @Test
    void checkH2() {
        try(var sessionFactory = HibernateUtil.buildSessionFactory();
            var session = sessionFactory.openSession();){
            session.beginTransaction();

            var company = Company.builder().companyName("AntonFLy")
                    .build();
            session.save(company);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOneToOne(){
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        User user = null;
        var profile = Profile.builder()
                .language("ru")
                .street("Kolosa 18")
                .build();

        session.save(user);
        profile.setUser(user);
        session.save(profile);

        session.getTransaction().commit();
    }

    @Test
    void deleteCompany(){
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        var user = session.get(User.class, 1L);
        session.delete(user);
        session.getTransaction().commit();
    }

    @Test
    void oneToMany(){
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var company = session.get(Company.class, 1);
        System.out.println(company);
        session.getTransaction().commit();
    }

    @Test
    void checkGetReflectionAPI() throws SQLException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException, InstantiationException, IllegalAccessException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = preparedStatement.getResultSet();
        resultSet.getString("username");
        resultSet.getString("lastname");
        
        Class<User> clazz = User.class;
        Constructor<User> constructor = clazz.getConstructor();
        var user = constructor.newInstance();
        var username = clazz.getDeclaredField("username");
        username.setAccessible(true);
        username.set(user, resultSet.getString("username"));
    }    
    @Test
    void checkReflectionApi() throws SQLException, IllegalAccessException {
        User user = null;
        //language=SQL
        String sql = """
                insert
                into 
                %s
                (%s)
                values
                (%s)
                """;

        var tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(user.getClass().getName());
        Field[] declaredFields = user.getClass().getDeclaredFields();
        var columnNames = Arrays.stream(declaredFields)
                .map(field -> Optional.ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(joining());

        var columnValues = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(joining());

        Connection connection = null;
        var preparedStatement = connection.prepareStatement(sql.formatted(tableName, columnNames, columnValues));
        for (Field field : declaredFields){
            field.setAccessible(true);
            preparedStatement.setObject(1, field.get(user));
        }
    }
}