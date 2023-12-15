package com.akos.database.dtos;

import com.akos.database.entities.Gender;
import com.akos.database.entities.UserRank;
import com.akos.database.entities.WorkingStatus;
import com.akos.database.serializer.LocalDateDeserializer;
import com.akos.database.serializer.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

/**
 * Data Transfer Object (DTO) for representing users.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    /**
     * Unique identifier for the user.
     */
    private Long id;

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The family name of the user.
     */
    private String familyName;

    /**
     * The number of work hours the user is available per week.
     */
    private Long workHoursPerWeek;

    /**
     * The rank or position of the user.
     */
    private UserRank rank;

    /**
     * The birthdate of the user.
     */
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthDate;

    /**
     * The gender of the user.
     */
    private Gender gender;

    /**
     * The working status of the user like RETIRED/ACTIVE.
     */
    private WorkingStatus workingStatus;

    /**
     * The set of tasks associated with the user.
     */
    private Set<TaskDto> tasks;
}
