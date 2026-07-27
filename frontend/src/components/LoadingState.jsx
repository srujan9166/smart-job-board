import { Loader2 } from 'lucide-react';

const LoadingState = ({ label = 'Loading content…', className = 'py-24' }) => (
  <div className={`flex flex-col items-center justify-center gap-3 ${className}`} role="status" aria-live="polite">
    <Loader2 className="h-9 w-9 animate-spin text-purple-400" aria-hidden="true" />
    <span className="text-sm text-slate-400">{label}</span>
  </div>
);

export default LoadingState;
