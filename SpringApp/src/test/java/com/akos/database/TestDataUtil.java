package com.akos.database;

import com.akos.database.dtos.TaskDto;
import com.akos.database.dtos.UserDto;
import com.akos.database.entities.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TestDataUtil {

    public static List<UserDto> createListOfUsers() {
        return Arrays.asList(createTestUserDtoA());
    }
    public static TaskDto createTaskDto1() {
        return TaskDto.builder()
                .name("Task 1")
                .description("Description for Task 1")
                .workTimePerWeekPerPerson(10L)
                .type(TaskType.TESTING)
                .deadLine(LocalDate.of(2023, 12, 31))
                .state(TaskState.IN_PROGRESS)
                .weeksNeeded(2L)
                .personsNeeded(3L)
                .build();
    }

    public static TaskDto createTaskDto2() {
        return TaskDto.builder()
                .name("Task 2")
                .description("Description for Task 2")
                .workTimePerWeekPerPerson(15L)
                .type(TaskType.BUG_FIX)
                .deadLine(LocalDate.of(2023, 12, 15))
                .state(TaskState.COMPLETED)
                .weeksNeeded(3L)
                .personsNeeded(2L)
                .build();
    }
    public static UserDto createTestUserDtoA() {

        UserDto userA = UserDto.builder()
                .firstName("Akos")
                .familyName("Albert")
                .workHoursPerWeek(30L)
                .rank(UserRank.WORKER)
                .birthDate(LocalDate.of(2003, 7, 11))
                .gender(Gender.MALE)
                .workingStatus(WorkingStatus.ACTIVE)
                .build();


        return userA;
    }

    public static UserDto createTestUserDtoB() {
        UserDto userB = UserDto.builder()
                .firstName("Bela")
                .familyName("Biro")
                .workHoursPerWeek(25L)
                .rank(UserRank.WORKER)
                .birthDate(LocalDate.of(1995, 5, 20))
                .gender(Gender.FEMALE)
                .workingStatus(WorkingStatus.ON_VACATION)
                .build();


        return userB;
    }
}
