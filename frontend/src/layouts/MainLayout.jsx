import React, { useState } from 'react';
import { Link, useNavigate, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { LogOut, Briefcase, PlusCircle, CheckSquare, Settings, Menu, X } from 'lucide-react';

/**
 * Main Layout component featuring sticky header navigation,
 * dynamic user role action links, and responsive mobile displays.
 */
const MainLayout = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="flex flex-col min-h-screen bg-slate-950 text-slate-100">
      {/* Navbar Header */}
      <nav className="border-b border-slate-800 bg-slate-900/80 backdrop-blur sticky top-0 z-40">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center">
              <Link to="/" className="flex items-center gap-2 text-xl font-bold bg-gradient-to-r from-purple-400 to-indigo-400 bg-clip-text text-transparent">
                <Briefcase className="w-6 h-6 text-purple-400" />
                <span>SmartBoard</span>
              </Link>
              
              <div className="hidden md:block ml-10">
                <div className="flex items-baseline space-x-4">
                  <Link to="/" className="text-slate-300 hover:text-white px-3 py-2 rounded-md text-sm font-medium transition-colors">Home</Link>
                  <Link to="/jobs" className="text-slate-300 hover:text-white px-3 py-2 rounded-md text-sm font-medium transition-colors">Find Jobs</Link>
                </div>
              </div>
            </div>

            <div className="hidden md:flex items-center gap-4">
              {user ? (
                <>
                  {user.role === 'JOB_SEEKER' && (
                    <Link to="/seeker" className="flex items-center gap-1.5 text-slate-300 hover:text-white px-3 py-2 text-sm font-medium transition-colors">
                      <CheckSquare className="w-4 h-4" /> Seeker Portal
                    </Link>
                  )}
                  {user.role === 'EMPLOYER' && (
                    <Link to="/employer" className="flex items-center gap-1.5 text-slate-300 hover:text-white px-3 py-2 text-sm font-medium transition-colors">
                      <PlusCircle className="w-4 h-4" /> Employer Portal
                    </Link>
                  )}
                  {user.role === 'ADMIN' && (
                    <Link to="/admin" className="flex items-center gap-1.5 text-slate-300 hover:text-white px-3 py-2 text-sm font-medium transition-colors">
                      <Settings className="w-4 h-4" /> Admin Portal
                    </Link>
                  )}

                  <div className="flex items-center gap-2 pl-4 border-l border-slate-800">
                    <span className="text-sm font-medium text-slate-300">Hello, {user.firstName}</span>
                    <button onClick={handleLogout} className="flex items-center gap-1 text-slate-400 hover:text-purple-400 p-2 rounded-md text-sm transition-colors" title="Logout">
                      <LogOut className="w-4 h-4" />
                    </button>
                  </div>
                </>
              ) : (
                <>
                  <Link to="/login" className="text-slate-300 hover:text-white text-sm font-medium transition-colors">Sign In</Link>
                  <Link to="/register" className="bg-purple-600 hover:bg-purple-500 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors shadow-lg shadow-purple-900/30">Register</Link>
                </>
              )}
            </div>

            {/* Mobile menu button */}
            <div className="md:hidden flex items-center">
              <button onClick={() => setMobileMenuOpen(!mobileMenuOpen)} className="text-slate-400 hover:text-white p-2">
                {mobileMenuOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
              </button>
            </div>
          </div>
        </div>

        {/* Mobile menu links */}
        {mobileMenuOpen && (
          <div className="md:hidden px-2 pt-2 pb-3 space-y-1 sm:px-3 bg-slate-900 border-b border-slate-800">
            <Link to="/" onClick={() => setMobileMenuOpen(false)} className="block text-slate-300 hover:text-white px-3 py-2 rounded-md text-base font-medium">Home</Link>
            <Link to="/jobs" onClick={() => setMobileMenuOpen(false)} className="block text-slate-300 hover:text-white px-3 py-2 rounded-md text-base font-medium">Find Jobs</Link>
            {user ? (
              <>
                {user.role === 'JOB_SEEKER' && <Link to="/seeker" onClick={() => setMobileMenuOpen(false)} className="block text-slate-300 hover:text-white px-3 py-2 rounded-md text-base font-medium">Seeker Portal</Link>}
                {user.role === 'EMPLOYER' && <Link to="/employer" onClick={() => setMobileMenuOpen(false)} className="block text-slate-300 hover:text-white px-3 py-2 rounded-md text-base font-medium">Employer Portal</Link>}
                {user.role === 'ADMIN' && <Link to="/admin" onClick={() => setMobileMenuOpen(false)} className="block text-slate-300 hover:text-white px-3 py-2 rounded-md text-base font-medium">Admin Portal</Link>}
                <div className="border-t border-slate-800 pt-2 mt-2 flex items-center justify-between px-3">
                  <span className="text-sm font-medium text-slate-400">{user.email}</span>
                  <button onClick={() => { setMobileMenuOpen(false); handleLogout(); }} className="flex items-center gap-1 text-red-400 hover:text-red-300 text-sm font-medium">
                    <LogOut className="w-4 h-4" /> Sign Out
                  </button>
                </div>
              </>
            ) : (
              <div className="border-t border-slate-800 pt-2 mt-2 space-y-2">
                <Link to="/login" onClick={() => setMobileMenuOpen(false)} className="block text-center text-slate-300 hover:text-white px-3 py-2 text-base font-medium">Sign In</Link>
                <Link to="/register" onClick={() => setMobileMenuOpen(false)} className="block text-center bg-purple-600 hover:bg-purple-500 text-white px-3 py-2 rounded-lg text-base font-medium">Register</Link>
              </div>
            )}
          </div>
        )}
      </nav>

      {/* Page Content */}
      <main className="flex-grow max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <Outlet />
      </main>

      {/* Footer */}
      <footer className="border-t border-slate-800 bg-slate-900 py-6 text-slate-500 text-sm">
        <div className="max-w-7xl mx-auto px-4 flex flex-col md:flex-row items-center justify-between gap-4">
          <p>© {new Date().getFullYear()} SmartBoard. Premium Candidate Vacancy Platform.</p>
          <div className="flex items-center gap-4">
            <Link to="/jobs" className="hover:text-slate-300 transition-colors">Vacancies</Link>
            <span className="text-slate-800">|</span>
            <a href="https://www.globalco.com" target="_blank" rel="noopener noreferrer" className="hover:text-slate-300 transition-colors">GlobalCo Group</a>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default MainLayout;
