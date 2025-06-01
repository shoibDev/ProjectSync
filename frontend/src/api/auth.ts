import api from "./axios.ts";
import {LoginFormData} from "../types/auth";

export const login = async (request : LoginFormData): Promise<void> => {
  try {
    const response = await api.post('http://localhost:8080/auth/login', { request });
    console.log("Login response:", response.data); // Log the response data
    return response.data;
  } catch (error) {
    throw error;
  }
};