import { useEffect, useMemo, useState } from "react";
import { Link, useNavigate, useParams, useLocation } from "react-router-dom";
import CompanyHeader from "../../components/Header/CompanyHeader";
import "./CompanyExpeditionDetail.css";

function normalizeStatus(s) {
  return String(s ?? "").trim().toUpperCase();
}

function statusLabel(s) {
  const st = normalizeStatus(s);
  if (st === "BOOKED") return "Booked";
  if (st === "AVAILABLE") return "Available";
  if (st === "RESERVED") return "Reserved";
  if (st === "CANCELLED") return "Cancelled";
  return st || "-";
}

function toValidId(raw) {
  // raw: string | number | undefined
  const n = parseInt(String(raw ?? ""), 10);
  return Number.isFinite(n) && n > 0 ? n : null;
}

export default function CompanyExpeditionDetail() {
  const navigate = useNavigate();
  const params = useParams(); // route: /company/expeditions/:expeditionId (or :id)
  const location = useLocation();

  // ✅ accept multiple sources:
  // 1) /company/expeditions/:expeditionId
  // 2) /company/expeditions/:id  (if route is different)
  // 3) navigate(path, { state: { expeditionId } })
  const expeditionIdRaw =
    params.expeditionId ?? params.id ?? location.state?.expeditionId;

  const expeditionIdNum = useMemo(
    () => toValidId(expeditionIdRaw),
    [expeditionIdRaw]
  );

  const [isLoading, setIsLoading] = useState(false);
  const [serverError, setServerError] = useState("");

  const [message, setMessage] = useState("");
  const [seats, setSeats] = useState([]);

  // UI filters
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [query, setQuery] = useState("");

  async function safeReadJson(response) {
    try {
      const data = await response.json();
      return data ?? null;
    } catch {
      return null;
    }
  }

  async function loadDetail(idNum) {
    setIsLoading(true);
    setServerError("");
    setMessage("");
    setSeats([]);

    // ✅ idNum zaten doğrulanmış gelmeli
    if (!Number.isFinite(idNum) || idNum <= 0) {
      setServerError(
        `Invalid expeditionId in URL/state. Got: "${String(expeditionIdRaw)}"`
      );
      setIsLoading(false);
      return;
    }

    let response = null;

    try {
      response = await fetch("/api/expedition/company/get/detail", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ expeditionId: idNum }),
      });

      const data = await safeReadJson(response);

      const backendMessage = data?.message ?? "";
      const list = Array.isArray(data?.seats) ? data.seats : [];

      if (!response.ok) {
        setServerError(backendMessage || `HTTP ${response.status}`);
        return;
      }

      setMessage(backendMessage);
      setSeats(list);
    } catch (err) {
      setServerError(err?.message || "Network/JS error");
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    if (expeditionIdNum == null) {
      setServerError(
        `Invalid expeditionId in URL/state. Got: "${String(expeditionIdRaw)}"`
      );
      return;
    }
    loadDetail(expeditionIdNum);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [expeditionIdNum]);

  const stats = useMemo(() => {
    const total = seats.length;

    const byStatus = seats.reduce((acc, x) => {
      const st = normalizeStatus(x?.status);
      acc[st] = (acc[st] || 0) + 1;
      return acc;
    }, {});

    const booked = (byStatus["BOOKED"] || 0) + (byStatus["SOLD"] || 0);
    const available = byStatus["AVAILABLE"] || 0;

    return { total, booked, available, byStatus };
  }, [seats]);

  const filteredSeats = useMemo(() => {
    const q = query.trim().toLowerCase();

    return seats
      .filter((x) => {
        const st = normalizeStatus(x?.status);
        if (statusFilter !== "ALL" && st !== statusFilter) return false;

        if (!q) return true;

        const seatNo = String(x?.seatNo ?? "");
        const fullName = String(x?.customerFullName ?? "").toLowerCase();
        const seatId = String(x?.seatId ?? "");
        return seatNo.includes(q) || seatId.includes(q) || fullName.includes(q);
      })
      .sort((a, b) => (a?.seatNo ?? 0) - (b?.seatNo ?? 0));
  }, [seats, statusFilter, query]);

  return (
    <>
      <CompanyHeader />

      <div className="companyDetailPage">
        <div className="companyDetailCard">
          <header className="header">
            <h1 className="title">
              Expedition <span>Detail</span>
            </h1>
            <p className="subtitle">
              Manage seats & passengers for expedition #
              {expeditionIdNum ?? "?"}
            </p>
          </header>

          <div className="topBar">
            <button
              type="button"
              className="backBtn"
              onClick={() => navigate(-1)}
              title="Go back"
            >
              ← Back
            </button>

            <div className="idPill">
              ID #{expeditionIdNum ?? String(expeditionIdRaw ?? "?")}
            </div>
          </div>

          {serverError && <div className="alert">{serverError}</div>}

          {!!message && !serverError && <div className="infoBox">{message}</div>}

          <div className="statsRow">
            <div className="statCard">
              <div className="statLabel">Total Seats</div>
              <div className="statValue">{stats.total}</div>
            </div>
            <div className="statCard">
              <div className="statLabel">Booked</div>
              <div className="statValue">{stats.booked}</div>
            </div>
            <div className="statCard">
              <div className="statLabel">Available</div>
              <div className="statValue">{stats.available}</div>
            </div>
          </div>

          <div className="controlsRow">
            <div className="control">
              <div className="controlLabel">Status</div>
              <select
                className="select"
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
              >
                <option value="ALL">All</option>
                <option value="AVAILABLE">Available</option>
                <option value="BOOKED">Booked</option>
                <option value="RESERVED">Reserved</option>
                <option value="CANCELLED">Cancelled</option>
              </select>
            </div>

            <div className="control grow">
              <div className="controlLabel">Search</div>
              <input
                className="input"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="Seat no, seatId or customer name..."
              />
            </div>

            <button
              type="button"
              className="refreshBtn"
              onClick={() => expeditionIdNum && loadDetail(expeditionIdNum)}
              disabled={isLoading || expeditionIdNum == null}
              title="Refresh data"
            >
              {isLoading ? "Refreshing..." : "Refresh"}
            </button>
          </div>

          <div className="listHeader">
            <div className="listTitle">Seats</div>
            <div className="listHint">
              {isLoading
                ? "Loading..."
                : `${filteredSeats.length} / ${seats.length} seat(s)`}
            </div>
          </div>

          <div className="tableWrap">
            {isLoading ? (
              <div className="emptyBox">Loading expedition detail...</div>
            ) : seats.length === 0 ? (
              <div className="emptyBox">No seats returned from API.</div>
            ) : filteredSeats.length === 0 ? (
              <div className="emptyBox">No seats match your filters.</div>
            ) : (
              <div className="table">
                <div className="thead">
                  <div>Seat No</div>
                  <div>Status</div>
                  <div>Passenger</div>
                  <div className="right">Seat ID</div>
                </div>

                {filteredSeats.map((x) => {
                  const st = normalizeStatus(x?.status);
                  return (
                    <div className="trow" key={x.seatId}>
                      <div className="seatNo">{x.seatNo}</div>

                      <div>
                        <span className={`badge ${st || "UNKNOWN"}`}>
                          {statusLabel(st)}
                        </span>
                      </div>

                      <div className="passenger">
                        {x.customerFullName ? (
                          x.customerFullName
                        ) : (
                          <span className="muted">—</span>
                        )}
                      </div>

                      <div className="right muted">#{x.seatId}</div>
                    </div>
                  );
                })}
              </div>
            )}
          </div>

          <p className="footerText">
            Back to{" "}
            <Link to="/company/expeditions" className="link">
              Expeditions List
            </Link>
          </p>
        </div>
      </div>
    </>
  );
}
