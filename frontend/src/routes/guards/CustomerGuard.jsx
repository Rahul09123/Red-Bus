import { useEffect, useState } from "react";
import { Outlet, useLocation, useNavigate } from "react-router-dom";

export default function CustomerGuard() {
  const navigate = useNavigate();
  const location = useLocation();

  const [isChecking, setIsChecking] = useState(true);
  const [isAllowed, setIsAllowed] = useState(false);

  useEffect(() => {
    let cancelled = false;

    async function run() {
      try {
        const res = await fetch("/api/auth/session/check/customer", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
          body: JSON.stringify({}), // backend body istemese de {} güvenli
        });

        if (cancelled) return;

        if (res.ok) {
          setIsAllowed(true);
        } else {
          setIsAllowed(false);
          navigate("/login", {
            replace: true,
            state: { from: location.pathname },
          });
        }
      } catch {
        if (cancelled) return;

        setIsAllowed(false);
        navigate("/login", {
          replace: true,
          state: { from: location.pathname },
        });
      } finally {
        if (!cancelled) setIsChecking(false);
      }
    }

    run();
    return () => {
      cancelled = true;
    };
  }, [navigate, location.pathname]);

  if (isChecking) {
    return <div style={{ padding: 24 }}>Checking session...</div>;
  }

  if (!isAllowed) {
    // navigate zaten çalıştı; burada boş dönmek yeterli
    return null;
  }

  return <Outlet />;
}
