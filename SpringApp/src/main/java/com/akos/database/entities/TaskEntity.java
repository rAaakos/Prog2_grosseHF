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
import java.util.List;
import java.util.Objects;

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


    //ez mukodik igy fetchel
    @ManyToMany(mappedBy = "tasks", fetch = FetchType.EAGER)
    private List<UserEntity> users;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TaskEntity that = (TaskEntity) obj;
        //bcs test only functions like this
        boolean isBothNull = users.isEmpty() && that.users.isEmpty();
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(workTimePerWeekPerPerson, that.workTimePerWeekPerPerson) &&
                type == that.type &&
                Objects.equals(deadLine, that.deadLine) &&
                state == that.state &&
                Objects.equals(weeksNeeded, that.weeksNeeded) &&
                Objects.equals(personsNeeded, that.personsNeeded) &&
                (Objects.equals(users, that.users) || isBothNull);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, workTimePerWeekPerPerson, type, deadLine, state, weeksNeeded, personsNeeded, users);
    }

}