import React, { createContext, useState, useContext, useCallback } from 'react';
import { CheckCircle, AlertCircle, Info, X } from 'lucide-react';

const ToastContext = createContext(null);

/**
 * ToastProvider coordinates slide-in overlay messages
 * for system operations feedback (successes, validation warnings, errors).
 */
export const ToastProvider = ({ children }) => {
  const [toasts, setToasts] = useState([]);

  const addToast = useCallback((message, type = 'success', duration = 4000) => {
    const id = Math.random().toString(36).substring(2, 9);
    setToasts((prev) => [...prev, { id, message, type }]);

    setTimeout(() => {
      removeToast(id);
    }, duration);
  }, []);

  const removeToast = useCallback((id) => {
    setToasts((prev) => prev.filter((t) => t.id !== id));
  }, []);

  return (
    <ToastContext.Provider value={{ showToast: addToast }}>
      {children}
      {/* Toast Alert Render Container */}
      <div className="fixed bottom-5 right-5 z-50 flex flex-col gap-3 max-w-sm w-full px-4 sm:px-0">
        {toasts.map((t) => (
          <div
            key={t.id}
            className={`flex items-start gap-3 p-4 rounded-xl shadow-2xl border transition-all duration-300 ${
              t.type === 'success'
                ? 'bg-slate-900 border-emerald-500/30 text-emerald-400'
                : t.type === 'error'
                ? 'bg-slate-900 border-red-500/30 text-red-400'
                : 'bg-slate-900 border-slate-800 text-slate-350'
            }`}
          >
            {t.type === 'success' && <CheckCircle className="w-5 h-5 flex-shrink-0 text-emerald-400" />}
            {t.type === 'error' && <AlertCircle className="w-5 h-5 flex-shrink-0 text-red-400" />}
            {t.type === 'info' && <Info className="w-5 h-5 flex-shrink-0 text-purple-400" />}
            
            <div className="flex-grow text-xs font-semibold leading-relaxed pr-2">
              {t.message}
            </div>

            <button
              onClick={() => removeToast(t.id)}
              className="text-slate-500 hover:text-white flex-shrink-0 p-0.5 rounded-md"
            >
              <X className="w-4 h-4" />
            </button>
          </div>
        ))}
      </div>
    </ToastContext.Provider>
  );
};

export const useToast = () => {
  const context = useContext(ToastContext);
  if (!context) {
    throw new Error('useToast must be used within a ToastProvider');
  }
  return context;
};
