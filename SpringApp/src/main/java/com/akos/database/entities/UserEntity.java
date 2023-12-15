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
@Table(name = "USERS")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "FAMILY_NAME", nullable = false)
    private String familyName;

    @Column(name = "WORKHOURS_PER_WEEK", nullable = true)
    private Long workHoursPerWeek;

    @Enumerated(EnumType.STRING)
    @Column(name = "RANK", nullable = false)
    private UserRank rank;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "BIRTHDATE", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", nullable = true)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "WORKING_STATUS", nullable = true)
    private WorkingStatus workingStatus;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USERS_TASKS",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "TASK_ID"))
    private Set<TaskEntity> tasks;

}
