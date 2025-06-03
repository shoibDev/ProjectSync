import {createBrowserRouter} from "react-router-dom";
import {ROUTE_PATHS} from "../constants/routePaths.ts";
import Login from "../pages/auth/Login.tsx";
import Signup from "../pages/auth/Signup.tsx";
import NotFound from "../pages/notfound/NotFound.tsx";
import Home from "../pages/dashboard/Home.tsx";
import ProtectedRoute from "./ProtectedRoute.tsx";
import ProtectedDashboardLayout from "../layouts/ProtectedDashboardLayout.tsx";
import TicketPage from "../pages/dashboard/TicketPage.tsx";
import ProjectTicketPage from "../pages/dashboard/ProjectTicketPage.tsx";


const router = createBrowserRouter([
    {path: ROUTE_PATHS.LOGIN, element: <Login /> },
    {path: ROUTE_PATHS.SIGNUP, element: <Signup /> },
    {
        path: '/',
        element: <ProtectedRoute />,
        children: [
            {
                path: '/',
                element: <ProtectedDashboardLayout />,
                children: [
                    { index: true, element: <Home /> },
                    { path: ROUTE_PATHS.TICKETS, element: <TicketPage /> },
                    { path: ROUTE_PATHS.PROJECTS, element: <ProjectTicketPage /> },

                ]
            }
        ]
    },
    {path: "*", element: <NotFound />},
]);

export default router;
