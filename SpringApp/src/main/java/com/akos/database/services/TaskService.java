package com.akos.database.services;

import com.akos.database.dtos.TaskDto;
import com.akos.database.entities.TaskEntity;
import com.akos.database.entities.TaskState;
import com.akos.database.entities.TaskType;
import com.akos.database.mappers.TaskMapper;
import com.akos.database.repositories.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service class providing business logic for managing tasks.
 */
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    /**
     * Constructs a TaskService with the provided TaskRepository and TaskMapper.
     *
     * @param taskRepository The repository for task entities.
     * @param taskMapper     The mapper for converting between task entities and DTOs.
     */
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    /**
     * Retrieves all tasks with pagination.
     *
     * @param pageable The pagination information.
     * @return A Page containing task DTOs.
     */
    public Page<TaskDto> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(taskMapper::toDto);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return The corresponding task DTO.
     * @throws IllegalArgumentException if the task is not found.
     */
    public TaskDto findById(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with this id:" + id));
        return taskMapper.toDto(taskEntity);
    }

    /**
     * Saves a new task.
     *
     * @param taskDto The task DTO to be saved.
     * @return The saved task DTO.
     */
    public TaskDto save(TaskDto taskDto) {
        TaskEntity taskEntity = taskMapper.toEntity(taskDto);
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        return taskMapper.toDto(savedTaskEntity);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to be deleted.
     * @throws NoSuchElementException if the task is not found.
     */
    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new NoSuchElementException("Task not found with Id: " + id);
        }
        taskRepository.deleteById(id);
    }

    /**
     * Updates a task.
     *
     * @param id      The ID of the task to be updated.
     * @param taskDto The updated task DTO.
     * @return The updated task DTO.
     * @throws IllegalArgumentException if the task is not found.
     */
    public TaskDto update(Long id, TaskDto taskDto) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id:" + id));

        taskMapper.updateEntity(taskDto, taskEntity);

        TaskEntity updatedTaskEntity = taskRepository.save(taskEntity);
        return taskMapper.toDto(updatedTaskEntity);
    }

    /**
     * Partially updates a task based on a partial task DTO.
     *
     * @param id             The ID of the task to be partially updated.
     * @param partialTaskDto The partial task DTO containing updated information.
     * @return The partially updated task DTO.
     * @throws NoSuchElementException if the task is not found.
     */
    public TaskDto partialUpdate(Long id, TaskDto partialTaskDto) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with this id: " + id));

        Optional.ofNullable(partialTaskDto.getName()).ifPresent(taskEntity::setName);
        Optional.ofNullable(partialTaskDto.getDescription()).ifPresent(taskEntity::setDescription);
        Optional.ofNullable(partialTaskDto.getWorkTimePerWeekPerPerson()).ifPresent(taskEntity::setWorkTimePerWeekPerPerson);
        Optional.ofNullable(partialTaskDto.getType()).ifPresent(taskEntity::setType);
        Optional.ofNullable(partialTaskDto.getDeadLine()).ifPresent(taskEntity::setDeadLine);
        Optional.ofNullable(partialTaskDto.getState()).ifPresent(taskEntity::setState);
        Optional.ofNullable(partialTaskDto.getWeeksNeeded()).ifPresent(taskEntity::setWeeksNeeded);
        Optional.ofNullable(partialTaskDto.getPersonsNeeded()).ifPresent(taskEntity::setPersonsNeeded);

        TaskEntity updatedTaskEntity = taskRepository.save(taskEntity);
        return taskMapper.toDto(updatedTaskEntity);
    }

    /**
     * Retrieves all available tasks.
     *
     * @param pageable The pagination information.
     * @return A Page containing available task DTOs.
     */
    public Page<TaskDto> findAvailableTasks(Pageable pageable) {
        Page<TaskEntity> taskEntities = taskRepository.findByStateNot(TaskState.COMPLETED, pageable);
        return taskEntities.map(taskMapper::toDto);
    }

    /**
     * Retrieves tasks of a specific type.
     *
     * @param pageable The pagination information.
     * @param taskType The type of tasks to retrieve.
     * @return A Page containing task DTOs of the specified type.
     */
    public Page<TaskDto> findTaskType(Pageable pageable, TaskType taskType) {
        Page<TaskEntity> taskEntities = taskRepository.findTaskTypeLikeThis(taskType, pageable);
        return taskEntities.map(taskMapper::toDto);
    }
}