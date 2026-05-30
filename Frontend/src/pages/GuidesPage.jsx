import { useState, useEffect } from 'react'
import Navbar from '../components/layout/Navbar'
import { guideService } from '../services/api'
import { useAuth } from '../context/AuthContext'
import { FileText, Download, Trash2, Upload, Loader2 } from 'lucide-react'

export default function GuidesPage() {
  const { isAdmin } = useAuth()
  const [guides, setGuides]           = useState([])
  const [loading, setLoading]         = useState(false)
  const [uploading, setUploading]     = useState(false)
  const [uploadForm, setUploadForm]   = useState({ title: '', file: null })
  const [error, setError]             = useState('')
  const [success, setSuccess]         = useState('')

  const fetchGuides = async () => {
    setLoading(true)
    try {
      const res = await guideService.getAll()
      setGuides(res.data)
    } catch { setError('Erreur lors du chargement des guides.') }
    finally { setLoading(false) }
  }

  useEffect(() => { fetchGuides() }, [])

  const handleDownload = async (guide) => {
    try {
      const res = await guideService.download(guide.id)
      const url = window.URL.createObjectURL(new Blob([res.data], { type: 'application/pdf' }))
      const a   = document.createElement('a')
      a.href    = url
      a.download = guide.fileName
      a.click()
      window.URL.revokeObjectURL(url)
    } catch { setError('Erreur lors du téléchargement.') }
  }

  const handleUpload = async (e) => {
    e.preventDefault()
    if (!uploadForm.file || !uploadForm.title) return
    setUploading(true)
    setError('')
    try {
      const fd = new FormData()
      fd.append('title', uploadForm.title)
      fd.append('file',  uploadForm.file)
      await guideService.upload(fd)
      setUploadForm({ title: '', file: null })
      setSuccess('Guide uploadé avec succès !')
      fetchGuides()
      setTimeout(() => setSuccess(''), 3000)
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur upload.')
    } finally { setUploading(false) }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Supprimer ce guide ?')) return
    try {
      await guideService.delete(id)
      fetchGuides()
    } catch { setError('Erreur lors de la suppression.') }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-amber-50 via-orange-50 to-yellow-50">
      <Navbar />
      <div className="max-w-4xl mx-auto px-6 py-10">

        <h1 className="font-display font-bold text-3xl text-gray-900 mb-8">
          IT Guides & Documentation
        </h1>

        {error   && <div className="mb-5 bg-red-50 text-red-700 text-sm rounded-xl px-4 py-3 border border-red-200">{error}</div>}
        {success && <div className="mb-5 bg-green-50 text-green-700 text-sm rounded-xl px-4 py-3 border border-green-200">{success}</div>}

        {/* Admin upload form */}
        {isAdmin() && (
          <div className="card mb-8">
            <h2 className="font-display font-bold text-lg text-gray-900 mb-4 flex items-center gap-2">
              <Upload size={18} className="text-gold" /> Upload a Guide
            </h2>
            <form onSubmit={handleUpload} className="flex flex-wrap gap-4 items-end">
              <div className="flex-1 min-w-48">
                <label className="block text-sm font-medium text-gray-700 mb-1">Title</label>
                <input
                  value={uploadForm.title}
                  onChange={e => setUploadForm(f => ({ ...f, title: e.target.value }))}
                  placeholder="Ex: Printer Guide"
                  className="input"
                  required
                />
              </div>
              <div className="flex-1 min-w-48">
                <label className="block text-sm font-medium text-gray-700 mb-1">PDF File</label>
                <input
                  type="file" accept=".pdf"
                  onChange={e => setUploadForm(f => ({ ...f, file: e.target.files[0] }))}
                  className="input py-2 text-sm cursor-pointer file:mr-3 file:py-1 file:px-3 file:rounded-lg file:border-0 file:text-sm file:bg-gold/10 file:text-gold hover:file:bg-gold/20"
                  required
                />
              </div>
              <button type="submit" disabled={uploading}
                className="btn-primary flex items-center gap-2 py-3">
                {uploading ? <Loader2 size={16} className="animate-spin" /> : <Upload size={16} />}
                Upload
              </button>
            </form>
          </div>
        )}

        {/* Guides List */}
        {loading ? (
          <div className="flex justify-center py-12">
            <Loader2 size={32} className="animate-spin text-gold" />
          </div>
        ) : guides.length === 0 ? (
          <div className="text-center py-16 text-gray-400">
            <FileText size={48} className="mx-auto mb-4 opacity-30" />
            Aucun guide disponible.
          </div>
        ) : (
          <div className="space-y-3">
            {guides.map(guide => (
              <div key={guide.id}
                className="bg-white rounded-2xl border border-gray-100 shadow-card px-6 py-4 flex items-center justify-between hover:shadow-card-hover transition-shadow group">
                <div className="flex items-center gap-4">
                  {/* PDF icon */}
                  <div className="w-10 h-10 bg-red-50 rounded-xl flex items-center justify-center flex-shrink-0">
                    <FileText size={20} className="text-red-500" />
                  </div>
                  <div>
                    <span className="font-medium text-gray-900">{guide.title}</span>
                    {guide.fileSize > 0 && (
                      <p className="text-xs text-gray-400">
                        {(guide.fileSize / 1024).toFixed(0)} KB
                      </p>
                    )}
                  </div>
                </div>

                <div className="flex items-center gap-2">
                  <button onClick={() => handleDownload(guide)}
                    className="flex items-center gap-1.5 text-sm text-gray-600 hover:text-gold font-medium px-3 py-1.5 rounded-lg hover:bg-gold/5 transition-colors">
                    <Download size={16} /> Download
                  </button>
                  {isAdmin() && (
                    <button onClick={() => handleDelete(guide.id)}
                      className="p-2 text-gray-400 hover:text-red-500 hover:bg-red-50 rounded-lg transition-colors">
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
  )
}
