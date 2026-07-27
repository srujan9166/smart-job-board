import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8084';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request Interceptor: Attach JWT Token if present
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response Interceptor: Handle Authentication/Authorization failures globally
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response ? error.response.status : null;

    if (status === 401 || status === 403) {
      // Clear credentials if token expires or becomes invalid
      localStorage.removeItem('accessToken');
      localStorage.removeItem('user');
      
      // Notify authentication context (by dispatching a custom event)
      window.dispatchEvent(new Event('auth-expired'));
    }

    return Promise.reject(error);
  }
);

export default api;
