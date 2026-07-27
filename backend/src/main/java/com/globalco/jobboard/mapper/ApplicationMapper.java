package com.globalco.jobboard.mapper;

import com.globalco.jobboard.dto.request.ApplicationRequestDTO;
import com.globalco.jobboard.dto.response.ApplicationResponseDTO;
import com.globalco.jobboard.dto.response.ApplicationStatusHistoryResponseDTO;
import com.globalco.jobboard.entity.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper component converting between {@link Application} entity and DTO types.
 */
@Component
public class ApplicationMapper {

    /**
     * Converts an Application entity to ApplicationResponseDTO, flattening
     * associations and logs.
     *
     * @param application application entity
     * @return application response DTO
     */
    public ApplicationResponseDTO toResponseDTO(Application application) {
        if (application == null) {
            return null;
        }

        List<ApplicationStatusHistoryResponseDTO> historyDTOs = new ArrayList<>();
        if (application.getStatusHistory() != null) {
            for (ApplicationStatusHistory ash : application.getStatusHistory()) {
                String actorName = null;
                if (ash.getChangedBy() != null) {
                    actorName = ash.getChangedBy().getFirstName() + " " + ash.getChangedBy().getLastName();
                }
                historyDTOs.add(ApplicationStatusHistoryResponseDTO.builder()
                        .id(ash.getId())
                        .applicationId(application.getId())
                        .status(ash.getStatus())
                        .changedById(ash.getChangedBy() != null ? ash.getChangedBy().getId() : null)
                        .changedByName(actorName)
                        .notes(ash.getNotes())
                        .changedAt(ash.getChangedAt())
                        .build());
            }
        }

        UUID jobId = null;
        String jobTitle = null;
        String companyName = null;
        if (application.getJob() != null) {
            jobId = application.getJob().getId();
            jobTitle = application.getJob().getTitle();
            if (application.getJob().getCompany() != null) {
                companyName = application.getJob().getCompany().getName();
            }
        }

        UUID seekerId = null;
        String seekerFirstName = null;
        String seekerLastName = null;
        if (application.getSeeker() != null) {
            seekerId = application.getSeeker().getUserId();
            if (application.getSeeker().getUser() != null) {
                seekerFirstName = application.getSeeker().getUser().getFirstName();
                seekerLastName = application.getSeeker().getUser().getLastName();
            }
        }

        return ApplicationResponseDTO.builder()
                .id(application.getId())
                .jobId(jobId)
                .jobTitle(jobTitle)
                .companyName(companyName)
                .seekerId(seekerId)
                .seekerFirstName(seekerFirstName)
                .seekerLastName(seekerLastName)
                .resumeUrl(application.getResumeUrl())
                .coverLetter(application.getCoverLetter())
                .status(application.getStatus())
                .appliedAt(application.getAppliedAt())
                .updatedAt(application.getUpdatedAt())
                .statusHistory(historyDTOs)
                .build();
    }

    /**
     * Converts an ApplicationRequestDTO to a new Application entity.
     *
     * @param dto application request DTO
     * @param job job association
     * @param seeker seeker profile association
     * @return application entity
     */
    public Application toEntity(ApplicationRequestDTO dto, Job job, SeekerProfile seeker) {
        if (dto == null) {
            return null;
        }
        return Application.builder()
                .job(job)
                .seeker(seeker)
                .resumeUrl(dto.getResumeUrl())
                .coverLetter(dto.getCoverLetter())
                .build();
    }

    /**
     * Updates an existing Application entity with properties from ApplicationRequestDTO.
     *
     * @param dto application request DTO containing updates
     * @param application target application entity to modify
     */
    public void updateEntity(ApplicationRequestDTO dto, Application application) {
        if (dto == null || application == null) {
            return;
        }
        application.setResumeUrl(dto.getResumeUrl());
        application.setCoverLetter(dto.getCoverLetter());
    }
}
