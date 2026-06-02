import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/useAuth";
import "./Dashboard.css";

export default function DashboardPage() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <div className="dashboard-logo">⬡ AuthSystem</div>
        <div className="dashboard-user">
          <span className="user-badge">
            {user?.username?.[0]?.toUpperCase()}
          </span>
          <span>{user?.username}</span>
          <button onClick={handleLogout} className="logout-btn">
            Sign out
          </button>
        </div>
      </header>

      <main className="dashboard-main">
        <div className="welcome-card">
          <h1>Welcome back, {user?.username}! 👋</h1>
          <p>You are successfully authenticated.</p>
        </div>

        <div className="info-grid">
          <div className="info-card">
            <div className="info-label">User ID</div>
            <div className="info-value">#{user?.userId}</div>
          </div>
          <div className="info-card">
            <div className="info-label">Username</div>
            <div className="info-value">{user?.username}</div>
          </div>
          <div className="info-card">
            <div className="info-label">Email</div>
            <div className="info-value">{user?.email}</div>
          </div>
          <div className="info-card">
            <div className="info-label">Role</div>
            <div className="info-value">
              <span className="role-badge">{user?.role}</span>
            </div>
          </div>
        </div>

        <div className="jwt-card">
          <div className="jwt-label">JWT Token</div>
          <code className="jwt-value">
            {localStorage.getItem("token")?.substring(0, 60)}...
          </code>
        </div>
      </main>
    </div>
  );
}
