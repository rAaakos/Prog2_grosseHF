package com.akos.database.repositories;

import com.akos.database.entities.TaskEntity;
import com.akos.database.entities.TaskState;
import com.akos.database.entities.TaskType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing TaskEntity instances.
 */
@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    /**
     * Retrieves tasks with a state different from the specified taskState.
     *
     * @param taskState The TaskState to exclude.
     * @param pageable  The pagination information.
     * @return A Page containing tasks with a state different from the specified taskState.
     */
    @Query("SELECT a FROM TaskEntity a WHERE a.state != :taskState")
    Page<TaskEntity> findByStateNot(@Param("taskState") TaskState taskState, Pageable pageable);

    /**
     * Retrieves tasks with a specific taskType.
     *
     * @param taskType The TaskType to match.
     * @param pageable The pagination information.
     * @return A Page containing tasks with the specified taskType.
     */
    @Query("SELECT a FROM TaskEntity a WHERE a.type = :taskType")
    Page<TaskEntity> findTaskTypeLikeThis(@Param("taskType") TaskType taskType, Pageable pageable);
}

