package ru.gubern.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class LocaleInfo {

    private String lang;
    private String description;
}
