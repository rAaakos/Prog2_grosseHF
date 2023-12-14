package com.akos.database.mappers;

public interface Mapper<A, B> {
    B toDto(A entity);

    A toEntity(B dto);

    void updateEntity(B dto, A entity);
}
