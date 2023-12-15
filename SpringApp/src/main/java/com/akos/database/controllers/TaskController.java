package com.akos.database.controllers;

import com.akos.database.dtos.TaskDto;
import com.akos.database.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<Page<TaskDto>> getAllTasks(Pageable pageable) {
        try {
            Page<TaskDto> tasks = taskService.findAll(pageable);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to find tasks", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        try {
            TaskDto taskDto = taskService.findById(id);
            return ResponseEntity.ok(taskDto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found", e);
        }
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        try {
            TaskDto createdTaskDto = taskService.save(taskDto);
            return new ResponseEntity<>(createdTaskDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Task data", e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found", e);
        }
    }

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

    @GetMapping("/availableTasks")
    public ResponseEntity<Page<TaskDto>> getAllAvailableTasks(Pageable pageable) {
        try {
            Page<TaskDto> tasks = taskService.findAvailableTasks(pageable);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to find tasks", e);
        }
    }
}
