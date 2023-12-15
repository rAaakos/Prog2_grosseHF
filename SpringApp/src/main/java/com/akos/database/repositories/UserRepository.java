package com.akos.database.repositories;

import com.akos.database.entities.UserEntity;
import com.akos.database.entities.UserRank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT a FROM UserEntity a WHERE a.workingStatus = 'ACTIVE'")
    Page<UserEntity> findUsersWhoAreActivelyWorking(Pageable pageable);

    @Query("SELECT a FROM UserEntity a WHERE a.rank = :rank")
    Page<UserEntity> findUsersWithThisTypeOfRank(@Param("rank") UserRank rank, Pageable pageable);

    @Query("SELECT a FROM UserEntity a WHERE a.workHoursPerWeek < :weeklyWorkHours")
    Page<UserEntity> findUsersWithLessWeeklyWorkHours(@Param("weeklyWorkHours") Long weeklyWorkHours, Pageable pageable);
}