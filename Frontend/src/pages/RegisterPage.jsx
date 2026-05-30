import { useRef, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { authService } from '../services/api'
import { Eye, EyeOff, AlertCircle, User } from 'lucide-react'

export default function RegisterPage() {
  const firstNameRef = useRef()
  const lastNameRef = useRef()
  const phoneRef = useRef()
  const passwordRef = useRef()
  const confirmRef = useRef()
  const [showPwd, setShowPwd] = useState(false)
  const [showConfirm, setShowConfirm] = useState(false)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const [preview, setPreview] = useState('')
  const { login } = useAuth()
  const navigate = useNavigate()

  const updatePreview = () => {
    const fn = firstNameRef.current?.value || ''
    const ln = lastNameRef.current?.value || ''
    if (fn && ln) setPreview((fn + '.' + ln).toLowerCase())
    else setPreview('')
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    if (passwordRef.current.value !== confirmRef.current.value)
      return setError('Les mots de passe ne correspondent pas.')
    if (passwordRef.current.value.length < 6)
      return setError('Le mot de passe doit avoir au moins 6 caractères.')
    setLoading(true)
    try {
      const res = await authService.register({
        firstName: firstNameRef.current.value,
        lastName: lastNameRef.current.value,
        phoneNumber: phoneRef.current.value,
        password: passwordRef.current.value,
      })
      login(res.data)
      navigate('/home')
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de la création du compte.')
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
            {/* FIX #1: Consistent logo filename */}
            <img
              src="/ola-energy-logo.png"
              alt="OLA Energy"
              className="h-10 w-auto object-contain"
              style={{ mixBlendMode: 'multiply' }}
            />
          </div>
          <h1 className="font-bold text-2xl text-gray-900 tracking-tight">SIGN UP</h1>
        </div>

        {error && (
          <div className="mb-5 flex items-center gap-2 bg-red-50 border border-red-200 text-red-700 text-sm rounded-xl px-4 py-3">
            <AlertCircle size={16} /> {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block text-xs font-bold text-gray-700 uppercase tracking-widest mb-2">First Name</label>
              <input ref={firstNameRef} type="text" placeholder="John"
                onChange={updatePreview} className={inputClass} required />
            </div>
            <div>
              <label className="block text-xs font-bold text-gray-700 uppercase tracking-widest mb-2">Last Name</label>
              <input ref={lastNameRef} type="text" placeholder="Doe"
                onChange={updatePreview} className={inputClass} required />
            </div>
          </div>

          {preview && (
            <div className="flex items-center gap-2 bg-blue-50 border border-blue-200 text-blue-700 text-sm rounded-xl px-4 py-2">
              <User size={14} />
              Votre identifiant : <span className="font-semibold">{preview}</span>
            </div>
          )}

          <div>
            <label className="block text-xs font-bold text-gray-700 uppercase tracking-widest mb-2">Phone Number</label>
            <input ref={phoneRef} type="tel" placeholder="+216 XX XXX XXX"
              className={inputClass} />
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

          <div>
            <label className="block text-xs font-bold text-gray-700 uppercase tracking-widest mb-2">Confirm Password</label>
            <div className="relative">
              <input ref={confirmRef} type={showConfirm ? 'text' : 'password'}
                placeholder="Confirm Password" defaultValue="" className={`${inputClass} pr-10`} required />
              <button type="button" onClick={() => setShowConfirm(v => !v)}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
                {showConfirm ? <EyeOff size={18} /> : <Eye size={18} />}
              </button>
            </div>
          </div>

          <button type="submit" disabled={loading}
            className="w-full bg-yellow-500 hover:bg-yellow-600 text-white font-semibold py-3 rounded-xl transition-all flex items-center justify-center gap-2 mt-2">
            {loading ? <span className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin" /> : 'Sign Up'}
          </button>
        </form>

        <p className="text-center text-sm text-gray-500 mt-6">
          Déjà un compte ?{' '}
          <Link to="/login" className="text-yellow-600 font-semibold hover:text-yellow-700">Se connecter</Link>
        </p>
      </div>
    </div>
  )
}
