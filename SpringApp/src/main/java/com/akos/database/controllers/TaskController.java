package com.akos.database.controllers;

import com.akos.database.dtos.TaskDto;
import com.akos.database.entities.TaskType;
import com.akos.database.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

/**
 * Controller class for managing tasks.
 * This class handles HTTP requests related to tasks, including retrieval, creation, updating, and deletion.
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    /**
     * Constructs a new TaskController with the specified TaskService.
     *
     * @param taskService The TaskService used for handling task-related operations.
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Retrieves all tasks with pagination support.
     *
     * @param pageable The pageable information for retrieving tasks.
     * @return ResponseEntity containing a Page of TaskDto or an error response.
     */
    @GetMapping
    public ResponseEntity<Page<TaskDto>> getAllTasks(Pageable pageable) {
        try {
            Page<TaskDto> tasks = taskService.findAll(pageable);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to find tasks", e);
        }
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return ResponseEntity containing a TaskDto or an error response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        try {
            TaskDto taskDto = taskService.findById(id);
            return ResponseEntity.ok(taskDto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found", e);
        }
    }

    /**
     * Creates a new task.
     *
     * @param taskDto The TaskDto representing the task to be created.
     * @return ResponseEntity containing the created TaskDto or an error response.
     */
    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        try {
            TaskDto createdTaskDto = taskService.save(taskDto);
            return new ResponseEntity<>(createdTaskDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Task data", e);
        }
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to delete.
     * @return ResponseEntity with no content or an error response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found", e);
        }
    }

    /**
     * Updates a task by its ID.
     *
     * @param id      The ID of the task to update.
     * @param taskDto The TaskDto representing the updated task data.
     * @return ResponseEntity containing the updated TaskDto or an error response.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        try {
            TaskDto updatedTaskDto = taskService.update(id, taskDto);
            return ResponseEntity.ok(updatedTaskDto);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Task data", e);
        }
    }

    /**
     * Partially updates a task by its ID.
     *
     * @param id      The ID of the task to partially update.
     * @param taskDto The TaskDto representing the partially updated task data.
     * @return ResponseEntity containing the partially updated TaskDto or an error response.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<TaskDto> partialUpdateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        try {
            TaskDto updatedTaskDto = taskService.partialUpdate(id, taskDto);
            return ResponseEntity.ok(updatedTaskDto);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Task data", e);
        }
    }

    /**
     * Retrieves all available tasks with pagination support.
     *
     * @param pageable The pageable information for retrieving tasks.
     * @return ResponseEntity containing a Page of TaskDto or an error response.
     */
    @GetMapping("/availableTasks")
    public ResponseEntity<Page<TaskDto>> getAllAvailableTasks(Pageable pageable) {
        try {
            Page<TaskDto> tasks = taskService.findAvailableTasks(pageable);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to find tasks", e);
        }
    }

    /**
     * Retrieves all available tasks of a specific type with pagination support.
     *
     * @param pageable The pageable information for retrieving tasks.
     * @param taskType The TaskType representing the type of tasks to retrieve.
     * @return ResponseEntity containing a Page of TaskDto or an error response.
     */
    @GetMapping("/taskType/{taskType}")
    public ResponseEntity<Page<TaskDto>> getAllAvailableTasks(Pageable pageable, @PathVariable TaskType taskType) {
        try {
            Page<TaskDto> tasks = taskService.findTaskType(pageable, taskType);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to find tasks", e);
        }
    }
}

