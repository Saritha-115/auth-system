import { Navigate } from "react-router-dom";
import { useAuth } from "../context/useAuth";

export default function ProtectedRoute({ children }) {
  const { user, loading } = useAuth();

  if (loading) {
    return (
      <div
        style={{
          minHeight: "100vh",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          background: "#0f1117",
          color: "#6366f1",
          fontSize: "2rem",
        }}
      >
        Loading...
      </div>
    );
  }

  return user ? children : <Navigate to="/login" replace />;
}
