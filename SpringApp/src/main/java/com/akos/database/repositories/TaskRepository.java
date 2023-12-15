package com.akos.database.repositories;

import com.akos.database.entities.TaskEntity;
import com.akos.database.entities.TaskState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    @Query("SELECT a from TaskEntity a WHERE a.state != ?1")
    Page<TaskEntity> findByStateNot(TaskState taskState, Pageable pageable);
}

