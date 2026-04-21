// src/components/CustomerTopBar.jsx
import { NavLink, useNavigate } from "react-router-dom";
import "./CustomerTopBar.css";

export default function CustomerTopBar() {
  const navigate = useNavigate();

const handleLogout = async () => {
  try {
    const response = await fetch("/api/auth/logout", {
      method: "POST",
      credentials: "include", // 🔥 şart
    });

    const data = await response.json().catch(() => null);

    if (!response.ok) {
      console.error("Logout failed:", data?.message || response.statusText);
      return;
    }

    localStorage.clear();
    navigate("/login", { replace: true });
  } catch (e) {
    console.error("Logout error:", e);
  }
};


  const linkClass = ({ isActive }) =>
    `navLink ${isActive ? "active" : ""}`;

  return (
    <header className="customerTopBar">
      <div className="left">
        <NavLink to="/" className="brand">
          Red<span>Bus</span>
        </NavLink>
      </div>

      <nav className="right">
        <NavLink to="/travel" className={linkClass}>
          Travel
        </NavLink>

        <NavLink to="/my-tickets" className={linkClass}>
          My Tickets
        </NavLink>

        <NavLink to="/profile" className={linkClass}>
          Profile
        </NavLink>

        <button className="logoutBtn" onClick={handleLogout}>
          Logout
        </button>
      </nav>
    </header>
  );
}