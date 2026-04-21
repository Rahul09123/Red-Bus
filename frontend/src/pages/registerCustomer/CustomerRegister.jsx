// CustomerRegister.jsx
import { useMemo, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./CustomerRegister.css";

const GENDERS = [
  { value: "", label: "Select" },
  { value: "Male", label: "Male" },
  { value: "Female", label: "Female" },
];

function isValidEmail(email) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function isValidPassword(pw) {
  // Minimum 8 characters + must contain letters and numbers
  const hasLetter = /[A-Za-z]/.test(pw);
  const hasNumber = /\d/.test(pw);
  return pw.length >= 8 && hasLetter && hasNumber;
}

function isValidName(name) {
  // Minimum 2 characters, letters and spaces only (Turkish chars allowed)
  const trimmed = name.trim();
  if (trimmed.length < 2) return false;
  return /^[A-Za-zÇĞİÖŞÜçğıöşü\s]+$/.test(trimmed);
}

export default function CustomerRegister() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    gender: "",
  });

  const [touched, setTouched] = useState({
    firstName: false,
    lastName: false,
    email: false,
    password: false,
    gender: false,
  });

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [serverError, setServerError] = useState("");
  const [serverSuccess, setServerSuccess] = useState("");

  const errors = useMemo(() => {
    const e = {};

    if (!form.firstName.trim()) e.firstName = "First name is required.";
    else if (!isValidName(form.firstName))
      e.firstName =
        "Please enter a valid first name (at least 2 letters, letters only).";

    if (!form.lastName.trim()) e.lastName = "Last name is required.";
    else if (!isValidName(form.lastName))
      e.lastName =
        "Please enter a valid last name (at least 2 letters, letters only).";

    if (!form.email.trim()) e.email = "E-mail is required.";
    else if (!isValidEmail(form.email.trim()))
      e.email = "Please enter a valid e-mail address.";

    if (!form.password) e.password = "Password is required.";
    else if (!isValidPassword(form.password))
      e.password =
        "Invalid password format (minimum 8 characters, letters and numbers).";

    if (!form.gender) e.gender = "Gender is required.";

    return e;
  }, [form]);

  const canSubmit = Object.keys(errors).length === 0 && !isSubmitting;

  function onChange(e) {
    const { name, value } = e.target;
    setForm((p) => ({ ...p, [name]: value }));
    if (serverError) setServerError("");
    if (serverSuccess) setServerSuccess("");
  }

  function onBlur(e) {
    const { name } = e.target;
    setTouched((p) => ({ ...p, [name]: true }));
  }

  function markAllTouched() {
    setTouched({
      firstName: true,
      lastName: true,
      email: true,
      password: true,
      gender: true,
    });
  }

  async function safeReadMessageDTO(response) {
    // Backend always returns MessageDTO, but be safe for empty/invalid bodies
    try {
      const data = await response.json();
      return data ?? null;
    } catch {
      return null;
    }
  }

  async function onSubmit(e) {
    e.preventDefault();
    markAllTouched();
    if (!canSubmit) return;

    setIsSubmitting(true);
    setServerError("");
    setServerSuccess("");

    try {
      const response = await fetch("/api/auth/register/customer", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({
          name: form.firstName.trim(),
          surname: form.lastName.trim(),
          gender: form.gender,
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
      setServerSuccess(backendMessage || ""); // if empty, show nothing
      // Redirect after success (optional delay)
      setTimeout(() => navigate("/login", { replace: true }), 500);
    } catch {
      // Network/CORS/etc: backend not reachable, no backend message possible
      setServerError("Unable to reach the server.");
    } finally {
      setIsSubmitting(false);
    }
  }

  function onCancel() {
    navigate("/register");
  }

  return (
    <div className="customerRegPage">
      <div className="customerRegCard">
        <header className="header">
          <h1 className="title">
            Customer <span>Sign Up</span>
          </h1>
          <p className="subtitle">Create your account by entering your details.</p>
        </header>

        {serverError && (
          <div className="alert alertError" role="alert" aria-live="polite">
            {serverError}
          </div>
        )}

        {serverSuccess && (
          <div className="alert alertOk" role="status" aria-live="polite">
            {serverSuccess}
          </div>
        )}

        <form className="form" onSubmit={onSubmit} noValidate>
          <div className="grid">
            <div className="field">
              <label className="label" htmlFor="firstName">
                First Name
              </label>
              <input
                id="firstName"
                name="firstName"
                className={`input ${touched.firstName && errors.firstName ? "inputError" : ""}`}
                value={form.firstName}
                onChange={onChange}
                onBlur={onBlur}
                placeholder="e.g. John"
                autoComplete="given-name"
              />
              {touched.firstName && errors.firstName && (
                <div className="error">{errors.firstName}</div>
              )}
            </div>

            <div className="field">
              <label className="label" htmlFor="lastName">
                Last Name
              </label>
              <input
                id="lastName"
                name="lastName"
                className={`input ${touched.lastName && errors.lastName ? "inputError" : ""}`}
                value={form.lastName}
                onChange={onChange}
                onBlur={onBlur}
                placeholder="e.g. Smith"
                autoComplete="family-name"
              />
              {touched.lastName && errors.lastName && (
                <div className="error">{errors.lastName}</div>
              )}
            </div>
          </div>

          <div className="field">
            <label className="label" htmlFor="email">
              E-mail
            </label>
            <input
              id="email"
              name="email"
              type="email"
              className={`input ${touched.email && errors.email ? "inputError" : ""}`}
              value={form.email}
              onChange={onChange}
              onBlur={onBlur}
              placeholder="example@email.com"
              autoComplete="email"
            />
            {touched.email && errors.email && (
              <div className="error">{errors.email}</div>
            )}
          </div>

          <div className="field">
            <label className="label" htmlFor="password">
              Password
            </label>
            <input
              id="password"
              name="password"
              type="password"
              className={`input ${touched.password && errors.password ? "inputError" : ""}`}
              value={form.password}
              onChange={onChange}
              onBlur={onBlur}
              placeholder="At least 8 characters (letters and numbers)"
              autoComplete="new-password"
            />
            {touched.password && errors.password && (
              <div className="error">{errors.password}</div>
            )}
          </div>

          <div className="field">
            <label className="label" htmlFor="gender">
              Gender
            </label>
            <select
              id="gender"
              name="gender"
              className={`select ${touched.gender && errors.gender ? "inputError" : ""}`}
              value={form.gender}
              onChange={onChange}
              onBlur={onBlur}
            >
              {GENDERS.map((g) => (
                <option key={g.value} value={g.value}>
                  {g.label}
                </option>
              ))}
            </select>
            {touched.gender && errors.gender && (
              <div className="error">{errors.gender}</div>
            )}
          </div>

          <button className="primaryButton" type="submit" disabled={!canSubmit}>
            {isSubmitting ? "Creating account..." : "Create Account"}
          </button>

          <div className="bottomRow">
            <button type="button" className="ghostButton" onClick={onCancel}>
              Cancel
            </button>

            <span className="muted">
              Already have an account?{" "}
              <Link className="link" to="/login">
                Log In
              </Link>
            </span>
          </div>
        </form>
      </div>
    </div>
  );
}
