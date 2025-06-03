import { ReactNode } from "react";
import { useHeader } from "../contexts/HeaderContext";
import './Dashboard.css'
import {Link} from "react-router-dom";
import {useAuth} from "../hooks/useAuth.ts";

interface DashboardLayoutProps {
  children: ReactNode;
}

const DashboardLayout = ({ children }: DashboardLayoutProps) => {
  const {title} = useHeader();
  const {logout} = useAuth();

  return (
      <div className="dashboard-layout">
        <header className="dashboard-header">
          {title}
        </header>
        <aside className="dashboard-sidebar">
          <nav className="dashboard-nav">
            <ul>
              <li><Link to="/">Dashboard</Link></li>
              <li><Link to="/tickets">Tickets</Link></li>
              <li>
                <button onClick={logout} className="logout-button">
                  Logout
                </button>
              </li>
            </ul>
          </nav>
        </aside>
        <main className="dashboard-content">
          {children}
        </main>
      </div>
  );
};

export default DashboardLayout;
