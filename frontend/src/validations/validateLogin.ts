import { LoginFormData} from "../types/auth";

export const validateLogin = (data: LoginFormData): string[] => {
  const errors: string[] = [];

    if (!data.email) {
      errors.push("Email is required.");
    } else if (!/\S+@\S+\.\S+/.test(data.email)) {
      errors.push("Email is invalid.");
    }

    if (!data.password) {
      errors.push("Password is required.");
    } else if (data.password.length < 6) {
      errors.push("Password must be at least 6 characters long.");
    }

    return errors;
}