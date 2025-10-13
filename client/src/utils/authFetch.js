// authFetch.js
export const authFetch = async (url, options = {}) => {
  const token = localStorage.getItem('token');
  
  const headers = {
    ...(options.headers || {}),
    'Authorization': token ? `Bearer ${token}` : '',
    'Content-Type': 'application/json',
  };

  const config = {
    ...options,
    headers,
  };

  return fetch(url, config);
};
