import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Search, Briefcase, Award, Users } from 'lucide-react';

/**
 * Public landing page for the application featuring hero keyword searches
 * and platform capabilities.
 */
const Home = () => {
  const navigate = useNavigate();
  const [keyword, setKeyword] = useState('');

  const handleSearch = (e) => {
    e.preventDefault();
    navigate(`/jobs?keyword=${encodeURIComponent(keyword)}`);
  };

  return (
    <div className="space-y-16 py-8">
      {/* Hero Section */}
      <section className="text-center space-y-6 max-w-4xl mx-auto px-4">
        <h1 className="text-4xl md:text-6xl font-extrabold tracking-tight bg-gradient-to-r from-purple-400 via-indigo-400 to-purple-600 bg-clip-text text-transparent">
          Discover Your Next Dream Career Move
        </h1>
        <p className="text-lg md:text-xl text-slate-400 max-w-2xl mx-auto leading-relaxed">
          Search thousands of job listings across top engineering, product, and design companies. Streamlined applications at your fingertips.
        </p>

        {/* Global Search Bar Console */}
        <form onSubmit={handleSearch} className="flex flex-col sm:flex-row gap-3 bg-slate-900 border border-slate-800 p-3 rounded-2xl max-w-2xl mx-auto shadow-2xl shadow-purple-950/20">
          <div className="flex-grow flex items-center gap-2 px-3 py-2 bg-slate-950 rounded-xl border border-slate-800">
            <Search className="w-5 h-5 text-purple-400" />
            <input
              aria-label="Search jobs"
              type="text"
              placeholder="Job titles, tech stacks, or keywords..."
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              className="bg-transparent border-none outline-none text-slate-100 placeholder-slate-500 w-full text-sm"
            />
          </div>
          <button
            type="submit"
            className="bg-purple-600 hover:bg-purple-500 text-white font-medium text-sm px-6 py-3 rounded-xl transition-all shadow-md shadow-purple-900/30 flex items-center justify-center gap-1.5"
          >
            Search Vacancies
          </button>
        </form>
      </section>

      {/* Feature stats blocks */}
      <section className="grid grid-cols-1 md:grid-cols-3 gap-8 max-w-6xl mx-auto px-4">
        <div className="bg-slate-900/50 border border-slate-800 p-6 rounded-2xl flex items-start gap-4">
          <div className="p-3 bg-purple-500/10 rounded-xl text-purple-400">
            <Briefcase className="w-6 h-6" />
          </div>
          <div>
            <h3 className="text-lg font-bold text-slate-200">10,000+ Active Jobs</h3>
            <p className="text-slate-400 text-sm mt-1">Direct listings across multiple engineering, product, and data branches.</p>
          </div>
        </div>

        <div className="bg-slate-900/50 border border-slate-800 p-6 rounded-2xl flex items-start gap-4">
          <div className="p-3 bg-indigo-500/10 rounded-xl text-indigo-400">
            <Award className="w-6 h-6" />
          </div>
          <div>
            <h3 className="text-lg font-bold text-slate-200">Top Tier Companies</h3>
            <p className="text-slate-400 text-sm mt-1">We host validated profiles of global industry leaders and fast-paced startups.</p>
          </div>
        </div>

        <div className="bg-slate-900/50 border border-slate-800 p-6 rounded-2xl flex items-start gap-4">
          <div className="p-3 bg-purple-500/10 rounded-xl text-purple-400">
            <Users className="w-6 h-6" />
          </div>
          <div>
            <h3 className="text-lg font-bold text-slate-200">Stateless Verification</h3>
            <p className="text-slate-400 text-sm mt-1">Fast and secure JWT-powered pipelines for candidate applications.</p>
          </div>
        </div>
      </section>

      {/* Action Portal Redirect Panels */}
      <section className="bg-gradient-to-b from-slate-900 to-slate-950 border border-slate-800 p-8 sm:p-12 rounded-3xl max-w-5xl mx-auto text-center space-y-6">
        <h2 className="text-3xl font-extrabold text-slate-200">Are you hiring talent?</h2>
        <p className="text-slate-400 max-w-xl mx-auto text-sm sm:text-base">
          Publish listings, target developer skillsets, track application pipeline stages, and manage candidate workflows all in one place.
        </p>
        <div className="flex flex-wrap items-center justify-center gap-4">
          <Link to="/register" className="bg-indigo-600 hover:bg-indigo-500 text-white font-medium text-sm px-6 py-3 rounded-xl transition-all shadow-md shadow-indigo-900/30">
            Register as Employer
          </Link>
          <Link to="/jobs" className="border border-slate-700 hover:border-slate-500 text-slate-300 px-6 py-3 rounded-xl text-sm font-medium transition-all">
            Browse Jobs
          </Link>
        </div>
      </section>
    </div>
  );
};

export default Home;
