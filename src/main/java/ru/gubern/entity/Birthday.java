package ru.gubern.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record Birthday(LocalDate birthday) {

    public long getAge(){
        return ChronoUnit.YEARS.between(birthday, LocalDate.now());
    }
}
