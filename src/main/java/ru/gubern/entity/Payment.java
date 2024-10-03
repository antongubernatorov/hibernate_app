package ru.gubern.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Audited
public class Payment implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer amount;

    @NotAudited
    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_id")
    private User receiver;
}
