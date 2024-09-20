package ru.gubern.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -7427478791336614246L;
    private String firstname;
    private String lastname;
    private Birthday birthDate;
}
