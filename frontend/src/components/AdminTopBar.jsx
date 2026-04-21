import { NavLink, useNavigate } from "react-router-dom";
import "./AdminTopBar.css";

export default function AdminTopBar() {
  const navigate = useNavigate();

const handleLogout = async () => {
  try {
    const response = await fetch("/api/auth/logout", {
      method: "POST",
      credentials: "include", // 🔥 session cookie gider
    });

    const data = await response.json().catch(() => null);

    if (!response.ok) {
      console.error("Logout failed:", data?.message || response.statusText);
      return;
    }

    console.log("LOGOUT SUCCESS:", data?.message || "Logged out");
  } catch (err) {
    console.error("Unable to reach the server.", err);
  } finally {
    // frontend temizliği (opsiyonel)
    localStorage.clear();
    navigate("/login", { replace: true });
  }
};


  const linkClass = ({ isActive }) =>
    `navLink ${isActive ? "active" : ""}`;

  return (
    <header className="adminTopBar">
      <NavLink to="/admin" className="brand">
        Red<span>Bus</span>
      </NavLink>

      <nav className="nav">
        <NavLink to="/admin/confirmcompanies" className={linkClass}>
          Company Confirm
        </NavLink>

        <NavLink to="/admin/confirmadmins" className={linkClass}>
          Admin Confirm
        </NavLink>

        <button className="logoutBtn" onClick={handleLogout}>
          Logout
        </button>
      </nav>
    </header>
  );
}
