import { Link, useNavigate } from "react-router-dom";
import "./CompanyHeader.css";

export default function CompanyHeader() {
  const navigate = useNavigate();

  async function handleLogout(e) {
    e.preventDefault();
    try {
      await fetch("/api/auth/logout", {
        method: "POST",
        credentials: "include",
      });
    } finally {
      navigate("/login", { replace: true });
    }
  }

  return (
    <header className="headerBar">
      <Link
        to="/company"
        className="headerBrandLink"
        aria-label="RedBus home"
      >
        <div className="headerBrand">
          Red<span>Bus</span>
        </div>
      </Link>

      <nav className="headerNav">
        <button
          type="button"
          className="headerLink"
          onClick={handleLogout}
        >
          Logout
        </button>
      </nav>
    </header>
  );
}
