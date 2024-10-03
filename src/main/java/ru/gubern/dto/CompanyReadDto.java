package ru.gubern.dto;

public record CompanyReadDto(Long id,
                             String name,
                             java.util.List<ru.gubern.entity.LocaleInfo> locales) {
}
