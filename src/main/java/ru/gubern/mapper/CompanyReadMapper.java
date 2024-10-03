package ru.gubern.mapper;

import org.hibernate.Hibernate;
import ru.gubern.dto.CompanyReadDto;
import ru.gubern.entity.Company;

public class CompanyReadMapper implements Mapper<Company, CompanyReadDto> {
    @Override
    public CompanyReadDto mapFrom(Company object) {
        Hibernate.initialize(object.getLocales());
        return new CompanyReadDto(
                object.getId(),
                object.getCompanyName(),
                object.getLocales()
        );
    }
}
