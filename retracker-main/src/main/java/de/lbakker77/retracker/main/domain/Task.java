package de.lbakker77.retracker.main.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Entity
@Getter
@Setter(AccessLevel.PACKAGE)
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

    private TaskCategory category;

    @ElementCollection(fetch = FetchType.LAZY)
    private SortedSet<EntryHistory> history = new TreeSet<>();

    public SortedSet<EntryHistory> getHistory() {
        return Collections.unmodifiableSortedSet(history);
    }

    Task( RetrackerList retrackerList, String name, LocalDate dueDate, RecurrenceConfig recurrenceConfig, TaskCategory category) {
        this.retrackerList = retrackerList;
        this.name = name;
        this.dueDate = dueDate;
        this.postponedDays = 0;
        this.recurrenceConfig = recurrenceConfig;
        this.category = category;
    }


    public void changeName(String newName) {
        Assert.isTrue(!newName.isEmpty(), "Name must not be empty");
        name = newName;
    }

    public void changeCategory(TaskCategory newCategory) {
        Assert.notNull(newCategory, "Category must not be null");
        category = newCategory;
    }

    public void registerCompletion(LocalDate dateOfCompletion) {
        var lastCompletionDate = determineLastEntry();
        Assert.isTrue(lastCompletionDate.isEmpty() || dateOfCompletion.isAfter(lastCompletionDate.get().completionDate()),"Completion date must be after last entry date");
        var lastCompletion = lastCompletionDate.map(EntryHistory::completionDate).orElse(null);
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
        var lastHistoryEntry = determineLastEntry().orElseThrow(() -> new IllegalStateException("The task has not been completed yet"));
        history.remove(lastHistoryEntry);
        dueDate = lastHistoryEntry.dueDate();
        postponedDays = lastHistoryEntry.postponedDays();
        lastEntryDate = lastHistoryEntry.lastCompletionDate();
    }

    public void postponeUntil(LocalDate newDueDate) {
        Assert.notNull(newDueDate, "New due date cannot be null");
        Assert.notNull(dueDate, "Task must be planned");
        Assert.isTrue(newDueDate.isAfter(dueDate), "New due date must be after current due date");
        postponedDays += (int)dueDate.until(newDueDate, ChronoUnit.DAYS);
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

    public void setManualDueDate(LocalDate manualDueDate) {
        Assert.notNull(manualDueDate, "Manual due date cannot be null");
        if (this.dueDate != null) {
            throw new IllegalStateException("Due date is already set for this task");
        }
        this.dueDate = manualDueDate;
    }
}
