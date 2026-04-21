import { Link } from "react-router-dom";
import CompanyHeader from "../../components/Header/CompanyHeader";
import "./CompanyHome.css";

export default function CompanyHome() {
  return (
    <>
      {/* COMPANY HEADER */}
      <CompanyHeader />

      {/* PAGE CONTENT */}
      <div className="companyHome">
        <div className="companyCard">
          <header className="header">
            <h1 className="title">
              Company <span>Panel</span>
            </h1>
            <p className="subtitle">
              Manage expeditions: create new trips or view existing ones
            </p>
          </header>

          <div className="actionsGrid">
            <Link to="/company/expeditions/create" className="actionBox">
              <div className="actionTitle">Create Expedition</div>
              <div className="actionDesc">
                Publish a new expedition with route, datetime, price and capacity.
              </div>
              <div className="actionCta">Go</div>
            </Link>

            <Link to="/company/expeditions" className="actionBox">
              <div className="actionTitle">View Expeditions</div>
              <div className="actionDesc">
                Browse all expeditions or filter only active ones and open details.
              </div>
              <div className="actionCta">Go</div>
            </Link>
          </div>
        </div>
      </div>
    </>
  );
}
