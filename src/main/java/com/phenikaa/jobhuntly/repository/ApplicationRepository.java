package com.phenikaa.jobhuntly.repository;

import com.phenikaa.jobhuntly.entity.Application;
import com.phenikaa.jobhuntly.entity.Job;
import com.phenikaa.jobhuntly.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer>, JpaSpecificationExecutor<Application> {
    Optional<Application> findByJobIdAndUserId(Integer jobId, Integer userId);

    @Query(value = "SELECT COUNT(*) FROM applications a WHERE a.job_id = :jobId AND a.status <> 'CANCELLED'", nativeQuery = true)
    int countByJobId(@Param("jobId") Integer jobId);

    @Query(value = "SELECT COUNT(*) as total, SUM(IF(a.status = 'INTERVIEWING', 1, 0)) as interviewing FROM applications a WHERE a.user_id = :userId", nativeQuery = true)
    Map<String, Integer> countAllAndByStatus(@Param("userId") Integer userId);

    @Query("SELECT a FROM Application as a WHERE a.user.id = :userId AND a.status = :status AND a.interviewTime <= :current")
    Page<Application> findByUserIdAndStatusAndInterviewTimeBefore(
            @Param("userId") Integer userId,
            @Param("status") ApplicationStatus status,
            @Param("current") LocalDateTime current,
            Pageable pageable
    );
}
