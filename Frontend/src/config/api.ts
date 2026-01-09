export const API_BASE_URL = 'http://localhost:8080/api';
export const ADMIN_TOKEN = 'admin-secure-token-2024-finpath';

export const getAuthHeaders = () => {
  const token = localStorage.getItem('adminToken') || ADMIN_TOKEN;
  return {
    'Content-Type': 'application/json',
    'X-Admin-Token': token,
  };
};
