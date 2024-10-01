package ru.gubern.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Programmer extends User {

    @Enumerated(EnumType.STRING)
    private Language language;

    @Builder
    public Programmer(long id, PersonInfo personInfo, String username, String info, Company company, Role role, Profile profile, Set<UserChat> userChats, Language language) {
        super(id, personInfo, username, info, company, role, profile, userChats);
        this.language = language;
    }
}
