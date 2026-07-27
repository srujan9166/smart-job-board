import React, { useState, useEffect } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import api from '../services/api';
import { Search, MapPin, Briefcase, DollarSign, Calendar, SlidersHorizontal, ChevronLeft, ChevronRight, Loader2 } from 'lucide-react';

/**
 * Public paginated job board listings page. Connects dynamic search keywords,
 * filters (category, type, level, location, salary), sorting, and page properties.
 */
const Jobs = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [jobs, setJobs] = useState([]);
  const [categories, setCategories] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(true);

  // Read params or fall back to defaults
  const keyword = searchParams.get('keyword') || '';
  const categoryId = searchParams.get('categoryId') || '';
  const location = searchParams.get('location') || '';
  const jobType = searchParams.get('jobType') || '';
  const experienceLevel = searchParams.get('experienceLevel') || '';
  const salaryMin = searchParams.get('salaryMin') || '';
  const salaryMax = searchParams.get('salaryMax') || '';
  const page = parseInt(searchParams.get('page') || '0');
  const size = 6;
  const sort = searchParams.get('sort') || 'createdAt,desc';

  // Fetch categories on mount
  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const res = await api.get('/api/categories');
        setCategories(res.data.data);
      } catch (err) {
        console.error('Failed to load categories', err);
      }
    };
    fetchCategories();
  }, []);

  // Fetch jobs when parameters change
  useEffect(() => {
    const fetchJobs = async () => {
      setLoading(true);
      try {
        const params = {
          page,
          size,
          sort,
        };
        if (keyword) params.keyword = keyword;
        if (categoryId) params.categoryId = categoryId;
        if (location) params.location = location;
        if (jobType) params.jobType = jobType;
        if (experienceLevel) params.experienceLevel = experienceLevel;
        if (salaryMin) params.salaryMin = salaryMin;
        if (salaryMax) params.salaryMax = salaryMax;

        const res = await api.get('/api/jobs', { params });
        setJobs(res.data.data.content);
        setTotalPages(res.data.data.totalPages);
        setTotalElements(res.data.data.totalElements);
      } catch (err) {
        console.error('Failed to load job listings', err);
      } finally {
        setLoading(false);
      }
    };
    fetchJobs();
  }, [keyword, categoryId, location, jobType, experienceLevel, salaryMin, salaryMax, page, sort]);

  const updateParam = (key, val) => {
    const newParams = new URLSearchParams(searchParams);
    if (val) {
      newParams.set(key, val);
    } else {
      newParams.delete(key);
    }
    // Reset page back to 0 on any filter modifications
    if (key !== 'page') {
      newParams.set('page', '0');
    }
    setSearchParams(newParams);
  };

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    const query = e.target.search.value;
    updateParam('keyword', query);
  };

  return (
    <div className="space-y-8">
      {/* Search and Metadata Info */}
      <div className="flex flex-col md:flex-row gap-4 items-center justify-between">
        <div>
          <h2 className="text-3xl font-extrabold text-slate-200">Find Your Career</h2>
          <p className="text-slate-400 text-sm mt-1">{totalElements} matching vacancies published</p>
        </div>

        {/* Inline Keyword Input Form */}
        <form onSubmit={handleSearchSubmit} className="w-full md:max-w-md flex items-center gap-2 bg-slate-900 border border-slate-800 p-2 rounded-xl">
          <Search className="w-4 h-4 text-purple-400 ml-2" />
          <input
            name="search"
            defaultValue={keyword}
            type="text"
            placeholder="Search titles, skills, or keywords..."
            className="bg-transparent border-none outline-none text-slate-100 placeholder-slate-600 text-sm w-full"
          />
          <button type="submit" className="bg-purple-600 hover:bg-purple-500 text-white font-medium text-xs px-4 py-2 rounded-lg transition-colors">
            Search
          </button>
        </form>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
        {/* Left Side: Filter Panels */}
        <div className="space-y-6 bg-slate-900 border border-slate-800 p-6 rounded-2xl h-fit">
          <div className="flex items-center gap-2 text-slate-200 border-b border-slate-800 pb-3 font-bold">
            <SlidersHorizontal className="w-4 h-4 text-purple-400" />
            <span>Filters</span>
          </div>

          {/* Category Filter */}
          <div className="space-y-2">
            <label className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Category</label>
            <select
              value={categoryId}
              onChange={(e) => updateParam('categoryId', e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-xl p-2.5 text-sm text-slate-200 focus:border-purple-500 outline-none"
            >
              <option value="">All Categories</option>
              {categories.map((c) => (
                <option key={c.id} value={c.id}>{c.name}</option>
              ))}
            </select>
          </div>

          {/* Location Filter */}
          <div className="space-y-2">
            <label className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Location</label>
            <input
              type="text"
              placeholder="e.g. Remote, New York"
              value={location}
              onChange={(e) => updateParam('location', e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-xl p-2.5 text-sm text-slate-200 placeholder-slate-700 focus:border-purple-500 outline-none"
            />
          </div>

          {/* Job Type Filter */}
          <div className="space-y-2">
            <label className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Job Type</label>
            <select
              value={jobType}
              onChange={(e) => updateParam('jobType', e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-xl p-2.5 text-sm text-slate-200 focus:border-purple-500 outline-none"
            >
              <option value="">All Types</option>
              <option value="FULL_TIME">Full Time</option>
              <option value="PART_TIME">Part Time</option>
              <option value="CONTRACT">Contract</option>
              <option value="INTERNSHIP">Internship</option>
              <option value="REMOTE">Remote</option>
            </select>
          </div>

          {/* Experience level Filter */}
          <div className="space-y-2">
            <label className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Seniority Level</label>
            <select
              value={experienceLevel}
              onChange={(e) => updateParam('experienceLevel', e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-xl p-2.5 text-sm text-slate-200 focus:border-purple-500 outline-none"
            >
              <option value="">All Seniorities</option>
              <option value="ENTRY">Entry Level</option>
              <option value="MID">Mid Level</option>
              <option value="SENIOR">Senior Level</option>
              <option value="LEAD">Lead Level</option>
              <option value="EXECUTIVE">Executive Level</option>
            </select>
          </div>

          {/* Sort Selection */}
          <div className="space-y-2 border-t border-slate-800 pt-4">
            <label className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Sort By</label>
            <select
              value={sort}
              onChange={(e) => updateParam('sort', e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-xl p-2.5 text-sm text-slate-200 focus:border-purple-500 outline-none"
            >
              <option value="createdAt,desc">Newest Published</option>
              <option value="createdAt,asc">Oldest Published</option>
              <option value="title,asc">Title A-Z</option>
              <option value="salaryMax,desc">Salary High-Low</option>
            </select>
          </div>
        </div>

        {/* Right Side: Job Grid Cards */}
        <div className="lg:col-span-3 space-y-6">
          {loading ? (
            <div className="flex justify-center items-center py-24">
              <Loader2 className="w-10 h-10 animate-spin text-purple-400" />
            </div>
          ) : jobs.length === 0 ? (
            <div className="bg-slate-900 border border-slate-800 p-12 text-center rounded-2xl space-y-3">
              <p className="text-slate-400 font-medium">No job postings found matching your parameters.</p>
              <button
                onClick={() => setSearchParams(new URLSearchParams())}
                className="text-purple-400 hover:text-purple-300 text-sm font-semibold hover:underline"
              >
                Clear all filters
              </button>
            </div>
          ) : (
            <>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {jobs.map((job) => (
                  <div key={job.id} className="bg-slate-900 border border-slate-800 p-6 rounded-2xl hover:border-purple-500/50 transition-all flex flex-col justify-between">
                    <div className="space-y-3">
                      <div className="flex items-center justify-between">
                        <span className="text-xs font-bold text-purple-400 bg-purple-500/10 px-2.5 py-1 rounded-full uppercase tracking-wider">
                          {job.jobType.replace('_', ' ')}
                        </span>
                        <span className="text-xs text-slate-500 flex items-center gap-1">
                          <Calendar className="w-3.5 h-3.5" />
                          {new Date(job.createdAt).toLocaleDateString()}
                        </span>
                      </div>

                      <h3 className="text-lg font-bold text-slate-200 hover:text-purple-400 transition-colors line-clamp-1">
                        <Link to={`/jobs/${job.id}`}>{job.title}</Link>
                      </h3>
                      <p className="text-slate-400 text-sm font-medium">{job.companyName}</p>

                      <div className="flex flex-wrap gap-y-2 gap-x-4 text-xs text-slate-400 pt-2">
                        <span className="flex items-center gap-1"><MapPin className="w-3.5 h-3.5 text-slate-500" /> {job.location}</span>
                        <span className="flex items-center gap-1"><Briefcase className="w-3.5 h-3.5 text-slate-500" /> {job.experienceLevel}</span>
                        {(job.salaryMin || job.salaryMax) && (
                          <span className="flex items-center gap-0.5"><DollarSign className="w-3.5 h-3.5 text-slate-500" /> {job.salaryMin} - {job.salaryMax} {job.currency}</span>
                        )}
                      </div>
                    </div>

                    <div className="pt-6 mt-4 border-t border-slate-800 flex items-center justify-between">
                      <div className="flex gap-1.5 flex-wrap">
                        {job.jobSkills.slice(0, 3).map((js) => (
                          <span key={js.skillId} className="bg-slate-950 text-slate-400 border border-slate-850 px-2 py-0.5 rounded text-[10px]">
                            {js.skillName}
                          </span>
                        ))}
                        {job.jobSkills.length > 3 && (
                          <span className="text-slate-500 text-[10px] self-center">+{job.jobSkills.length - 3} more</span>
                        )}
                      </div>
                      <Link to={`/jobs/${job.id}`} className="text-purple-400 hover:text-purple-300 text-xs font-bold transition-all">
                        View Details →
                      </Link>
                    </div>
                  </div>
                ))}
              </div>

              {/* Pagination controls */}
              {totalPages > 1 && (
                <div className="flex items-center justify-between border-t border-slate-800 pt-6">
                  <button
                    disabled={page === 0}
                    onClick={() => updateParam('page', (page - 1).toString())}
                    className="flex items-center gap-1 px-4 py-2 border border-slate-800 bg-slate-900 rounded-xl hover:border-slate-700 disabled:opacity-50 text-sm font-medium transition-all"
                  >
                    <ChevronLeft className="w-4 h-4" /> Previous
                  </button>
                  <span className="text-slate-400 text-sm">Page {page + 1} of {totalPages}</span>
                  <button
                    disabled={page >= totalPages - 1}
                    onClick={() => updateParam('page', (page + 1).toString())}
                    className="flex items-center gap-1 px-4 py-2 border border-slate-800 bg-slate-900 rounded-xl hover:border-slate-700 disabled:opacity-50 text-sm font-medium transition-all"
                  >
                    Next <ChevronRight className="w-4 h-4" />
                  </button>
                </div>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default Jobs;
