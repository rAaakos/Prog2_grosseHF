package com.akos.database.entities;

import com.akos.database.serializer.LocalDateDeserializer;
import com.akos.database.serializer.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

/**
 * JPA Entity class representing tasks stored in the database.
 */
@Entity
@Table(name = "TASKS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

    /**
     * Unique identifier for the task.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the task.
     */
    @Column(name = "NAME", nullable = false)
    private String name;

    /**
     * A description providing additional details about the task.
     */
    @Column(name = "DESCRIPTION", nullable = true)
    private String description;

    /**
     * The amount of work time required per week per person for this task.
     */
    @Column(name = "WORKTIME_IN_HOURS_PER_WEEK_PER_PERSON", nullable = false)
    private Long workTimePerWeekPerPerson;

    /**
     * The type of the task like BUG_FIX, TESTING
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private TaskType type;

    /**
     * The deadline for completing the task.
     */
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "DEADLINE", nullable = false)
    private LocalDate deadLine;

    /**
     * The current state of the task.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "STATE", nullable = false)
    private TaskState state;

    /**
     * The number of weeks needed to complete the task.
     */
    @Column(name = "WEEKS_NEEDED", nullable = false)
    private Long weeksNeeded;

    /**
     * The number of persons needed to work on the task.
     */
    @Column(name = "PERSONS_NEED")
    private Long personsNeeded;

    /**
     * Set of users who are doing with this task.
     */
    @ManyToMany(mappedBy = "tasks", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UserEntity> users;
}