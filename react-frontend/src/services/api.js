import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { "Content-Type": "application/json" },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => {
    console.log("API SUCCESS:", response.config.url, response.status);
    return response;
  },
  (error) => {
    console.log("API ERROR:", error.config?.url, error.response?.status);
    return Promise.reject(error);
  },
);

export const authApi = {
  login: (credentials) => api.post("/auth/login", credentials),
  validate: (token) => api.post("/auth/validate", { token }),
};

export const userApi = {
  register: (data) => api.post("/users/register", data),
  getUser: (id) => api.get(`/users/${id}`),
  updateUser: (id, data) => api.put(`/users/${id}`, data),
  deleteUser: (id) => api.delete(`/users/${id}`),
};

export default api;
