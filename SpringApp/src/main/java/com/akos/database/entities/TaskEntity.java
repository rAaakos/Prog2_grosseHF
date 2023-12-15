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

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "TASKS")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "Description", nullable = true)
    private String description;

    @Column(name = "WORKTIME_IN_HOURS_PER_WEEK_PER_PERSON", nullable = false)
    private Long workTimePerWeekPerPerson;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private TaskType type;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "DEADLINE", nullable = false)
    private LocalDate deadLine;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE", nullable = false)
    private TaskState state;

    @Column(name = "WEEKS_NEEDED", nullable = false)
    private Long weeksNeeded;

    @Column(name = "PERSONS_NEED")
    private Long personsNeeded;


    @ManyToMany(mappedBy = "tasks", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UserEntity> users;


}