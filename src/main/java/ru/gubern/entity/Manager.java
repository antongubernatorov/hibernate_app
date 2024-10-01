package ru.gubern.entity;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manager extends User {

    private String projectName;

    @Builder
    public Manager(long id, PersonInfo personInfo, String username, String info, Company company, Role role, Profile profile, Set<UserChat> userChats, String projectName) {
        super(id, personInfo, username, info, company, role, profile, userChats);
        this.projectName = projectName;
    }
}
