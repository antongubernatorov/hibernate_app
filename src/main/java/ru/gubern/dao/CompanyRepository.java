package ru.gubern.dao;

import ru.gubern.entity.Company;

import javax.persistence.EntityManager;

public class CompanyRepository extends RepositoryBase<Long, Company> {
    public CompanyRepository(EntityManager entityManager) {
        super(Company.class, entityManager);
        //todo
    }
}
