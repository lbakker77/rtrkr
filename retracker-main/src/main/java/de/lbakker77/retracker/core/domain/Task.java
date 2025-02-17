package de.lbakker77.retracker.main.core.domain;


import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Entity
@Getter
public class Task {
    protected Task() {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private UUID id;

    @Version
    private Integer version;

    @ManyToOne(targetEntity = RetrackerList.class, optional = false)
    @JoinColumn(name = "retracker_list_id")
    private RetrackerList retrackerList;

    public UUID getListId() {
        return retrackerList.getId();
    }

    private String name;

    private LocalDate dueDate;

    private LocalDate lastEntryDate;

    private int postponedDays;

    @Embedded
    private RecurrenceConfig recurrenceConfig;

    @Embedded
    private UserCategory userCategory;

    @ElementCollection(fetch = FetchType.LAZY)
    private SortedSet<EntryHistory> history = new TreeSet<>();

    public SortedSet<EntryHistory> getHistory() {
        return Collections.unmodifiableSortedSet(history);
    }

    Task( RetrackerList retrackerList, String name, LocalDate dueDate, RecurrenceConfig recurrenceConfig, UserCategory userCategory) {
        this.retrackerList = retrackerList;
        this.name = name;
        this.dueDate = dueDate;
        this.postponedDays = 0;
        this.recurrenceConfig = recurrenceConfig;
        this.userCategory = userCategory;
    }


    public void changeName(String newName) {
        Assert.isTrue(!newName.isEmpty(), "Name cannot be empty");
        name = newName;
    }

    public void changeUserCategory(String categoryName, String categoryColor) {
        Assert.isTrue(!categoryName.isEmpty(), "Category name cannot be empty");
        userCategory = new  UserCategory(categoryName, categoryColor);
    }

    public void registerCompletion(LocalDate dateOfCompletion) {
        var lastEntryDate = determineLastEntry();
        Assert.isTrue(lastEntryDate.isEmpty() || dateOfCompletion.isAfter(lastEntryDate.get().completionDate()),"Completion date must be after last entry date");
        var lastCompletion = lastEntryDate.map(EntryHistory::completionDate).orElse(null);
        var actualPostponedDays = getPostponedDays();
        if (getDueDate() != null && dateOfCompletion.isBefore(getDueDate())) {
            actualPostponedDays = Math.max(0, actualPostponedDays - (int) dateOfCompletion.until(getDueDate(), ChronoUnit.DAYS));
        }
        history.add(new EntryHistory(dateOfCompletion, getDueDate(), actualPostponedDays, lastCompletion));
        postponedDays = 0;
        if(recurrenceConfig != null) {
            dueDate = recurrenceConfig.calcRecurrenceDate(dateOfCompletion);
        }else {
            dueDate = null;
        }
        this.lastEntryDate = dateOfCompletion;
    }

    public void undoLastCompletion() {
        var lastHistoryEntry = determineLastEntry();
        Assert.isTrue(lastHistoryEntry.isPresent(), "No history entries available");
        history.remove(lastHistoryEntry.get());
        dueDate = lastHistoryEntry.get().dueDate();
        postponedDays = lastHistoryEntry.get().postponedDays();
        lastEntryDate = lastHistoryEntry.get().lastCompletionDate();
    }

    public void postponeUntil(LocalDate newDueDate) {
        Assert.notNull(newDueDate, "New due date cannot be null");
        Assert.notNull(dueDate, "Due date cannot be null");
        Assert.isTrue(newDueDate.isAfter(dueDate), "New due date must be after current due date");
        postponedDays = (int)dueDate.until(newDueDate, ChronoUnit.DAYS);
        dueDate = newDueDate;
    }

    public void updateRecurrenceConfig(RecurrenceConfig newRecurrenceConfig) {
        if (!newRecurrenceConfig.equals(recurrenceConfig)) {
            recurrenceConfig = newRecurrenceConfig;
            var lastEntry = determineLastEntry();
            if (lastEntry.isPresent()) {
                postponedDays = 0;
                dueDate = recurrenceConfig.calcRecurrenceDate(lastEntry.get().completionDate());
            }
        }
    }

    public void deleteRecurrenceConfig() {
        if (recurrenceConfig != null) {
            recurrenceConfig = null;
            postponedDays = 0;
        }
    }

    public Optional<EntryHistory> determineLastEntry() {
        return history != null ? history.stream()
                .max(Comparator.comparing(EntryHistory::completionDate)) : Optional.empty();
    }
}
