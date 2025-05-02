import {createBrowserRouter} from "react-router-dom";
import {ROUTE_PATHS} from "../constants/routePaths.ts";
import Login from "../pages/auth/Login.tsx";
import Signup from "../pages/auth/Signup.tsx";
import NotFound from "../pages/NotFound.tsx";
import DashboardLayout from "../layouts/DashboardLayout.tsx";
import ProtectedRoute from "../components/ProtectedRoute.tsx";
import Home from "../pages/dashboard/Home.tsx";


const router = createBrowserRouter([
    {path: ROUTE_PATHS.LOGIN, element: <Login /> },
    {path: ROUTE_PATHS.SIGNUP, element: <Signup /> },
    {
        path: ROUTE_PATHS.ROOT,
        element: (
            <ProtectedRoute>
                <DashboardLayout />
            </ProtectedRoute>
        ),
        children: [
            {index: true, element: <Home />},
        ],
    },
    {path: "*", element: <NotFound />},
]);

export default router;