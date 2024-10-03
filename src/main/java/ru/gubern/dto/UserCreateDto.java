package ru.gubern.dto;

import ru.gubern.entity.PersonInfo;
import ru.gubern.entity.Role;
import ru.gubern.validation.UpdateCheck;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public record UserCreateDto(
                            @Valid
                            PersonInfo personInfo,
                            @NotNull(groups = UpdateCheck.class)
                            String username,
                            String info,
                            Role role,
                            Long companyId) {
}
