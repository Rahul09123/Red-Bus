// AdminHomePage.jsx
import { useNavigate } from "react-router-dom";
import "./AdminHomePage.css";

export default function AdminHomePage() {
  const navigate = useNavigate();

  return (
    <div className="adminHome withBusBg">
      <main className="content">
        <div className="card">
          <div className="hero">
            <h1 className="title">Admin Panel</h1>
            <p className="subtitle">
              Review and approve pending Company and Admin applications from here.
            </p>
          </div>

          <div className="quickGrid">
            <button
              className="quickCard"
              type="button"
              onClick={() => navigate("/admin/confirmcompanies")}
            >
              <div className="quickTop">
                <div className="quickTitle">Company Confirmation</div>
                <div className="pill">Review</div>
              </div>
              <div className="quickDesc">
                View pending company registrations and approve or reject them.
              </div>
            </button>

            <button
              className="quickCard"
              type="button"
              onClick={() => navigate("/admin/confirmadmins")}
            >
              <div className="quickTop">
                <div className="quickTitle">Admin Confirmation</div>
                <div className="pill">Review</div>
              </div>
              <div className="quickDesc">
                View pending admin applications and approve or reject them.
              </div>
            </button>
          </div>
        </div>
      </main>
    </div>
  );
}
