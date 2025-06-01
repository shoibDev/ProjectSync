import api from "./axios.ts";
import {API_ENDPOINTS} from "../constants/apiEndPoints.ts";
import {AxiosResponse} from "axios";
import {Project} from "../types/application";

export const getAllProjects = async () => {
  const response: AxiosResponse<Project[]> = await api.get(API_ENDPOINTS.PROJECTS.GET_ALL);
  console.log("Get all projects response:", response.data); // Log the response data
  return response.data;
};