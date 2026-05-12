import React, { useEffect } from "react";
import { MapContainer, TileLayer, Marker, Polyline, useMap } from "react-leaflet";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import "./RouteMap.css";

// Fix for default marker icons in Leaflet
import markerIcon from "leaflet/dist/images/marker-icon.png";
import markerShadow from "leaflet/dist/images/marker-shadow.png";

let DefaultIcon = L.icon({
  iconUrl: markerIcon,
  shadowUrl: markerShadow,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
});
L.Marker.prototype.options.icon = DefaultIcon;

const CITY_COORDS = {
  Mumbai: [19.076, 72.8777],
  Delhi: [28.6139, 77.209],
  Bangalore: [12.9716, 77.5946],
  Hyderabad: [17.385, 78.4867],
  Ahmedabad: [23.0225, 72.5714],
  Chennai: [13.0827, 80.2707],
  Kolkata: [22.5726, 88.3639],
  Surat: [21.1702, 72.8311],
  Pune: [18.5204, 73.8567],
  Jaipur: [26.9124, 75.7873],
  Lucknow: [26.8467, 80.9462],
  Kanpur: [26.4499, 80.3319],
};

function ChangeView({ center, zoom }) {
  const map = useMap();
  useEffect(() => {
    map.setView(center, zoom);
  }, [center, zoom, map]);
  return null;
}

export default function RouteMap({ fromCity, toCity }) {
  const from = CITY_COORDS[fromCity];
  const to = CITY_COORDS[toCity];

  const defaultCenter = [20.5937, 78.9629]; // Center of India
  const defaultZoom = 5;

  const positions = [];
  if (from) positions.push(from);
  if (to) positions.push(to);

  return (
    <div className="routeMapContainer">
      <MapContainer center={defaultCenter} zoom={defaultZoom} scrollWheelZoom={false} className="leaflet-map">
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        {from && <Marker position={from} />}
        {to && <Marker position={to} />}
        {from && to && (
          <Polyline positions={[from, to]} color="#DC2626" weight={3} dashArray="10, 10" />
        )}
        {positions.length > 0 && (
          <ChangeView center={positions[0]} zoom={positions.length > 1 ? 5 : 6} />
        )}
      </MapContainer>
    </div>
  );
}
