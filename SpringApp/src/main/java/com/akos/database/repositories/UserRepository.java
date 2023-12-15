package com.akos.database.repositories;

import com.akos.database.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT a FROM UserEntity a WHERE a.workingStatus ='ACTIVE' ")
    Page<UserEntity> findUsersWhoAreActivelyWorking(Pageable pageable);
}