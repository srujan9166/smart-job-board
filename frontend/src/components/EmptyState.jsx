import React from 'react';
import { Inbox } from 'lucide-react';

/**
 * Reusable empty list/query results component mapping icons,
 * title warnings, description text, and optional click triggers.
 */
const EmptyState = ({ title, description, actionText, onActionClick, icon: Icon = Inbox }) => {
  return (
    <div className="bg-slate-900 border border-slate-800 p-12 text-center rounded-3xl max-w-md mx-auto space-y-5">
      <div className="flex justify-center">
        <div className="p-4 bg-slate-950 rounded-2xl text-slate-500 border border-slate-800">
          <Icon className="w-10 h-10 text-purple-400" />
        </div>
      </div>
      <div className="space-y-1.5">
        <h4 className="text-lg font-bold text-slate-200">{title}</h4>
        <p className="text-slate-500 text-xs sm:text-sm leading-relaxed">{description}</p>
      </div>
      {actionText && onActionClick && (
        <button
          onClick={onActionClick}
          className="bg-purple-600 hover:bg-purple-500 text-white font-medium text-xs px-5 py-2.5 rounded-xl transition-all shadow-md shadow-purple-900/30"
        >
          {actionText}
        </button>
      )}
    </div>
  );
};

export default EmptyState;
