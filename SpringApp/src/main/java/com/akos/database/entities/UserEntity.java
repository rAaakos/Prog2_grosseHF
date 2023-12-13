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
@Table(name = "USERS")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

    @Column(name = "BIRTHDATE", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", nullable = true)
    private Gender gender;

    @Column(name = "IS_ACTIVE", nullable = true)
    private boolean isActive;

    @ManyToMany
    @JoinTable(name = "USERS_TASKS",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "TASK_ID"))
    private List<TaskEntity> tasks;
}
