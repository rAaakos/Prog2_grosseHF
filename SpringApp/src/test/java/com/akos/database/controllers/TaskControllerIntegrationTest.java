package com.akos.database.controllers;

import com.akos.database.TestDataUtil;
import com.akos.database.dtos.TaskDto;
import com.akos.database.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final TaskService taskService;

    @Autowired
    public TaskControllerIntegrationTest(MockMvc mockMvc, TaskService taskService) {
        this.mockMvc = mockMvc;
        this.taskService = taskService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateTaskSuccessfullyReturnsHttp201Created() throws Exception {
        TaskDto testTask = TestDataUtil.createTaskDto1();
        String taskJson = objectMapper.writeValueAsString(testTask);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateTaskSuccessfullyReturnedTask() throws Exception {
        TaskDto testTask = TestDataUtil.createTaskDto1();
        String taskJson = objectMapper.writeValueAsString(testTask);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Task 1")
        );
    }

    @Test
    public void testThatCreateUserSuccessfullyReturnsHttp200FindAll() throws Exception {
        TaskDto testTask = TestDataUtil.createTaskDto1();
        String taskJson = objectMapper.writeValueAsString(testTask);
        taskService.save(testTask);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatTasksSuccessfullyFound() throws Exception {
        TaskDto testTask1 = TestDataUtil.createTaskDto1();
        TaskDto testTask2 = TestDataUtil.createTaskDto2();
        TaskDto savedTask1 = taskService.save(testTask1);
        TaskDto savedTask2 = taskService.save(testTask2);

        int page = 0;
        int size = 10;

        mockMvc.perform(
                MockMvcRequestBuilders.get("/tasks")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].id").value(savedTask1.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].name").value("Task 1")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[1].id").value(savedTask2.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[1].name").value("Task 2")
        );
    }

    @Test
    public void testDeleteTaskByIdRemovesTaskAndGetsHttp204() throws Exception {
        TaskDto testTask = TestDataUtil.createTaskDto1();
        TaskDto savedTask = taskService.save(testTask);
        String taskJson = objectMapper.writeValueAsString(testTask);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/tasks/{id}", savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );

        // Verify that the task has been deleted
        mockMvc.perform(
                MockMvcRequestBuilders.get("/tasks/{id}", savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testGetTaskByIdReturnsHttp200AndGetsTask() throws Exception {
        TaskDto testTask = TestDataUtil.createTaskDto1();
        TaskDto savedTask = taskService.save(testTask);
        String taskJson = objectMapper.writeValueAsString(testTask);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/tasks/{id}", savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/tasks/{id}", savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedTask.getId().intValue())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Task 1")
        );
    }

    @Test
    public void testUpdateTaskSuccessfullyReturnsHttp200() throws Exception {
        TaskDto testTask = TestDataUtil.createTaskDto1();
        TaskDto savedTask = taskService.save(testTask);
        String taskJson = objectMapper.writeValueAsString(testTask);

        TaskDto updatedTaskDto = TestDataUtil.createTaskDto1();
        updatedTaskDto.setWorkTimePerWeekPerPerson(20L);
        String updatedTaskJson = objectMapper.writeValueAsString(updatedTaskDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/tasks/{id}", savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTaskJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.workTimePerWeekPerPerson").value(20L)
        );
    }

    @Test
    public void testPartialUpdateTaskSuccessfullyReturnsHttp200() throws Exception {
        TaskDto testTask = TestDataUtil.createTaskDto1();
        TaskDto savedTask = taskService.save(testTask);

        TaskDto partialUpdateDto = new TaskDto();
        partialUpdateDto.setWorkTimePerWeekPerPerson(20L);
        String partialUpdateJson = objectMapper.writeValueAsString(partialUpdateDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/tasks/{id}", savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(partialUpdateJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.workTimePerWeekPerPerson").value(20L)
        );
    }

    @Test
    void getAllAvailableTasks() throws Exception {
        TaskDto testTask1 = TestDataUtil.createTaskDto1();
        TaskDto testTask2 = TestDataUtil.createTaskDto2();
        TaskDto savedTask1 = taskService.save(testTask1);
        TaskDto savedTask2 = taskService.save(testTask2);

        int page = 0;
        int size = 10;

        mockMvc.perform(
                MockMvcRequestBuilders.get("/tasks/availableTasks")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content").isArray()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content").value(hasSize(1))
        ).andExpect(MockMvcResultMatchers.jsonPath("$.content[0].state").value("IN_PROGRESS")
        );
    }
}

