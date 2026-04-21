// src/App.jsx

import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import AuthGuard from './routes/guards/AuthGuard.jsx';
import AuthLayout from './layouts/AuthLayout.jsx';
import LoginPage from './pages/login/LoginPage.jsx';
import RegisterPage from "./pages/register/RegisterPage.jsx";
import CustomerRegister from "./pages/registerCustomer/CustomerRegister.jsx";
import CompanyRegister from "./pages/registerCompany/CompanyRegister.jsx";
import AdminRegister from "./pages/registerAdmin/AdminRegister.jsx";

import CustomerGuard from './routes/guards/CustomerGuard.jsx';
import CustomerLayout from './layouts/CustomerLayout.jsx';
import CustomerHomePage from './pages/customer/CustomerHomePage.jsx';
import Travel from "./pages/travel/Travel.jsx";
import ProfilePage from './pages/profile/ProfilePage.jsx';

import CompanyGuard from './routes/guards/CompanyGuard.jsx';
import AdminLayout from './layouts/AdminLayout.jsx';
import AdminHomePage from './pages/Admin/AdminHomePage.jsx';
import CompanyConfirmPage from './pages/Admin/CompanyConfirmPage.jsx';
import AdminConfirmPage from './pages/Admin/AdminConfirmPage.jsx';

import AdminGuard from './routes/guards/AdminGuard.jsx';
import CompanyHome from './pages/companyHome/CompanyHome.jsx';
import CompanyExpeditionCreate from './pages/expeditionCreate/CompanyExpeditionCreate.jsx';
import CompanyExpeditionList from './pages/companyExpeditionList/CompanyExpeditionList.jsx';
import CompanyExpeditionDetail from './pages/companyExpeditionDetail/CompanyExpeditionDetail.jsx';
import MyTicketsPage from './pages/tickets/MyTicketsPage.jsx';

function App() {
  return (

    <Router>
      <Routes>
        <Route element={<AuthGuard />}>
          <Route element={<AuthLayout />}>
            {/* Auth ile ilgili sayfalar buraya */}
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/register/customer" element={<CustomerRegister />} />
            <Route path="/register/company" element={<CompanyRegister />} />
            <Route path="/register/admin" element={<AdminRegister />} />
          </Route>
        </Route>

        <Route element={<CustomerGuard />}>
          <Route element={<CustomerLayout />}>
            {/* Müşteri ile ilgili sayfalar buraya */}
            <Route path="/" element={<CustomerHomePage />} />
            <Route path="/travel" element={<Travel />} />
            <Route path="/profile" element={<ProfilePage />} />
            <Route path="/my-tickets" element={<MyTicketsPage />} />
          </Route>
        </Route>

        <Route element={<AdminGuard />}>
          <Route element={<AdminLayout />}>
            {/* Admin ile ilgili sayfalar buraya */}
            <Route path="/admin" element={<AdminHomePage />} />
            <Route path="/admin/confirmcompanies" element={<CompanyConfirmPage />} />
            <Route path="/admin/confirmadmins" element={<AdminConfirmPage />} />
          </Route>
        </Route>

        <Route path="/travel" element={<Travel />} />

        <Route element={<CompanyGuard />}>
          {/* Şirket ile ilgili sayfalar buraya */}
          <Route path="/company" element={<CompanyHome />} />
          <Route path="/company/expeditions/create" element={<CompanyExpeditionCreate />} />
          <Route path="/company/expeditions" element={<CompanyExpeditionList />} />
          <Route path="/company/expeditions/:id" element={<CompanyExpeditionDetail />} />
        </Route>

      </Routes>
    </Router>
  );
}

export default App;