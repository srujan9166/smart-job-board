import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';
import { useForm } from 'react-hook-form';
import { Layers, FileText, CheckCircle2, XCircle, AlertCircle, Trash2, Edit2, Loader2, Briefcase } from 'lucide-react';
import EmptyState from '../components/EmptyState';
import { useToast } from '../context/ToastContext';

/**
 * Seeker dashboard tab layout combining application audit lists,
 * status changes, withdrawals, and profile info updates.
 */
const SeekerDashboard = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const { showToast } = useToast();
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('applications');
  const [submittingProfile, setSubmittingProfile] = useState(false);

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm();

  // Load applications submitted by seeker
  const loadApplications = async () => {
    try {
      const res = await api.get(`/api/applications/seeker/${user.id}`);
      setApplications(res.data.data.content || []);
    } catch (e) {
      console.error('Failed to load applications', e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadApplications();
    // Pre-fill profile form values
    setValue('firstName', user.firstName);
    setValue('lastName', user.lastName);
    setValue('phoneNumber', user.phoneNumber || '');
  }, [user, setValue]);

  const handleWithdraw = async (appId) => {
    if (!window.confirm('Are you sure you want to withdraw this application? This will remove it from the recruitment pipeline.')) {
      return;
    }
    try {
      await api.delete(`/api/applications/${appId}`);
      setApplications(applications.filter((app) => app.id !== appId));
      showToast('Application withdrawn successfully.', 'success');
    } catch (e) {
      showToast(e.response?.data?.message || 'Failed to withdraw application.', 'error');
    }
  };

  const onProfileSubmit = async (data) => {
    setSubmittingProfile(true);
    try {
      // Fetch current user payload first
      const userRes = await api.get(`/api/users/${user.id}`);
      const currentUser = userRes.data.data;

      // Update basic fields
      const updatePayload = {
        ...currentUser,
        firstName: data.firstName,
        lastName: data.lastName,
        phoneNumber: data.phoneNumber || null,
      };

      const res = await api.put(`/api/users/${user.id}`, updatePayload);
      localStorage.setItem('user', JSON.stringify(res.data.data));
      showToast('Profile updated successfully!', 'success');
      setTimeout(() => {
        window.location.reload();
      }, 1500);
    } catch (e) {
      showToast(e.response?.data?.message || 'Failed to update profile details.', 'error');
    } finally {
      setSubmittingProfile(false);
    }
  };

  // Status badge style helper
  const getStatusBadge = (status) => {
    switch (status) {
      case 'APPLIED':
        return <span className="bg-blue-500/10 text-blue-400 border border-blue-500/20 text-xs px-2.5 py-1 rounded-full font-semibold">APPLIED</span>;
      case 'SCREENING':
        return <span className="bg-yellow-500/10 text-yellow-400 border border-yellow-500/20 text-xs px-2.5 py-1 rounded-full font-semibold">SCREENING</span>;
      case 'INTERVIEWING':
        return <span className="bg-purple-500/10 text-purple-400 border border-purple-500/20 text-xs px-2.5 py-1 rounded-full font-semibold">INTERVIEWING</span>;
      case 'OFFERED':
        return <span className="bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 text-xs px-2.5 py-1 rounded-full font-semibold">OFFERED</span>;
      case 'REJECTED':
        return <span className="bg-red-500/10 text-red-400 border border-red-500/20 text-xs px-2.5 py-1 rounded-full font-semibold">REJECTED</span>;
      default:
        return <span className="bg-slate-800 text-slate-400 border border-slate-700 text-xs px-2.5 py-1 rounded-full font-semibold">{status}</span>;
    }
  };

  return (
    <div className="space-y-8">
      {/* Page Header */}
      <div>
        <h2 className="text-3xl font-extrabold text-slate-200">Candidate Dashboard</h2>
        <p className="text-slate-400 text-sm mt-1">Manage your active job applications and profile metadata</p>
      </div>

      {/* Tab Navigation */}
      <div className="flex gap-4 border-b border-slate-850">
        <button
          onClick={() => setActiveTab('applications')}
          className={`pb-3 text-sm font-bold border-b-2 transition-all ${
            activeTab === 'applications'
              ? 'border-purple-500 text-purple-400'
              : 'border-transparent text-slate-400 hover:text-slate-200'
          }`}
        >
          My Applications ({applications.length})
        </button>
        <button
          onClick={() => setActiveTab('profile')}
          className={`pb-3 text-sm font-bold border-b-2 transition-all ${
            activeTab === 'profile'
              ? 'border-purple-500 text-purple-400'
              : 'border-transparent text-slate-400 hover:text-slate-200'
          }`}
        >
          My Profile
        </button>
      </div>

      {/* Tab Contents */}
      {activeTab === 'applications' ? (
        loading ? (
          <div className="flex justify-center items-center py-16">
            <Loader2 className="w-8 h-8 animate-spin text-purple-400" />
          </div>
        ) : applications.length === 0 ? (
          <EmptyState
            title="No Active Applications"
            description="You haven't submitted any job applications yet. Discover matching roles and apply now."
            actionText="Browse Available Jobs"
            onActionClick={() => navigate('/jobs')}
            icon={Briefcase}
          />
        ) : (
          <div className="overflow-hidden border border-slate-800 rounded-2xl bg-slate-900">
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-slate-800">
                <thead className="bg-slate-950">
                  <tr>
                    <th className="px-6 py-4 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Position / Company</th>
                    <th className="px-6 py-4 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Applied Date</th>
                    <th className="px-6 py-4 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Current Pipeline Status</th>
                    <th className="px-6 py-4 text-right text-xs font-semibold text-slate-400 uppercase tracking-wider">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-800 bg-slate-900">
                  {applications.map((app) => (
                    <tr key={app.id}>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="font-bold text-slate-200">{app.jobTitle}</div>
                        <div className="text-slate-400 text-sm mt-0.5">{app.companyName}</div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-400">
                        {new Date(app.appliedAt).toLocaleDateString()}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {getStatusBadge(app.status)}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-right text-sm">
                        {app.status !== 'REJECTED' && app.status !== 'WITHDRAWN' ? (
                          <button
                            onClick={() => handleWithdraw(app.id)}
                            className="text-red-400 hover:text-red-300 font-semibold inline-flex items-center gap-1 hover:underline transition-colors"
                          >
                            <Trash2 className="w-4 h-4" /> Withdraw
                          </button>
                        ) : (
                          <span className="text-slate-600 font-medium">Inactive</span>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )
      ) : (
        /* Profile Tab view */
        <div className="max-w-xl bg-slate-900 border border-slate-800 p-8 rounded-3xl space-y-6">
          <h3 className="text-xl font-bold text-slate-200 border-b border-slate-800 pb-3">Update Profile Info</h3>

          <form onSubmit={handleSubmit(onProfileSubmit)} className="space-y-4">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">First Name</label>
                <input
                  type="text"
                  {...register('firstName', { required: 'First name is required' })}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
                />
                {errors.firstName && <p className="text-red-400 text-xs mt-1">{errors.firstName.message}</p>}
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Last Name</label>
                <input
                  type="text"
                  {...register('lastName', { required: 'Last name is required' })}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
                />
                {errors.lastName && <p className="text-red-400 text-xs mt-1">{errors.lastName.message}</p>}
              </div>
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Contact Phone</label>
              <input
                type="text"
                {...register('phoneNumber')}
                className="w-full bg-slate-950 border border-slate-800 rounded-xl p-3 text-sm text-slate-200 focus:border-purple-500 outline-none"
              />
            </div>

            <button
              type="submit"
              disabled={submittingProfile}
              className="bg-purple-600 hover:bg-purple-500 disabled:bg-purple-800 text-white font-medium px-6 py-3 rounded-xl transition-all shadow-md shadow-purple-900/30 flex items-center justify-center gap-2"
            >
              {submittingProfile ? <Loader2 className="w-4 h-4 animate-spin" /> : <Edit2 className="w-4 h-4" />}
              Save Profile
            </button>
          </form>
        </div>
      )}
    </div>
  );
};

export default SeekerDashboard;
