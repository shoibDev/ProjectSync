import { CreateProjectFormData } from "../types/project";

export const validateCreateProject = (data: CreateProjectFormData): string[] => {
  const errors: string[] = [];

  if (!data.name) {
    errors.push("Project name is required.");
  } else if (data.name.length < 3) {
    errors.push("Project name must be at least 3 characters long.");
  } else if (data.name.length > 50) {
    errors.push("Project name cannot exceed 50 characters.");
  }

  if (!data.description) {
    errors.push("Project description is required.");
  } else if (data.description.length < 10) {
    errors.push("Project description must be at least 10 characters long.");
  } else if (data.description.length > 500) {
    errors.push("Project description cannot exceed 500 characters.");
  }

  return errors;
}