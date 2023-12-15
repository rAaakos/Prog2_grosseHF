package com.akos.database.services;

import com.akos.database.dtos.UserDto;
import com.akos.database.entities.TaskEntity;
import com.akos.database.entities.UserEntity;
import com.akos.database.mappers.UserMapper;
import com.akos.database.repositories.TaskRepository;
import com.akos.database.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.userMapper = userMapper;
    }

    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    public UserDto findById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with this id:" + id));
        return userMapper.toDto(userEntity);
    }

    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found with Id: " + id);
        }
        userRepository.deleteById(id);
    }

    public UserDto save(UserDto userDto) {
        UserEntity userEntity = userMapper.toEntity(userDto);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return userMapper.toDto(savedUserEntity);
    }

    public UserDto update(Long id, UserDto userDto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id:" + id));
        userMapper.updateEntity(userDto, userEntity);
        UserEntity updatedUserEntity = userRepository.save(userEntity);
        return userMapper.toDto(updatedUserEntity);
    }

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

    public UserDto addTaskToUser(Long userId, Long taskId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        TaskEntity taskEntity = taskRepository.findById(taskId).
                orElseThrow(() -> new IllegalArgumentException("Task not found with this id:" + taskId));
        userEntity.getTasks().add(taskEntity);

        UserEntity updatedUser = userRepository.save(userEntity);
        return userMapper.toDto(updatedUser);
    }

    public Page<UserDto> findAvailableUsers(Pageable pageable) {
        Page<UserEntity> userEntities = userRepository.findUsersWhoAreActivelyWorking(pageable);
        return userEntities.map(userMapper::toDto);
    }
}
