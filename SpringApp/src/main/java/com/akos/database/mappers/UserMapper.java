package com.akos.database.mappers;

import com.akos.database.dtos.UserDto;
import com.akos.database.entities.UserEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Service class implementing the Mapper interface for mapping between UserEntity and UserDto.
 */
@Service
public class UserMapper implements Mapper<UserEntity, UserDto> {
    private final ModelMapper modelMapper;

    /**
     * Constructs a TaskMapper with the provided ModelMapper instance.
     *
     * @param modelMapper The ModelMapper instance to be used for mapping.
     */
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    @Override
    public UserDto toDto(UserEntity user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserEntity toEntity(UserDto dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    @Override
    public void updateEntity(UserDto dto, UserEntity entity) {
        modelMapper.map(dto, entity);
    }
}
