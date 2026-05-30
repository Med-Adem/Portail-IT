import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import Navbar from '../components/layout/Navbar'
import { Monitor, Download, Laptop, BookOpen } from 'lucide-react'

const cards = [
  {
    to: '/support-tickets',
    icon: Monitor,
    title: 'Support Tickets',
    desc: 'Submit and track IT support requests.',
  },
  {
    to: '/software-request',
    icon: Download,
    title: 'Software Request',
    desc: 'Request software installation or access.',
  },
  {
    to: '/hardware-inventory',
    icon: Laptop,
    title: 'Hardware Inventory',
    desc: 'View and manage company devices.',
  },
  {
    to: '/guides',
    icon: BookOpen,
    title: 'Guides',
    desc: 'User guides, common issues and procedures.',
  },
]

export default function HomePage() {
  const { user } = useAuth()

  return (
    <div className="min-h-screen bg-gradient-to-br from-amber-50 via-orange-50 to-yellow-50">
      <Navbar />

      <main className="max-w-5xl mx-auto px-6 py-20 text-center">
        <h1 className="font-display font-extrabold text-5xl text-gray-900 mb-4 tracking-tight">
          OETN – IT Portal
        </h1>
        <p className="text-gray-500 text-lg mb-16 max-w-xl mx-auto">
          Centralized IT services for support, software requests, hardware management, and internal guides.
        </p>

        {/* Cards grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {cards.map(({ to, icon: Icon, title, desc }) => (
            <div key={to} className="card text-left flex flex-col gap-3 hover:shadow-card-hover transition-shadow group">
              <div className="w-10 h-10 bg-gold/10 rounded-xl flex items-center justify-center group-hover:bg-gold/20 transition-colors">
                <Icon size={20} className="text-gold" />
              </div>
              <div>
                <h3 className="font-display font-bold text-gray-900 text-base mb-1">{title}</h3>
                <p className="text-gray-500 text-sm leading-relaxed">{desc}</p>
              </div>
              <Link
                to={to}
                className="mt-auto btn-primary text-sm py-2 px-4 text-center"
              >
                Open
              </Link>
            </div>
          ))}
        </div>
      </main>

      <footer className="text-center text-sm text-gray-400 pb-8">
        © 2026 OETN — Internal IT Portal
      </footer>
    </div>
  )
}
