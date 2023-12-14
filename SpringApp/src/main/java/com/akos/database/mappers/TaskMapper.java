package com.akos.database.mappers;

import com.akos.database.dtos.TaskDto;
import com.akos.database.entities.TaskEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TaskMapper implements Mapper<TaskEntity, TaskDto> {

    private final ModelMapper modelMapper;

    public TaskMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
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
