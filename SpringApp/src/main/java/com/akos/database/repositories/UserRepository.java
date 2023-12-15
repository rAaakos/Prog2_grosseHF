package com.akos.database.repositories;

import com.akos.database.entities.UserEntity;
import com.akos.database.entities.UserRank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing UserEntity instances.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Retrieves users who are actively working, arent retired or on vacation
     *
     * @param pageable The pagination information.
     * @return A Page containing users who are actively working.
     */
    @Query("SELECT a FROM UserEntity a WHERE a.workingStatus = 'ACTIVE'")
    Page<UserEntity> findUsersWhoAreActivelyWorking(Pageable pageable);

    /**
     * Retrieves users with a specific UserRank.
     *
     * @param rank     The UserRank to match.
     * @param pageable The pagination information.
     * @return A Page containing users with the specified UserRank.
     */
    @Query("SELECT a FROM UserEntity a WHERE a.rank = :rank")
    Page<UserEntity> findUsersWithThisTypeOfRank(@Param("rank") UserRank rank, Pageable pageable);

    /**
     * Retrieves users with less weekly work hours than the specified limit.
     *
     * @param weeklyWorkHours The maximum number of weekly work hours.
     * @param pageable        The pagination information.
     * @return A Page containing users with less weekly work hours than the specified limit.
     */
    @Query("SELECT a FROM UserEntity a WHERE a.workHoursPerWeek < :weeklyWorkHours")
    Page<UserEntity> findUsersWithLessWeeklyWorkHours(@Param("weeklyWorkHours") Long weeklyWorkHours, Pageable pageable);
}