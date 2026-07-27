import React from 'react';

/**
 * Pulse Skeleton component mocking the visual footprint of a Job Card.
 */
const JobCardSkeleton = () => {
  return (
    <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl animate-pulse space-y-4">
      <div className="flex justify-between items-center">
        <div className="h-6 w-20 bg-slate-800 rounded-full"></div>
        <div className="h-4 w-24 bg-slate-800 rounded"></div>
      </div>
      <div className="h-6 w-3/4 bg-slate-800 rounded"></div>
      <div className="h-4 w-1/2 bg-slate-800 rounded"></div>
      <div className="flex gap-4 pt-2">
        <div className="h-4 w-20 bg-slate-800 rounded"></div>
        <div className="h-4 w-20 bg-slate-800 rounded"></div>
        <div className="h-4 w-20 bg-slate-800 rounded"></div>
      </div>
      <div className="pt-4 border-t border-slate-800 flex justify-between items-center">
        <div className="flex gap-2">
          <div className="h-5 w-12 bg-slate-800 rounded"></div>
          <div className="h-5 w-12 bg-slate-800 rounded"></div>
        </div>
        <div className="h-4 w-16 bg-slate-800 rounded"></div>
      </div>
    </div>
  );
};

export default JobCardSkeleton;
