import { NavLink, Link } from "react-router-dom";
import "./Header.css";

export default function Header({ brandTo = "/login" }) {
  return (
    <header className="headerBar">
      <Link to={brandTo} className="headerBrandLink" aria-label="RedBus home">
        <div className="headerBrand">
          Red<span>Bus</span>
        </div>
      </Link>

      <nav className="headerNav">
        <NavLink
          to="/login"
          className={({ isActive }) => (isActive ? "headerLink active" : "headerLink")}
        >
          Login
        </NavLink>
        <NavLink
          to="/register"
          className={({ isActive }) => (isActive ? "headerLink active" : "headerLink")}
        >
          Sign Up
        </NavLink>
      </nav>
    </header>
  );
}
