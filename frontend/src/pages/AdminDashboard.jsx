import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { useForm } from 'react-hook-form';
import { Plus, Trash2, CheckCircle, AlertCircle, Loader2 } from 'lucide-react';

/**
 * Admin portal dashboard enabling classification category mapping,
 * skill directories registrations, and companies listings audits.
 */
const AdminDashboard = () => {
  const [categories, setCategories] = useState([]);
  const [skills, setSkills] = useState([]);
  const [companies, setCompanies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('categories');

  // Creation Sub-states
  const [msg, setMsg] = useState(null);
  const [err, setErr] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  const { register: regCat, handleSubmit: handleCatSubmit, reset: resetCat } = useForm();
  const { register: regSkill, handleSubmit: handleSkillSubmit, reset: resetSkill } = useForm();

  const loadData = async () => {
    try {
      const catRes = await api.get('/api/categories');
      setCategories(catRes.data.data);

      const skillRes = await api.get('/api/skills');
      setSkills(skillRes.data.data);

      const compRes = await api.get('/api/companies');
      setCompanies(compRes.data.data.content || []);
    } catch (e) {
      console.error('Failed to load admin logs', e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const onCreateCategory = async (data) => {
    setMsg(null);
    setErr(null);
    setSubmitting(true);
    try {
      await api.post('/api/categories', {
        name: data.name,
        description: data.description || '',
      });
      setMsg('Category added successfully!');
      resetCat();
      loadData();
    } catch (e) {
      setErr(e.response?.data?.message || 'Failed to create category.');
    } finally {
      setSubmitting(false);
    }
  };

  const onCreateSkill = async (data) => {
    setMsg(null);
    setErr(null);
    setSubmitting(true);
    try {
      await api.post('/api/skills', {
        name: data.name,
        categoryId: data.categoryId,
      });
      setMsg('Skill added successfully!');
      resetSkill();
      loadData();
    } catch (e) {
      setErr(e.response?.data?.message || 'Failed to create skill.');
    } finally {
      setSubmitting(false);
    }
  };

  const onDeleteCategory = async (id) => {
    if (!window.confirm('Are you sure you want to delete this category? If jobs are linked, it will fail.')) {
      return;
    }
    try {
      await api.delete(`/api/categories/${id}`);
      loadData();
    } catch (e) {
      alert(e.response?.data?.message || 'Failed to delete category.');
    }
  };

  const onDeleteSkill = async (id) => {
    if (!window.confirm('Are you sure you want to delete this skill?')) {
      return;
    }
    try {
      await api.delete(`/api/skills/${id}`);
      loadData();
    } catch (e) {
      alert(e.response?.data?.message || 'Failed to delete skill.');
    }
  };

  const onDeleteCompany = async (id) => {
    if (!window.confirm('Are you sure you want to delete this company profile? This action is irreversible.')) {
      return;
    }
    try {
      await api.delete(`/api/companies/${id}`);
      loadData();
    } catch (e) {
      alert(e.response?.data?.message || 'Failed to delete company profile.');
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
      {/* Admin Header */}
      <div>
        <h2 className="text-3xl font-extrabold text-slate-200">Admin Control Panel</h2>
        <p className="text-slate-400 text-sm mt-1">Manage global categories trees, skills directories, and company registries</p>
      </div>

      {/* Tab controls */}
      <div className="flex gap-4 overflow-x-auto border-b border-slate-800" role="tablist" aria-label="Admin dashboard sections">
        <button
          onClick={() => { setActiveTab('categories'); setMsg(null); setErr(null); }}
          className={`pb-3 text-sm font-bold border-b-2 transition-all ${
            activeTab === 'categories' ? 'border-purple-500 text-purple-400' : 'border-transparent text-slate-400 hover:text-slate-200'
          }`}
        >
          Manage Categories ({categories.length})
        </button>
        <button
          onClick={() => { setActiveTab('skills'); setMsg(null); setErr(null); }}
          className={`pb-3 text-sm font-bold border-b-2 transition-all ${
            activeTab === 'skills' ? 'border-purple-500 text-purple-400' : 'border-transparent text-slate-400 hover:text-slate-200'
          }`}
        >
          Manage Skills ({skills.length})
        </button>
        <button
          onClick={() => { setActiveTab('companies'); setMsg(null); setErr(null); }}
          className={`pb-3 text-sm font-bold border-b-2 transition-all ${
            activeTab === 'companies' ? 'border-purple-500 text-purple-400' : 'border-transparent text-slate-400 hover:text-slate-200'
          }`}
        >
          Manage Companies ({companies.length})
        </button>
      </div>

      {/* Tab Panels */}
      {activeTab === 'categories' && (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Create Category Form */}
          <div className="bg-slate-900 border border-slate-800 p-6 rounded-3xl h-fit space-y-4">
            <h3 className="text-lg font-bold text-slate-200 border-b border-slate-800 pb-2">Add New Category</h3>
            {msg && <div className="bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 p-3 rounded-xl text-xs flex items-center gap-1.5"><CheckCircle className="w-4 h-4" />{msg}</div>}
            {err && <div className="bg-red-500/10 border border-red-500/30 text-red-400 p-3 rounded-xl text-xs flex items-center gap-1.5"><AlertCircle className="w-4 h-4" />{err}</div>}

            <form onSubmit={handleCatSubmit(onCreateCategory)} className="space-y-4">
              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Category Name</label>
                <input
                  type="text"
                  placeholder="e.g. Data Science"
                  {...regCat('name', { required: true })}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-2.5 text-sm text-slate-200 focus:border-purple-500 outline-none"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Description</label>
                <input
                  type="text"
                  placeholder="Classification description..."
                  {...regCat('description')}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-2.5 text-sm text-slate-200 focus:border-purple-500 outline-none"
                />
              </div>

              <button type="submit" disabled={submitting} className="w-full bg-purple-600 hover:bg-purple-500 disabled:bg-purple-800 text-white font-medium py-2.5 rounded-xl text-sm transition-colors flex items-center justify-center gap-1.5">
                {submitting ? <Loader2 className="w-4 h-4 animate-spin" /> : <Plus className="w-4 h-4" />} Create Category
              </button>
            </form>
          </div>

          {/* Categories list */}
          <div className="lg:col-span-2 space-y-4">
            <div className="overflow-x-auto border border-slate-800 rounded-2xl bg-slate-900">
              <table className="min-w-full divide-y divide-slate-800">
                <thead className="bg-slate-950">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-semibold text-slate-400 uppercase">Category Name</th>
                    <th className="px-6 py-3 text-left text-xs font-semibold text-slate-400 uppercase">Slug / Description</th>
                    <th className="px-6 py-3 text-right text-xs font-semibold text-slate-400 uppercase">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-800 bg-slate-900">
                  {categories.map((c) => (
                    <tr key={c.id}>
                      <td className="px-6 py-4 whitespace-nowrap font-bold text-slate-200">{c.name}</td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-400">
                        <div className="font-mono text-xs">{c.slug}</div>
                        <div className="text-xs text-slate-500 mt-0.5">{c.description || 'No description'}</div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-right text-sm">
                        <button onClick={() => onDeleteCategory(c.id)} className="text-red-400 hover:text-red-300 font-semibold inline-flex items-center gap-1 hover:underline">
                          <Trash2 className="w-3.5 h-3.5" /> Remove
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}

      {activeTab === 'skills' && (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Create Skill Form */}
          <div className="bg-slate-900 border border-slate-800 p-6 rounded-3xl h-fit space-y-4">
            <h3 className="text-lg font-bold text-slate-200 border-b border-slate-800 pb-2">Add New Skill</h3>
            {msg && <div className="bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 p-3 rounded-xl text-xs flex items-center gap-1.5"><CheckCircle className="w-4 h-4" />{msg}</div>}
            {err && <div className="bg-red-500/10 border border-red-500/30 text-red-400 p-3 rounded-xl text-xs flex items-center gap-1.5"><AlertCircle className="w-4 h-4" />{err}</div>}

            <form onSubmit={handleSkillSubmit(onCreateSkill)} className="space-y-4">
              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Skill Name</label>
                <input
                  type="text"
                  placeholder="e.g. Java"
                  {...regSkill('name', { required: true })}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-2.5 text-sm text-slate-200 focus:border-purple-500 outline-none"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider mb-2">Category</label>
                <select
                  {...regSkill('categoryId', { required: true })}
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl p-2.5 text-sm text-slate-200 focus:border-purple-500 outline-none"
                >
                  <option value="">Select Parent Category</option>
                  {categories.map((c) => (
                    <option key={c.id} value={c.id}>{c.name}</option>
                  ))}
                </select>
              </div>

              <button type="submit" disabled={submitting} className="w-full bg-purple-600 hover:bg-purple-500 disabled:bg-purple-800 text-white font-medium py-2.5 rounded-xl text-sm transition-colors flex items-center justify-center gap-1.5">
                {submitting ? <Loader2 className="w-4 h-4 animate-spin" /> : <Plus className="w-4 h-4" />} Create Skill
              </button>
            </form>
          </div>

          {/* Skills list */}
          <div className="lg:col-span-2 space-y-4">
            <div className="overflow-x-auto border border-slate-800 rounded-2xl bg-slate-900">
              <table className="min-w-full divide-y divide-slate-800">
                <thead className="bg-slate-950">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-semibold text-slate-400 uppercase">Skill</th>
                    <th className="px-6 py-3 text-left text-xs font-semibold text-slate-400 uppercase">Parent Category</th>
                    <th className="px-6 py-3 text-right text-xs font-semibold text-slate-400 uppercase">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-800 bg-slate-900">
                  {skills.map((s) => (
                    <tr key={s.id}>
                      <td className="px-6 py-4 whitespace-nowrap font-bold text-slate-200">{s.name}</td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-400">{s.categoryName}</td>
                      <td className="px-6 py-4 whitespace-nowrap text-right text-sm">
                        <button onClick={() => onDeleteSkill(s.id)} className="text-red-400 hover:text-red-300 font-semibold inline-flex items-center gap-1 hover:underline">
                          <Trash2 className="w-3.5 h-3.5" /> Remove
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}

      {activeTab === 'companies' && (
        <div className="overflow-x-auto border border-slate-800 rounded-2xl bg-slate-900">
          <table className="min-w-full divide-y divide-slate-800">
            <thead className="bg-slate-950">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-400 uppercase">Company Name</th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-400 uppercase">Industry</th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-400 uppercase">Headquarters</th>
                <th className="px-6 py-3 text-right text-xs font-semibold text-slate-400 uppercase">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800 bg-slate-900">
              {companies.map((c) => (
                <tr key={c.id}>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="font-bold text-slate-200">{c.name}</div>
                    <div className="text-xs text-slate-500">{c.website}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-400">{c.industry || 'N/A'}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-400">{c.headquarters || 'N/A'}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm">
                    <button onClick={() => onDeleteCompany(c.id)} className="text-red-400 hover:text-red-300 font-semibold inline-flex items-center gap-1 hover:underline">
                      <Trash2 className="w-3.5 h-3.5" /> Remove Profile
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;
