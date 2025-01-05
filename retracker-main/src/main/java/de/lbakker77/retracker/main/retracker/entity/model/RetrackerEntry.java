package de.lbakker77.retracker.main.retracker.entity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RetrackerEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private UUID id;

    @ManyToOne(targetEntity = RetrackerList.class, optional = false)
    @JoinColumn(name = "retracker_list_id")
    private RetrackerList retrackerList;

    @Version
    private int version;

    @NotNull
    @NotBlank
    private String name;

    private LocalDate dueDate;

    private int postponedDays;

    private LocalDate lastEntryDate;

    @Embedded
    private RecurrenceConfig recurrenceConfig;

    @Embedded
    private UserCategory userCategory;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<EntryHistory> history = new LinkedList<>();

    private Optional<EntryHistory> determineLastEntryDate() {
        return history != null ? history.stream()
                .max(Comparator.comparing(EntryHistory::completionDate)) : Optional.empty();
    }

    public void registerCompletion(LocalDate dateOfCompletion) {
        Assert.isTrue(getLastEntryDate() == null || dateOfCompletion.isAfter(getLastEntryDate()),"Completion date must be after last entry date");

        history.add(new EntryHistory(dateOfCompletion, dueDate, postponedDays));
        setLastEntryDate(dateOfCompletion);
        setPostponedDays(0);
        if(recurrenceConfig != null) {
            var neuDueDate = recurrenceConfig.calcRecurrenceDate(dateOfCompletion);
            setDueDate(neuDueDate);
        }else {
            setDueDate(null);
        }
    }

    public void undoLastCompletion() {
        var lastHistoryEntry = determineLastEntryDate();
        if (lastHistoryEntry.isPresent()) {
            history.remove(lastHistoryEntry.get());
            setDueDate(lastHistoryEntry.get().lastDueDate());
            setPostponedDays(lastHistoryEntry.get().postponedDays());
            lastEntryDate = lastHistoryEntry.get().completionDate();
        }
    }

    public void postponeUntil(LocalDate newDueDate) {
        Assert.isTrue(newDueDate.isAfter(dueDate), "New due date must be after current due date");
        postponedDays += (int)newDueDate.until(dueDate, ChronoUnit.DAYS);
        dueDate = newDueDate;
    }

}
