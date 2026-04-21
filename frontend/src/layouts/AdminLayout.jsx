import { Outlet } from "react-router-dom";
import AdminTopBar from "../components/AdminTopBar.jsx";

export default function AdminLayout() {
  return (
    <>
      <AdminTopBar />
      <Outlet />
    </>
  );
}
