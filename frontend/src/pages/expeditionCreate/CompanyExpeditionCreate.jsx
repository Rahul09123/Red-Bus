import { useMemo, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import CompanyHeader from "../../components/Header/CompanyHeader";
import "./CompanyExpeditionCreate.css";

const CITIES = [
  "Adana", 
  "Adıyaman", 
  "Afyonkarahisar", 
  "Ağrı", 
  "Aksaray", 
  "Amasya", 
  "Ankara", 
  "Antalya", 
  "Ardahan", 
  "Artvin", 
  "Aydın", 
  "Balıkesir", 
  "Bartın", 
  "Batman", 
  "Bayburt", 
  "Bilecik", 
  "Bingöl", 
  "Bitlis", 
  "Bolu", 
  "Burdur", 
  "Bursa", 
  "Çanakkale", 
  "Çankırı", 
  "Çorum", 
  "Denizli", 
  "Diyarbakır", 
  "Düzce", 
  "Edirne", 
  "Elazığ", 
  "Erzincan", 
  "Erzurum", 
  "Eskişehir", 
  "Gaziantep", 
  "Giresun", 
  "Gümüşhane", 
  "Hakkari", 
  "Hatay", 
  "Iğdır", 
  "Isparta", 
  "İstanbul", 
  "İzmir", 
  "Kahramanmaras", 
  "Karabük", 
  "Karaman", 
  "Kars", 
  "Kastamonu", 
  "Kayseri", 
  "Kırıkkale", 
  "Kırklareli", 
  "Kırşehir", 
  "Kilis", 
  "Kocaeli", 
  "Konya", 
  "Kütahya", 
  "Malatya", 
  "Manisa", 
  "Mardin", 
  "Mersin", 
  "Muğla", 
  "Muş", 
  "Nevşehir", 
  "Niğde", 
  "Ordu", 
  "Osmaniye", 
  "Rize", 
  "Sakarya", 
  "Samsun", 
  "Siirt", 
  "Sinop", 
  "Sivas", 
  "Şanlıurfa", 
  "Şırnak", 
  "Tekirdağ", 
  "Tokat", 
  "Trabzon", 
  "Tunceli", 
  "Uşak", 
  "Van", 
  "Yalova", 
  "Yozgat", 
  "Zonguldak"
];

function todayISO() {
  const d = new Date();
  const yyyy = d.getFullYear();
  const mm = String(d.getMonth() + 1).padStart(2, "0");
  const dd = String(d.getDate()).padStart(2, "0");
  return `${yyyy}-${mm}-${dd}`;
}

export default function CompanyExpeditionCreate() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    departureCity: "",
    arrivalCity: "",
    date: "",
    time: "",
    price: "",
    duration: "",
    capacity: "",
  });

  const [touched, setTouched] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const [serverError, setServerError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [createdSnapshot, setCreatedSnapshot] = useState(null);

  const errors = useMemo(() => {
    const e = {};

    if (!form.departureCity) e.departureCity = "Departure city is required.";
    if (!form.arrivalCity) e.arrivalCity = "Arrival city is required.";

    if (
      form.departureCity &&
      form.arrivalCity &&
      form.departureCity === form.arrivalCity
    ) {
      e.arrivalCity = "Arrival city must be different from departure city.";
    }

    if (!form.date) e.date = "Date is required.";
    if (!form.time) e.time = "Time is required.";

    const price = Number(form.price);
    if (form.price === "") e.price = "Price is required.";
    else if (Number.isNaN(price) || price < 0)
      e.price = "Price must be 0 or greater.";

    const duration = Number(form.duration);
    if (form.duration === "") e.duration = "Duration is required.";
    else if (Number.isNaN(duration) || duration <= 0)
      e.duration = "Duration must be greater than 0.";

    const capacity = Number(form.capacity);
    if (form.capacity === "") e.capacity = "Capacity is required.";
    else if (Number.isNaN(capacity) || capacity <= 0)
      e.capacity = "Capacity must be greater than 0.";

    return e;
  }, [form]);

  const canSubmit = Object.keys(errors).length === 0 && !isSubmitting;

  function onChange(e) {
    const { name, value } = e.target;
    setForm((p) => ({ ...p, [name]: value }));
    if (serverError) setServerError("");
    if (successMessage) setSuccessMessage("");
  }

  function onBlur(e) {
    const { name } = e.target;
    setTouched((p) => ({ ...p, [name]: true }));
  }

  async function safeReadMessageDTO(response) {
    try {
      const data = await response.json();
      return data ?? null;
    } catch {
      return null;
    }
  }

  async function onSubmit(e) {
    e.preventDefault();

    setTouched({
      departureCity: true,
      arrivalCity: true,
      date: true,
      time: true,
      price: true,
      duration: true,
      capacity: true,
    });

    if (!canSubmit) return;

    setIsSubmitting(true);
    setServerError("");
    setSuccessMessage("");
    setCreatedSnapshot(null);

    const snapshot = { ...form };

    try {
      const response = await fetch("/api/expedition/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({
          departureCity: form.departureCity,
          arrivalCity: form.arrivalCity,
          date: form.date,
          time: form.time,
          price: Number(form.price),
          duration: Number(form.duration),
          capacity: Number(form.capacity),
        }),
      });

      const data = await safeReadMessageDTO(response);
      const backendMessage = data?.message ?? "";

      if (!response.ok) {
        setServerError(backendMessage);
        return;
      }

      setSuccessMessage(backendMessage);
      setCreatedSnapshot(snapshot);
    } catch {
      setServerError("");
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <>
      <CompanyHeader />

      <div className="companyCreatePage">
        <div className="companyCreateCard">
          <header className="header">
            <h1 className="title">
              Create <span>Expedition</span>
            </h1>
            <p className="subtitle">
              Fill the information below and publish a new expedition
            </p>
          </header>

          {serverError && <div className="alert">{serverError}</div>}

          <form className="form" onSubmit={onSubmit} noValidate>
            <div className="grid2">
              <div className="field">
                <label>Departure City</label>
                <select
                  name="departureCity"
                  value={form.departureCity}
                  onChange={onChange}
                  onBlur={onBlur}
                  className={
                    touched.departureCity && errors.departureCity
                      ? "input error"
                      : "input"
                  }
                >
                  <option value="" disabled>
                    Select a city
                  </option>
                  {CITIES.map((c) => (
                    <option key={c} value={c}>
                      {c}
                    </option>
                  ))}
                </select>
                {touched.departureCity && errors.departureCity && (
                  <span className="errorText">{errors.departureCity}</span>
                )}
              </div>

              <div className="field">
                <label>Arrival City</label>
                <select
                  name="arrivalCity"
                  value={form.arrivalCity}
                  onChange={onChange}
                  onBlur={onBlur}
                  className={
                    touched.arrivalCity && errors.arrivalCity
                      ? "input error"
                      : "input"
                  }
                >
                  <option value="" disabled>
                    Select a city
                  </option>
                  {CITIES.map((c) => (
                    <option key={c} value={c}>
                      {c}
                    </option>
                  ))}
                </select>
                {touched.arrivalCity && errors.arrivalCity && (
                  <span className="errorText">{errors.arrivalCity}</span>
                )}
              </div>
            </div>

            <div className="grid2">
              <div className="field">
                <label>Date</label>
                <input
                  name="date"
                  type="date"
                  min={todayISO()}
                  value={form.date}
                  onChange={onChange}
                  onBlur={onBlur}
                  className={touched.date && errors.date ? "input error" : "input"}
                />
                {touched.date && errors.date && (
                  <span className="errorText">{errors.date}</span>
                )}
              </div>

              <div className="field">
                <label>Time</label>
                <input
                  name="time"
                  type="time"
                  value={form.time}
                  onChange={onChange}
                  onBlur={onBlur}
                  className={touched.time && errors.time ? "input error" : "input"}
                />
                {touched.time && errors.time && (
                  <span className="errorText">{errors.time}</span>
                )}
              </div>
            </div>

            <div className="grid3">
              <div className="field">
                <label>Price</label>
                <input
                  name="price"
                  inputMode="decimal"
                  placeholder="111.00"
                  value={form.price}
                  onChange={onChange}
                  onBlur={onBlur}
                  className={touched.price && errors.price ? "input error" : "input"}
                />
                {touched.price && errors.price && (
                  <span className="errorText">{errors.price}</span>
                )}
              </div>

              <div className="field">
                <label>Duration (min)</label>
                <input
                  name="duration"
                  inputMode="numeric"
                  placeholder="90"
                  value={form.duration}
                  onChange={onChange}
                  onBlur={onBlur}
                  className={touched.duration && errors.duration ? "input error" : "input"}
                />
                {touched.duration && errors.duration && (
                  <span className="errorText">{errors.duration}</span>
                )}
              </div>

              <div className="field">
                <label>Capacity</label>
                <input
                  name="capacity"
                  inputMode="numeric"
                  placeholder="42"
                  value={form.capacity}
                  onChange={onChange}
                  onBlur={onBlur}
                  className={touched.capacity && errors.capacity ? "input error" : "input"}
                />
                {touched.capacity && errors.capacity && (
                  <span className="errorText">{errors.capacity}</span>
                )}
              </div>
            </div>

            <button className="primaryButton" disabled={!canSubmit}>
              {isSubmitting ? "Creating..." : "Create Expedition"}
            </button>

            <p className="footerText">
              Back to{" "}
              <Link to="/company" className="link">
                Company Home
              </Link>
            </p>
          </form>

          {successMessage && createdSnapshot && (
            <div className="createdBox">
              <div className="createdTitle">{successMessage}</div>

              <div className="createdGrid">
                <div className="createdItem">
                  <div className="createdLabel">From</div>
                  <div className="createdValue">{createdSnapshot.departureCity}</div>
                </div>

                <div className="createdItem">
                  <div className="createdLabel">To</div>
                  <div className="createdValue">{createdSnapshot.arrivalCity}</div>
                </div>

                <div className="createdItem">
                  <div className="createdLabel">Date</div>
                  <div className="createdValue">{createdSnapshot.date}</div>
                </div>

                <div className="createdItem">
                  <div className="createdLabel">Time</div>
                  <div className="createdValue">{createdSnapshot.time}</div>
                </div>

                <div className="createdItem">
                  <div className="createdLabel">Price</div>
                  <div className="createdValue">{createdSnapshot.price}</div>
                </div>

                <div className="createdItem">
                  <div className="createdLabel">Duration</div>
                  <div className="createdValue">{createdSnapshot.duration} min</div>
                </div>

                <div className="createdItem">
                  <div className="createdLabel">Capacity</div>
                  <div className="createdValue">{createdSnapshot.capacity}</div>
                </div>
              </div>

              <div
                style={{
                  marginTop: 10,
                  fontSize: 13,
                  color: "#64748B",
                  textAlign: "center",
                }}
              >
                Note: Backend response does not include expedition id, so details link cannot be generated.
              </div>
            </div>
          )}
        </div>
      </div>
    </>
  );
}
