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

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    @Query("SELECT a FROM TaskEntity a WHERE a.state != :taskState")
    Page<TaskEntity> findByStateNot(@Param("taskState") TaskState taskState, Pageable pageable);

    @Query("SELECT a FROM TaskEntity a WHERE a.type = :taskType")
    Page<TaskEntity> findTaskTypeLikeThis(@Param("taskType") TaskType taskType, Pageable pageable);

}


