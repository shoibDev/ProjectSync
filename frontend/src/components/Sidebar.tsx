import { ROUTE_PATHS } from "../constants/routePaths.ts";
import { NavLink } from "react-router-dom";

export default function Sidebar() {
  return (
      <aside className="h-full w-full bg-white/5 backdrop-blur-xl border-r border-white/10 p-6 space-y-6 text-white">
        {/* App Name / Logo */}
        <h2 className="text-2xl font-bold tracking-wide text-blue-400 mb-4 select-none">
          ProjectSync
        </h2>

        {/* Navigation Links */}
        <nav className="space-y-2">
          <NavLink
              to={ROUTE_PATHS.ROOT}
              className={({ isActive }) =>
                  `block px-3 py-2 rounded-md text-sm transition-all ${
                      isActive
                          ? "bg-blue-500 text-white font-semibold shadow-md"
                          : "text-gray-300 hover:text-blue-400 hover:bg-white/10"
                  }`
              }
          >
            Home
          </NavLink>

          <NavLink
              to="/logout"
              className="absolute bottom-6 left-6 right-6 flex justify-center items-center px-3 py-2 rounded-md text-sm font-medium text-red-400 transition-all
              hover:text-red-500 hover:bg-white/10
              hover:shadow-[0_0_10px_rgba(239,68,68,0.3)]"
          >
            Logout
          </NavLink>
        </nav>
      </aside>
  );
}
