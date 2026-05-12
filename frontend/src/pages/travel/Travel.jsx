
import React, { useState, useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./Travel.css";

const CITIES = [
    "Mumbai",
    "Delhi",
    "Bangalore",
    "Hyderabad",
    "Ahmedabad",
    "Chennai",
    "Kolkata",
    "Surat",
    "Pune",
    "Jaipur",
    "Lucknow",
    "Kanpur"
];

// Mocks removed

export default function Travel() {
    const navigate = useNavigate();

    // Search filter states
    const [fromCity, setFromCity] = useState("");
    const [toCity, setToCity] = useState("");


    // Default date: Tomorrow
    const [date, setDate] = useState(() => {
        const d = new Date();
        d.setDate(d.getDate() + 1);
        return d.toISOString().split("T")[0];
    });

    // Custom Date Picker State
    const [showCalendar, setShowCalendar] = useState(false);
    const [calendarViewDate, setCalendarViewDate] = useState(new Date()); // For navigation
    const calendarRef = useRef(null);

    // Data states
    const [expeditions, setExpeditions] = useState([]); // Start empty
    const [isLoading, setIsLoading] = useState(false);
    const [hasSearched, setHasSearched] = useState(false); // To distinguish initial load vs empty search result

    // Inline Seat Selection States
    const [expandedExpeditionId, setExpandedExpeditionId] = useState(null);
    const [seats, setSeats] = useState([]); // Array of {seatNo, status}
    const [selectedSeat, setSelectedSeat] = useState(null); // Single selection
    const [isSeatsLoading, setIsSeatsLoading] = useState(false);

    // Payment Modal State
    const [showPaymentModal, setShowPaymentModal] = useState(false);
    const [isProcessingPayment, setIsProcessingPayment] = useState(false);
    const [savedCards, setSavedCards] = useState([]);
    const [isCardsLoading, setIsCardsLoading] = useState(false);

    // Close calendar when clicking outside
    useEffect(() => {
        function handleClickOutside(event) {
            if (calendarRef.current && !calendarRef.current.contains(event.target)) {
                setShowCalendar(false);
            }
        }
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    // Sync calendar view date with selected date on open
    useEffect(() => {
        if (showCalendar && date) {
            setCalendarViewDate(new Date(date));
        }
    }, [showCalendar, date]);

    // Fetch Cards when Modal Opens
    useEffect(() => {
        if (showPaymentModal) {
            fetchCards();
        }
    }, [showPaymentModal]);

    const fetchCards = async () => {
        setIsCardsLoading(true);
        try {
            const response = await fetch("/api/profile/customer/get/cards", {
                method: "POST"
            });
            if (!response.ok) {
                console.error("Failed to fetch cards");
                return;
            }
            const data = await response.json();
            if (data.cards) {
                setSavedCards(data.cards);
            }
        } catch (error) {
            console.error("Error fetching cards:", error);
        } finally {
            setIsCardsLoading(false);
        }
    };

    const fetchExpeditions = async () => {
        setIsLoading(true);
        setExpeditions([]);
        setHasSearched(true);
        setExpandedExpeditionId(null); // Reset expansion

        try {
            const response = await fetch("/api/expedition/customer/get/search/expeditions", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    departureCity: fromCity,
                    arrivalCity: toCity,
                    date: date
                })
            });

            if (!response.ok) {
                // Handle error (could show a toast or alert)
                console.error("Search failed:", response.statusText);
                setIsLoading(false);
                return;
            }

            const data = await response.json();
            if (data.expeditions) {
                setExpeditions(data.expeditions);
            }
        } catch (error) {
            console.error("Error fetching expeditions:", error);
        } finally {
            setIsLoading(false);
        }
    };


    const handleSearch = (e) => {
        e.preventDefault();
        fetchExpeditions();
    };

    const toggleExpedition = async (expeditionId) => {
        if (expandedExpeditionId === expeditionId) {
            setExpandedExpeditionId(null);
            setSeats([]);
            setSelectedSeat(null);
            return;
        }

        setExpandedExpeditionId(expeditionId);
        setSelectedSeat(null);
        setIsSeatsLoading(true);
        setSeats([]);

        try {
            const response = await fetch("/api/expedition/customer/get/search/seats", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    expeditionId: expeditionId
                })
            });

            if (!response.ok) {
                console.error("Failed to fetch seats:", response.statusText);
                setIsSeatsLoading(false);
                return;
            }

            const data = await response.json();
            if (data.seats) {
                setSeats(data.seats);
            }
        } catch (error) {
            console.error("Error fetching seats:", error);
        } finally {
            setIsSeatsLoading(false);
        }
    };

    const onSelectSeat = (seatNo) => {
        if (selectedSeat === seatNo) {
            setSelectedSeat(null);
        } else {
            setSelectedSeat(seatNo);
        }
    };

    const handleProceedToCheckout = () => {
        if (!selectedSeat || !expandedExpeditionId) return;
        handleBookTicket(0); // Call direct booking with dummy cardId
    };

    const handleBookTicket = async (cardId) => {
        setIsProcessingPayment(true);
        try {
            const response = await fetch("/api/ticket/buy", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    expeditionId: expandedExpeditionId,
                    seatNo: selectedSeat,
                    cardId: cardId
                })
            });

            const data = await response.json();

            if (!response.ok) {
                alert(data.message || "Booking failed. Please try again.");
                setIsProcessingPayment(false);
                return;
            }

            // Success
            console.log("Ticket Created:", data.ticketInternalDTO);
            alert(data.message || "Booking Successful! Your ticket has been reserved.");

            setExpandedExpeditionId(null); // Close expansion and reset view
        } catch (error) {
            console.error("Booking error:", error);
            alert("An error occurred during booking.");
        } finally {
            setIsProcessingPayment(false);
        }
    };

    // --- Custom Calendar Helpers ---
    const getDaysInMonth = (year, month) => new Date(year, month + 1, 0).getDate();
    const getFirstDayOfMonth = (year, month) => {
        // 0 = Sun, 1 = Mon ... 
        // We want 1 = Mon, ... 7 = Sun for our grid
        let day = new Date(year, month, 1).getDay();
        return day === 0 ? 6 : day - 1; // Shift so Mon is 0 index
    };

    const renderCalendarDays = () => {
        const year = calendarViewDate.getFullYear();
        const month = calendarViewDate.getMonth();
        const daysInMonth = getDaysInMonth(year, month);
        const startDay = getFirstDayOfMonth(year, month);

        const days = [];
        // Empty slots for start
        for (let i = 0; i < startDay; i++) {
            days.push(<div key={`empty-${i}`} className="calendarDay empty"></div>);
        }

        for (let d = 1; d <= daysInMonth; d++) {
            const currentDayStr = `${year}-${String(month + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`;
            const isSelected = date === currentDayStr;
            const isToday = currentDayStr === new Date().toISOString().split("T")[0];

            days.push(
                <div
                    key={d}
                    className={`calendarDay ${isSelected ? "selected" : ""} ${isToday ? "today" : ""}`}
                    onClick={() => {
                        setDate(currentDayStr);
                        setShowCalendar(false);
                    }}
                >
                    {d}
                </div>
            );
        }
        return days;
    };

    const nextMonth = () => {
        setCalendarViewDate(new Date(calendarViewDate.getFullYear(), calendarViewDate.getMonth() + 1, 1));
    };

    const prevMonth = () => {
        setCalendarViewDate(new Date(calendarViewDate.getFullYear(), calendarViewDate.getMonth() - 1, 1));
    };

    const MONTH_NAMES = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];


    return (
        <>
            <div className="travelPage">
                <div className="travelCard">

                    {/* Header adapted from ProfilePage */}
                    <header className="travelHeader">
                        <div>
                            <h1 className="title">Find Your <span>Expedition</span></h1>
                            <p className="subtitle">Search for the best bus tickets across the country</p>
                        </div>
                    </header>

                    <div className="travelContent">
                        <form className="searchForm" onSubmit={handleSearch}>
                            <div className="formGroup">
                                <label className="formLabel">From</label>
                                <select
                                    className="formInput"
                                    value={fromCity}
                                    onChange={(e) => setFromCity(e.target.value)}
                                >
                                    <option value="">Select city</option>
                                    {CITIES.map(c => (
                                        <option key={c} value={c}>{c}</option>
                                    ))}
                                </select>
                            </div>

                            <div className="formGroup">
                                <label className="formLabel">To</label>
                                <select
                                    className="formInput"
                                    value={toCity}
                                    onChange={(e) => setToCity(e.target.value)}
                                >
                                    <option value="">Select city</option>
                                    {CITIES.map(c => (
                                        <option key={c} value={c}>{c}</option>
                                    ))}
                                </select>
                            </div>

                            <div className="formGroup dateGroup" ref={calendarRef}>
                                <label className="formLabel">Date</label>
                                {/* Custom Date Input Display */}
                                <div
                                    className="customDateInput"
                                    onClick={() => setShowCalendar(!showCalendar)}
                                >
                                    <span className="dateText">{date || "Select Date"}</span>
                                    <span className="calendarIcon">📅</span>
                                </div>

                                {/* Custom Calendar Popup */}
                                {showCalendar && (
                                    <div className="calendarPopup">
                                        <div className="calendarHeader">
                                            <button type="button" onClick={prevMonth}>&lt;</button>
                                            <span>{MONTH_NAMES[calendarViewDate.getMonth()]} {calendarViewDate.getFullYear()}</span>
                                            <button type="button" onClick={nextMonth}>&gt;</button>
                                        </div>
                                        <div className="calendarWeekdays">
                                            <span>Mo</span><span>Tu</span><span>We</span><span>Th</span><span>Fr</span><span>Sa</span><span>Su</span>
                                        </div>
                                        <div className="calendarGrid">
                                            {renderCalendarDays()}
                                        </div>
                                    </div>
                                )}
                            </div>

                            <button type="submit" className="searchBtn" disabled={isLoading}>
                                {isLoading ? "Searching..." : "Search"}
                            </button>
                        </form>

                        <div className="listWrap">
                            {isLoading ? (
                                <div className="emptyBox">Searching...</div>
                            ) : !hasSearched ? (
                                <div className="emptyBox">
                                    Enter your travel details above to see available expeditions.
                                </div>
                            ) : expeditions.length === 0 ? (
                                <div className="emptyBox">
                                    No expeditions found matching your criteria.
                                </div>
                            ) : (
                                expeditions.map((exp) => {
                                    const isExpanded = expandedExpeditionId === exp.expeditionId;

                                    return (
                                        <div key={exp.expeditionId} className={`expeditionCard ${isExpanded ? "expanded" : ""}`}>
                                            <div className="cardMain">
                                                <div className="cardLeft">
                                                    <div className="routeInfo">
                                                        <span className="companyName">{exp.companyName}</span>
                                                        <div className="routeText">
                                                            {exp.departureCity}
                                                            <span className="routeArrow">→</span>
                                                            {exp.arrivalCity}
                                                        </div>
                                                    </div>

                                                    <div className="detailsGrid">
                                                        <div className="detailItem">
                                                            <span className="detailLabel">Date</span>
                                                            <span className="detailValue">{exp.date}</span>
                                                        </div>
                                                        <div className="detailItem">
                                                            <span className="detailLabel">Time</span>
                                                            <span className="detailValue">{exp.time}</span>
                                                        </div>
                                                        <div className="detailItem">
                                                            <span className="detailLabel">Duration</span>
                                                            <span className="detailValue">{Math.floor(exp.duration / 60)}h {exp.duration % 60}m</span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div className="cardRight">
                                                    <span className="priceTag">₹{exp.price}</span>
                                                    <button
                                                        className="buyBtn"
                                                        onClick={() => toggleExpedition(exp.expeditionId)}
                                                    >
                                                        {isExpanded ? "Close" : "Buy Ticket"}
                                                    </button>
                                                </div>
                                            </div>

                                            {isExpanded && (
                                                <div className="seatSelectionArea">
                                                    <div className="seatAreaHeader">
                                                        <h3>Select your Seat</h3>
                                                        <div className="seatLegend">
                                                            <span className="legendItem"><span className="dot available"></span> Available</span>
                                                            <span className="legendItem"><span className="dot booked"></span> Reserved</span>
                                                            <span className="legendItem"><span className="dot selected"></span> Selected</span>
                                                        </div>
                                                    </div>

                                                    {isSeatsLoading ? (
                                                        <div className="seatsLoading">Loading seats...</div>
                                                    ) : (
                                                        <div className="seatGridContainer">
                                                            <div className="driverArea">
                                                                <span className="driverSeat">Driver</span>
                                                            </div>
                                                            <div className="busLayout">
                                                                {seats.map((seat) => (
                                                                    <button
                                                                        key={seat.seatNo}
                                                                        type="button"
                                                                        className={`seat ${seat.status.toLowerCase()} ${selectedSeat === seat.seatNo ? "selected" : ""}`}
                                                                        disabled={seat.status === "RESERVED"}
                                                                        onClick={() => onSelectSeat(seat.seatNo)}
                                                                    >
                                                                        {seat.seatNo}
                                                                    </button>
                                                                ))}
                                                            </div>
                                                        </div>
                                                    )}

                                                    <div className="seatFooter">
                                                        <div className="selectionSummary">
                                                            {selectedSeat ? `Seat #${selectedSeat} selected` : "No seat selected"}
                                                        </div>
                                                        <button
                                                            className="confirmBtn"
                                                            disabled={!selectedSeat || isProcessingPayment}
                                                            onClick={handleProceedToCheckout}
                                                        >
                                                            {isProcessingPayment ? "Booking..." : "Confirm Booking"}
                                                        </button>
                                                    </div>
                                                </div>
                                            )}
                                        </div>
                                    );
                                })
                            )}
                        </div>
                    </div>

                    <p className="footerText">
                        <Link to="/company" className="link">Are you a company?</Link>
                    </p>
                </div>
            </div>

        </>
    );
}
