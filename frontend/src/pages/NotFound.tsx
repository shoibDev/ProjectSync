import {ROUTE_PATHS} from "../constants/routePaths.ts";
import { useNavigate } from "react-router-dom";

const NotFound = () => {
  const navigate = useNavigate();

  return (
      <div className="w-full min-h-screen flex items-center justify-center bg-transparent text-center">
        <div className="bg-white/5 backdrop-blur-md border border-white/10 px-8 py-12 rounded-xl shadow-lg">
          <h1 className="text-4xl font-bold text-blue-400 mb-4">404</h1>
          <p className="text-lg text-white mb-2">Page Not Found</p>
          <p className="text-sm text-gray-400 mb-6">
            The page you're looking for doesn't exist.
          </p>

          <button
              onClick={() => navigate(ROUTE_PATHS.ROOT)}
              className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-2 rounded-md text-sm font-medium transition"
          >
            Go Home
          </button>
        </div>
      </div>
  );
};

export default NotFound;
