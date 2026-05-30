import { useRef, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { authService } from '../services/api'
import { Eye, EyeOff, AlertCircle } from 'lucide-react'

export default function LoginPage() {
  const usernameRef = useRef()
  const passwordRef = useRef()
  const [showPwd, setShowPwd] = useState(false)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const res = await authService.login({
        username: usernameRef.current.value.toLowerCase(),
        password: passwordRef.current.value,
      })
      login(res.data)
      navigate('/home')
    } catch (err) {
      setError(err.response?.data?.message || "Nom d'utilisateur ou mot de passe incorrect.")
    } finally {
      setLoading(false)
    }
  }

  const inputClass = "w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 text-sm focus:outline-none focus:ring-2 focus:ring-yellow-400/30 focus:border-yellow-400 transition-all"

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-amber-200 via-orange-300 to-amber-500">
      <div className="bg-white rounded-3xl shadow-2xl w-full max-w-md mx-4 p-10">

        <div className="flex items-center justify-between mb-8">
          <div className="bg-white rounded-2xl shadow-md p-2 border border-gray-100">
            {/* FIX #1: Consistent logo filename — no space before .png */}
            <img
              src="/ola-energy-logo.png"
              alt="OLA Energy"
              className="h-10 w-auto object-contain"
              style={{ mixBlendMode: 'multiply' }}
            />
          </div>
          <h1 className="font-bold text-2xl text-gray-900 tracking-tight">LOGIN</h1>
        </div>

        {error && (
          <div className="mb-5 flex items-center gap-2 bg-red-50 border border-red-200 text-red-700 text-sm rounded-xl px-4 py-3">
            <AlertCircle size={16} /> {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label className="block text-xs font-bold text-gray-700 uppercase tracking-widest mb-2">Username</label>
            <input ref={usernameRef} type="text" placeholder="prenom.nom"
              defaultValue="" className={inputClass} required />
            <p className="text-xs text-gray-400 mt-1">Format : <span className="font-medium">prenom.nom</span> (ex: john.doe)</p>
          </div>

          <div>
            <label className="block text-xs font-bold text-gray-700 uppercase tracking-widest mb-2">Password</label>
            <div className="relative">
              <input ref={passwordRef} type={showPwd ? 'text' : 'password'}
                placeholder="Password" defaultValue="" className={`${inputClass} pr-10`} required />
              <button type="button" onClick={() => setShowPwd(v => !v)}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
                {showPwd ? <EyeOff size={18} /> : <Eye size={18} />}
              </button>
            </div>
          </div>

          <button type="submit" disabled={loading}
            className="w-full bg-yellow-500 hover:bg-yellow-600 text-white font-semibold py-3 rounded-xl transition-all flex items-center justify-center gap-2 mt-2">
            {loading ? <span className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin" /> : 'Sign In'}
          </button>
        </form>

        <p className="text-center text-sm text-gray-500 mt-6">
          Pas encore de compte ?{' '}
          <Link to="/register" className="text-yellow-600 font-semibold hover:text-yellow-700">Créer un compte</Link>
        </p>
      </div>
    </div>
  )
}
