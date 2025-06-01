import React from "react";
import { useNavigate } from "react-router-dom";

import { LoginFormData } from "../../types/auth";
import "./Login.css";
import { showError } from "../../utils/toast";
import { validateLogin } from "../../validations/validateLogin";
import {useAuth} from "../../hooks/useAuth.ts";

const Login: React.FC = () => {
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const formData = new FormData(event.target as HTMLFormElement);
    const rawData = Object.fromEntries(formData.entries());

    const data: LoginFormData = {
      email: rawData.email as string,
      password: rawData.password as string,
    };

    const errors = validateLogin(data);
    if (errors.length > 0) {
      showError(errors[0]);
      return;
    }

    try {
      await login(data.email, data.password);
      navigate("/");
    } catch (err: any) {
      showError(err.response?.data?.message || "Login failed");
    }
  };

  return (
      <main className="login-container">
        <form className="form-card" onSubmit={handleSubmit}>
          <h1 className="form-title">Welcome back</h1>
          <p className="form-subtitle">Please enter your details to sign in</p>

          <div className="form-group">
            <label htmlFor="email">Your Email Address</label>
            <input
                id="email"
                name="email"
                type="email"
                placeholder="Your Email Address"
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
                id="password"
                name="password"
                type="password"
                placeholder="***********"
            />
          </div>

          <div className="form-options">
            <a href="/forgot-password" className="link">
              Forgot password?
            </a>
          </div>

          <button type="submit" className="primary-btn">
            Sign in
          </button>

          <p className="form-footer">
            Don’t have an account?{" "}
            <a href="/signup" className="link">
              Sign up
            </a>
          </p>
        </form>
      </main>
  );
};

export default Login;
