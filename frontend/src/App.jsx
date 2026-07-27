import React, { Suspense, lazy } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { ToastProvider } from './context/ToastContext';
import MainLayout from './layouts/MainLayout';
import ProtectedRoute from './components/ProtectedRoute';
import { Loader2 } from 'lucide-react';

// Lazy load pages for initial bundle optimizations
const Home = lazy(() => import('./pages/Home'));
const Jobs = lazy(() => import('./pages/Jobs'));
const JobDetails = lazy(() => import('./pages/JobDetails'));
const Login = lazy(() => import('./pages/Login'));
const Register = lazy(() => import('./pages/Register'));
const SeekerDashboard = lazy(() => import('./pages/SeekerDashboard'));
const EmployerDashboard = lazy(() => import('./pages/EmployerDashboard'));
const AdminDashboard = lazy(() => import('./pages/AdminDashboard'));

// Page loading block fallback
const PageLoader = () => (
  <div className="flex justify-center items-center min-h-[50vh]">
    <Loader2 className="w-10 h-10 animate-spin text-purple-400" />
  </div>
);

/**
 * Main application routing container configured with route code-splitting,
 * session contexts, and dynamic notifications modules.
 */
function App() {
  return (
    <BrowserRouter>
      <ToastProvider>
        <AuthProvider>
          <Suspense fallback={<PageLoader />}>
            <Routes>
              <Route path="/" element={<MainLayout />}>
                {/* Public paths */}
                <Route index element={<Home />} />
                <Route path="jobs" element={<Jobs />} />
                <Route path="jobs/:id" element={<JobDetails />} />
                <Route path="login" element={<Login />} />
                <Route path="register" element={<Register />} />

                {/* Candidate seeker portals */}
                <Route
                  path="seeker"
                  element={
                    <ProtectedRoute allowedRoles={['JOB_SEEKER', 'ADMIN']}>
                      <SeekerDashboard />
                    </ProtectedRoute>
                  }
                />

                {/* Employer publisher portals */}
                <Route
                  path="employer"
                  element={
                    <ProtectedRoute allowedRoles={['EMPLOYER', 'ADMIN']}>
                      <EmployerDashboard />
                    </ProtectedRoute>
                  }
                />

                {/* Administrator console portals */}
                <Route
                  path="admin"
                  element={
                    <ProtectedRoute allowedRoles={['ADMIN']}>
                      <AdminDashboard />
                    </ProtectedRoute>
                  }
                />

                {/* Catch-all redirect */}
                <Route path="*" element={<Navigate to="/" replace />} />
              </Route>
            </Routes>
          </Suspense>
        </AuthProvider>
      </ToastProvider>
    </BrowserRouter>
  );
}

export default App;
