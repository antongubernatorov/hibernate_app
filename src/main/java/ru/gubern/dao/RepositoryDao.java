package ru.gubern.dao;

import ru.gubern.entity.BaseEntity;

import java.io.Serializable;
import java.util.*;

import static java.util.Collections.*;

public interface RepositoryDao<K extends Serializable, E extends BaseEntity<K>> {
    E save(E entity);

    void delete(K id);

    void update(E entity);

    default Optional<E> findById(K id){
        return findById(id, emptyMap());
    }

    Optional<E> findById(K id, Map<String, Object> properties);

    List<E> findAll();
}
