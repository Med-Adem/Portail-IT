import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import ProtectedRoute from './components/layout/ProtectedRoute'
import AdminRoute from './components/layout/AdminRoute'

import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import HomePage from './pages/HomePage'
import SupportTicketsPage from './pages/SupportTicketsPage'
import SoftwareRequestPage from './pages/SoftwareRequestPage'
import HardwareInventoryPage from './pages/HardwareInventoryPage'
import GuidesPage from './pages/GuidesPage'
import AdminUsersPage from './pages/AdminUsersPage'

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Public routes */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          {/* Protected routes */}
          <Route path="/home" element={
            <ProtectedRoute><HomePage /></ProtectedRoute>
          } />
          <Route path="/support-tickets" element={
            <ProtectedRoute><SupportTicketsPage /></ProtectedRoute>
          } />
          <Route path="/software-request" element={
            <ProtectedRoute><SoftwareRequestPage /></ProtectedRoute>
          } />
          <Route path="/hardware-inventory" element={
            <ProtectedRoute><HardwareInventoryPage /></ProtectedRoute>
          } />
          <Route path="/guides" element={
            <ProtectedRoute><GuidesPage /></ProtectedRoute>
          } />

          {/* Admin-only route */}
          <Route path="/admin/users" element={
            <AdminRoute><AdminUsersPage /></AdminRoute>
          } />

          {/* Default redirect */}
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}
