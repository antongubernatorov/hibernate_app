package ru.gubern.dao;

import lombok.Getter;
import ru.gubern.entity.Company;
import ru.gubern.entity.User;

import javax.persistence.EntityManager;


@Getter
public class UserRepository extends RepositoryBase<Long, User> {
    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }

}
