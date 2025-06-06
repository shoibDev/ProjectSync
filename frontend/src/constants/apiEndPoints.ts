export const API_BASE_URL = "http://localhost:8080";
export const API_ENDPOINTS = {
  USERS: {
    GET_ALL: "/account",
    GET_BY_ID: (id: string) => `${API_BASE_URL}/users/${id}`,
    CREATE: `${API_BASE_URL}/users`,
    UPDATE: (id: string) => `${API_BASE_URL}/users/${id}`,
    DELETE: (id: string) => `${API_BASE_URL}/users/${id}`,
  },
  PROJECTS: {
    GET_ALL: `/projects`,
    GET_BY_ID: (id: string) => `${API_BASE_URL}/projects/${id}`,
    CREATE: `/projects`,
    UPDATE: (id: string) => `${API_BASE_URL}/projects/${id}`,
    DELETE: (id: string) => `${API_BASE_URL}/projects/${id}`,
    ADD_ACCOUNT: (projectId: string, userId: string) => `/projects/${projectId}/assign/${userId}`,
  },
  TICKETS: {
    GET_ALL: `${API_BASE_URL}/tickets`,
    GET_BY_ID: (id: string) => `${API_BASE_URL}/tickets/${id}`,
    CREATE: `${API_BASE_URL}/tickets`,
    UPDATE: (id: string) => `${API_BASE_URL}/tickets/${id}`,
    DELETE: (id: string) => `${API_BASE_URL}/tickets/${id}`,
  }
};