// CustomerHomePage.jsx
import { useNavigate } from "react-router-dom";
import "./CustomerHomePage.css";

export default function CustomerHomePage() {
  const navigate = useNavigate();

  return (
    <div className="customerHome withBusBg">
      <main className="content">
        <div className="card">
          <div className="hero">
            <h1 className="title">Welcome 👋</h1>
            <p className="subtitle">
              Search for trips in the Travel section, manage your tickets in My Tickets,
              and update your personal information from Profile.
            </p>
          </div>

          <div className="quickGrid">
            <button
              className="quickCard"
              type="button"
              onClick={() => navigate("/travel")}
            >
              <div className="quickTop">
                <div className="quickTitle">Travel</div>
                <div className="pill">Search trips</div>
              </div>
              <div className="quickDesc">
                Choose departure and arrival locations, select a date, and buy your ticket.
              </div>
            </button>

            <button
              className="quickCard"
              type="button"
              onClick={() => navigate("/my-tickets")}
            >
              <div className="quickTop">
                <div className="quickTitle">My Tickets</div>
                <div className="pill">My tickets</div>
              </div>
              <div className="quickDesc">
                View your active tickets and check trip details.
              </div>
            </button>

            <button
              className="quickCard"
              type="button"
              onClick={() => navigate("/profile")}
            >
              <div className="quickTop">
                <div className="quickTitle">Profile</div>
                <div className="pill">My account</div>
              </div>
              <div className="quickDesc">
                Manage your personal information and account settings.
              </div>
            </button>
          </div>
        </div>
      </main>
    </div>
  );
}
