package com.akos.database.controllers;

import com.akos.database.dtos.UserDto;
import com.akos.database.entities.UserRank;
import com.akos.database.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

/**
 * Controller class for managing users.
 * This class handles HTTP requests related to users, including retrieval, creation, updating, and deletion.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructs a new UserController with the specified UserService.
     *
     * @param userService The UserService used for handling user-related operations.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users with pagination support.
     *
     * @param pageable The pageable information for retrieving users.
     * @return ResponseEntity containing a Page of UserDto or an error response.
     */
    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable) {
        try {
            Page<UserDto> users = userService.findAll(pageable);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to find users", e);
        }
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity containing a UserDto or an error response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        try {
            UserDto userDto = userService.findById(id);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    /**
     * Creates a new user.
     *
     * @param userDto The UserDto representing the user to be created.
     * @return ResponseEntity containing the created UserDto or an error response.
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        try {
            UserDto createdUserDto = userService.save(userDto);
            return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid User data", e);
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @return ResponseEntity with no content or an error response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    /**
     * Updates a user by their ID.
     *
     * @param id      The ID of the user to update.
     * @param userDto The UserDto representing the updated user data.
     * @return ResponseEntity containing the updated UserDto or an error response.
     */
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

    /**
     * Partially updates a user by their ID.
     *
     * @param id      The ID of the user to partially update.
     * @param userDto The UserDto representing the partially updated user data.
     * @return ResponseEntity containing the partially updated UserDto or an error response.
     */
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

    /**
     * Adds a new task to a user.
     *
     * @param userId The ID of the user.
     * @param taskId The ID of the task to add to the user.
     * @return ResponseEntity containing the updated UserDto or an error response.
     */
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

    /**
     * Retrieves all available users with pagination support.
     *
     * @param pageable The pageable information for retrieving users.
     * @return ResponseEntity containing a Page of UserDto or an error response.
     */
    @GetMapping("/availableUsers")
    public ResponseEntity<Page<UserDto>> getAvailableUsers(Pageable pageable) {
        try {
            Page<UserDto> users = userService.findAvailableUsers(pageable);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to find users", e);
        }
    }

    /**
     * Retrieves users with a specific rank and pagination support.
     *
     * @param pageable The pageable information for retrieving users.
     * @param rank     The UserRank representing the rank of users to retrieve.
     * @return ResponseEntity containing a Page of UserDto or an error response.
     */
    @GetMapping("/usersRank/{rank}")
    public ResponseEntity<Page<UserDto>> getUsersWithLessThanWeeklyWorkHours(
            Pageable pageable, @PathVariable UserRank rank) {
        try {
            Page<UserDto> users = userService.findUsersWithRank(pageable, rank);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to find users", e);
        }
    }

    /**
     * Retrieves users with weekly work hours less than a specified value and pagination support.
     *
     * @param pageable The pageable information for retrieving users.
     * @param hours    The maximum weekly work hours allowed.
     * @return ResponseEntity containing a Page of UserDto or an error response.
     */
    @GetMapping("/workingLessThan/{hours}")
    public ResponseEntity<Page<UserDto>> getUsersWithLessThanWeeklyWorkHours(
            Pageable pageable, @PathVariable Long hours) {
        try {
            Page<UserDto> users = userService.findUsersWithLessWeeklyWorkHours(pageable, hours);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to find users", e);
        }
    }
}