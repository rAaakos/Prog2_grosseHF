package com.akos.database.controllers;

import com.akos.database.TestDataUtil;
import com.akos.database.dtos.TaskDto;
import com.akos.database.dtos.UserDto;
import com.akos.database.services.TaskService;
import com.akos.database.services.UserService;
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
public class UserControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final TaskService taskService;

    @Autowired
    public UserControllerIntegrationTest(MockMvc mockMvc, UserService userService, TaskService taskService) {
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.taskService = taskService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateUserSuccessfullyReturnsHttp201Created() throws Exception {
        UserDto testUser = TestDataUtil.createTestUserDtoA();
        String userJson = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateUserSuccessfullyReturnedUser() throws Exception {
        UserDto testUser = TestDataUtil.createTestUserDtoA();
        String userJson = objectMapper.writeValueAsString(testUser);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value("Akos")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.familyName").value("Albert")
        );
    }

    @Test
    public void testThatCreateUserSuccessfullyReturnsHttp200FindAll() throws Exception {
        UserDto testUser = TestDataUtil.createTestUserDtoA();
        String userJson = objectMapper.writeValueAsString(testUser);
        userService.save(testUser);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUsersSuccessfullyFound() throws Exception {
        UserDto testUser = TestDataUtil.createTestUserDtoA();
        String userJson = objectMapper.writeValueAsString(testUser);
        UserDto savedUser = userService.save(testUser);

        int page = 0;
        int size = 10;

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users", savedUser.getId())
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].id").value(savedUser.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].firstName").value("Akos")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].familyName").value("Albert")
        );
    }

    @Test
    public void testDeleteUserByIdRemovesUserAndReturnsHttp204() throws Exception {
        UserDto testUser = TestDataUtil.createTestUserDtoA();
        UserDto savedUser = userService.save(testUser);
        String userJson = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/{id}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/{id}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testUpdateUserSuccessfullyReturnsHttp200() throws Exception {
        UserDto testUser = TestDataUtil.createTestUserDtoA();
        UserDto savedUser = userService.save(testUser);
        String userJson = objectMapper.writeValueAsString(testUser);

        UserDto updatedUserDto = TestDataUtil.createTestUserDtoA();
        updatedUserDto.setWorkHoursPerWeek(20L);
        String updatedUserJson = objectMapper.writeValueAsString(updatedUserDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/{id}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.workHoursPerWeek").value(20L)
        );
    }

    @Test
    public void testPartialUpdateUserSuccessfullyReturnsHttp200() throws Exception {
        UserDto testUser = TestDataUtil.createTestUserDtoA();
        UserDto savedUser = userService.save(testUser);

        UserDto partialUpdateDto = new UserDto();
        partialUpdateDto.setWorkHoursPerWeek(20L);
        String partialUpdateJson = objectMapper.writeValueAsString(partialUpdateDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/users/{id}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(partialUpdateJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.workHoursPerWeek").value(20L)
        );
    }

    @Test
    public void testAddsTaskToUser() throws Exception {
        UserDto testUser = TestDataUtil.createTestUserDtoA();
        UserDto savedUser = userService.save(testUser);

        TaskDto testTask = TestDataUtil.createTaskDto1();
        TaskDto savedTask = taskService.save(testTask);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/users/{userId}/addNewTask/{taskId}", savedUser.getId(), savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.tasks").value(hasSize(1))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.tasks[0].name").value("Task 1")
        );
    }

    @Test
    void getAllAvailableUsers() throws Exception {
        UserDto userDtoA = TestDataUtil.createTestUserDtoA();
        UserDto userDtoB = TestDataUtil.createTestUserDtoB();
        UserDto savedUser1 = userService.save(userDtoA);
        UserDto savedUser2 = userService.save(userDtoB);

        int page = 0;
        int size = 10;

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/availableUsers")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content").isArray()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content").value(hasSize(1))
        ).andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value("Akos")
        );
    }

    @Test
    void testGetAllUsersWithThisRank() throws Exception {
        UserDto userDtoA = TestDataUtil.createTestUserDtoA();
        UserDto userDtoB = TestDataUtil.createTestUserDtoB();
        UserDto savedUser1 = userService.save(userDtoA);
        UserDto savedUser2 = userService.save(userDtoB);

        int page = 0;
        int size = 10;

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/usersRank/{rank}", savedUser1.getRank())
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content").isArray()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content").value(hasSize(1))
        ).andExpect(MockMvcResultMatchers.jsonPath("$.content[0].rank").value("BOSS")
        );
    }

    @Test
    void testGetAllUsersWithLessWorkingHourPerWeek() throws Exception {
        UserDto userDtoA = TestDataUtil.createTestUserDtoA();
        UserDto userDtoB = TestDataUtil.createTestUserDtoB();
        UserDto savedUser1 = userService.save(userDtoA);
        UserDto savedUser2 = userService.save(userDtoB);

        int page = 0;
        int size = 10;

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/workingLessThan/{hours}", savedUser1.getWorkHoursPerWeek())
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content").isArray()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content").value(hasSize(1))
        ).andExpect(MockMvcResultMatchers.jsonPath("$.content[0].workHoursPerWeek").value(25L)
        );
    }
}

