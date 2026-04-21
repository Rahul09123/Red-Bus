import { Outlet } from "react-router-dom";
import CustomerTopBar from "../components/CustomerTopBar.jsx";



export default function CustomerLayout() {
  return (
    <>
      <CustomerTopBar />
      <Outlet />
    </>
  );
}