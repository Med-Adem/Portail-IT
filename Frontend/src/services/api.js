import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
})

// Intercepteur : ajouter le token JWT à chaque requête
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// Intercepteur : si 401, vider le token et rediriger vers login
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// ===== AUTH =====
export const authService = {
  login:    (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
}

// ===== SUPPORT TICKETS =====
export const ticketService = {
  create:       (data) => api.post('/tickets', data),
  getMyTickets: ()     => api.get('/tickets/my'),
  getAll:       ()     => api.get('/tickets'),
  getById:      (id)   => api.get(`/tickets/${id}`),
  updateStatus: (id, data) => api.patch(`/tickets/${id}/status`, data),
  delete:       (id)   => api.delete(`/tickets/${id}`),
}

// ===== SOFTWARE REQUESTS =====
export const softwareService = {
  create:     (data) => api.post('/software-requests', data),
  getMyReqs:  ()     => api.get('/software-requests/my'),
  getAll:     ()     => api.get('/software-requests'),
  updateStatus: (id, data) => api.patch(`/software-requests/${id}/status`, data),
  delete:     (id)   => api.delete(`/software-requests/${id}`),
}

// ===== HARDWARE =====
export const hardwareService = {
  submit:    (data) => api.post('/hardware', data),
  getMyDevices: () => api.get('/hardware/my'),
  getAll:    ()    => api.get('/hardware'),
  delete:    (id)  => api.delete(`/hardware/${id}`),
}

// ===== GUIDES =====
export const guideService = {
  getAll:   ()         => api.get('/guides'),
  upload:   (formData) => api.post('/guides', formData, { headers: { 'Content-Type': 'multipart/form-data' } }),
  download: (id)       => api.get(`/guides/${id}/download`, { responseType: 'blob' }),
  delete:   (id)       => api.delete(`/guides/${id}`),
}

export default api
