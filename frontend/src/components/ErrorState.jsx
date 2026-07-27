import { AlertCircle, RefreshCw } from 'lucide-react';

const ErrorState = ({ message = 'We could not load this content. Please try again.', onRetry, className = '' }) => (
  <div className={`rounded-2xl border border-red-500/30 bg-red-500/10 p-6 text-center ${className}`} role="alert">
    <AlertCircle className="mx-auto mb-3 h-8 w-8 text-red-400" aria-hidden="true" />
    <p className="text-sm text-red-200">{message}</p>
    {onRetry && <button type="button" onClick={onRetry} className="mt-4 inline-flex items-center gap-2 rounded-lg border border-red-400/40 px-3 py-2 text-sm font-medium text-red-200 transition-colors hover:bg-red-500/10"><RefreshCw className="h-4 w-4" aria-hidden="true" /> Try again</button>}
  </div>
);

export default ErrorState;
