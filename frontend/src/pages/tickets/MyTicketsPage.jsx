// src/pages/customer/MyTicketsPage.jsx
import { useEffect, useMemo, useState } from "react";
import "./MyTicketsPage.css";

const API_PATH = "/api/ticket/get/customer"; // API Gateway route (Postman'de çalışan)

async function safeJson(res) {
  const text = await res.text();
  try {
    return text ? JSON.parse(text) : null;
  } catch {
    return { message: text || "Unexpected response." };
  }
}

function combineDateTime(date, time) {
  if (!date && !time) return null;

  // date: "2025-12-26", time: "17:45"
  // ISO'ya çevirmeye çalışalım
  if (date && time) return new Date(`${date}T${time}:00`);
  if (date) return new Date(`${date}T00:00:00`);
  return null;
}

function isPastTicket(t) {
  const d = combineDateTime(t?.date, t?.time);
  if (!d || Number.isNaN(d.getTime())) return false;
  return d.getTime() < Date.now();
}

export default function MyTicketsPage() {
  const [tickets, setTickets] = useState([]);
  const [query, setQuery] = useState("");
  const [statusFilter, setStatusFilter] = useState("all"); // all | upcoming | past
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const fetchTickets = async () => {
    try {
      setLoading(true);
      setError("");

      const res = await fetch(API_PATH, {
        method: "POST", // backend @PostMapping
        headers: { "Content-Type": "application/json" },
        credentials: "include", // session cookie şart
        body: JSON.stringify({}), // Postman'de {} gönderiyorsun, aynısı
      });

      const data = await safeJson(res);

      if (!res.ok) {
        setTickets([]);
        setError(data?.message || "Failed to load tickets.");
        return;
      }

      const list = Array.isArray(data?.ticketsDTO) ? data.ticketsDTO : [];
      setTickets(list);
    } catch (e) {
      console.error(e);
      setTickets([]);
      setError("Unable to reach the server.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTickets();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const filteredTickets = useMemo(() => {
    const q = query.trim().toLowerCase();

    return tickets
      .filter((t) => {
        const company = String(t?.companyName ?? "").toLowerCase();
        const from = String(t?.departureCity ?? "").toLowerCase();
        const to = String(t?.arrivalCity ?? "").toLowerCase();
        const pnr = String(t?.pnr ?? "").toLowerCase();

        const matchesQuery =
          !q ||
          company.includes(q) ||
          from.includes(q) ||
          to.includes(q) ||
          pnr.includes(q);

        if (!matchesQuery) return false;

        if (statusFilter === "all") return true;

        const past = isPastTicket(t);
        if (statusFilter === "upcoming") return !past;
        if (statusFilter === "past") return past;

        return true;
      })
      .sort((a, b) => {
        // Yakın tarihler öne gelsin
        const da = combineDateTime(a?.date, a?.time);
        const db = combineDateTime(b?.date, b?.time);
        const ta = da && !Number.isNaN(da.getTime()) ? da.getTime() : 0;
        const tb = db && !Number.isNaN(db.getTime()) ? db.getTime() : 0;
        return ta - tb;
      });
  }, [tickets, query, statusFilter]);

  return (
    <div className="myTicketsPage withBusBg">
      <div className="myTicketsCard">
        <div className="headerRow">
          <div>
            <h1 className="title">My Tickets</h1>
            <p className="subtitle">View and manage your purchased tickets.</p>
          </div>

          <button
            className="refreshBtn"
            type="button"
            onClick={fetchTickets}
            disabled={loading}
          >
            {loading ? "Refreshing..." : "Refresh"}
          </button>
        </div>

        <div className="controls">
          <div className="searchWrap">
            <label className="label" htmlFor="q">
              Search
            </label>
            <input
              id="q"
              className="input"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              placeholder="Search by PNR, company, or route..."
            />
          </div>

          <div className="filterWrap">
            <label className="label" htmlFor="status">
              Filter
            </label>
            <select
              id="status"
              className="select"
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
            >
              <option value="all">All</option>
              <option value="upcoming">Upcoming</option>
              <option value="past">Past</option>
            </select>
          </div>
        </div>

        {error && <p className="error">{error}</p>}

        {!error && loading && <p className="info">Loading tickets...</p>}

        {!loading && !error && filteredTickets.length === 0 && (
          <div className="empty">
            <div className="emptyTitle">No tickets found</div>
            <div className="emptyText">
              Try changing the filter or search query.
            </div>
          </div>
        )}

        {!loading && !error && filteredTickets.length > 0 && (
          <div className="tableWrap">
            <table className="table">
              <thead>
                <tr>
                  <th>PNR</th>
                  <th>Company</th>
                  <th>Route</th>
                  <th>Date</th>
                  <th>Time</th>
                  <th className="right">Seat</th>
                  <th className="right">Duration</th>
                </tr>
              </thead>
              <tbody>
                {filteredTickets.map((t, idx) => (
                  <tr key={`${t?.pnr ?? "pnr"}-${idx}`}>
                    <td className="mono">{t?.pnr ?? "—"}</td>
                    <td>{t?.companyName ?? "—"}</td>
                    <td>
                      {t?.departureCity ?? "—"} → {t?.arrivalCity ?? "—"}
                    </td>
                    <td>{t?.date ?? "—"}</td>
                    <td>{t?.time ?? "—"}</td>
                    <td className="right">{t?.seatNo ?? "—"}</td>
                    <td className="right">{t?.duration ?? "—"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        <p className="hint">
          Note: This page uses your session cookie (credentials: include). If
          you are logged out, the server will return an error.
        </p>
      </div>
    </div>
  );
}
