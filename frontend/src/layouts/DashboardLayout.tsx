import { PageHeaderProvider } from "../contexts/PageHeaderContext.tsx";
import { Outlet } from "react-router-dom";
import Sidebar from "../components/Sidebar";
import Header from "../components/Header";

export default function DashboardLayout() {
  return (
      <PageHeaderProvider>
        <div className="grid min-h-screen grid-cols-[16rem_1fr] grid-rows-[4rem_1fr] bg-gradient-to-br from-gray-950 via-gray-900 to-black text-white">
          {/* Sidebar (column 1, spans 2 rows) */}
          <div className="row-span-2 bg-white/5 backdrop-blur-xl border-r border-white/10">
            <Sidebar />
          </div>

          {/* Header (column 2, row 1) */}
          <div className="col-start-2 row-start-1">
            <Header />
          </div>

          {/* Main content (column 2, row 2) */}
          <main className="col-start-2 row-start-2 p-6 overflow-y-auto bg-white/5 backdrop-blur-md rounded-tl-xl border-t border-white/10 shadow-inner">
            <Outlet />
          </main>
        </div>
      </PageHeaderProvider>
  );
}
