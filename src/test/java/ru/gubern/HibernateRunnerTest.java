package ru.gubern;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;
import ru.gubern.entity.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

    @Test
    void checkGetReflectionAPI() throws SQLException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException, InstantiationException, IllegalAccessException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = preparedStatement.getResultSet();
        resultSet.getString("username");
        resultSet.getString("lastname");
        
        Class<User> clazz = User.class;
        Constructor<User> constructor = clazz.getConstructor();
        constructor.newInstance()
        var username = clazz.getDeclaredField("username");
        username.setAccessible(true);
        username.set(user, resultSet.getString("username"));
    }    
    @Test
    void checkReflectionApi() throws SQLException, IllegalAccessException {
        User user = User.builder()
                .build();
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