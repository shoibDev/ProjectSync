import {LoginFormData, LoginResponse} from "../types/auth";

export const login = async (request : LoginFormData): Promise<LoginResponse> => {
  console.log(request)
  try {
    const response = await fetch('http://localhost:8080/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    });
    const data: LoginResponse = await response.json();
    console.log("Login response:", data); // Log the response data
    return data;
  } catch (error) {
    throw error;
  }
};