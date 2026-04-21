// RegisterPage.jsx
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./RegisterPage.css";

export default function RegisterPage() {
  const navigate = useNavigate();
  const [selected, setSelected] = useState(null); // "customer" | "company" | "admin"

  function onContinue() {
    if (!selected) return;

    if (selected === "customer") navigate("/register/customer");
    if (selected === "company") navigate("/register/company");
    if (selected === "admin") navigate("/register/admin"); // admin has a separate flow
  }

  function onCancel() {
    navigate("/login");
  }

  return (
    <div className="registerPage">
      <div className="registerCard">
        <header className="header">
          <h1 className="title">
            Shu<span>Bilet</span>
          </h1>
          <p className="subtitle">Choose your registration type</p>
        </header>

        <div
          className="options"
          role="radiogroup"
          aria-label="User type selection"
        >
          <button
            type="button"
            className={`option ${selected === "customer" ? "active" : ""}`}
            onClick={() => setSelected("customer")}
            role="radio"
            aria-checked={selected === "customer"}
          >
            <div className="optionTop">
              <span className="badge">Customer</span>
              <span className="pill">Buy tickets</span>
            </div>
            <p className="optionDesc">
              Search for trips, choose seats, purchase tickets, and manage your
              reservations.
            </p>
          </button>

          <button
            type="button"
            className={`option ${selected === "company" ? "active" : ""}`}
            onClick={() => setSelected("company")}
            role="radio"
            aria-checked={selected === "company"}
          >
            <div className="optionTop">
              <span className="badge">Company</span>
              <span className="pill">Publish trips</span>
            </div>
            <p className="optionDesc">
              Create trips, manage pricing, track sales, and view reports.
            </p>
          </button>

          <button
            type="button"
            className={`option ${selected === "admin" ? "active" : ""}`}
            onClick={() => setSelected("admin")}
            role="radio"
            aria-checked={selected === "admin"}
          >
            <div className="optionTop">
              <span className="badge">Admin</span>
              <span className="pill">System management</span>
            </div>
            <p className="optionDesc">
              Manage users and companies, and control system settings.
            </p>
          </button>
        </div>

        <div className="actions">
          <button
            className="primaryButton"
            type="button"
            disabled={!selected}
            onClick={onContinue}
          >
            Continue
          </button>

          <button className="ghostButton" type="button" onClick={onCancel}>
            Cancel
          </button>

          <p className="footerText">
            Already have an account?{" "}
            <Link className="link" to="/login">
              Log In
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}
