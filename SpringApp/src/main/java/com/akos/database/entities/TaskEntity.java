package com.akos.database.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "TASKS")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String Name;

    @Column(name = "Description", nullable = true)
    private String description;

    @Column(name = "WORKTIME_IN_HOURS_PER_WEEK_PER_PERSON", nullable = false)
    private Long workTimePerWeekPerPerson;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private UserRank type;

    @Column(name = "DEADLINE", nullable = false)
    private LocalDate deadLine;

    @Enumerated
    @Column(name = "STATE", nullable = false)
    private TaskState state;

    @Column(name = "WEEKS_NEEDED", nullable = false)
    private Long weeksNeeded;

    @Column(name = "PERSONS_NEED")
    private Long personsNeeded;

    @ManyToMany(mappedBy = "tasks")
    private List<UserEntity> users;
}