import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useAuth } from '../context/AuthContext';
import { User, Mail, KeyRound, Phone, Briefcase, AlertCircle, Loader2 } from 'lucide-react';

/**
 * Registration page using react-hook-form and global AuthContext register routines.
 */
const Register = () => {
  const { register: registerUser } = useAuth();
  const navigate = useNavigate();
  const [errorMsg, setErrorMsg] = useState(null);
  const [successMsg, setSuccessMsg] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm({
    defaultValues: {
      role: 'JOB_SEEKER',
    },
  });

  const watchPassword = watch('password');

  const onSubmit = async (data) => {
    setErrorMsg(null);
    setSuccessMsg(null);
    setSubmitting(true);
    try {
      await registerUser({
        email: data.email,
        password: data.password,
        firstName: data.firstName,
        lastName: data.lastName,
        phoneNumber: data.phoneNumber || null,
        role: data.role,
        isActive: true,
      });

      setSuccessMsg('Account registered successfully! Redirecting to login...');
      setTimeout(() => {
        navigate('/login');
      }, 2500);
    } catch (err) {
      setErrorMsg(err);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="flex justify-center items-center py-8 px-4 sm:px-6 lg:px-8">
      <div className="max-w-lg w-full space-y-8 bg-slate-900 border border-slate-800 p-8 rounded-3xl shadow-xl">
        <div className="text-center">
          <h2 className="text-3xl font-extrabold tracking-tight bg-gradient-to-r from-purple-400 to-indigo-400 bg-clip-text text-transparent">
            Create an Account
          </h2>
          <p className="mt-2 text-sm text-slate-400">
            Join SmartBoard to manage vacancies or applications
          </p>
        </div>

        {errorMsg && (
          <div className="bg-red-500/10 border border-red-500/30 text-red-400 text-sm p-4 rounded-xl flex items-start gap-2.5">
            <AlertCircle className="w-5 h-5 flex-shrink-0" />
            <span>{errorMsg}</span>
          </div>
        )}

        {successMsg && (
          <div className="bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 text-sm p-4 rounded-xl flex items-start gap-2.5">
            <CheckCircleIcon className="w-5 h-5 flex-shrink-0" />
            <span>{successMsg}</span>
          </div>
        )}

        <form onSubmit={handleSubmit(onSubmit)} className="mt-8 space-y-6">
          <div className="space-y-4">
            {/* First and Last Name in one row */}
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">First Name</label>
                <div className="flex items-center gap-2 px-3 py-2.5 bg-slate-950 rounded-xl border border-slate-800 focus-within:border-purple-500 transition-colors">
                  <User className="w-4 h-4 text-slate-500" />
                  <input
                    type="text"
                    placeholder="Jane"
                    {...register('firstName', { required: 'First name is required' })}
                    className="bg-transparent border-none outline-none text-slate-100 placeholder-slate-600 w-full text-sm"
                  />
                </div>
                {errors.firstName && (
                  <p className="text-red-400 text-xs mt-1">{errors.firstName.message}</p>
                )}
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Last Name</label>
                <div className="flex items-center gap-2 px-3 py-2.5 bg-slate-950 rounded-xl border border-slate-800 focus-within:border-purple-500 transition-colors">
                  <User className="w-4 h-4 text-slate-500" />
                  <input
                    type="text"
                    placeholder="Doe"
                    {...register('lastName', { required: 'Last name is required' })}
                    className="bg-transparent border-none outline-none text-slate-100 placeholder-slate-600 w-full text-sm"
                  />
                </div>
                {errors.lastName && (
                  <p className="text-red-400 text-xs mt-1">{errors.lastName.message}</p>
                )}
              </div>
            </div>

            {/* Email and Phone */}
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Email Address</label>
                <div className="flex items-center gap-2 px-3 py-2.5 bg-slate-950 rounded-xl border border-slate-800 focus-within:border-purple-500 transition-colors">
                  <Mail className="w-4 h-4 text-slate-500" />
                  <input
                    type="email"
                    placeholder="jane.doe@example.com"
                    {...register('email', {
                      required: 'Email is required',
                      pattern: {
                        value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                        message: 'Invalid email address',
                      },
                    })}
                    className="bg-transparent border-none outline-none text-slate-100 placeholder-slate-600 w-full text-sm"
                  />
                </div>
                {errors.email && (
                  <p className="text-red-400 text-xs mt-1">{errors.email.message}</p>
                )}
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Phone Number</label>
                <div className="flex items-center gap-2 px-3 py-2.5 bg-slate-950 rounded-xl border border-slate-800 focus-within:border-purple-500 transition-colors">
                  <Phone className="w-4 h-4 text-slate-500" />
                  <input
                    type="text"
                    placeholder="+15550199"
                    {...register('phoneNumber')}
                    className="bg-transparent border-none outline-none text-slate-100 placeholder-slate-600 w-full text-sm"
                  />
                </div>
              </div>
            </div>

            {/* Role dropdown */}
            <div>
              <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Account Role</label>
              <div className="flex items-center gap-2 px-3 py-2.5 bg-slate-950 rounded-xl border border-slate-800 focus-within:border-purple-500 transition-colors">
                <Briefcase className="w-4 h-4 text-slate-500" />
                <select
                  {...register('role', { required: 'Please select a role' })}
                  className="bg-transparent border-none outline-none text-slate-100 placeholder-slate-600 w-full text-sm focus:ring-0 focus:outline-none"
                >
                  <option value="JOB_SEEKER" className="bg-slate-950">Job Seeker (Candidate)</option>
                  <option value="EMPLOYER" className="bg-slate-950">Employer (Recruiter / Hiring Manager)</option>
                </select>
              </div>
            </div>

            {/* Passwords */}
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Password</label>
                <div className="flex items-center gap-2 px-3 py-2.5 bg-slate-950 rounded-xl border border-slate-800 focus-within:border-purple-500 transition-colors">
                  <KeyRound className="w-4 h-4 text-slate-500" />
                  <input
                    type="password"
                    placeholder="••••••••"
                    {...register('password', {
                      required: 'Password is required',
                      minLength: { value: 8, message: 'Password must be at least 8 characters' },
                    })}
                    className="bg-transparent border-none outline-none text-slate-100 placeholder-slate-600 w-full text-sm"
                  />
                </div>
                {errors.password && (
                  <p className="text-red-400 text-xs mt-1">{errors.password.message}</p>
                )}
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Confirm Password</label>
                <div className="flex items-center gap-2 px-3 py-2.5 bg-slate-950 rounded-xl border border-slate-800 focus-within:border-purple-500 transition-colors">
                  <KeyRound className="w-4 h-4 text-slate-500" />
                  <input
                    type="password"
                    placeholder="••••••••"
                    {...register('confirmPassword', {
                      required: 'Please confirm password',
                      validate: (val) => val === watchPassword || 'Passwords do not match',
                    })}
                    className="bg-transparent border-none outline-none text-slate-100 placeholder-slate-600 w-full text-sm"
                  />
                </div>
                {errors.confirmPassword && (
                  <p className="text-red-400 text-xs mt-1">{errors.confirmPassword.message}</p>
                )}
              </div>
            </div>
          </div>

          <button
            type="submit"
            disabled={submitting}
            className="w-full bg-purple-600 hover:bg-purple-500 disabled:bg-purple-800 text-white font-medium py-3 rounded-xl transition-all shadow-md shadow-purple-900/30 flex items-center justify-center gap-2"
          >
            {submitting ? (
              <>
                <Loader2 className="w-5 h-5 animate-spin" />
                Creating Account...
              </>
            ) : (
              'Create Account'
            )}
          </button>

          <p className="text-center text-sm text-slate-400 mt-4">
            Already have an account?{' '}
            <Link to="/login" className="text-purple-400 hover:text-purple-300 font-medium font-sans">
              Sign In
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
};

// Simple inline icons to avoid extra libraries
const CheckCircleIcon = (props) => (
  <svg fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" {...props}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M9 12.75 11.25 15 15 9.75M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
  </svg>
);

export default Register;
