package ru.gubern.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", schema = "public")
@ToString(exclude = {"company", "profile", "userChats"})
@EqualsAndHashCode(of = "username")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @AttributeOverride(name = "BirthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;

    @Column(unique = true, nullable = false)
    private String username;

    private String info;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    private Company company;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user")
    private Profile profile;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    @OrderBy("username DESC, personalInfo.lastname")
    private Set<UserChat> userChats = new HashSet<>();

}
