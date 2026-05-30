import { useState, useEffect } from 'react'
import Navbar from '../components/layout/Navbar'
import { useAuth } from '../context/AuthContext'
import { useNavigate } from 'react-router-dom'
import api from '../services/api'
import {
  Users, Edit2, Trash2, Save, X, ShieldCheck,
  ShieldOff, Loader2, Search, Phone, User
} from 'lucide-react'

export default function AdminUsersPage() {
  const { isAdmin } = useAuth()
  const navigate = useNavigate()

  const [users, setUsers] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [search, setSearch] = useState('')
  const [editingId, setEditingId] = useState(null)
  const [editForm, setEditForm] = useState({})
  const [saving, setSaving] = useState(false)
  const [deletingId, setDeletingId] = useState(null)

  useEffect(() => {
    if (!isAdmin()) {
      navigate('/home')
      return
    }
    fetchUsers()
  }, [])

  const fetchUsers = async () => {
    setLoading(true)
    setError('')
    try {
      const res = await api.get('/admin/users')
      setUsers(res.data)
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors du chargement des utilisateurs.')
    } finally {
      setLoading(false)
    }
  }

  const startEdit = (user) => {
    setEditingId(user.id)
    setEditForm({
      firstName: user.firstName,
      lastName: user.lastName,
      phoneNumber: user.phoneNumber || '',
      role: user.role,
      newPassword: '',
    })
    setError('')
    setSuccess('')
  }

  const cancelEdit = () => {
    setEditingId(null)
    setEditForm({})
  }

  const handleSave = async (id) => {
    setSaving(true)
    setError('')
    try {
      await api.put(`/admin/users/${id}`, editForm)
      setSuccess('Utilisateur mis à jour avec succès !')
      setEditingId(null)
      fetchUsers()
      setTimeout(() => setSuccess(''), 3000)
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de la mise à jour.')
    } finally {
      setSaving(false)
    }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Supprimer cet utilisateur ? Cette action est irréversible.')) return
    setDeletingId(id)
    setError('')
    try {
      await api.delete(`/admin/users/${id}`)
      setSuccess('Utilisateur supprimé.')
      fetchUsers()
      setTimeout(() => setSuccess(''), 3000)
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de la suppression.')
    } finally {
      setDeletingId(null)
    }
  }

  const filtered = users.filter(u => {
    const q = search.toLowerCase()
    return (
      u.username?.toLowerCase().includes(q) ||
      u.firstName?.toLowerCase().includes(q) ||
      u.lastName?.toLowerCase().includes(q) ||
      u.role?.toLowerCase().includes(q)
    )
  })

  return (
    <div className="min-h-screen bg-gradient-to-br from-amber-50 via-orange-50 to-yellow-50">
      <Navbar />
      <div className="max-w-6xl mx-auto px-6 py-10">

        {/* Header */}
        <div className="flex items-center gap-3 mb-2">
          <div className="w-10 h-10 bg-gold/10 rounded-xl flex items-center justify-center">
            <Users size={22} className="text-gold" />
          </div>
          <div>
            <h1 className="font-display font-bold text-3xl text-gray-900">User Management</h1>
            <p className="text-gray-500 text-sm">View, edit and manage all portal users</p>
          </div>
        </div>

        {/* Stats bar */}
        <div className="flex gap-4 mt-6 mb-8">
          <div className="bg-white rounded-xl px-5 py-3 border border-gray-100 shadow-sm flex items-center gap-3">
            <span className="text-2xl font-bold text-gray-900">{users.length}</span>
            <span className="text-sm text-gray-500">Total Users</span>
          </div>
          <div className="bg-white rounded-xl px-5 py-3 border border-gray-100 shadow-sm flex items-center gap-3">
            <span className="text-2xl font-bold text-gold">{users.filter(u => u.role === 'ADMIN').length}</span>
            <span className="text-sm text-gray-500">Admins</span>
          </div>
          <div className="bg-white rounded-xl px-5 py-3 border border-gray-100 shadow-sm flex items-center gap-3">
            <span className="text-2xl font-bold text-blue-600">{users.filter(u => u.role === 'USER').length}</span>
            <span className="text-sm text-gray-500">Regular Users</span>
          </div>
        </div>

        {/* Alerts */}
        {error && (
          <div className="mb-5 bg-red-50 text-red-700 text-sm rounded-xl px-4 py-3 border border-red-200 flex items-center gap-2">
            <X size={16} /> {error}
          </div>
        )}
        {success && (
          <div className="mb-5 bg-green-50 text-green-700 text-sm rounded-xl px-4 py-3 border border-green-200">
            {success}
          </div>
        )}

        {/* Search */}
        <div className="relative mb-6">
          <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
          <input
            value={search}
            onChange={e => setSearch(e.target.value)}
            placeholder="Search by name, username or role..."
            className="input pl-9 w-full max-w-sm"
          />
        </div>

        {/* Table */}
        {loading ? (
          <div className="flex justify-center py-20">
            <Loader2 size={36} className="animate-spin text-gold" />
          </div>
        ) : (
          <div className="bg-white rounded-2xl border border-gray-100 shadow-card overflow-hidden">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-gray-50 border-b border-gray-100">
                  <th className="text-left px-5 py-3.5 font-semibold text-gray-600">User</th>
                  <th className="text-left px-5 py-3.5 font-semibold text-gray-600">Username</th>
                  <th className="text-left px-5 py-3.5 font-semibold text-gray-600">Phone</th>
                  <th className="text-left px-5 py-3.5 font-semibold text-gray-600">Role</th>
                  <th className="text-left px-5 py-3.5 font-semibold text-gray-600">Created</th>
                  <th className="text-right px-5 py-3.5 font-semibold text-gray-600">Actions</th>
                </tr>
              </thead>
              <tbody>
                {filtered.length === 0 ? (
                  <tr>
                    <td colSpan={6} className="text-center py-16 text-gray-400">
                      <Users size={40} className="mx-auto mb-3 opacity-20" />
                      No users found.
                    </td>
                  </tr>
                ) : filtered.map(user => (
                  <tr key={user.id} className={`border-b border-gray-50 hover:bg-gray-50/60 transition-colors ${editingId === user.id ? 'bg-amber-50/40' : ''}`}>

                    {editingId === user.id ? (
                      /* ── EDIT ROW ── */
                      <>
                        <td className="px-5 py-3" colSpan={2}>
                          <div className="flex gap-2">
                            <div className="flex-1">
                              <label className="text-xs text-gray-500 mb-1 block">First Name</label>
                              <input
                                value={editForm.firstName}
                                onChange={e => setEditForm(f => ({ ...f, firstName: e.target.value }))}
                                className="input text-sm py-1.5"
                                placeholder="First name"
                              />
                            </div>
                            <div className="flex-1">
                              <label className="text-xs text-gray-500 mb-1 block">Last Name</label>
                              <input
                                value={editForm.lastName}
                                onChange={e => setEditForm(f => ({ ...f, lastName: e.target.value }))}
                                className="input text-sm py-1.5"
                                placeholder="Last name"
                              />
                            </div>
                          </div>
                        </td>
                        <td className="px-5 py-3">
                          <label className="text-xs text-gray-500 mb-1 block">Phone</label>
                          <div className="relative">
                            <Phone size={14} className="absolute left-2.5 top-1/2 -translate-y-1/2 text-gray-400" />
                            <input
                              value={editForm.phoneNumber}
                              onChange={e => setEditForm(f => ({ ...f, phoneNumber: e.target.value }))}
                              className="input text-sm py-1.5 pl-8"
                              placeholder="+216 XX XXX XXX"
                            />
                          </div>
                        </td>
                        <td className="px-5 py-3">
                          <label className="text-xs text-gray-500 mb-1 block">Role</label>
                          <select
                            value={editForm.role}
                            onChange={e => setEditForm(f => ({ ...f, role: e.target.value }))}
                            className="input text-sm py-1.5"
                          >
                            <option value="USER">USER</option>
                            <option value="ADMIN">ADMIN</option>
                          </select>
                        </td>
                        <td className="px-5 py-3">
                          <label className="text-xs text-gray-500 mb-1 block">New Password</label>
                          <input
                            type="password"
                            value={editForm.newPassword}
                            onChange={e => setEditForm(f => ({ ...f, newPassword: e.target.value }))}
                            className="input text-sm py-1.5"
                            placeholder="Leave blank to keep"
                          />
                        </td>
                        <td className="px-5 py-3 text-right">
                          <div className="flex justify-end gap-2 mt-4">
                            <button
                              onClick={() => handleSave(user.id)}
                              disabled={saving}
                              className="flex items-center gap-1.5 bg-green-600 hover:bg-green-700 text-white text-xs font-semibold px-3 py-1.5 rounded-lg transition-colors"
                            >
                              {saving ? <Loader2 size={13} className="animate-spin" /> : <Save size={13} />}
                              Save
                            </button>
                            <button
                              onClick={cancelEdit}
                              className="flex items-center gap-1.5 bg-gray-100 hover:bg-gray-200 text-gray-700 text-xs font-semibold px-3 py-1.5 rounded-lg transition-colors"
                            >
                              <X size={13} /> Cancel
                            </button>
                          </div>
                        </td>
                      </>
                    ) : (
                      /* ── VIEW ROW ── */
                      <>
                        <td className="px-5 py-3.5">
                          <div className="flex items-center gap-3">
                            <div className="w-8 h-8 rounded-full bg-gold/10 flex items-center justify-center flex-shrink-0">
                              <User size={14} className="text-gold" />
                            </div>
                            <span className="font-medium text-gray-900">
                              {user.firstName} {user.lastName}
                            </span>
                          </div>
                        </td>
                        <td className="px-5 py-3.5 text-gray-500 font-mono text-xs">{user.username}</td>
                        <td className="px-5 py-3.5 text-gray-500">{user.phoneNumber || '—'}</td>
                        <td className="px-5 py-3.5">
                          {user.role === 'ADMIN' ? (
                            <span className="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-medium bg-amber-50 text-amber-700 border border-amber-200">
                              <ShieldCheck size={11} /> ADMIN
                            </span>
                          ) : (
                            <span className="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-50 text-blue-700 border border-blue-200">
                              <ShieldOff size={11} /> USER
                            </span>
                          )}
                        </td>
                        <td className="px-5 py-3.5 text-gray-400 text-xs">
                          {user.createdAt ? new Date(user.createdAt).toLocaleDateString('fr-FR', {
                            day: '2-digit', month: 'short', year: 'numeric'
                          }) : '—'}
                        </td>
                        <td className="px-5 py-3.5 text-right">
                          <div className="flex justify-end gap-2">
                            <button
                              onClick={() => startEdit(user)}
                              className="p-1.5 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
                              title="Edit user"
                            >
                              <Edit2 size={15} />
                            </button>
                            <button
                              onClick={() => handleDelete(user.id)}
                              disabled={deletingId === user.id}
                              className="p-1.5 text-gray-400 hover:text-red-500 hover:bg-red-50 rounded-lg transition-colors"
                              title="Delete user"
                            >
                              {deletingId === user.id
                                ? <Loader2 size={15} className="animate-spin" />
                                : <Trash2 size={15} />
                              }
                            </button>
                          </div>
                        </td>
                      </>
                    )}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  )
}
