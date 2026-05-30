import { useState, useEffect } from 'react'
import Navbar from '../components/layout/Navbar'
import StatusBadge from '../components/ui/StatusBadge'
import { ticketService } from '../services/api'
import { useAuth } from '../context/AuthContext'
import { Search, X, PlusCircle, Loader2 } from 'lucide-react'

export default function SupportTicketsPage() {
  const { isAdmin } = useAuth()
  const [tickets, setTickets]       = useState([])
  const [form, setForm]             = useState({ title: '', description: '' })
  const [search, setSearch]         = useState('')
  const [statusFilter, setStatus]   = useState('All')
  const [loading, setLoading]       = useState(false)
  const [submitting, setSubmitting] = useState(false)
  const [error, setError]           = useState('')
  const [success, setSuccess]       = useState('')

  const fetchTickets = async () => {
    setLoading(true)
    try {
      const res = isAdmin() ? await ticketService.getAll() : await ticketService.getMyTickets()
      setTickets(res.data)
    } catch { setError('Erreur lors du chargement des tickets.') }
    finally { setLoading(false) }
  }

  useEffect(() => { fetchTickets() }, [])

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!form.title.trim() || !form.description.trim()) return
    setSubmitting(true)
    setError('')
    try {
      await ticketService.create(form)
      setForm({ title: '', description: '' })
      setSuccess('Ticket créé avec succès !')
      fetchTickets()
      setTimeout(() => setSuccess(''), 3000)
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de la création.')
    } finally { setSubmitting(false) }
  }

  // FIX #7: Added error handling for status update
  const handleStatusUpdate = async (id, status) => {
    try {
      await ticketService.updateStatus(id, { status })
      fetchTickets()
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de la mise à jour du statut.')
    }
  }

  const filtered = tickets.filter(t => {
    const matchSearch = t.title.toLowerCase().includes(search.toLowerCase())
    const matchStatus = statusFilter === 'All' || t.status === statusFilter
    return matchSearch && matchStatus
  })

  // FIX #4: Same gradient background as HomePage
  return (
    <div className="min-h-screen bg-gradient-to-br from-amber-50 via-orange-50 to-yellow-50">
      <Navbar />
      <div className="max-w-4xl mx-auto px-6 py-10">

        <h1 className="font-display font-bold text-3xl text-gray-900 mb-1">Support Tickets</h1>
        <p className="text-gray-500 mb-8">Create and track your IT support tickets.</p>

        {/* Create Form */}
        <div className="card mb-8">
          <h2 className="font-display font-bold text-xl text-blue-900 mb-5">Create Ticket</h2>
          {error   && <div className="mb-4 bg-red-50 text-red-700 text-sm rounded-xl px-4 py-3 border border-red-200">{error}</div>}
          {success && <div className="mb-4 bg-green-50 text-green-700 text-sm rounded-xl px-4 py-3 border border-green-200">{success}</div>}
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Title</label>
              <input
                value={form.title}
                onChange={e => setForm(f => ({ ...f, title: e.target.value }))}
                placeholder="Ex: Laptop not working"
                className="input"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
              <textarea
                value={form.description}
                onChange={e => setForm(f => ({ ...f, description: e.target.value }))}
                placeholder="Describe the issue..."
                rows={4}
                className="input resize-none"
                required
              />
            </div>
            <button type="submit" disabled={submitting}
              className="w-full bg-blue-800 hover:bg-blue-900 text-white font-semibold py-3 rounded-xl transition-all flex items-center justify-center gap-2">
              {submitting ? <Loader2 size={18} className="animate-spin" /> : <PlusCircle size={18} />}
              Create Ticket
            </button>
          </form>
        </div>

        {/* Search & Filter */}
        <div className="flex flex-wrap gap-3 mb-6">
          <div className="relative flex-1 min-w-48">
            <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
            <input
              value={search}
              onChange={e => setSearch(e.target.value)}
              placeholder="Search tickets..."
              className="input pl-9 pr-8"
            />
            {search && (
              <button onClick={() => setSearch('')} className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
                <X size={14} />
              </button>
            )}
          </div>
          <select
            value={statusFilter}
            onChange={e => setStatus(e.target.value)}
            className="input w-auto"
          >
            <option value="All">All Status</option>
            <option value="OPEN">Open</option>
            <option value="IN_PROGRESS">In Progress</option>
            <option value="CLOSED">Closed</option>
          </select>
        </div>

        {/* Tickets List */}
        <div>
          <h2 className="font-display font-bold text-xl text-blue-900 mb-4">Tickets List</h2>
          {loading ? (
            <div className="flex justify-center py-12">
              <Loader2 size={32} className="animate-spin text-gold" />
            </div>
          ) : filtered.length === 0 ? (
            <div className="text-center py-12 text-gray-400">Aucun ticket trouvé.</div>
          ) : (
            <div className="space-y-3">
              {filtered.map(ticket => (
                <div key={ticket.id} className="card hover:shadow-card-hover transition-shadow">
                  <div className="flex items-start justify-between gap-4">
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center gap-2 mb-1">
                        <span className="font-semibold text-gray-900 truncate">{ticket.title}</span>
                        <StatusBadge status={ticket.status} />
                      </div>
                      <p className="text-sm text-gray-500 truncate">{ticket.description}</p>
                      {isAdmin() && (
                        <p className="text-xs text-gray-400 mt-1">Par : {ticket.userEmail}</p>
                      )}
                    </div>
                    {isAdmin() && (
                      <select
                        value={ticket.status}
                        onChange={e => handleStatusUpdate(ticket.id, e.target.value)}
                        className="text-xs border border-gray-200 rounded-lg px-2 py-1 bg-white focus:outline-none focus:ring-1 focus:ring-gold"
                      >
                        <option value="OPEN">Open</option>
                        <option value="IN_PROGRESS">In Progress</option>
                        <option value="CLOSED">Closed</option>
                      </select>
                    )}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
