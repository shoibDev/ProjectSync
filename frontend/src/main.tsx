import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import '../src/styles/globals.css'
import {RouterProvider} from "react-router-dom";
import router from "./routes/AppRoutes.tsx";

import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import {AuthProvider} from "./contexts/AuthContext.tsx";
import {HeaderProvider} from "./contexts/HeaderContext.tsx";

createRoot(document.getElementById('root')!).render(
    <StrictMode>
      <HeaderProvider>
        <AuthProvider>
          <RouterProvider router={router} />
          <ToastContainer />
        </AuthProvider>
      </HeaderProvider>
    </StrictMode>
);
