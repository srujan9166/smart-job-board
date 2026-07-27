-- PostgreSQL DDL Schema for Smart Job Board Application
-- Design: Production-ready, 3NF compliant, optimized for JPA mapping

-- Drop existing tables to ensure clean execution (in reverse dependency order)
DROP TABLE IF EXISTS application_status_history CASCADE;
DROP TABLE IF EXISTS applications CASCADE;
DROP TABLE IF EXISTS seeker_skills CASCADE;
DROP TABLE IF EXISTS job_skills CASCADE;
DROP TABLE IF EXISTS jobs CASCADE;
DROP TABLE IF EXISTS skills CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS seeker_profiles CASCADE;
DROP TABLE IF EXISTS recruiter_profiles CASCADE;
DROP TABLE IF EXISTS companies CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Enable UUID extension (PostgreSQL default Gen Random UUID)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =========================================================================
-- 1. USERS TABLE
-- =========================================================================
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    role VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_user_role CHECK (role IN ('ADMIN', 'EMPLOYER', 'JOB_SEEKER'))
);

-- =========================================================================
-- 2. COMPANIES TABLE
-- =========================================================================
CREATE TABLE companies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL UNIQUE,
    slug VARCHAR(255) NOT NULL UNIQUE,
    website VARCHAR(255),
    logo_url VARCHAR(500),
    description TEXT,
    industry VARCHAR(100),
    founded_date DATE,
    headquarters VARCHAR(255),
    created_by UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_company_creator FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE RESTRICT
);

-- =========================================================================
-- 3. RECRUITER PROFILES (Supporting Table for Employers)
-- =========================================================================
CREATE TABLE recruiter_profiles (
    user_id UUID PRIMARY KEY,
    company_id UUID NOT NULL,
    job_title VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_recruiter_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_recruiter_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE RESTRICT
);

-- =========================================================================
-- 4. SEEKER PROFILES (Supporting Table for Job Seekers)
-- =========================================================================
CREATE TABLE seeker_profiles (
    user_id UUID PRIMARY KEY,
    bio TEXT,
    resume_url VARCHAR(500),
    github_url VARCHAR(255),
    linkedin_url VARCHAR(255),
    portfolio_url VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_seeker_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =========================================================================
-- 5. CATEGORIES TABLE
-- =========================================================================
CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    slug VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =========================================================================
-- 6. SKILLS TABLE
-- =========================================================================
CREATE TABLE skills (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    category_id UUID,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_skill_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

-- =========================================================================
-- 7. JOBS TABLE
-- =========================================================================
CREATE TABLE jobs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL,
    category_id UUID NOT NULL,
    posted_by UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    requirements TEXT,
    responsibilities TEXT,
    location VARCHAR(255) NOT NULL,
    job_type VARCHAR(50) NOT NULL,
    experience_level VARCHAR(50) NOT NULL,
    salary_min DECIMAL(15, 2),
    salary_max DECIMAL(15, 2),
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    expires_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_job_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE RESTRICT,
    CONSTRAINT fk_job_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT,
    CONSTRAINT fk_job_poster FOREIGN KEY (posted_by) REFERENCES users(id) ON DELETE RESTRICT,
    
    CONSTRAINT chk_job_type CHECK (job_type IN ('FULL_TIME', 'PART_TIME', 'CONTRACT', 'INTERNSHIP', 'REMOTE', 'TEMPORARY')),
    CONSTRAINT chk_job_exp CHECK (experience_level IN ('ENTRY', 'MID', 'SENIOR', 'LEAD', 'EXECUTIVE')),
    CONSTRAINT chk_job_status CHECK (status IN ('DRAFT', 'ACTIVE', 'CLOSED', 'ARCHIVED')),
    CONSTRAINT chk_salary_min CHECK (salary_min >= 0),
    CONSTRAINT chk_salary_range CHECK (salary_max >= salary_min)
);

-- =========================================================================
-- 8. JOB SKILLS (Junction Table for Jobs & Skills)
-- =========================================================================
CREATE TABLE job_skills (
    job_id UUID NOT NULL,
    skill_id UUID NOT NULL,
    importance VARCHAR(20) NOT NULL DEFAULT 'REQUIRED',
    
    PRIMARY KEY (job_id, skill_id),
    CONSTRAINT fk_jobskill_job FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    CONSTRAINT fk_jobskill_skill FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE RESTRICT,
    CONSTRAINT chk_skill_importance CHECK (importance IN ('REQUIRED', 'PREFERRED'))
);

-- =========================================================================
-- 9. SEEKER SKILLS (Junction Table for Seekers & Skills)
-- =========================================================================
CREATE TABLE seeker_skills (
    seeker_id UUID NOT NULL,
    skill_id UUID NOT NULL,
    proficiency_level VARCHAR(20),
    
    PRIMARY KEY (seeker_id, skill_id),
    CONSTRAINT fk_seekerskill_seeker FOREIGN KEY (seeker_id) REFERENCES seeker_profiles(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_seekerskill_skill FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE RESTRICT,
    CONSTRAINT chk_proficiency CHECK (proficiency_level IN ('BEGINNER', 'INTERMEDIATE', 'EXPERT'))
);

-- =========================================================================
-- 10. APPLICATIONS TABLE
-- =========================================================================
CREATE TABLE applications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_id UUID NOT NULL,
    seeker_id UUID NOT NULL,
    resume_url VARCHAR(500),
    cover_letter TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'APPLIED',
    applied_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_application_job FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE RESTRICT,
    CONSTRAINT fk_application_seeker FOREIGN KEY (seeker_id) REFERENCES seeker_profiles(user_id) ON DELETE RESTRICT,
    CONSTRAINT chk_app_status CHECK (status IN ('APPLIED', 'SCREENING', 'INTERVIEWING', 'OFFERED', 'REJECTED', 'WITHDRAWN')),
    -- Prevent duplicate applications: a candidate can apply only once per job post
    CONSTRAINT uq_job_seeker UNIQUE (job_id, seeker_id)
);

-- =========================================================================
-- 11. APPLICATION STATUS HISTORY (Audit Log / Pipeline Tracking)
-- =========================================================================
CREATE TABLE application_status_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    application_id UUID NOT NULL,
    status VARCHAR(30) NOT NULL,
    changed_by UUID NOT NULL,
    notes TEXT,
    changed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_history_application FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE,
    CONSTRAINT fk_history_user FOREIGN KEY (changed_by) REFERENCES users(id) ON DELETE RESTRICT
);

-- =========================================================================
-- INDEX DESIGN (Optimized for Query Patterns & Foreign Keys)
-- =========================================================================

-- Index foreign keys to prevent table scans on joins and deletes
CREATE INDEX idx_companies_created_by ON companies(created_by);
CREATE INDEX idx_recruiter_profiles_company ON recruiter_profiles(company_id);
CREATE INDEX idx_skills_category_id ON skills(category_id);
CREATE INDEX idx_jobs_company_id ON jobs(company_id);
CREATE INDEX idx_jobs_category_id ON jobs(category_id);
CREATE INDEX idx_jobs_posted_by ON jobs(posted_by);
CREATE INDEX idx_applications_job_id ON applications(job_id);
CREATE INDEX idx_applications_seeker_id ON applications(seeker_id);
CREATE INDEX idx_app_history_app_id ON application_status_history(application_id);

-- Performance indexes for search, filtering, and sorting
CREATE INDEX idx_jobs_status_created ON jobs(status, created_at DESC);
CREATE INDEX idx_jobs_location ON jobs(location);
CREATE INDEX idx_jobs_job_type ON jobs(job_type);
CREATE INDEX idx_jobs_experience_level ON jobs(experience_level);
CREATE INDEX idx_jobs_salary_min ON jobs(salary_min);

-- Partial index for active listings only (to keep it fast and light)
CREATE INDEX idx_active_jobs ON jobs(created_at DESC) WHERE status = 'ACTIVE';

-- Full-Text Search index for Job titles and descriptions using PostgreSQL GIN
-- This will dramatically speed up keyword matching search functionality
CREATE INDEX idx_jobs_fts ON jobs USING gin(to_tsvector('english', title || ' ' || description));
