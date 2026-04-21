// src/pages/registerAdmin/AdminRegister.jsx
import { useState } from "react";
import { Link } from "react-router-dom";
import "./AdminRegister.css";

function isValidEmail(email) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function isValidPassword(pw) {
  return pw.length >= 8 && /[A-Za-z]/.test(pw) && /\d/.test(pw);
}

function isValidName(name) {
  const t = name.trim();
  if (t.length < 2) return false;
  return /^[A-Za-zÇĞİÖŞÜçğıöşü\s]+$/.test(t);
}

export default function AdminRegister() {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
  });

  const [errors, setErrors] = useState({});
  const [submitted, setSubmitted] = useState(false);

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [serverError, setServerError] = useState("");
  const [serverSuccess, setServerSuccess] = useState("");

  function validate() {
    const e = {};

    if (!form.firstName) e.firstName = "First name is required.";
    else if (!isValidName(form.firstName))
      e.firstName = "At least 2 characters, letters only.";

    if (!form.lastName) e.lastName = "Last name is required.";
    else if (!isValidName(form.lastName))
      e.lastName = "At least 2 characters, letters only.";

    if (!form.email) e.email = "E-mail is required.";
    else if (!isValidEmail(form.email))
      e.email = "Please enter a valid e-mail address.";

    if (!form.password) e.password = "Password is required.";
    else if (!isValidPassword(form.password))
      e.password =
        "Password must be at least 8 characters and include letters and numbers.";

    setErrors(e);
    return Object.keys(e).length === 0;
  }

  async function safeReadMessageDTO(response) {
    try {
      const data = await response.json();
      return data ?? null;
    } catch {
      return null;
    }
  }

  async function onSubmit(e) {
    e.preventDefault();
    if (!validate()) return;

    setIsSubmitting(true);
    setServerError("");
    setServerSuccess("");

    try {
      const response = await fetch("/api/auth/register/admin", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({
          // ✅ AdminRegistrationDTO fields
          name: form.firstName.trim(),
          surname: form.lastName.trim(),
          email: form.email.trim(),
          password: form.password,
        }),
      });

      const data = await safeReadMessageDTO(response);
      const backendMessage = data?.message;

      if (!response.ok) {
        // ✅ Always show backend error message
        setServerError(backendMessage || "");
        return;
      }

      // ✅ Success message also comes from backend
      setServerSuccess(backendMessage || "");
      setSubmitted(true);
    } catch {
      // Network/CORS/etc: backend not reachable
      setServerError("Unable to reach the server.");
    } finally {
      setIsSubmitting(false);
    }
  }

  if (submitted) {
    return (
      <div className="adminRegPage withBusBg">
        <div className="adminRegCard">
          <h2 className="infoTitle">Application Received</h2>

          {serverSuccess && (
            <div className="alert alertOk" role="status" aria-live="polite">
              {serverSuccess}
            </div>
          )}

          <p className="infoText">
            Your admin application is currently in{" "}
            <strong>Pending Approval</strong> status.
            <br />
            You will be notified after the review is completed.
          </p>

          <Link to="/login" className="primaryButton">
            Back to Login
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="adminRegPage withBusBg">
      <div className="adminRegCard">
        <header className="header">
          <h1 className="title">
            Admin <span>Sign Up</span>
          </h1>
          <p className="subtitle">Enter your admin details</p>
        </header>

        {serverError && (
          <div className="alert alertError" role="alert" aria-live="polite">
            {serverError}
          </div>
        )}

        <form className="form" onSubmit={onSubmit} noValidate>
          <div className="grid">
            <div className="field">
              <label>First Name</label>
              <input
                className="input"
                value={form.firstName}
                onChange={(e) =>
                  setForm({ ...form, firstName: e.target.value })
                }
                placeholder="e.g. John"
              />
              {errors.firstName && (
                <div className="error">{errors.firstName}</div>
              )}
            </div>

            <div className="field">
              <label>Last Name</label>
              <input
                className="input"
                value={form.lastName}
                onChange={(e) =>
                  setForm({ ...form, lastName: e.target.value })
                }
                placeholder="e.g. Smith"
              />
              {errors.lastName && (
                <div className="error">{errors.lastName}</div>
              )}
            </div>
          </div>

          <div className="field">
            <label>E-mail</label>
            <input
              className="input"
              value={form.email}
              onChange={(e) => setForm({ ...form, email: e.target.value })}
              placeholder="example@email.com"
            />
            {errors.email && <div className="error">{errors.email}</div>}
          </div>

          <div className="field">
            <label>Password</label>
            <input
              type="password"
              className="input"
              value={form.password}
              onChange={(e) =>
                setForm({ ...form, password: e.target.value })
              }
              placeholder="At least 8 characters (letters and numbers)"
            />
            {errors.password && (
              <div className="error">{errors.password}</div>
            )}
          </div>

          <button className="primaryButton" type="submit" disabled={isSubmitting}>
            {isSubmitting ? "Submitting..." : "Submit Admin Application"}
          </button>

          <p className="footerText">
            Want to cancel?{" "}
            <Link className="link" to="/register">
              Go back
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
}
