import {LoginFormData} from "../types/auth";

export const login = async (request : LoginFormData): Promise<void> => {
  try {
    const response = await fetch('http://localhost:8080/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    });
    console.log("Login response:", response.json()); // Log the response data
    return response.json();
  } catch (error) {
    throw error;
  }
};