import api from "./auth.ts";

import {API_ENDPOINTS} from "../constants/apiEndPoints.ts";

import {AxiosResponse} from "axios";

import {User} from "../types/application.tsx";

export const getUserById = async (userId: string): Promise<User> => {
  const response: AxiosResponse<User> = await api.get(`${API_ENDPOINTS.USERS.GET_BY_ID}/${userId}`);
  console.log("Get user by ID response:", response.data); // Log the response data
  return response.data;
}