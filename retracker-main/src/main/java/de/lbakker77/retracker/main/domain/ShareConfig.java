package de.lbakker77.retracker.main.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShareConfig {

    @Column(nullable = false)
    private UUID sharedWithUserId;

    @Column(nullable = false)
    private ShareStatus status;

    @Column(nullable = false)
    private ZonedDateTime sharedAt;

    public void accept() {
        if (status == ShareStatus.REJECTED) throw new IllegalStateException("Cannot accept a rejected share");
        this.status = ShareStatus.ACCEPTED;
    }

    public void reject() {
        if (status == ShareStatus.ACCEPTED) throw new IllegalStateException("Cannot reject an accepted share");
    }

}
