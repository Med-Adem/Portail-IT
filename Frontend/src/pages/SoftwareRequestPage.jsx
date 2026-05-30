import { useState, useEffect } from 'react'
import Navbar from '../components/layout/Navbar'
import StatusBadge from '../components/ui/StatusBadge'
import { softwareService } from '../services/api'
import { useAuth } from '../context/AuthContext'
import { PlusCircle, Loader2 } from 'lucide-react'

export default function SoftwareRequestPage() {
  const { isAdmin } = useAuth()
  const [requests, setRequests]     = useState([])
  const [form, setForm]             = useState({ softwareName: '', reason: '' })
  const [loading, setLoading]       = useState(false)
  const [submitting, setSubmitting] = useState(false)
  const [error, setError]           = useState('')
  const [success, setSuccess]       = useState('')

  const fetchRequests = async () => {
    setLoading(true)
    try {
      const res = isAdmin() ? await softwareService.getAll() : await softwareService.getMyReqs()
      setRequests(res.data)
    } catch { setError('Erreur lors du chargement.') }
    finally { setLoading(false) }
  }

  useEffect(() => { fetchRequests() }, [])

  const handleSubmit = async (e) => {
    e.preventDefault()
    setSubmitting(true)
    setError('')
    try {
      await softwareService.create(form)
      setForm({ softwareName: '', reason: '' })
      setSuccess('Demande envoyée avec succès !')
      fetchRequests()
      setTimeout(() => setSuccess(''), 3000)
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de la création.')
    } finally { setSubmitting(false) }
  }

  // FIX #7: Added error handling for status update
  const handleStatusUpdate = async (id, status) => {
    try {
      await softwareService.updateStatus(id, { status })
      fetchRequests()
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de la mise à jour du statut.')
    }
  }

  // FIX #4: Same gradient background as HomePage
  return (
    <div className="min-h-screen bg-gradient-to-br from-amber-50 via-orange-50 to-yellow-50">
      <Navbar />
      <div className="max-w-4xl mx-auto px-6 py-10">

        <h1 className="font-display font-bold text-3xl text-gray-900 mb-1">Software Request</h1>
        <p className="text-gray-500 mb-8">Request installation or access to software.</p>

        {/* Form */}
        <div className="card mb-8">
          <h2 className="font-display font-bold text-xl text-blue-900 mb-5">New Request</h2>
          {error   && <div className="mb-4 bg-red-50 text-red-700 text-sm rounded-xl px-4 py-3 border border-red-200">{error}</div>}
          {success && <div className="mb-4 bg-green-50 text-green-700 text-sm rounded-xl px-4 py-3 border border-green-200">{success}</div>}
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Software Name</label>
              <input
                value={form.softwareName}
                onChange={e => setForm(f => ({ ...f, softwareName: e.target.value }))}
                placeholder="Ex: AutoCAD"
                className="input"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Reason</label>
              <textarea
                value={form.reason}
                onChange={e => setForm(f => ({ ...f, reason: e.target.value }))}
                placeholder="Why do you need it?"
                rows={4}
                className="input resize-none"
                required
              />
            </div>
            <button type="submit" disabled={submitting}
              className="w-full bg-blue-800 hover:bg-blue-900 text-white font-semibold py-3 rounded-xl transition-all flex items-center justify-center gap-2">
              {submitting ? <Loader2 size={18} className="animate-spin" /> : <PlusCircle size={18} />}
              Submit Request
            </button>
          </form>
        </div>

        {/* List */}
        <div>
          <h2 className="font-display font-bold text-xl text-blue-900 mb-4">Requests List</h2>
          {loading ? (
            <div className="flex justify-center py-12">
              <Loader2 size={32} className="animate-spin text-gold" />
            </div>
          ) : requests.length === 0 ? (
            <div className="text-center py-12 text-gray-400">Aucune demande pour le moment.</div>
          ) : (
            <div className="space-y-3">
              {requests.map(req => (
                <div key={req.id} className="card hover:shadow-card-hover transition-shadow">
                  <div className="flex items-start justify-between gap-4">
                    <div className="flex-1">
                      <div className="flex items-center gap-2 mb-1">
                        <span className="font-bold text-blue-900 uppercase text-sm">{req.softwareName}</span>
                        <StatusBadge status={req.status} />
                      </div>
                      <p className="text-sm text-gray-500">{req.reason}</p>
                      {isAdmin() && (
                        <p className="text-xs text-gray-400 mt-1">Par : {req.userEmail}</p>
                      )}
                    </div>
                    {isAdmin() && (
                      <select
                        value={req.status}
                        onChange={e => handleStatusUpdate(req.id, e.target.value)}
                        className="text-xs border border-gray-200 rounded-lg px-2 py-1 bg-white focus:outline-none focus:ring-1 focus:ring-gold"
                      >
                        <option value="PENDING">Pending</option>
                        <option value="APPROVED">Approved</option>
                        <option value="REJECTED">Rejected</option>
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
