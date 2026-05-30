import { Link, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import { LogOut, Users } from 'lucide-react'

const navLinks = [
  { to: '/home', label: 'Home' },
  { to: '/support-tickets', label: 'Support Tickets' },
  { to: '/software-request', label: 'Software Request' },
  { to: '/hardware-inventory', label: 'Hardware Inventory' },
  { to: '/guides', label: 'Guides' },
]

export default function Navbar() {
  const { pathname } = useLocation()
  const { logout, user, isAdmin } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <nav className="bg-white border-b border-gray-100 shadow-sm sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-6 h-16 flex items-center justify-between">

        {/* Logo */}
        <Link to="/home" className="flex items-center gap-2.5">
          <img
            src="/ola-energy-logo.png"
            alt="OLA Energy"
            className="h-10 w-auto object-contain"
          />
          <span className="font-display font-bold text-gray-900 text-base tracking-tight">
            OETN – IT Portal
          </span>
        </Link>

        {/* Nav links */}
        <div className="hidden md:flex items-center gap-1">
          {navLinks.map(link => (
            <Link
              key={link.to}
              to={link.to}
              className={`px-4 py-2 rounded-lg text-sm font-medium transition-all duration-150 ${pathname === link.to
                ? 'text-gold border-b-2 border-gold font-semibold'
                : 'text-gray-600 hover:text-gray-900 hover:bg-gray-50'
                }`}
            >
              {link.label}
            </Link>
          ))}

          {/* Admin-only: User Management */}
          {isAdmin() && (
            <Link
              to="/admin/users"
              className={`px-4 py-2 rounded-lg text-sm font-medium transition-all duration-150 flex items-center gap-1.5 ${pathname === '/admin/users'
                ? 'text-gold border-b-2 border-gold font-semibold'
                : 'text-gray-600 hover:text-gray-900 hover:bg-gray-50'
                }`}
            >
              <Users size={14} />
              Users
            </Link>
          )}
        </div>

        {/* Right side */}
        <div className="flex items-center gap-3">
          {user && (
            <div className="hidden md:flex items-center gap-2">
              {isAdmin() && (
                <span className="text-xs bg-amber-100 text-amber-700 font-semibold px-2 py-0.5 rounded-full">
                  ADMIN
                </span>
              )}
              <span className="text-sm text-gray-700 font-medium">
                {user.username}
              </span>
            </div>
          )}
          <button
            onClick={handleLogout}
            className="btn-primary flex items-center gap-2 text-sm py-2 px-4"
          >
            <LogOut size={14} />
            Logout
          </button>
        </div>
      </div>
    </nav>
  )
}
