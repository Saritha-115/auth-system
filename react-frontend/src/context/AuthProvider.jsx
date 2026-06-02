import { useState, useEffect, useCallback } from "react";
import { AuthContext } from "./AuthContext";
import { authApi } from "../services/api";

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  const logout = useCallback(() => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    setUser(null);
  }, []);

  useEffect(() => {
    const token = localStorage.getItem("token");
    const savedUser = localStorage.getItem("user");
    let cancelled = false;

    if (token && savedUser) {
      authApi
        .validate(token)
        .then(({ data }) => {
          if (cancelled) return;
          if (data.valid) {
            setUser(JSON.parse(savedUser));
          } else {
            logout();
          }
        })
        .catch(() => {
          if (!cancelled) logout();
        })
        .finally(() => {
          if (!cancelled) setLoading(false);
        });
    } else {
      const timer = setTimeout(() => {
        if (!cancelled) setLoading(false);
      }, 0);
      return () => clearTimeout(timer);
    }

    return () => {
      cancelled = true;
    };
  }, [logout]);

  const login = async (username, password) => {
    const { data } = await authApi.login({ username, password });
    localStorage.setItem("token", data.token);
    localStorage.setItem("user", JSON.stringify(data));
    setUser(data);
    return data;
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
}
