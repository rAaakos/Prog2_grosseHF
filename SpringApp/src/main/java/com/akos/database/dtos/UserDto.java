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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String firstName;

    private String familyName;


    private Long workHoursPerWeek;

    private UserRank rank;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthDate;

    private Gender gender;

    private WorkingStatus workingStatus;

    private Set<TaskDto> tasks;
}
