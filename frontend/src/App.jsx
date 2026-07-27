import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import MainLayout from './layouts/MainLayout';
import ProtectedRoute from './components/ProtectedRoute';

// Pages
import Home from './pages/Home';
import Jobs from './pages/Jobs';
import JobDetails from './pages/JobDetails';
import Login from './pages/Login';
import Register from './pages/Register';
import SeekerDashboard from './pages/SeekerDashboard';
import EmployerDashboard from './pages/EmployerDashboard';
import AdminDashboard from './pages/AdminDashboard';

/**
 * App component registering routing paths, global provider wrappers,
 * and auth redirects.
 */
function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
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
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
