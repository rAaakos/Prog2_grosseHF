package com.akos.database.mappers;

/**
 * Interface defining a contract for mapping between entities and DTOs, as well as updating entities based on DTOs. From this interface are the two mapper-classes implemented
 *
 * @param <A> The type of the entity.
 * @param <B> The type of the DTO.
 */

public interface Mapper<A, B> {

    /**
     * Converts an entity to a DTO.
     *
     * @param entity The entity to be converted.
     * @return The corresponding DTO.
     */
    B toDto(A entity);

    /**
     * Converts a DTO to an entity.
     *
     * @param dto The DTO to be converted.
     * @return The corresponding entity.
     */
    A toEntity(B dto);

    /**
     * Updates an entity based on information from a DTO.
     *
     * @param dto    The DTO containing information to update the entity.
     * @param entity The entity to be updated.
     */
    void updateEntity(B dto, A entity);
}
