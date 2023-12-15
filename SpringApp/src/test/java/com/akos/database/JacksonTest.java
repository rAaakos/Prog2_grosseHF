package com.akos.database;

import com.akos.database.entities.TaskEntity;
import com.akos.database.entities.TaskState;
import com.akos.database.entities.TaskType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class JacksonTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void FromJavaToJSON() throws JsonProcessingException {
        TaskEntity task = TaskEntity.builder()
                .name("Task 1")
                .description("Description for Task 1")
                .workTimePerWeekPerPerson(10L)
                .type(TaskType.TESTING)
                .deadLine(LocalDate.of(2023, 12, 31))
                .state(TaskState.IN_PROGRESS)
                .weeksNeeded(2L)
                .personsNeeded(3L)
                .build();


        String result = objectMapper.writeValueAsString(task);
        assertThat(result).isEqualTo("{\"id\":null,\"name\":\"Task 1\",\"description\":\"Description for Task 1\",\"workTimePerWeekPerPerson\":10,\"type\":\"TESTING\",\"deadLine\":\"2023-12-31\",\"state\":\"IN_PROGRESS\",\"weeksNeeded\":2,\"personsNeeded\":3}");
    }


}

