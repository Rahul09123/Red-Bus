// src/pages/admin/AdminConfirmPage.jsx
import { useEffect, useState } from "react";
import "./AdminConfirmPage.css";
import { NavLink } from "react-router-dom";

export default function AdminConfirmPage() {
  const [admins, setAdmins] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // TODO: login entegrasyonunda burası localStorage / session’dan alınacak
  const adminId = 1;

  useEffect(() => {
    fetchUnverifiedAdmins();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const fetchUnverifiedAdmins = async () => {
    try {
      setLoading(true);
      setError("");

      const res = await fetch("/api/verification/get/unverified/admins", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({  }),
      });

      const data = await res.json();

      if (!res.ok) {
        setError(data?.message || "Failed to fetch admins.");
        setAdmins([]);
        return;
      }

      setAdmins(data?.admins || []);
    } catch {
      setError("Server connection error.");
      setAdmins([]);
    } finally {
      setLoading(false);
    }
  };

  const handleVerify = async (adminId) => {
    try {
      const res = await fetch("http://localhost:3000/api/verification/verify/admin", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({
          candidateAdminId: adminId
        }),
      });

      const data = await res.json();

      if (!res.ok) {
        alert(data?.message || "Verification failed.");
        return;
      }

      // başarıyla onaylananı listeden düş
      setAdmins((prev) => prev.filter((a) => a.id !== adminId));
      alert("Admin successfully verified!");


    } catch {
      alert("Server error during verification.");
    }
  };

  return (
    <div className="adminConfirmPage">
      <div className="adminConfirmCard">
        <h1 className="title">Admin Verification</h1>
        <p className="subtitle">Review and approve administrator applications.</p>

        {loading && <p className="info">Loading admins...</p>}
        {error && <p className="error">{error}</p>}

        {!loading && !error && admins.length === 0 && (
          <p className="info">No admins awaiting verification.</p>
        )}

        {!loading && admins.length > 0 && (
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Full Name</th>
                <th>Email</th>
                <th>Status</th>
                <th className="actionCol">Action</th>
              </tr>
            </thead>
            <tbody className="textColor">
              {admins.map((admin) => (
                <tr key={admin.id}>
                  <td>{admin.id}</td>
                  <td>{admin.name} {admin.surname}</td>
                  <td>{admin.email}</td>
                  <td>
                    <span className={`badge ${admin.isVerified ? "ok" : "pending"}`}>
                      {admin.isVerified ? "Verified" : "Pending"}
                    </span>
                  </td>
                  <td className="actionCol">
                    <button
                      className="approveBtn"
                      type="button"
                      onClick={() => handleVerify(admin.id)}
                    >
                      Approve
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}

        <div className="footerRow">
          <button className="refreshBtn" type="button" onClick={fetchUnverifiedAdmins}>
            Refresh List
          </button>
        </div>
      </div>
    </div>
  );
}
