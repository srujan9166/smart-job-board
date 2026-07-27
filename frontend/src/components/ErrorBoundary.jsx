import React from 'react';
import ErrorState from './ErrorState';

class ErrorBoundary extends React.Component {
  state = { hasError: false };
  static getDerivedStateFromError() { return { hasError: true }; }
  componentDidCatch(error) { console.error('Unhandled application error', error); }
  render() {
    return this.state.hasError
      ? <ErrorState className="mx-auto mt-12 max-w-xl" message="Something unexpected went wrong. Refresh the page to continue." onRetry={() => window.location.reload()} />
      : this.props.children;
  }
}

export default ErrorBoundary;
