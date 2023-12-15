package com.akos.database.services;

import com.akos.database.dtos.UserDto;
import com.akos.database.entities.TaskEntity;
import com.akos.database.entities.TaskState;
import com.akos.database.entities.UserEntity;
import com.akos.database.entities.UserRank;
import com.akos.database.mappers.UserMapper;
import com.akos.database.repositories.TaskRepository;
import com.akos.database.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service class providing business logic for managing users.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final UserMapper userMapper;

    /**
     * Constructs a UserService with the provided UserRepository, UserMapper, and TaskRepository.
     *
     * @param userRepository The repository for user entities.
     * @param userMapper     The mapper for converting between user entities and DTOs.
     * @param taskRepository The repository for task entities.
     */
    public UserService(UserRepository userRepository, UserMapper userMapper, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.userMapper = userMapper;
    }

    /**
     * Retrieves all users with pagination.
     *
     * @param pageable The pagination information.
     * @return A Page containing user DTOs.
     */
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The corresponding user DTO.
     * @throws IllegalArgumentException if the user is not found.
     */
    public UserDto findById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with this id:" + id));
        return userMapper.toDto(userEntity);
    }

    /**
     * Deletes a user by its ID.
     *
     * @param id The ID of the user to be deleted.
     * @throws NoSuchElementException if the user is not found.
     */
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found with Id: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Saves a new user.
     *
     * @param userDto The user DTO to be saved.
     * @return The saved user DTO.
     */
    public UserDto save(UserDto userDto) {
        UserEntity userEntity = userMapper.toEntity(userDto);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return userMapper.toDto(savedUserEntity);
    }

    /**
     * Updates a user.
     *
     * @param id      The ID of the user to be updated.
     * @param userDto The updated user DTO.
     * @return The updated user DTO.
     * @throws IllegalArgumentException if the user is not found.
     */
    public UserDto update(Long id, UserDto userDto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id:" + id));
        userMapper.updateEntity(userDto, userEntity);
        UserEntity updatedUserEntity = userRepository.save(userEntity);
        return userMapper.toDto(updatedUserEntity);
    }

    /**
     * Partially updates a user based on a partial user DTO.
     *
     * @param id             The ID of the user to be partially updated.
     * @param partialUserDto The partial user DTO containing updated information.
     * @return The partially updated user DTO.
     * @throws NoSuchElementException if the user is not found.
     */
    public UserDto partialUpdate(Long id, UserDto partialUserDto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with this id: " + id));

        Optional.ofNullable(partialUserDto.getWorkHoursPerWeek()).ifPresent(userEntity::setWorkHoursPerWeek);
        Optional.ofNullable(partialUserDto.getWorkingStatus()).ifPresent(userEntity::setWorkingStatus);
        Optional.ofNullable(partialUserDto.getGender()).ifPresent(userEntity::setGender);
        Optional.ofNullable(partialUserDto.getRank()).ifPresent(userEntity::setRank);

        UserEntity updatedUser = userRepository.save(userEntity);
        return userMapper.toDto(updatedUser);
    }

    /**
     * Adds a task to a user.
     *
     * @param userId The ID of the user.
     * @param taskId The ID of the task to be added.
     * @return The updated user DTO.
     * @throws IllegalArgumentException if the user or task is not found, or if the task has already been completed.
     */
    public UserDto addTaskToUser(Long userId, Long taskId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        TaskEntity taskEntity = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with this id:" + taskId));

        if (taskEntity.getState().equals(TaskState.COMPLETED)) {
            throw new IllegalArgumentException("Task has already been completed");
        }

        userEntity.getTasks().add(taskEntity);

        UserEntity updatedUser = userRepository.save(userEntity);
        return userMapper.toDto(updatedUser);
    }

    /**
     * Retrieves all available users.
     *
     * @param pageable The pagination information.
     * @return A Page containing available user DTOs.
     */
    public Page<UserDto> findAvailableUsers(Pageable pageable) {
        Page<UserEntity> userEntities = userRepository.findUsersWhoAreActivelyWorking(pageable);
        return userEntities.map(userMapper::toDto);
    }

    /**
     * Retrieves users with a specific rank.
     *
     * @param pageable The pagination information.
     * @param rank     The rank of users to retrieve.
     * @return A Page containing user DTOs with the specified rank.
     */
    public Page<UserDto> findUsersWithRank(Pageable pageable, UserRank rank) {
        Page<UserEntity> userEntities = userRepository.findUsersWithThisTypeOfRank(rank, pageable);
        return userEntities.map(userMapper::toDto);
    }

    /**
     * Retrieves users with less weekly work hours.
     *
     * @param pageable        The pagination information.
     * @param weeklyWorkHours The maximum weekly work hours.
     * @return A Page containing user DTOs with less weekly work hours.
     */
    public Page<UserDto> findUsersWithLessWeeklyWorkHours(Pageable pageable, Long weeklyWorkHours) {
        Page<UserEntity> userEntities = userRepository.findUsersWithLessWeeklyWorkHours(weeklyWorkHours, pageable);
        return userEntities.map(userMapper::toDto);
    }
}
