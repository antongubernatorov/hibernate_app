package ru.gubern.entity;

import javax.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "users")
@EqualsAndHashCode(of = "companyName")
@Audited
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String companyName;

    @Builder.Default
    @NotAudited
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<User> users = new HashSet<>();

    @Builder.Default
    @ElementCollection
    @NotAudited
    private List<LocaleInfo> locales = new ArrayList<>();

    public void addUser(User user){
        users.add(user);
        user.setCompany(this);
    }
}
