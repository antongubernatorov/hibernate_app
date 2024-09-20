package ru.gubern.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", schema = "public")
public class User {
    @EmbeddedId
    @AttributeOverride(name = "BirthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;

    @Column(unique = true)
    private String username;

    private String info;

    @Enumerated(EnumType.STRING)
    private Role role;
}
