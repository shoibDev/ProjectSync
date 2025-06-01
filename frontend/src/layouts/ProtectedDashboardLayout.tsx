import DashboardLayout from "./DashboardLayout";
import { Outlet } from "react-router-dom";

export default function ProtectedDashboardLayout() {
  return (
      <DashboardLayout>
        <Outlet />
      </DashboardLayout>
  );
}