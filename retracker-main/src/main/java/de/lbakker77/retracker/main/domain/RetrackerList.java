package de.lbakker77.retracker.main.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
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

    @Column(nullable = false)
    private String icon;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<ShareConfig> shareConfigEntries = new LinkedList<>();

    public List<ShareConfig> getShareConfigEntries() {
        return Collections.unmodifiableList(shareConfigEntries);
    }

    public boolean hasAccess(UUID userId) {
        return ownerId.equals(userId) || shareConfigEntries.stream().anyMatch(s -> s.getSharedWithUserId().equals(userId) && s.getStatus() == ShareStatus.ACCEPTED);
    }

    public boolean isInvited(UUID userId) {
        return shareConfigEntries.stream().anyMatch(s -> s.getSharedWithUserId().equals(userId) && s.getStatus() == ShareStatus.PENDING);
    }

    public boolean mayChangeOrDelete(UUID userId) {
        return ownerId.equals(userId);
    }

    public void inviteUser(UUID userId) {
        if (shareConfigEntries.stream().anyMatch(s -> s.getSharedWithUserId().equals(userId))) {
            throw new IllegalStateException("User is already invited to this list");
        }
        if (userId == ownerId) {
            throw new IllegalArgumentException("Cannot invite the owner of the list");
        }
        shareConfigEntries.add(new ShareConfig(userId, ShareStatus.PENDING, ZonedDateTime.now()));
    }

    public void acceptInvitation(UUID userId) {
        var shareConfig = getShareConfig(userId);
        shared = true;
        shareConfig.accept();
    }

    public void removeAccess(UUID userId) {
        shareConfigEntries.removeIf(s -> s.getSharedWithUserId().equals(userId));
        shared = shareConfigEntries.stream().anyMatch(s -> s.getStatus() == ShareStatus.ACCEPTED);
    }

    public void rejectInvitation(UUID userId) {
        var shareConfig = getShareConfig(userId);
        shareConfig.reject();
    }

    private ShareConfig getShareConfig(UUID userId) {
        return shareConfigEntries.stream().filter(s -> s.getSharedWithUserId().equals(userId)).findFirst().orElseThrow(() -> new IllegalArgumentException("User is not invited to this list"));
    }

    public void changeName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Name must not be empty");
        }
        name = newName;
    }

    public void changeIcon(String newIcon) {
        if (newIcon == null || newIcon.trim().isEmpty()) {
            throw new IllegalArgumentException("Icom must not be empty");
        }
        icon = newIcon;
    }

}
