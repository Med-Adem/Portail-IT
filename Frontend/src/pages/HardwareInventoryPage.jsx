import { useState, useEffect } from 'react'
import Navbar from '../components/layout/Navbar'
import { hardwareService } from '../services/api'
import { useAuth } from '../context/AuthContext'
import { Laptop, Info, Loader2, Trash2 } from 'lucide-react'

export default function HardwareInventoryPage() {
  const { user, isAdmin } = useAuth()
  const [devices, setDevices] = useState([])
  const [form, setForm] = useState({ laptopModel: '', serialNumber: '' })
  const [loading, setLoading] = useState(false)
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  const fetchDevices = async () => {
    setLoading(true)
    try {
      const res = isAdmin() ? await hardwareService.getAll() : await hardwareService.getMyDevices()
      setDevices(res.data)
    } catch {
      setError('Erreur lors du chargement.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchDevices() }, [])

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!form.serialNumber.trim()) return
    setSubmitting(true)
    setError('')
    setSuccess('')
    try {
      await hardwareService.submit({
        laptopModel: form.laptopModel,
        serialNumber: form.serialNumber,
      })
      setForm({ laptopModel: '', serialNumber: '' })
      setSuccess('Appareil enregistré avec succès !')
      fetchDevices()
      setTimeout(() => setSuccess(''), 3000)
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de la soumission.')
    } finally {
      setSubmitting(false)
    }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Supprimer cet appareil ?')) return
    try {
      await hardwareService.delete(id)
      fetchDevices()
    } catch {
      setError('Erreur lors de la suppression.')
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-amber-50 via-orange-50 to-yellow-50">
      <Navbar />
      <div className="max-w-3xl mx-auto px-6 py-10">

        <h1 className="font-display font-bold text-3xl text-gray-900 mb-1">Hardware Inventory</h1>
        <p className="text-gray-500 mb-8">
          Enter your Serial Number (S/N) shown directly on your desktop background at the top right corner. Laptop Model is optional.
        </p>

        {/* Info box */}
        <div className="bg-amber-50 border border-amber-200 rounded-2xl p-5 mb-8">
          <div className="flex items-center gap-2 font-semibold text-amber-800 mb-3">
            <Info size={16} className="text-red-500" />
            Where to find your Laptop Info
          </div>
          <ul className="space-y-1 text-sm text-amber-700">
            <li><span className="font-semibold">Serial Number (S/N):</span> Displayed on your desktop background, top-right corner.</li>
            <li><span className="font-semibold">Laptop Model (optional):</span> Usually on the label under your laptop.</li>
          </ul>
        </div>

        {/* Form */}
        <div className="card mb-8">
          {error && <div className="mb-4 bg-red-50 text-red-700 text-sm rounded-xl px-4 py-3 border border-red-200">{error}</div>}
          {success && <div className="mb-4 bg-green-50 text-green-700 text-sm rounded-xl px-4 py-3 border border-green-200">{success}</div>}

          <form onSubmit={handleSubmit} className="space-y-4">
            {/* Username — read only */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Username</label>
              <input
                type="text"
                value={user?.username || ''}
                readOnly
                className="input bg-gray-100 cursor-not-allowed text-gray-500"
              />
            </div>

            {/* Laptop Model */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Laptop Model <span className="text-gray-400">(optional)</span>
              </label>
              <input
                value={form.laptopModel}
                onChange={e => setForm(f => ({ ...f, laptopModel: e.target.value }))}
                placeholder="Dell Latitude 5420"
                className="input"
              />
            </div>

            {/* Serial Number */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Serial Number <span className="text-red-500">*</span>
              </label>
              <input
                value={form.serialNumber}
                onChange={e => setForm(f => ({ ...f, serialNumber: e.target.value }))}
                placeholder="S/N from desktop top-right"
                className="input"
                required
              />
            </div>

            <button
              type="submit"
              disabled={submitting}
              className="w-full btn-primary py-3 text-base flex items-center justify-center gap-2"
            >
              {submitting ? <Loader2 size={18} className="animate-spin" /> : <Laptop size={18} />}
              Submit Device
            </button>
          </form>
        </div>

        {/* Devices List */}
        <div>
          <h2 className="font-display font-bold text-xl text-gray-900 mb-4">Registered Devices</h2>
          {loading ? (
            <div className="flex justify-center py-12">
              <Loader2 size={32} className="animate-spin text-gold" />
            </div>
          ) : devices.length === 0 ? (
            <div className="text-center py-12 text-gray-400">
              <Laptop size={48} className="mx-auto mb-4 opacity-30" />
              Aucun appareil enregistré.
            </div>
          ) : (
            <div className="space-y-3">
              {devices.map(device => (
                <div key={device.id} className="card hover:shadow-card-hover transition-shadow">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-4">
                      <div className="w-10 h-10 bg-gold/10 rounded-xl flex items-center justify-center">
                        <Laptop size={20} className="text-gold" />
                      </div>
                      <div>
                        <p className="font-semibold text-gray-900">{device.serialNumber}</p>
                        <p className="text-sm text-gray-500">
                          {device.laptopModel || 'Model not specified'} — {device.username}
                        </p>
                        {isAdmin() && (
                          <p className="text-xs text-gray-400">Soumis par : {device.userEmail}</p>
                        )}
                      </div>
                    </div>
                    {isAdmin() && (
                      <button
                        onClick={() => handleDelete(device.id)}
                        className="p-2 text-gray-400 hover:text-red-500 hover:bg-red-50 rounded-lg transition-colors"
                        title="Supprimer"
                      >
                        <Trash2 size={16} />
                      </button>
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