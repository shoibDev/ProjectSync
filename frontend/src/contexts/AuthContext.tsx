import React, { createContext, useEffect, useState } from "react";
import { LoginFormData } from "../types/auth";
import { AuthContextType } from "../types/auth";

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
      const res = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ request }),
      });

      if (!res.ok) throw new Error("Login failed");

      const data = await res.json();
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
