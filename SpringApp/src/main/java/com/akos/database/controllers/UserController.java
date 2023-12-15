package com.akos.database.controllers;

import com.akos.database.dtos.UserDto;
import com.akos.database.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable) {
        try {
            Page<UserDto> users = userService.findAll(pageable);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to find users", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        try {
            UserDto userDto = userService.findById(id);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        try {
            UserDto createdUserDto = userService.save(userDto);
            return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid User data", e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        try {
            UserDto updatedUserDto = userService.update(id, userDto);
            return ResponseEntity.ok(updatedUserDto);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid User data", e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> partialUpdateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        try {
            UserDto updatedUserDto = userService.partialUpdate(id, userDto);
            return ResponseEntity.ok(updatedUserDto);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid User data", e);
        }
    }

    @PatchMapping("/{userId}/addNewTask/{taskId}")
    public ResponseEntity<UserDto> addNewTaskToUser(@PathVariable Long userId, @PathVariable Long taskId) {
        try {
            UserDto updatedUserDto = userService.addTaskToUser(userId, taskId);
            return ResponseEntity.ok(updatedUserDto);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid User data", e);
        }
    }

    @GetMapping("/availableUsers")
    public ResponseEntity<Page<UserDto>> getAvailableUsers(Pageable pageable) {
        try {
            Page<UserDto> users = userService.findAvailableUsers(pageable);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to find users", e);
        }
    }


}
