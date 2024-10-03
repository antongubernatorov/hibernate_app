package ru.gubern.entity;

import jdk.dynalink.Operation;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Serializable entityId;

    private String entityName;

    private String entityContent;

    @Enumerated(EnumType.STRING)
    private Operation operation;

    public enum Operation{
        SAVE, UPDATE, DELETE, INSERT
    }

}
