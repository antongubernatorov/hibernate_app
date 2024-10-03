package ru.gubern.mapper;

import lombok.RequiredArgsConstructor;
import ru.gubern.dao.CompanyRepository;
import ru.gubern.dto.UserCreateDto;
import ru.gubern.entity.User;

@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<UserCreateDto, User> {

    private final CompanyRepository companyRepository;

    @Override
    public User mapFrom(UserCreateDto object) {
        return User.builder()
                .personInfo(object.personInfo())
                .username(object.username())
                .info(object.info())
                .role(object.role())
                .company(companyRepository.findById(object.companyId()).orElseThrow(IllegalArgumentException::new))
                .build();
    }
}
