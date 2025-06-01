// src/components/ProtectedRoute.tsx
import { Navigate, Outlet } from "react-router-dom";

import { ROUTE_PATHS } from "../constants/routePaths";
import {useAuth} from "../hooks/useAuth.ts";

export default function ProtectedRoute() {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? <Outlet /> : <Navigate to={ROUTE_PATHS.LOGIN} replace />;
}
