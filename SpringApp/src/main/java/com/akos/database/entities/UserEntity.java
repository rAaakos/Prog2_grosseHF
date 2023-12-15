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
 * JPA Entity class representing users stored in the database.
 */
@Entity
@Table(name = "USERS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    /**
     * ID for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The first name of the user.
     */
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    /**
     * The family name of the user.
     */
    @Column(name = "FAMILY_NAME", nullable = false)
    private String familyName;

    /**
     * The number of work hours the user is available per week.
     */
    @Column(name = "WORKHOURS_PER_WEEK", nullable = true)
    private Long workHoursPerWeek;

    /**
     * The  position in the company of the user, (later ROLES could be implemented by this field)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "RANK", nullable = false)
    private UserRank rank;

    /**
     * The birthdate of the user.
     */
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "BIRTHDATE", nullable = false)
    private LocalDate birthDate;

    /**
     * The gender of the user.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", nullable = true)
    private Gender gender;

    /**
     * The working status of the user for example ACTIVE or RETIRED
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "WORKING_STATUS", nullable = true)
    private WorkingStatus workingStatus;

    /**
     * Set of tasks associated this  user is currently doing
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USERS_TASKS",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "TASK_ID"))
    private Set<TaskEntity> tasks;
}