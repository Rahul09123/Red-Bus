import { useEffect, useState } from "react";
import { Outlet, useLocation, useNavigate } from "react-router-dom";

async function postCheck(url) {
  // Returns true if response.ok, else false
  try {
    const res = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify({}),
    });
    return !!res.ok;
  } catch {
    return false;
  }
}

export default function AuthGuard() {
  const navigate = useNavigate();
  const location = useLocation();

  const [isChecking, setIsChecking] = useState(true);

  useEffect(() => {
    let cancelled = false;

    async function run() {
      // 1) general check
      const generalOk = await postCheck("/api/auth/session/check");

      if (cancelled) return;

      // generalOk değilse: auth sayfalarına izin ver
      if (!generalOk) {
        setIsChecking(false);
        return;
      }

      // generalOk ise: role chain
      const isAdmin = await postCheck("/api/auth/session/check/admin");
      if (cancelled) return;
      if (isAdmin) {
        navigate("/admin", { replace: true });
        return;
      }

      const isCompany = await postCheck("/api/auth/session/check/company");
      if (cancelled) return;
      if (isCompany) {
        navigate("/company", { replace: true });
        return;
      }

      const isCustomer = await postCheck("/api/auth/session/check/customer");
      if (cancelled) return;
      if (isCustomer) {
        navigate("/", { replace: true });
        return;
      }

      // hiçbiri değilse: "sıçtık" senaryosu :)
      // auth sayfasını yine de gösterelim (kullanıcı tekrar login olabilir)
      setIsChecking(false);
    }

    run();

    return () => {
      cancelled = true;
    };
  }, [navigate]);

  if (isChecking) {
    return <div style={{ padding: 24 }}>Checking session...</div>;
  }

  // checkler izin verdiyse auth sayfalarını göster
  return <Outlet />;
}
