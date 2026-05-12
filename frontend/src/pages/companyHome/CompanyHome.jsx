import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import CompanyHeader from "../../components/Header/CompanyHeader";
import "./CompanyHome.css";

export default function CompanyHome() {
  const [stats, setStats] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    async function fetchStats() {
      try {
        const response = await fetch("/api/view/company/stats", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ companyId: 0 }), // Gateway/Session usually injects this or we use 0 as dummy
          credentials: "include"
        });
        if (response.ok) {
          const data = await response.json();
          setStats(data.stats);
        }
      } catch (error) {
        console.error("Failed to fetch stats", error);
      } finally {
        setIsLoading(false);
      }
    }
    fetchStats();
  }, []);

  const fmtCurrency = (v) => "₹" + Number(v || 0).toFixed(2);

  return (
    <>
      <CompanyHeader />

      <div className="companyHome">
        <div className="companyCard">
          <header className="header">
            <h1 className="title">
              Operator <span>Dashboard</span>
            </h1>
            <p className="subtitle">
              Monitor your business performance and manage trips in real-time
            </p>
          </header>

          {/* STATS GRID */}
          <div className="statsGrid">
            <div className="statCard">
              <div className="statValue">{isLoading ? "..." : stats?.totalExpeditions}</div>
              <div className="statLabel">Total Trips</div>
            </div>
            <div className="statCard">
              <div className="statValue">{isLoading ? "..." : stats?.activeExpeditions}</div>
              <div className="statLabel">Active Now</div>
            </div>
            <div className="statCard highlight">
              <div className="statValue">{isLoading ? "..." : stats?.totalBookedSeats}</div>
              <div className="statLabel">Tickets Sold</div>
            </div>
            <div className="statCard success">
              <div className="statValue">{isLoading ? "..." : fmtCurrency(stats?.totalProfit)}</div>
              <div className="statLabel">Net Revenue</div>
            </div>
          </div>

          <div className="actionsGrid">
            <Link to="/company/expeditions/create" className="actionBox">
              <div className="actionTitle">Create Expedition</div>
              <div className="actionDesc">
                Publish a new expedition with route, datetime, and capacity.
              </div>
              <div className="actionCta">New Trip</div>
            </Link>

            <Link to="/company/expeditions" className="actionBox">
              <div className="actionTitle">Manage Fleet</div>
              <div className="actionDesc">
                Browse active expeditions, view passenger lists, and check profit.
              </div>
              <div className="actionCta">View List</div>
            </Link>
          </div>
        </div>
      </div>
    </>
  );
}
