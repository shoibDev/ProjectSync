
export type AuthContextType = {
  isAuthenticated: boolean;
  login: (request : LoginFormData) => Promise<void>;
  logout: () => void;
};


export type SignUpFormData = {

}

export type LoginFormData = {
  email: string;
  password: string;
};