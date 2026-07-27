import React, { lazy, Suspense } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import MainLayout from './layouts/MainLayout';
import ProtectedRoute from './components/ProtectedRoute';
import LoadingState from './components/LoadingState';
import ErrorBoundary from './components/ErrorBoundary';

// Pages
const Home = lazy(() => import('./pages/Home'));
const Jobs = lazy(() => import('./pages/Jobs'));
const JobDetails = lazy(() => import('./pages/JobDetails'));
const Login = lazy(() => import('./pages/Login'));
const Register = lazy(() => import('./pages/Register'));
const SeekerDashboard = lazy(() => import('./pages/SeekerDashboard'));
const EmployerDashboard = lazy(() => import('./pages/EmployerDashboard'));
const AdminDashboard = lazy(() => import('./pages/AdminDashboard'));

/**
 * App component registering routing paths, global provider wrappers,
 * and auth redirects.
 */
function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <ErrorBoundary>
          <Suspense fallback={<LoadingState label="Loading page…" className="min-h-screen" />}>
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
        </ErrorBoundary>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
