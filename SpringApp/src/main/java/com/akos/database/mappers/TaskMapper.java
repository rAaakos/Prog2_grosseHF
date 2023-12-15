package com.akos.database.mappers;

import com.akos.database.dtos.TaskDto;
import com.akos.database.entities.TaskEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Service class implementing the Mapper interface for mapping between TaskEntity and TaskDto.
 */
@Service
public class TaskMapper implements Mapper<TaskEntity, TaskDto> {

    private final ModelMapper modelMapper;

    /**
     * Constructs a TaskMapper with the provided ModelMapper instance.
     *
     * @param modelMapper The ModelMapper instance to be used for mapping.
     */
    public TaskMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }
    @Override
    public TaskDto toDto(TaskEntity task) {
        return modelMapper.map(task, TaskDto.class);
    }

    @Override
    public TaskEntity toEntity(TaskDto dto) {
        return modelMapper.map(dto, TaskEntity.class);
    }

    @Override
    public void updateEntity(TaskDto dto, TaskEntity entity) {
        modelMapper.map(dto, entity);
    }

}
