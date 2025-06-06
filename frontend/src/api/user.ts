import api from "../api/axios.ts"
import {API_ENDPOINTS} from "../constants/apiEndPoints.ts";
import {AxiosResponse} from "axios";
import {User} from "../types/application.tsx";

export const getAllUsers = async (): Promise<User[]> => {// Log the request URL
  const response: AxiosResponse<User[]> = await api.get(API_ENDPOINTS.USERS.GET_ALL);
  console.log("Get all users response:", response); // Log the response data
  return response.data;
}

export const getUserById = async (userId: string): Promise<User> => {
  const response: AxiosResponse<User> = await api.get(`${API_ENDPOINTS.USERS.GET_BY_ID}/${userId}`);
  console.log("Get user by ID response:", response.data); // Log the response data
  return response.data;
}