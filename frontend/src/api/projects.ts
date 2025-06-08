import api from "./axios.ts";
import {API_ENDPOINTS} from "../constants/apiEndPoints.ts";
import {AxiosResponse} from "axios";
import {Project} from "../types/application";
import {CreateProjectFormData} from "../types/project";

export const getAllProjects = async () => {
  const response: AxiosResponse<Project[]> = await api.get(API_ENDPOINTS.PROJECTS.GET_ALL);
  console.log("Get all projects response:", response.data); // Log the response data
  return response.data;
};

export const createProject = async (data: CreateProjectFormData): Promise<Project> => {
  const response: AxiosResponse<Project> = await api.post(API_ENDPOINTS.PROJECTS.CREATE, data);
  console.log("Create project response:", response.data); // Log the response data
  return response.data;
}

export const getProjectById = async (projectId: string): Promise<Project> => {
  const response: AxiosResponse<Project> = await api.get(API_ENDPOINTS.PROJECTS.GET_BY_ID(projectId));
  console.log("Get project by ID response:", response.data); // Log the response data
  return response.data;
}

export const assignUserToProject = async (projectId: string, userId: string): Promise<void> => {
  const response: AxiosResponse<void> = await api.post(API_ENDPOINTS.PROJECTS.ADD_ACCOUNT(projectId, userId));
  console.log("Assign user to project response:", response.data); // Log the response data
  return response.data;
}
