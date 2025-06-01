export const API_BASE_URL = "http://localhost:8080";
export const API_ENDPOINTS = {
  PROJECTS: {
    GET_ALL: `${API_BASE_URL}/projects`,
    GET_BY_ID: (id: string) => `${API_BASE_URL}/projects/${id}`,
    CREATE: `${API_BASE_URL}/projects`,
    UPDATE: (id: string) => `${API_BASE_URL}/projects/${id}`,
    DELETE: (id: string) => `${API_BASE_URL}/projects/${id}`,
  },
  TICKETS: {
    GET_ALL: `${API_BASE_URL}/tickets`,
    GET_BY_ID: (id: string) => `${API_BASE_URL}/tickets/${id}`,
    CREATE: `${API_BASE_URL}/tickets`,
    UPDATE: (id: string) => `${API_BASE_URL}/tickets/${id}`,
    DELETE: (id: string) => `${API_BASE_URL}/tickets/${id}`,
  }
};