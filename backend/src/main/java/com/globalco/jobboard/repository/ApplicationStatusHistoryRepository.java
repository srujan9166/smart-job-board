package com.globalco.jobboard.repository;

import com.globalco.jobboard.entity.ApplicationStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link ApplicationStatusHistory} entity persistence.
 */
@Repository
public interface ApplicationStatusHistoryRepository extends JpaRepository<ApplicationStatusHistory, UUID> {

    /**
     * Retrieves the chronological audit history of an application, newest first.
     *
     * @param applicationId application identifier
     * @return list of status history entries
     */
    List<ApplicationStatusHistory> findByApplicationIdOrderByChangedAtDesc(UUID applicationId);
}
