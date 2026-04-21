import { useEffect, useMemo, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import CompanyHeader from "../../components/Header/CompanyHeader";
import "./CompanyExpeditionList.css";

const MODE = {
  ALL: "ALL",
  AVAILABLE: "AVAILABLE",
};

function fmtMoney(v) {
  if (v == null || Number.isNaN(Number(v))) return "";
  return Number(v).toFixed(2);
}

export default function CompanyExpeditionList() {
  const navigate = useNavigate();

  const [mode, setMode] = useState(MODE.ALL);
  const [items, setItems] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  // hata mesajını sadece backend'den basacağız
  const [serverError, setServerError] = useState("");

  async function safeReadJson(response) {
    try {
      const data = await response.json();
      return data ?? null;
    } catch {
      return null;
    }
  }

  async function loadExpeditions(nextMode) {
    setIsLoading(true);
    setServerError("");

    let response = null;

    try {
      if (nextMode === MODE.AVAILABLE) {
        response = await fetch("/api/expedition/company/get/futures", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
          body: JSON.stringify({}),
        });
      } else if (nextMode === MODE.ALL) {
        response = await fetch("/api/expedition/company/get/all", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
          body: JSON.stringify({}),
        });
      } else {
        setItems([]);
        setServerError("Unknown mode");
        return;
      }

      const data = await safeReadJson(response);
      const backendMessage = data?.message ?? "";
      const list = Array.isArray(data?.expeditions) ? data.expeditions : [];

      if (!response.ok) {
        setServerError(backendMessage || `HTTP ${response.status}`);
        setItems([]);
        return;
      }

      setItems(list);
    } catch (err) {
      setServerError(err?.message || "Network/JS error");
      setItems([]);
    } finally {
      setIsLoading(false);
    }
  }


  useEffect(() => {
    loadExpeditions(mode);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [mode]);

  const titleText = useMemo(() => {
    if (mode === MODE.ALL) return "All Expeditions";
    return "Future Expeditions";
  }, [mode]);

  return (
    <>
      {/* COMPANY HEADER */}
      <CompanyHeader />

      <div className="companyListPage">
        <div className="companyListCard">
          <header className="header">
            <h1 className="title">
              Expeditions <span>List</span>
            </h1>
            <p className="subtitle">
              Choose a list type and browse your expeditions
            </p>
          </header>

          <div className="segmentRow">
            <button
              type="button"
              className={mode === MODE.AVAILABLE ? "segment active" : "segment"}
              onClick={() => setMode(MODE.AVAILABLE)}
              title="Coming soon (API is not connected yet)"
            >
              Future Expeditions
              <span className="pill">Soon</span>
            </button>

            <button
              type="button"
              className={mode === MODE.ALL ? "segment active" : "segment"}
              onClick={() => setMode(MODE.ALL)}
            >
              All Expeditions
            </button>
          </div>

          {serverError && <div className="alert">{serverError}</div>}

          <div className="listHeader">
            <div className="listTitle">{titleText}</div>
            <div className="listHint">
              {isLoading ? "Loading..." : `${items.length} expedition(s)`}
            </div>
          </div>

          <div className="listWrap">
            {isLoading ? (
              <div className="emptyBox">Loading expeditions...</div>
            ) : items.length === 0 ? (
              <div className="emptyBox">
                No expeditions found for this list.
              </div>
            ) : (
              items.map((x) => {
                const remaining = Math.max(
                  0,
                  x.capacity - x.numberOfBookedSeats
                );
                const occupancy =
                  x.capacity > 0
                    ? Math.round(
                        (x.numberOfBookedSeats / x.capacity) * 100
                      )
                    : 0;

                return (
                  <button
                    key={x.expeditionId}
                    type="button"
                    className="rowCard"
                    onClick={() =>
                      navigate(`/company/expeditions/${x.expeditionId}`)
                    }
                  >
                    <div className="rowTop">
                      <div className="rowRoute">
                        <span className="routeCity">{x.departureCity}</span>
                        <span className="routeArrow">→</span>
                        <span className="routeCity">{x.arrivalCity}</span>
                      </div>

                      <div className="rightTop">
                        <div className="idBadge">
                          ID #{x.expeditionId}
                        </div>
                      </div>
                    </div>

                    <div className="rowMetaWide">
                      <div className="metaItem">
                        <div className="metaLabel">Date</div>
                        <div className="metaValue">{x.date}</div>
                      </div>

                      <div className="metaItem">
                        <div className="metaLabel">Time</div>
                        <div className="metaValue">{x.time}</div>
                      </div>

                      <div className="metaItem">
                        <div className="metaLabel">Duration</div>
                        <div className="metaValue">{x.duration} min</div>
                      </div>

                      <div className="metaItem">
                        <div className="metaLabel">Capacity</div>
                        <div className="metaValue">{x.capacity}</div>
                      </div>

                      <div className="metaItem">
                        <div className="metaLabel">Booked</div>
                        <div className="metaValue">
                          {x.numberOfBookedSeats}{" "}
                          <span className="muted">
                            ({occupancy}%)
                          </span>
                        </div>
                      </div>

                      <div className="metaItem">
                        <div className="metaLabel">Remaining</div>
                        <div className="metaValue">{remaining}</div>
                      </div>

                      <div className="metaItem">
                        <div className="metaLabel">Price</div>
                        <div className="metaValue">
                          {fmtMoney(x.price)}
                        </div>
                      </div>

                      <div className="metaItem profitItem">
                        <div className="metaLabel">Profit</div>
                        <div className="metaValue profitValue">
                          {fmtMoney(x.profit)}
                        </div>
                      </div>
                    </div>

                    <div className="rowCta">Open details</div>
                  </button>
                );
              })
            )}
          </div>

          <p className="footerText">
            Back to{" "}
            <Link to="/company" className="link">
              Company Home
            </Link>
          </p>
        </div>
      </div>
    </>
  );
}
