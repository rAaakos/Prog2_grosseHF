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

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public Page<TaskDto> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(taskMapper::toDto);
    }

    public TaskDto findById(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with this id:" + id));
        return taskMapper.toDto(taskEntity);
    }


    public TaskDto save(TaskDto taskDto) {
        TaskEntity taskEntity = taskMapper.toEntity(taskDto);
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        return taskMapper.toDto(savedTaskEntity);
    }

    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new NoSuchElementException("Task not found with Id: " + id);
        }
        taskRepository.deleteById(id);
    }


    public TaskDto update(Long id, TaskDto taskDto) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id:" + id));

        taskMapper.updateEntity(taskDto, taskEntity);

        TaskEntity updatedTaskEntity = taskRepository.save(taskEntity);
        return taskMapper.toDto(updatedTaskEntity);
    }

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

    public Page<TaskDto> findAvailableTasks(Pageable pageable) {
        Page<TaskEntity> taskEntities = taskRepository.findByStateNot(TaskState.COMPLETED, pageable);
        return taskEntities.map(taskMapper::toDto);
    }

    public Page<TaskDto> findTaskType(Pageable pageable, TaskType taskType) {
        Page<TaskEntity> taskEntities = taskRepository.findTaskTypeLikeThis(taskType, pageable);
        return taskEntities.map(taskMapper::toDto);
    }
}
