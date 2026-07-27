import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';
import { MapPin, Briefcase, DollarSign, Calendar, Mail, FileText, ChevronLeft, Loader2, AlertCircle, CheckCircle } from 'lucide-react';

/**
 * Public Job Details view page with interactive application submission modal
 * restricted to JOB_SEEKER users.
 */
const JobDetails = () => {
  const { id } = useParams();
  const { user } = useAuth();
  const [job, setJob] = useState(null);
  const [loading, setLoading] = useState(true);
  const [applyModalOpen, setApplyModalOpen] = useState(false);
  const [applySuccess, setApplySuccess] = useState(false);
  const [applyError, setApplyError] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const [alreadyApplied, setAlreadyApplied] = useState(false);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm();

  // Load job details and check if already applied
  useEffect(() => {
    const loadJobDetails = async () => {
      setLoading(true);
      try {
        const res = await api.get(`/api/jobs/${id}`);
        setJob(res.data.data);

        // If user is a seeker, check if they have already applied to this job
        if (user && user.role === 'JOB_SEEKER') {
          try {
            const appsRes = await api.get('/api/applications', {
              params: { seekerId: user.id, jobId: id },
            });
            if (appsRes.data.data.content && appsRes.data.data.content.length > 0) {
              setAlreadyApplied(true);
            }
          } catch (e) {
            console.error('Failed to verify application states', e);
          }
        }
      } catch (err) {
        console.error('Failed to load job', err);
      } finally {
        setLoading(false);
      }
    };
    loadJobDetails();
  }, [id, user]);

  const handleApplySubmit = async (data) => {
    setApplyError(null);
    setSubmitting(true);
    try {
      await api.post('/api/applications', {
        jobId: id,
        seekerId: user.id,
        resumeUrl: data.resumeUrl || null,
        coverLetter: data.coverLetter || '',
      });

      setApplySuccess(true);
      setAlreadyApplied(true);
      reset();
      setTimeout(() => {
        setApplyModalOpen(false);
        setApplySuccess(false);
      }, 3000);
    } catch (err) {
      setApplyError(err.response?.data?.message || 'Failed to submit application. Please try again.');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center py-32">
        <Loader2 className="w-10 h-10 animate-spin text-purple-400" />
      </div>
    );
  }

  if (!job) {
    return (
      <div className="bg-slate-900 border border-slate-800 p-12 text-center rounded-2xl">
        <p className="text-slate-400 font-medium">Job posting not found.</p>
        <Link to="/jobs" className="text-purple-400 font-semibold hover:underline mt-2 inline-block">
          Return to listings
        </Link>
      </div>
    );
  }

  return (
    <div className="space-y-8">
      {/* Back button link */}
      <div>
        <Link to="/jobs" className="flex items-center gap-1.5 text-slate-400 hover:text-slate-200 text-sm font-semibold transition-colors">
          <ChevronLeft className="w-4 h-4" /> Back to Listings
        </Link>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Main Details Panel */}
        <div className="lg:col-span-2 space-y-8 bg-slate-900 border border-slate-800 p-8 rounded-3xl">
          <div className="space-y-4 border-b border-slate-800 pb-6">
            <div className="flex flex-wrap gap-2">
              <span className="text-xs font-bold text-purple-400 bg-purple-500/10 px-2.5 py-1 rounded-full uppercase tracking-wider">
                {job.jobType.replace('_', ' ')}
              </span>
              <span className="text-xs font-bold text-indigo-400 bg-indigo-500/10 px-2.5 py-1 rounded-full uppercase tracking-wider">
                {job.experienceLevel}
              </span>
            </div>

            <h1 className="text-3xl font-extrabold text-slate-100">{job.title}</h1>
            <p className="text-lg font-medium text-slate-300">{job.companyName}</p>

            <div className="flex flex-wrap gap-y-2 gap-x-6 text-sm text-slate-400 pt-2">
              <span className="flex items-center gap-1.5"><MapPin className="w-4 h-4 text-slate-500" /> {job.location}</span>
              <span className="flex items-center gap-1.5"><Calendar className="w-4 h-4 text-slate-500" /> {new Date(job.createdAt).toLocaleDateString()}</span>
              {(job.salaryMin || job.salaryMax) && (
                <span className="flex items-center gap-1.5"><DollarSign className="w-4 h-4 text-slate-500" /> {job.salaryMin} - {job.salaryMax} {job.currency}</span>
              )}
            </div>
          </div>

          {/* Job Description details */}
          <div className="space-y-6">
            <div className="space-y-2">
              <h3 className="text-lg font-bold text-slate-200">About the Role</h3>
              <p className="text-slate-300 text-sm sm:text-base leading-relaxed whitespace-pre-line">{job.description}</p>
            </div>

            {job.requirements && (
              <div className="space-y-2">
                <h3 className="text-lg font-bold text-slate-200">Qualifications & Requirements</h3>
                <p className="text-slate-300 text-sm sm:text-base leading-relaxed whitespace-pre-line">{job.requirements}</p>
              </div>
            )}

            {job.responsibilities && (
              <div className="space-y-2">
                <h3 className="text-lg font-bold text-slate-200">Daily Responsibilities</h3>
                <p className="text-slate-300 text-sm sm:text-base leading-relaxed whitespace-pre-line">{job.responsibilities}</p>
              </div>
            )}
          </div>
        </div>

        {/* Sidebar Metadata */}
        <div className="space-y-6">
          {/* Apply Card box */}
          <div className="bg-slate-900 border border-slate-800 p-6 rounded-3xl space-y-6">
            <h3 className="text-lg font-bold text-slate-200 border-b border-slate-800 pb-3">Apply to Position</h3>
            
            {user ? (
              user.role === 'JOB_SEEKER' ? (
                alreadyApplied ? (
                  <div className="bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 text-sm p-4 rounded-xl text-center font-semibold">
                    You have already applied to this position.
                  </div>
                ) : (
                  <button
                    onClick={() => setApplyModalOpen(true)}
                    className="w-full bg-purple-600 hover:bg-purple-500 text-white font-medium py-3 rounded-xl transition-all shadow-md shadow-purple-900/30 text-center text-sm"
                  >
                    Apply Now
                  </button>
                )
              ) : (
                <div className="text-slate-400 text-sm text-center">
                  Only Seeker candidates can apply to job listings.
                </div>
              )
            ) : (
              <div className="space-y-4">
                <p className="text-slate-400 text-sm text-center">Sign in as a Seeker to apply for this vacancy.</p>
                <Link to="/login" className="block w-full bg-slate-800 hover:bg-slate-700 text-white text-center font-medium py-3 rounded-xl text-sm transition-all">
                  Sign In to Apply
                </Link>
              </div>
            )}
          </div>

          {/* Skill Requirements list */}
          {job.jobSkills && job.jobSkills.length > 0 && (
            <div className="bg-slate-900 border border-slate-800 p-6 rounded-3xl space-y-4">
              <h3 className="text-lg font-bold text-slate-200 border-b border-slate-800 pb-3">Skill Criteria</h3>
              <div className="flex flex-wrap gap-2">
                {job.jobSkills.map((js) => (
                  <span
                    key={js.skillId}
                    className={`text-xs px-3 py-1 rounded-full border ${
                      js.importance === 'REQUIRED'
                        ? 'bg-purple-500/10 border-purple-500/30 text-purple-400 font-semibold'
                        : 'bg-slate-950 border-slate-850 text-slate-400'
                    }`}
                  >
                    {js.skillName} {js.importance === 'REQUIRED' ? '(Required)' : ''}
                  </span>
                ))}
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Apply Modal popup */}
      {applyModalOpen && (
        <div className="fixed inset-0 bg-slate-950/80 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-slate-900 border border-slate-800 w-full max-w-lg rounded-3xl p-6 sm:p-8 space-y-6 shadow-2xl relative">
            <button
              onClick={() => setApplyModalOpen(false)}
              className="absolute top-4 right-4 text-slate-400 hover:text-white p-1 rounded-md"
            >
              ✕
            </button>

            <div>
              <h3 className="text-2xl font-extrabold text-slate-200">Submit Application</h3>
              <p className="text-sm text-slate-400 mt-1">Applying for: {job.title} at {job.companyName}</p>
            </div>

            {applySuccess ? (
              <div className="bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 p-6 rounded-2xl flex flex-col items-center justify-center text-center gap-3">
                <CheckCircle className="w-12 h-12 text-emerald-400" />
                <h4 className="font-bold text-lg">Application Submitted!</h4>
                <p className="text-xs text-slate-400">Your materials were successfully passed to the hiring manager.</p>
              </div>
            ) : (
              <form onSubmit={handleSubmit(handleApplySubmit)} className="space-y-4">
                {applyError && (
                  <div className="bg-red-500/10 border border-red-500/30 text-red-400 text-sm p-4 rounded-xl flex items-start gap-2">
                    <AlertCircle className="w-5 h-5 flex-shrink-0" />
                    <span>{applyError}</span>
                  </div>
                )}

                {/* Resume URL Input */}
                <div className="space-y-2">
                  <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider">Resume File Link URL</label>
                  <div className="flex items-center gap-2 px-3.5 py-3 bg-slate-950 rounded-xl border border-slate-800 focus-within:border-purple-500 transition-colors">
                    <FileText className="w-5 h-5 text-slate-500" />
                    <input
                      type="url"
                      placeholder="https://s3.amazonaws.com/my-resume.pdf"
                      {...register('resumeUrl', {
                        required: 'Resume URL is required',
                        pattern: {
                          value: /^(https?:\/\/)?([\da-z.-]+)\.([a-z.]{2,6})([\/\w .-]*)*\/?$/,
                          message: 'Please enter a valid URL',
                        },
                      })}
                      className="bg-transparent border-none outline-none text-slate-100 placeholder-slate-700 w-full text-sm"
                    />
                  </div>
                  {errors.resumeUrl && (
                    <p className="text-red-400 text-xs mt-1">{errors.resumeUrl.message}</p>
                  )}
                </div>

                {/* Cover Letter Input */}
                <div className="space-y-2">
                  <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider">Cover Letter (Optional)</label>
                  <textarea
                    rows="6"
                    placeholder="Introduce yourself to the hiring team..."
                    {...register('coverLetter')}
                    className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3.5 text-sm text-slate-200 placeholder-slate-700 focus:border-purple-500 outline-none"
                  ></textarea>
                </div>

                <div className="pt-4 flex gap-4">
                  <button
                    type="button"
                    onClick={() => setApplyModalOpen(false)}
                    className="flex-1 border border-slate-700 hover:border-slate-500 text-slate-300 font-medium py-3 rounded-xl text-center text-sm transition-all"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    disabled={submitting}
                    className="flex-1 bg-purple-600 hover:bg-purple-500 disabled:bg-purple-800 text-white font-medium py-3 rounded-xl transition-all shadow-md shadow-purple-900/30 flex items-center justify-center gap-2"
                  >
                    {submitting ? (
                      <>
                        <Loader2 className="w-4 h-4 animate-spin" />
                        Submitting...
                      </>
                    ) : (
                      'Submit Application'
                    )}
                  </button>
                </div>
              </form>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default JobDetails;
