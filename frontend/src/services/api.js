const API_BASE = '/api';

async function request(endpoint, options = {}) {
  const url = `${API_BASE}${endpoint}`;
  const config = {
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    ...options,
  };

  const response = await fetch(url, config);
  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.message || 'Request failed');
  }

  return data;
}

export const api = {
  // Auth
  login: (username, password) =>
    request('/auth/login', { method: 'POST', body: JSON.stringify({ username, password }) }),
  logout: () =>
    request('/auth/logout', { method: 'POST' }),
  validate: () =>
    request('/auth/validate'),
  register: (data) =>
    request('/auth/register', { method: 'POST', body: JSON.stringify(data) }),
  changePassword: (oldPassword, newPassword) =>
    request('/auth/change-password', { method: 'POST', body: JSON.stringify({ oldPassword, newPassword }) }),

  // Rooms
  getRooms: () => request('/rooms'),
  getRoom: (id) => request(`/rooms/${id}`),
  createRoom: (data) => request('/rooms', { method: 'POST', body: JSON.stringify(data) }),
  updateRoom: (id, data) => request(`/rooms/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteRoom: (id) => request(`/rooms/${id}`, { method: 'DELETE' }),
  getRoomTypes: () => request('/room-types'),
  getRoomType: (id) => request(`/room-types/${id}`),
  createRoomType: (data) => request('/room-types', { method: 'POST', body: JSON.stringify(data) }),
  updateRoomType: (id, data) => request(`/room-types/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteRoomType: (id) => request(`/room-types/${id}`, { method: 'DELETE' }),

  // Guests
  getGuests: (search) => request(`/guests${search ? `?search=${encodeURIComponent(search)}` : ''}`),
  getGuest: (id) => request(`/guests/${id}`),
  createGuest: (data) => request('/guests', { method: 'POST', body: JSON.stringify(data) }),
  updateGuest: (id, data) => request(`/guests/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteGuest: (id) => request(`/guests/${id}`, { method: 'DELETE' }),

  // Reservations
  getReservations: () => request('/reservations'),
  getReservation: (id) => request(`/reservations/${id}`),
  createReservation: (data) => request('/reservations', { method: 'POST', body: JSON.stringify(data) }),
  updateReservation: (id, data) => request(`/reservations/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteReservation: (id) => request(`/reservations/${id}`, { method: 'DELETE' }),
  checkAvailability: (roomId, checkIn, checkOut) =>
    request(`/reservations/check-availability?roomId=${roomId}&checkIn=${checkIn}&checkOut=${checkOut}`),

  // Users
  getUsers: () => request('/users'),
  getUser: (id) => request(`/users/${id}`),
  createUser: (data) => request('/users', { method: 'POST', body: JSON.stringify(data) }),
  updateUser: (id, data) => request(`/users/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteUser: (id) => request(`/users/${id}`, { method: 'DELETE' }),

  // Bills
  getBills: () => request('/bills'),
  getBill: (id) => request(`/bills/${id}`),
  createBill: (data) => request('/bills', { method: 'POST', body: JSON.stringify(data) }),
  updateBill: (id, data) => request(`/bills/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteBill: (id) => request(`/bills/${id}`, { method: 'DELETE' }),

  // Payments
  getPayments: () => request('/payments'),
  getPayment: (id) => request(`/payments/${id}`),
  createPayment: (data) => request('/payments', { method: 'POST', body: JSON.stringify(data) }),
  updatePayment: (id, data) => request(`/payments/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deletePayment: (id) => request(`/payments/${id}`, { method: 'DELETE' }),

  // Reports
  getReports: () => request('/reports'),
};
