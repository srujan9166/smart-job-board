package com.globalco.jobboard.service.impl;

import com.globalco.jobboard.dto.request.JobRequestDTO;
import com.globalco.jobboard.dto.response.JobResponseDTO;
import com.globalco.jobboard.entity.*;
import com.globalco.jobboard.exception.ResourceNotFoundException;
import com.globalco.jobboard.mapper.JobMapper;
import com.globalco.jobboard.repository.CategoryRepository;
import com.globalco.jobboard.repository.CompanyRepository;
import com.globalco.jobboard.repository.JobRepository;
import com.globalco.jobboard.repository.UserRepository;
import com.globalco.jobboard.service.JobService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of {@link JobService} managing job openings postings.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobServiceImpl implements JobService {

    private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final JobMapper jobMapper;

    @Override
    @Transactional
    public JobResponseDTO createJob(JobRequestDTO dto) {
        log.info("Attempting to post job: {}", dto.getTitle());

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + dto.getCompanyId()));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + dto.getCategoryId()));

        User poster = userRepository.findById(dto.getPostedById())
                .orElseThrow(() -> new ResourceNotFoundException("Poster user not found with ID: " + dto.getPostedById()));

        // Double check business constraint validation
        if (dto.getSalaryMin() != null && dto.getSalaryMax() != null && dto.getSalaryMax().compareTo(dto.getSalaryMin()) < 0) {
            throw new com.globalco.jobboard.exception.InvalidOperationException(
                    "Maximum salary cannot be less than minimum salary.");
        }

        Job job = jobMapper.toEntity(dto, company, category, poster);
        Job savedJob = jobRepository.save(job);

        log.info("Successfully posted job with ID: {}", savedJob.getId());
        return jobMapper.toResponseDTO(savedJob);
    }

    @Override
    public JobResponseDTO getJobById(UUID id) {
        log.debug("Retrieving job by ID: {}", id);
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + id));
        return jobMapper.toResponseDTO(job);
    }

    @Override
    public Page<JobResponseDTO> getActiveJobs(Pageable pageable) {
        log.debug("Retrieving all active jobs");
        return jobRepository.findByStatus(JobStatus.ACTIVE, pageable)
                .map(jobMapper::toResponseDTO);
    }

    @Override
    public Page<JobResponseDTO> searchJobsByKeyword(String keyword, Pageable pageable) {
        log.debug("Searching active jobs by keyword: '{}'", keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            return getActiveJobs(pageable);
        }
        return jobRepository.searchActiveJobsByKeywordNative(keyword, pageable)
                .map(jobMapper::toResponseDTO);
    }

    @Override
    public Page<JobResponseDTO> getJobsByCompany(UUID companyId, Pageable pageable) {
        log.debug("Retrieving active jobs for company ID: {}", companyId);
        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException("Company not found with ID: " + companyId);
        }
        return jobRepository.findByCompanyIdAndStatus(companyId, JobStatus.ACTIVE, pageable)
                .map(jobMapper::toResponseDTO);
    }

    @Override
    public Page<JobResponseDTO> getJobsByCategory(UUID categoryId, Pageable pageable) {
        log.debug("Retrieving active jobs for category ID: {}", categoryId);
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with ID: " + categoryId);
        }
        return jobRepository.findByCategoryIdAndStatus(categoryId, JobStatus.ACTIVE, pageable)
                .map(jobMapper::toResponseDTO);
    }

    @Override
    public Page<JobResponseDTO> getJobsByLocation(String location, Pageable pageable) {
        log.debug("Retrieving active jobs matching location: '{}'", location);
        return jobRepository.findByLocationContainingIgnoreCaseAndStatus(location, JobStatus.ACTIVE, pageable)
                .map(jobMapper::toResponseDTO);
    }

    @Override
    public Page<JobResponseDTO> getJobsByJobType(JobType jobType, Pageable pageable) {
        log.debug("Retrieving active jobs for job type: {}", jobType);
        return jobRepository.findByJobTypeAndStatus(jobType, JobStatus.ACTIVE, pageable)
                .map(jobMapper::toResponseDTO);
    }

    @Override
    public Page<JobResponseDTO> getJobsByExperienceLevel(ExperienceLevel experienceLevel, Pageable pageable) {
        log.debug("Retrieving active jobs for experience level: {}", experienceLevel);
        return jobRepository.findByExperienceLevelAndStatus(experienceLevel, JobStatus.ACTIVE, pageable)
                .map(jobMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public JobResponseDTO updateJob(UUID id, JobRequestDTO dto) {
        log.info("Updating job with ID: {}", id);

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + id));

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + dto.getCompanyId()));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + dto.getCategoryId()));

        // Double check business constraint validation
        if (dto.getSalaryMin() != null && dto.getSalaryMax() != null && dto.getSalaryMax().compareTo(dto.getSalaryMin()) < 0) {
            throw new com.globalco.jobboard.exception.InvalidOperationException(
                    "Maximum salary cannot be less than minimum salary.");
        }

        jobMapper.updateEntity(dto, job, company, category);
        Job updatedJob = jobRepository.save(job);

        log.info("Successfully updated job with ID: {}", updatedJob.getId());
        return jobMapper.toResponseDTO(updatedJob);
    }

    @Override
    @Transactional
    public void deleteJob(UUID id) {
        log.info("Deleting job with ID: {}", id);
        if (!jobRepository.existsById(id)) {
            throw new ResourceNotFoundException("Job not found with ID: " + id);
        }
        jobRepository.deleteById(id);
        log.info("Successfully deleted job with ID: {}", id);
    }
}
