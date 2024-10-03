package ru.gubern.dto;

import ru.gubern.entity.PersonInfo;
import ru.gubern.entity.Role;

public record UserReadDto(Long id,
                          PersonInfo personInfo,
                          String username,
                          String info,
                          Role role,
                          CompanyReadDto companyReadDto) {
}
