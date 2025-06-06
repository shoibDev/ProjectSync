import { LoginResponse } from "./auth";
export type AuthContextType = {
  isAuthenticated: boolean;
  login: (request : LoginFormData) => Promise<LoginResponse>;
  logout: () => void;
};


export type SignUpFormData = {

}

export type LoginFormData = {
  email: string;
  password: string;
};

export type LoginResponse = {
  token: string;
  role?: "user" | "admin";
}