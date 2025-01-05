package de.lbakker77.retracker.main.retracker.entity.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetrackerList {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private UUID id;

    @Version
    private Integer version;

    @Column(nullable = false)
    private UUID ownerId;

    @Column(nullable = false)
    private String name;

    private boolean shared;

    private boolean defaultList;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<ShareConfig> shareConfigEntries = new LinkedList<>();

}
