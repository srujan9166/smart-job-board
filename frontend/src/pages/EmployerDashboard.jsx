import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';
import { useForm } from 'react-hook-form';
import { Plus, Briefcase, Building, CheckCircle, AlertCircle, Edit, Trash2, Loader2 } from 'lucide-react';

/**
 * Employer portal dashboard allowing creation/management of company profiles,
 * job publishing, and applicant review (updating status pipelines).
 */
const EmployerDashboard = () => {
  const { user } = useAuth();
  const [company, setCompany] = useState(null);
  const [jobs, setJobs] = useState([]);
  const [applications, setApplications] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('jobs');

  // Multi-step sub-states
  const [companyMsg, setCompanyMsg] = useState(null);
  const [companyErr, setCompanyErr] = useState(null);
  const [submittingCompany, setSubmittingCompany] = useState(false);

  const [jobMsg, setJobMsg] = useState(null);
  const [jobErr, setJobErr] = useState(null);
  const [submittingJob, setSubmittingJob] = useState(false);
  const [editingJob, setEditingJob] = useState(null);

  const { register: regCompany, handleSubmit: handleCompanySubmit, setValue: setCompanyValue } = useForm();
  const { register: regJob, handleSubmit: handleJobSubmit, setValue: setJobValue, reset: resetJobForm } = useForm();

  // Load everything
  const loadData = async () => {
    try {
      // 1. Resolve the company from the authenticated employer, never from a client-owned ID.
      let userComp = null;
      try {
        const compRes = await api.get('/api/company/me');
        userComp = compRes.data.data;
      } catch (error) {
        if (error.response?.status !== 404) throw error;
      }

      if (userComp) {
        setCompany(userComp);
        setCompanyValue('name', userComp.name);
        setCompanyValue('website', userComp.website || '');
        setCompanyValue('logoUrl', userComp.logoUrl || '');
        setCompanyValue('description', userComp.description || '');
        setCompanyValue('industry', userComp.industry || '');
        setCompanyValue('foundedDate', userComp.foundedDate || '');
        setCompanyValue('headquarters', userComp.headquarters || '');

        // 2. Fetch jobs belonging to this company
        const jobsRes = await api.get(`/api/jobs/company/${userComp.id}`);
        const jobsList = jobsRes.data.data.content || [];
        setJobs(jobsList);

        // 3. Fetch applications submitted to this company's jobs
        const appsList = [];
        for (const j of jobsList) {
          try {
            const appsRes = await api.get('/api/applications', { params: { jobId: j.id } });
            if (appsRes.data.data.content) {
              appsList.push(...appsRes.data.data.content);
            }
          } catch (err) {
            console.error('Failed to load apps for job ' + j.id, err);
          }
        }
        setApplications(appsList);
      } else {
        setActiveTab('company');
      }

      // 4. Load categories for job creation forms
      const catRes = await api.get('/api/categories');
      setCategories(catRes.data.data);

    } catch (e) {
      console.error('Failed to load employer dashboard data', e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, [user, setCompanyValue]);

  // Handle Company profile save (create or update)
  const onSaveCompany = async (data) => {
    setCompanyMsg(null);
    setCompanyErr(null);
    setSubmittingCompany(true);
    try {
      const payload = {
        name: data.name,
        website: data.website || null,
        logoUrl: data.logoUrl || null,
        description: data.description || null,
        industry: data.industry || null,
        foundedDate: data.foundedDate || null,
        headquarters: data.headquarters || null,
      };

      let res;
      if (company) {
        res = await api.put('/api/company/me', payload);
        setCompanyMsg('Company updated successfully!');
      } else {
        res = await api.post('/api/company', payload);
        setCompanyMsg('Company profile created! Loading jobs dashboard...');
      }
      setCompany(res.data.data);
      loadData();
    } catch (e) {
      setCompanyErr(e.response?.data?.message || 'Failed to save company details.');
    } finally {
      setSubmittingCompany(false);
    }
  };

  // Handle Job Post or Edit submit
  const onSaveJob = async (data) => {
    setJobMsg(null);
    setJobErr(null);
    setSubmittingJob(true);
    try {
      const payload = {
        categoryId: data.categoryId,
        title: data.title,
        description: data.description,
        requirements: data.requirements || null,
        responsibilities: data.responsibilities || null,
        location: data.location,
        jobType: data.jobType,
        experienceLevel: data.experienceLevel,
        salaryMin: data.salaryMin ? parseFloat(data.salaryMin) : null,
        salaryMax: data.salaryMax ? parseFloat(data.salaryMax) : null,
        currency: data.currency || 'USD',
        status: data.status || 'ACTIVE',
        expiresAt: data.expiresAt ? new Date(data.expiresAt).toISOString() : null,
      };

      if (editingJob) {
        await api.put(`/api/jobs/${editingJob.id}`, payload);
        setJobMsg('Job listing updated successfully!');
      } else {
        await api.post('/api/jobs', payload);
        setJobMsg('Job listing published successfully!');
      }

      resetJobForm();
      setEditingJob(null);
      setActiveTab('jobs');
      loadData();
    } catch (e) {
      setJobErr(e.response?.data?.message || 'Failed to publish job opening.');
    } finally {
      setSubmittingJob(false);
    }
  };

  const handleEditJobClick = (jobItem) => {
    setEditingJob(jobItem);
    setActiveTab('post-job');
    // Pre-populate fields
    setTimeout(() => {
      setJobValue('title', jobItem.title);
      setJobValue('categoryId', jobItem.categoryId);
      setJobValue('description', jobItem.description);
      setJobValue('requirements', jobItem.requirements || '');
      setJobValue('responsibilities', jobItem.responsibilities || '');
      setJobValue('location', jobItem.location);
      setJobValue('jobType', jobItem.jobType);
      setJobValue('experienceLevel', jobItem.experienceLevel);
      setJobValue('salaryMin', jobItem.salaryMin || '');
      setJobValue('salaryMax', jobItem.salaryMax || '');
      setJobValue('currency', jobItem.currency || 'USD');
      setJobValue('status', jobItem.status);
      if (jobItem.expiresAt) {
        setJobValue('expiresAt', jobItem.expiresAt.substring(0, 10));
      }
    }, 100);
  };

  const handleDeleteJob = async (jobId) => {
    if (!window.confirm('Are you sure you want to delete this job posting? This cannot be undone.')) {
      return;
    }
    try {
      await api.delete(`/api/jobs/${jobId}`);
      loadData();
    } catch (e) {
      alert(e.response?.data?.message || 'Failed to delete job posting.');
    }
  };

  // Handle applicant status update
  const handleStatusChange = async (appId, newStatus) => {
    try {
      await api.put(`/api/applications/${appId}/status`, null, {
        params: { status: newStatus, actorId: user.id },
      });
      loadData();
    } catch (e) {
      alert(e.response?.data?.message || 'Invalid status transition requested.');
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center py-32">
        <Loader2 className="w-10 h-10 animate-spin text-purple-400" />
      </div>
    );
  }

  return (
    <div className="space-y-8">
      {/* Portal Header */}
      <div>
        <h2 className="text-3xl font-extrabold text-slate-200">Employer Recruiting Console</h2>
        {company ? (
          <p className="text-slate-400 text-sm mt-1">Managing recruitment for {company.name}</p>
        ) : (
          <p className="text-red-400 text-sm mt-1">You must configure your company profile before posting jobs</p>
        )}
      </div>

      {/* Navigation tabs */}
      <div className="flex gap-4 overflow-x-auto border-b border-slate-800" role="tablist" aria-label="Employer dashboard sections">
        <button
          disabled={!company}
          onClick={() => { setActiveTab('jobs'); setEditingJob(null); resetJobForm(); }}
          className={`pb-3 text-sm font-bold border-b-2 transition-all ${activeTab === 'jobs' ? 'border-purple-500 text-purple-400' : 'border-transparent text-slate-400 hover:text-slate-200'
            } disabled:opacity-50`}
        >
          Active Vacancies ({jobs.length})
        </button>
        <button
          disabled={!company}
          onClick={() => { setActiveTab('applications'); setEditingJob(null); }}
          className={`pb-3 text-sm font-bold border-b-2 transition-all ${activeTab === 'applications' ? 'border-purple-500 text-purple-400' : 'border-transparent text-slate-400 hover:text-slate-200'
            } disabled:opacity-50`}
        >
          Received Applications ({applications.length})
        </button>
        <button
          disabled={!company}
          onClick={() => { setActiveTab('post-job'); setEditingJob(null); resetJobForm(); }}
          className={`pb-3 text-sm font-bold border-b-2 transition-all ${activeTab === 'post-job' ? 'border-purple-500 text-purple-400' : 'border-transparent text-slate-400 hover:text-slate-200'
            } disabled:opacity-50`}
        >
          {editingJob ? 'Edit Vacancy' : 'Post a Job'}
        </button>
        <button
          onClick={() => { setActiveTab('company'); setEditingJob(null); }}
          className={`pb-3 text-sm font-bold border-b-2 transition-all ${activeTab === 'company' ? 'border-purple-500 text-purple-400' : 'border-transparent text-slate-400 hover:text-slate-200'
            }`}
        >
          Company Settings
        </button>
      </div>

      {!company && activeTab !== 'company' && (
        <div className="rounded-2xl border border-amber-500/30 bg-amber-500/10 p-6 text-center">
          <h3 className="font-bold text-amber-100">Complete your company profile</h3>
          <p className="mt-1 text-sm text-amber-200">You must configure your company profile before posting jobs.</p>
          <button type="button" onClick={() => setActiveTab('company')} className="mt-4 rounded-lg bg-purple-600 px-4 py-2 text-sm font-medium text-white hover:bg-purple-500">Create company profile</button>
        </div>
      )}

      {/* Tab Panels */}
      {activeTab === 'jobs' && company && (
        <div className="space-y-6">
          <div className="flex justify-end">
            <button
              onClick={() => setActiveTab('post-job')}
              className="bg-purple-600 hover:bg-purple-500 text-white font-medium text-xs px-4 py-2.5 rounded-xl transition-colors flex items-center gap-1"
            >
              <Plus className="w-4 h-4" /> Publish Vacancy
            </button>
          </div>

          {jobs.length === 0 ? (
            <div className="bg-slate-900 border border-slate-800 p-12 text-center rounded-2xl">
              <p className="text-slate-400 font-medium">You haven't posted any jobs yet.</p>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {jobs.map((job) => (
                <div key={job.id} className="bg-slate-900 border border-slate-800 p-6 rounded-2xl flex flex-col justify-between">
                  <div className="space-y-2">
                    <div className="flex justify-between items-center">
                      <span className="text-xs font-bold text-slate-400">{job.jobType}</span>
                      <span className={`text-[10px] px-2 py-0.5 rounded font-bold ${job.status === 'ACTIVE' ? 'bg-emerald-500/10 text-emerald-400' : 'bg-yellow-500/10 text-yellow-400'}`}>
                        {job.status}
                      </span>
                    </div>
                    <h4 className="text-lg font-bold text-slate-200">{job.title}</h4>
                    <p className="text-xs text-slate-500">Posted on: {new Date(job.createdAt).toLocaleDateString()}</p>
                  </div>

                  <div className="flex items-center justify-end gap-3 border-t border-slate-800 pt-4 mt-6">
                    <button
                      onClick={() => handleEditJobClick(job)}
                      className="text-indigo-400 hover:text-indigo-300 text-xs font-semibold flex items-center gap-1"
                    >
                      <Edit className="w-3.5 h-3.5" /> Edit
                    </button>
                    <button
                      onClick={() => handleDeleteJob(job.id)}
                      className="text-red-400 hover:text-red-300 text-xs font-semibold flex items-center gap-1"
                    >
                      <Trash2 className="w-3.5 h-3.5" /> Delete
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {activeTab === 'applications' && company && (
        applications.length === 0 ? (
          <div className="bg-slate-900 border border-slate-800 p-12 text-center rounded-2xl">
            <p className="text-slate-400 font-medium">No candidates have applied to your job postings yet.</p>
          </div>
        ) : (
            <div className="overflow-x-auto border border-slate-800 rounded-2xl bg-slate-900">
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-slate-800">
                <thead className="bg-slate-950">
                  <tr>
                    <th className="px-6 py-4 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Candidate / Email</th>
                    <th className="px-6 py-4 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Job Listing</th>
                    <th className="px-6 py-4 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Resume Link</th>
                    <th className="px-6 py-4 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Pipeline Stage</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-800 bg-slate-900">
                  {applications.map((app) => (
                    <tr key={app.id}>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="font-bold text-slate-200">{app.seekerFirstName} {app.seekerLastName}</div>
                        <div className="text-slate-500 text-xs mt-0.5">{app.seekerId}</div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-300">
                        {app.jobTitle}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {app.resumeUrl ? (
                          <a href={app.resumeUrl} target="_blank" rel="noopener noreferrer" className="text-purple-400 hover:text-purple-300 text-xs font-semibold underline">
                            View Resume PDF
                          </a>
                        ) : (
                          <span className="text-slate-600 text-xs">No Link</span>
                        )}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <select
                          value={app.status}
                          onChange={(e) => handleStatusChange(app.id, e.target.value)}
                          className="bg-slate-950 border border-slate-800 rounded-lg p-1.5 text-xs text-slate-300 focus:border-purple-500 outline-none"
                        >
                          <option value="APPLIED">Applied</option>
                          <option value="SCREENING">Screening</option>
                          <option value="INTERVIEWING">Interviewing</option>
                          <option value="OFFERED">Offered</option>
                          <option value="REJECTED">Rejected</option>
                          <option value="WITHDRAWN" disabled>Withdrawn</option>
                        </select>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )
      )}

      {activeTab === 'post-job' && company && (
        <div className="max-w-3xl bg-slate-900 border border-slate-800 p-8 rounded-3xl space-y-6">
          <h3 className="text-xl font-bold text-slate-200 border-b border-slate-800 pb-3">
            {editingJob ? 'Edit Vacancy Listing' : 'Publish New Vacancy'}
          </h3>

          {jobMsg && (
            <div className="bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 text-sm p-4 rounded-xl flex items-start gap-2.5">
              <CheckCircle className="w-5 h-5 flex-shrink-0" />
              <span>{jobMsg}</span>
            </div>
          )}

          {jobErr && (
            <div className="bg-red-500/10 border border-red-500/30 text-red-400 text-sm p-4 rounded-xl flex items-start gap-2.5">
              <AlertCircle className="w-5 h-5 flex-shrink-0" />
              <span>{jobErr}</span>
            </div>
          )}

          <form onSubmit={handleJobSubmit(onSaveJob)} className="space-y-4">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Job Title</label>
                <input
                  type="text"
                  placeholder="e.g. Senior Backend Engineer"
                  {...regJob('title', { required: 'Job title is required' })}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Category Classification</label>
                <select
                  {...regJob('categoryId', { required: 'Category is required' })}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
                >
                  <option value="">Select Category</option>
                  {categories.map((c) => (
                    <option key={c.id} value={c.id}>{c.name}</option>
                  ))}
                </select>
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Location</label>
                <input
                  type="text"
                  placeholder="e.g. Remote, NY"
                  {...regJob('location', { required: 'Location is required' })}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Employment Type</label>
                <select
                  {...regJob('jobType', { required: 'Type is required' })}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
                >
                  <option value="FULL_TIME">Full Time</option>
                  <option value="PART_TIME">Part Time</option>
                  <option value="CONTRACT">Contract</option>
                  <option value="INTERNSHIP">Internship</option>
                  <option value="REMOTE">Remote</option>
                </select>
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Seniority Level</label>
                <select
                  {...regJob('experienceLevel', { required: 'Experience level is required' })}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
                >
                  <option value="ENTRY">Entry Level</option>
                  <option value="MID">Mid Level</option>
                  <option value="SENIOR">Senior Level</option>
                  <option value="LEAD">Lead Level</option>
                  <option value="EXECUTIVE">Executive Level</option>
                </select>
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-4 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Min Salary</label>
                <input
                  type="number"
                  placeholder="e.g. 80000"
                  {...regJob('salaryMin')}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Max Salary</label>
                <input
                  type="number"
                  placeholder="e.g. 120000"
                  {...regJob('salaryMax')}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Currency</label>
                <input
                  type="text"
                  placeholder="USD"
                  {...regJob('currency')}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Expires At</label>
                <input
                  type="date"
                  {...regJob('expiresAt')}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
                />
              </div>
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Job Description</label>
              <textarea
                rows="5"
                placeholder="Job description details..."
                {...regJob('description', { required: 'Description is required' })}
                className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3.5 text-sm text-slate-200 focus:border-purple-500 outline-none"
              ></textarea>
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Requirements (One per line)</label>
              <textarea
                rows="3"
                placeholder="Qualifications..."
                {...regJob('requirements')}
                className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3.5 text-sm text-slate-200 focus:border-purple-500 outline-none"
              ></textarea>
            </div>

            <button
              type="submit"
              disabled={submittingJob}
              className="bg-purple-600 hover:bg-purple-500 disabled:bg-purple-800 text-white font-medium px-6 py-3 rounded-xl transition-all shadow-md shadow-purple-900/30 flex items-center justify-center gap-2"
            >
              {submittingJob ? <Loader2 className="w-4 h-4 animate-spin" /> : <Briefcase className="w-4 h-4" />}
              {editingJob ? 'Update Job' : 'Publish Job'}
            </button>
          </form>
        </div>
      )}

      {activeTab === 'company' && (
        <div className="max-w-2xl bg-slate-900 border border-slate-800 p-8 rounded-3xl space-y-6">
          <h3 className="text-xl font-bold text-slate-200 border-b border-slate-800 pb-3">Company Metadata Registry</h3>

          {companyMsg && (
            <div className="bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 text-sm p-4 rounded-xl flex items-start gap-2.5">
              <CheckCircle className="w-5 h-5 flex-shrink-0" />
              <span>{companyMsg}</span>
            </div>
          )}

          {companyErr && (
            <div className="bg-red-500/10 border border-red-500/30 text-red-400 text-sm p-4 rounded-xl flex items-start gap-2.5">
              <AlertCircle className="w-5 h-5 flex-shrink-0" />
              <span>{companyErr}</span>
            </div>
          )}

          <form onSubmit={handleCompanySubmit(onSaveCompany)} className="space-y-4">
            <div>
              <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Company Legal Name</label>
              <input
                type="text"
                placeholder="GlobalCo Inc."
                {...regCompany('name', { required: 'Company name is required' })}
                className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
              />
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Website URL</label>
                <input
                  type="url"
                  placeholder="https://www.globalco.com"
                  {...regCompany('website')}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Logo CDN URL</label>
                <input
                  type="url"
                  placeholder="https://cdn.globalco.com/logo.png"
                  {...regCompany('logoUrl')}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
                />
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Industry Sector</label>
                <input
                  type="text"
                  placeholder="Information Technology"
                  {...regCompany('industry')}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Headquarters City</label>
                <input
                  type="text"
                  placeholder="New York, USA"
                  {...regCompany('headquarters')}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Founded Date</label>
                <input
                  type="date"
                  {...regCompany('foundedDate')}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
                />
              </div>
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Company Overview Description</label>
              <textarea
                rows="4"
                placeholder="Describe your company history and product offerings..."
                {...regCompany('description')}
                className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3.5 text-sm text-slate-200 focus:border-purple-500 outline-none"
              ></textarea>
            </div>

            <button
              type="submit"
              disabled={submittingCompany}
              className="bg-purple-600 hover:bg-purple-500 disabled:bg-purple-800 text-white font-medium px-6 py-3 rounded-xl transition-all shadow-md shadow-purple-900/30 flex items-center justify-center gap-2"
            >
              {submittingCompany ? <Loader2 className="w-4 h-4 animate-spin" /> : <Building className="w-4 h-4" />}
              Save Company Settings
            </button>
          </form>
        </div>
      )}
    </div>
  );
};

export default EmployerDashboard;
