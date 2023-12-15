package com.akos.database.dtos;

import com.akos.database.entities.TaskState;
import com.akos.database.entities.TaskType;
import com.akos.database.serializer.LocalDateDeserializer;
import com.akos.database.serializer.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for tasks.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    /**
     * ID for the task.
     */
    private Long id;

    /**
     * The name of the task.
     */
    private String name;

    /**
     * A description providing details about the task.
     */
    private String description;

    /**
     * The amount of work time required per week per person for this task.
     */
    private Long workTimePerWeekPerPerson;

    /**
     * The type of the task.
     */
    private TaskType type;

    /**
     * The deadline for completing the task.
     */
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate deadLine;

    /**
     * The current state of task.
     */
    private TaskState state;

    /**
     * The number of weeks needed to complete the task.
     */
    private Long weeksNeeded;

    /**
     * The number of persons needed to work on the task to complete it
     */
    private Long personsNeeded;
}
