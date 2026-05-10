import { useMemo, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./LoginPage.css";

export default function LoginPage() {
  const navigate = useNavigate();

  const [form, setForm] = useState({ email: "", password: "" });
  const [touched, setTouched] = useState({ email: false, password: false });
  const [showPassword, setShowPassword] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [serverError, setServerError] = useState("");

  const errors = useMemo(() => {
    const e = {};
    const email = form.email.trim();
    const password = form.password;

    if (!email) e.email = "Email is required.";
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email))
      e.email = "Please enter a valid email address.";

    if (!password) e.password = "Password is required.";
    else if (password.length < 6)
      e.password = "Password must be at least 6 characters.";

    return e;
  }, [form]);

  const canSubmit = Object.keys(errors).length === 0 && !isSubmitting;

  function onChange(e) {
    const { name, value } = e.target;
    setForm((p) => ({ ...p, [name]: value }));
    if (serverError) setServerError("");
  }

  function onBlur(e) {
    const { name } = e.target;
    setTouched((p) => ({ ...p, [name]: true }));
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
    setTouched({ email: true, password: true });
    if (!canSubmit) return;

    setIsSubmitting(true);
    setServerError("");

    try {
      // Step 1: Validate credentials via member-service
      const credResponse = await fetch("/api/auth/checkCredentials", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({
          email: form.email.trim(),
          password: form.password,
        }),
      });

      const credData = await safeReadMessageDTO(credResponse);

      if (!credResponse.ok) {
        setServerError(credData?.message || "Invalid email or password.");
        return;
      }

      const { userId, userType } = credData;

      if (!userId || !userType) {
        setServerError("Invalid credentials.");
        return;
      }

      // Step 2: Create session via security-service
      const sessionResponse = await fetch("/api/auth/createSession", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({
          cookie: {},
          userId: userId,
          userType: userType,
        }),
      });

      const sessionData = await safeReadMessageDTO(sessionResponse);
      const backendMessage = sessionData?.message;

      if (!sessionResponse.ok) {
        setServerError(backendMessage || "Login failed. Please try again.");
        return;
      }

      if (backendMessage) console.log("LOGIN SUCCESS:", backendMessage);

      navigate("/", { replace: true });
    } catch {
      setServerError("Unable to reach the server.");
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div className="loginPage">
      <div className="loginCard">
        <header className="header">
          <h1 className="title">
            Red<span>Bus</span>
          </h1>
          <p className="subtitle">
            Sign in to start your journey
          </p>
        </header>

        {serverError && <div className="alert">{serverError}</div>}

        <form className="form" onSubmit={onSubmit} noValidate>
          <div className="field">
            <label>Email Address</label>
            <input
              name="email"
              type="email"
              placeholder="example@email.com"
              value={form.email}
              onChange={onChange}
              onBlur={onBlur}
              className={touched.email && errors.email ? "input error" : "input"}
            />
            {touched.email && errors.email && (
              <span className="errorText">{errors.email}</span>
            )}
          </div>

          <div className="field passwordField">
            <label>Password</label>
            <div className="passwordInput">
              <input
                name="password"
                type={showPassword ? "text" : "password"}
                placeholder="********"
                value={form.password}
                onChange={onChange}
                onBlur={onBlur}
                className={
                  touched.password && errors.password ? "input error" : "input"
                }
              />
              <button
                type="button"
                className="togglePassword"
                onClick={() => setShowPassword((p) => !p)}
                aria-label={showPassword ? "Hide password" : "Show password"}
              >
                {showPassword ? "Hide" : "Show"}
              </button>
            </div>
            {touched.password && errors.password && (
              <span className="errorText">{errors.password}</span>
            )}
          </div>

          <button className="primaryButton" disabled={!canSubmit}>
            {isSubmitting ? "Signing in..." : "Sign In"}
          </button>

          <p className="footerText">
            Don’t have an account?{" "}
            <Link to="/register" className="link">
              Sign Up Now
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
}
