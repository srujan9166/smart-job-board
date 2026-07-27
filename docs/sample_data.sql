-- PostgreSQL Sample Data Script for Smart Job Board Application
-- Deterministic dataset with fixed valid UUIDs (using hex characters 0-9, a-f only)

-- Clear existing data (in correct order of dependency)
TRUNCATE TABLE application_status_history CASCADE;
TRUNCATE TABLE applications CASCADE;
TRUNCATE TABLE seeker_skills CASCADE;
TRUNCATE TABLE job_skills CASCADE;
TRUNCATE TABLE jobs CASCADE;
TRUNCATE TABLE skills CASCADE;
TRUNCATE TABLE categories CASCADE;
TRUNCATE TABLE seeker_profiles CASCADE;
TRUNCATE TABLE recruiter_profiles CASCADE;
TRUNCATE TABLE companies CASCADE;
TRUNCATE TABLE users CASCADE;

-- =========================================================================
-- 1. INSERT USERS
-- =========================================================================
-- Password hashes represent bcrypt('$2a$10$...') placeholder values
INSERT INTO users (id, email, password_hash, first_name, last_name, phone_number, role, is_active) VALUES
-- Employers
('e7b0efbf-d29b-4e89-98ff-8a0b5a32de7a', 'recruiter.alice@globalco.com', '$2a$10$H2m8.6t8.Ld.ZgYQo234OedX3hFv3Vd6Z2QjCKeF2LhR2v/KVeL/i', 'Alice', 'Smith', '+1-555-0101', 'EMPLOYER', TRUE),
('e8b0efbf-d29b-4e89-98ff-8a0b5a32de7b', 'recruiter.bob@techsoft.com', '$2a$10$H2m8.6t8.Ld.ZgYQo234OedX3hFv3Vd6Z2QjCKeF2LhR2v/KVeL/i', 'Bob', 'Johnson', '+1-555-0102', 'EMPLOYER', TRUE),
-- Job Seekers
('a1111111-1111-1111-1111-111111111111', 'candidate.charlie@gmail.com', '$2a$10$H2m8.6t8.Ld.ZgYQo234OedX3hFv3Vd6Z2QjCKeF2LhR2v/KVeL/i', 'Charlie', 'Brown', '+1-555-0201', 'JOB_SEEKER', TRUE),
('b2222222-2222-2222-2222-222222222222', 'candidate.dana@gmail.com', '$2a$10$H2m8.6t8.Ld.ZgYQo234OedX3hFv3Vd6Z2QjCKeF2LhR2v/KVeL/i', 'Dana', 'White', '+1-555-0202', 'JOB_SEEKER', TRUE),
-- Admin
('c3333333-3333-3333-3333-333333333333', 'admin.system@smartjobboard.com', '$2a$10$H2m8.6t8.Ld.ZgYQo234OedX3hFv3Vd6Z2QjCKeF2LhR2v/KVeL/i', 'System', 'Administrator', '+1-555-9999', 'ADMIN', TRUE);

-- =========================================================================
-- 2. INSERT COMPANIES
-- =========================================================================
INSERT INTO companies (id, name, slug, website, logo_url, description, industry, founded_date, headquarters, created_by) VALUES
('c1111111-c111-c111-c111-c11111111111', 'Global Enterprise Solutions', 'global-enterprise-solutions', 'https://www.globalco.com', 'https://www.globalco.com/logo.png', 'Leading global provider of enterprise hardware and software solutions.', 'Information Technology', '1998-03-15', 'New York, NY', 'e7b0efbf-d29b-4e89-98ff-8a0b5a32de7a'),
('c2222222-c222-c222-c222-c22222222222', 'TechSoft Innovations', 'techsoft-innovations', 'https://www.techsoft.io', 'https://www.techsoft.io/logo.png', 'Next-generation cloud computing and artificial intelligence startups developer.', 'Cloud Services', '2015-08-20', 'San Francisco, CA', 'e8b0efbf-d29b-4e89-98ff-8a0b5a32de7b');

-- =========================================================================
-- 3. INSERT RECRUITER PROFILES
-- =========================================================================
INSERT INTO recruiter_profiles (user_id, company_id, job_title) VALUES
('e7b0efbf-d29b-4e89-98ff-8a0b5a32de7a', 'c1111111-c111-c111-c111-c11111111111', 'Lead Talent Acquisition'),
('e8b0efbf-d29b-4e89-98ff-8a0b5a32de7b', 'c2222222-c222-c222-c222-c22222222222', 'Senior Recruiter');

-- =========================================================================
-- 4. INSERT SEEKER PROFILES
-- =========================================================================
INSERT INTO seeker_profiles (user_id, bio, resume_url, github_url, linkedin_url, portfolio_url) VALUES
('a1111111-1111-1111-1111-111111111111', 'Full Stack Developer with 4 years of experience specializing in Java and React.', 'https://storage.smartjobboard.com/resumes/charlie_brown.pdf', 'https://github.com/charliebrown', 'https://linkedin.com/in/charliebrown', 'https://charliebrown.dev'),
('b2222222-2222-2222-2222-222222222222', 'Cloud Architect and Backend Specialist with expertise in Spring Boot and AWS.', 'https://storage.smartjobboard.com/resumes/dana_white.pdf', 'https://github.com/danawhite', 'https://linkedin.com/in/danawhite', NULL);

-- =========================================================================
-- 5. INSERT CATEGORIES
-- =========================================================================
INSERT INTO categories (id, name, slug, description) VALUES
('d1111111-d111-d111-d111-d11111111111', 'Software Engineering', 'software-engineering', 'Backend, frontend, full-stack, DevOps, and mobile software developers.'),
('d2222222-d222-d222-d222-d22222222222', 'Product Management', 'product-management', 'Product owners, product managers, and agile project specialists.'),
('d3333333-d333-d333-d333-d33333333333', 'Data Science & Analytics', 'data-science-analytics', 'Data scientists, data engineers, and business intelligence analysts.');

-- =========================================================================
-- 6. INSERT SKILLS
-- =========================================================================
INSERT INTO skills (id, name, category_id) VALUES
-- Software Engineering Skills
('f1111111-f111-f111-f111-f11111111111', 'Java', 'd1111111-d111-d111-d111-d11111111111'),
('f2222222-f222-f222-f222-f22222222222', 'Spring Boot', 'd1111111-d111-d111-d111-d11111111111'),
('f3333333-f333-f333-f333-f33333333333', 'PostgreSQL', 'd1111111-d111-d111-d111-d11111111111'),
('f4444444-f444-f444-f444-f44444444444', 'React', 'd1111111-d111-d111-d111-d11111111111'),
('f5555555-f555-f555-f555-f55555555555', 'Docker', 'd1111111-d111-d111-d111-d11111111111'),
-- Product Management Skills
('f6666666-f666-f666-f666-f66666666666', 'Agile Methodologies', 'd2222222-d222-d222-d222-d22222222222'),
('f7777777-f777-f777-f777-f77777777777', 'Jira', 'd2222222-d222-d222-d222-d22222222222'),
-- Data Science Skills
('f8888888-f888-f888-f888-f88888888888', 'Python', 'd3333333-d333-d333-d333-d33333333333'),
('f9999999-f999-f999-f999-f99999999999', 'TensorFlow', 'd3333333-d333-d333-d333-d33333333333');

-- =========================================================================
-- 7. INSERT JOBS
-- =========================================================================
INSERT INTO jobs (id, company_id, category_id, posted_by, title, description, requirements, responsibilities, location, job_type, experience_level, salary_min, salary_max, currency, status, expires_at) VALUES
-- Job 1: Senior Java Developer (Global Enterprise Solutions)
('0b111111-1111-1111-1111-111111111111', 
 'c1111111-c111-c111-c111-c11111111111', 
 'd1111111-d111-d111-d111-d11111111111', 
 'e7b0efbf-d29b-4e89-98ff-8a0b5a32de7a', 
 'Senior Java & Spring Boot Developer', 
 'We are looking for a Senior Java Developer to join our high-performing core backend team. You will be designing scalable API infrastructures, optimizing SQL queries, and moving systems towards containerized cloud setups.',
 'Bachelor degree in CS; 5+ years of Java experience; Proficient in Spring Boot and Relational Databases (PostgreSQL/Oracle).',
 'Design and write high-quality REST APIs; Mentor junior developers; Refactor legacy code to improve performance by 30%.',
 'New York, NY', 
 'FULL_TIME', 
 'SENIOR', 
 120000.00, 
 150000.00, 
 'USD', 
 'ACTIVE', 
 NOW() + INTERVAL '30 days'),

-- Job 2: Remote Cloud Engineer (TechSoft Innovations)
('0b222222-2222-2222-2222-222222222222', 
 'c2222222-c222-c222-c222-c22222222222', 
 'd1111111-d111-d111-d111-d11111111111', 
 'e8b0efbf-d29b-4e89-98ff-8a0b5a32de7b', 
 'Cloud DevOps Engineer', 
 'Join us to manage our AWS cloud infrastructure and build scalable CI/CD pipelines. This role is fully remote.',
 '3+ years experience with AWS; Docker and Kubernetes expertise; Strong scripting knowledge (Bash/Python).',
 'Manage Kubernetes clusters; Automate deployment pipelines; Respond to infrastructure incidents.',
 'Remote', 
 'REMOTE', 
 'MID', 
 100000.00, 
 130000.00, 
 'USD', 
 'ACTIVE', 
 NOW() + INTERVAL '45 days');

-- =========================================================================
-- 8. INSERT JOB SKILLS
-- =========================================================================
INSERT INTO job_skills (job_id, skill_id, importance) VALUES
-- Senior Java Developer skills
('0b111111-1111-1111-1111-111111111111', 'f1111111-f111-f111-f111-f11111111111', 'REQUIRED'), -- Java
('0b111111-1111-1111-1111-111111111111', 'f2222222-f222-f222-f222-f22222222222', 'REQUIRED'), -- Spring Boot
('0b111111-1111-1111-1111-111111111111', 'f3333333-f333-f333-f333-f33333333333', 'REQUIRED'), -- PostgreSQL
('0b111111-1111-1111-1111-111111111111', 'f5555555-f555-f555-f555-f55555555555', 'PREFERRED'), -- Docker
-- Cloud DevOps Engineer skills
('0b222222-2222-2222-2222-222222222222', 'f5555555-f555-f555-f555-f55555555555', 'REQUIRED'), -- Docker
('0b222222-2222-2222-2222-222222222222', 'f8888888-f888-f888-f888-f88888888888', 'PREFERRED'); -- Python

-- =========================================================================
-- 9. INSERT SEEKER SKILLS
-- =========================================================================
INSERT INTO seeker_skills (seeker_id, skill_id, proficiency_level) VALUES
-- Charlie Brown (Full Stack Dev)
('a1111111-1111-1111-1111-111111111111', 'f1111111-f111-f111-f111-f11111111111', 'EXPERT'),       -- Java
('a1111111-1111-1111-1111-111111111111', 'f2222222-f222-f222-f222-f22222222222', 'EXPERT'),       -- Spring Boot
('a1111111-1111-1111-1111-111111111111', 'f4444444-f444-f444-f444-f44444444444', 'INTERMEDIATE'), -- React
-- Dana White (Cloud/Backend Specialist)
('b2222222-2222-2222-2222-222222222222', 'f2222222-f222-f222-f222-f22222222222', 'EXPERT'),       -- Spring Boot
('b2222222-2222-2222-2222-222222222222', 'f5555555-f555-f555-f555-f55555555555', 'EXPERT'),       -- Docker
('b2222222-2222-2222-2222-222222222222', 'f3333333-f333-f333-f333-f33333333333', 'INTERMEDIATE'); -- PostgreSQL

-- =========================================================================
-- 10. INSERT APPLICATIONS
-- =========================================================================
INSERT INTO applications (id, job_id, seeker_id, resume_url, cover_letter, status) VALUES
-- Charlie Brown applies for Senior Java Developer job
('a9999999-9999-9999-9999-999999999999', 
 '0b111111-1111-1111-1111-111111111111', 
 'a1111111-1111-1111-1111-111111111111', 
 'https://storage.smartjobboard.com/resumes/charlie_brown_custom.pdf', 
 'I am highly interested in this role. I have extensive experience building scalable backends with Spring Boot and PostgreSQL and would love to join your team.', 
 'INTERVIEWING'),

-- Dana White applies for Cloud DevOps Engineer job
('b9999999-9999-9999-9999-999999999999', 
 '0b222222-2222-2222-2222-222222222222', 
 'b2222222-2222-2222-2222-222222222222', 
 NULL, -- Defaults to profile resume
 'I have a strong background in AWS cloud infrastructure and automating delivery pipelines. I would love to help optimize TechSoft''s systems.', 
 'APPLIED');

-- =========================================================================
-- 11. INSERT APPLICATION STATUS HISTORY (Audit Trail)
-- =========================================================================
-- Charlie's application history (APPLIED -> SCREENING -> INTERVIEWING)
INSERT INTO application_status_history (id, application_id, status, changed_by, notes, changed_at) VALUES
('e1111111-1111-1111-1111-111111111111', 
 'a9999999-9999-9999-9999-999999999999', 
 'APPLIED', 
 'a1111111-1111-1111-1111-111111111111', -- Seeker applied
 'Initial submission via portal.', 
 NOW() - INTERVAL '3 days'),

('e2222222-2222-2222-2222-222222222222', 
 'a9999999-9999-9999-9999-999999999999', 
 'SCREENING', 
 'e7b0efbf-d29b-4e89-98ff-8a0b5a32de7a', -- Employer updated
 'Resume fits initial filters. Moving to phone screen.', 
 NOW() - INTERVAL '2 days'),

('e3333333-3333-3333-3333-333333333333', 
 'a9999999-9999-9999-9999-999999999999', 
 'INTERVIEWING', 
 'e7b0efbf-d29b-4e89-98ff-8a0b5a32de7a', -- Employer updated
 'Phone screening passed. Arranging panel interview.', 
 NOW() - INTERVAL '1 day');

-- Dana's application history (APPLIED only)
INSERT INTO application_status_history (id, application_id, status, changed_by, notes, changed_at) VALUES
('e4444444-4444-4444-4444-444444444444', 
 'b9999999-9999-9999-9999-999999999999', 
 'APPLIED', 
 'b2222222-2222-2222-2222-222222222222', -- Seeker applied
 'Initial submission via portal.', 
 NOW() - INTERVAL '12 hours');
