import {ROUTE_PATHS} from "../../constants/routePaths.ts";
import { useNavigate } from "react-router-dom";
import "./NotFound.css";

const NotFound = () => {
  const navigate = useNavigate();

  return (
      <div className="notfound-wrapper">
        <div className="notfound-container">
          <h1 className="notfound-title">404</h1>
          <p className="notfound-subtitle">Page Not Found</p>
          <p className="notfound-description">
            The page you're looking for doesn't exist.
          </p>
          <button
              onClick={() => navigate(ROUTE_PATHS.ROOT)}
              className="notfound-button"
          >
            Go Home
          </button>
        </div>
      </div>
  );
};

export default NotFound;
