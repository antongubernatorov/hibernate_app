package ru.gubern.entity;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
@Entity
public class PersonInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String firstname;
    private String lastname;
    private LocalDate birthDate;
}