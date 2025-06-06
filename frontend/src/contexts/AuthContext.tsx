import React, { createContext, useEffect, useState } from "react";
import {LoginFormData, LoginResponse} from "../types/auth";
import { AuthContextType } from "../types/auth";
import {login as LoginRequest} from "../api/auth.ts"

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [token, setToken] = useState<string | null>(localStorage.getItem("token"));
  const [loading, setLoading] = useState(true);

  // When first rendered, check if a token exists in localStorage
  // TODO: Implement a more robust token validation mechanism
  useEffect(() => {
    setLoading(false);
  }, []);

  const login = async (request: LoginFormData) => {
    try {
      const data: LoginResponse = await LoginRequest(request);

      const jwtToken = data.token;
      setToken(jwtToken);
      localStorage.setItem("token", jwtToken);
    } catch (error) {
      console.error("Login error:", error);
      throw error;
    }
  };

  const logout = () => {
    setToken(null);
    localStorage.removeItem("token");
  };

  const isAuthenticated = !!token;

  return (
      <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
        {!loading && children}
      </AuthContext.Provider>
  );
};
