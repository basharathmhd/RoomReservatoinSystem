import { useState, useEffect } from "react";
import { api } from "../services/api";
import { 
  Plus, 
  Search, 
  Pencil, 
  Trash2, 
  Calendar, 
  Filter,
  User,
  BedDouble,
  Clock
} from "lucide-react";
import { Button } from "../components/ui/Button";
import { Input } from "../components/ui/Input";
import { Card, CardContent, CardHeader, CardTitle } from "../components/ui/Card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "../components/ui/Table";
import { Badge } from "../components/ui/Badge";
import { Dialog } from "../components/ui/Dialog";
import { ConfirmDialog } from "../components/ui/ConfirmDialog";
import { toast } from "react-hot-toast";

export default function ReservationsPage() {
  const [reservations, setReservations] = useState([]);
  const [guests, setGuests] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [confirmDelete, setConfirmDelete] = useState(null);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({ 
    guestId: "", 
    roomId: "", 
    checkInDate: "", 
    checkOutDate: "", 
    numberOfGuests: "", 
    specialRequests: "", 
    status: "CONFIRMED" 
  });

  const today = new Date().toISOString().split('T')[0];

  useEffect(() => {
    loadData();
  }, []);

  async function loadData() {
    setLoading(true);
    try {
      const [resRes, guestsRes, roomsRes] = await Promise.all([
        api.getReservations(),
        api.getGuests(),
        api.getRooms()
      ]);
      setReservations(resRes.data || []);
      setGuests(guestsRes.data || []);
      setRooms(roomsRes.data || []);
    } catch (err) {
      console.error("Failed to load reservations data:", err);
    } finally {
      setLoading(false);
    }
  }

  const filteredReservations = reservations.filter(r => 
    r.reservationNumber.toLowerCase().includes(searchTerm.toLowerCase()) ||
    r.guestId.toLowerCase().includes(searchTerm.toLowerCase())
  );

  function openCreate() {
    setEditing(null);
    setForm({ 
      guestId: "", 
      roomId: "", 
      checkInDate: "", 
      checkOutDate: "", 
      numberOfGuests: "", 
      specialRequests: "", 
      status: "CONFIRMED" 
    });
    setShowModal(true);
  }

  function openEdit(r) {
    setEditing(r);
    setForm({ 
      guestId: r.guestId, 
      roomId: r.roomId, 
      checkInDate: r.checkInDate || "", 
      checkOutDate: r.checkOutDate || "", 
      numberOfGuests: r.numberOfGuests || "", 
      specialRequests: r.specialRequests || "", 
      status: r.status 
    });
    setShowModal(true);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      if (editing) {
        await api.updateReservation(editing.reservationNumber, form);
        toast.success("Reservation updated successfully");
      } else {
        await api.createReservation(form);
        toast.success("Reservation created successfully");
      }
      setShowModal(false);
      loadData();
    } catch (err) {
      toast.error(err.message || "An error occurred");
    }
  }

  function handleDelete(id) {
    setConfirmDelete(id);
  }

  async function confirmDeleteAction() {
    try {
      await api.deleteReservation(confirmDelete);
      toast.success("Reservation cancelled successfully");
      loadData();
    } catch (err) {
      toast.error(err.message || "Failed to cancel reservation");
    } finally {
      setConfirmDelete(null);
    }
  }

  const getGuestName = (id) => {
    const g = guests.find(g => g.guestId === id);
    return g ? `${g.firstName} ${g.lastName}` : id;
  };

  const getRoomNumber = (id) => {
    const r = rooms.find(r => r.roomId === id);
    return r ? r.roomNumber : id;
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-3xl font-bold tracking-tight">Reservations</h2>
          <p className="text-muted-foreground">Manage guest stays and room availability.</p>
        </div>
        <Button onClick={openCreate} className="gap-2">
          <Plus className="w-4 h-4" /> New Reservation
        </Button>
      </div>

      <Card>
        <CardHeader className="pb-3">
          <div className="flex items-center gap-4">
            <div className="relative flex-1 max-w-sm">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder="Search reservations..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-9"
              />
            </div>
            <Button variant="outline" size="icon">
              <Filter className="w-4 h-4" />
            </Button>
          </div>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Res # / Guest</TableHead>
                <TableHead>Room</TableHead>
                <TableHead>Dates</TableHead>
                <TableHead>Guests</TableHead>
                <TableHead>Status</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={6} className="text-center py-10 text-muted-foreground">Loading reservations...</TableCell>
                </TableRow>
              ) : filteredReservations.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={6} className="text-center py-10 text-muted-foreground">No reservations found.</TableCell>
                </TableRow>
              ) : (
                filteredReservations.map((r) => (
                  <TableRow key={r.reservationNumber}>
                    <TableCell>
                      <div className="flex flex-col">
                        <span className="font-mono text-xs text-muted-foreground">{r.reservationNumber}</span>
                        <span className="font-medium flex items-center gap-1 mt-0.5">
                          <User className="w-3 h-3" /> {getGuestName(r.guestId)}
                        </span>
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center gap-1">
                        <BedDouble className="w-3 h-3 text-muted-foreground" />
                        <span>{getRoomNumber(r.roomId)}</span>
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="flex flex-col text-xs">
                        <span className="flex items-center gap-1"><Clock className="w-3 h-3" /> In: {r.checkInDate}</span>
                        <span className="flex items-center gap-1"><Clock className="w-3 h-3" /> Out: {r.checkOutDate}</span>
                      </div>
                    </TableCell>
                    <TableCell>{r.numberOfGuests}</TableCell>
                    <TableCell>
                      <Badge variant={
                        r.status === "CONFIRMED" ? "secondary" : 
                        r.status === "CHECKED_IN" ? "success" : 
                        r.status === "CHECKED_OUT" ? "outline" : "destructive"
                      }>
                        {r.status}
                      </Badge>
                    </TableCell>
                    <TableCell className="text-right">
                      <div className="flex justify-end gap-2">
                        <Button variant="ghost" size="icon" onClick={() => openEdit(r)}>
                          <Pencil className="w-4 h-4" />
                        </Button>
                        <Button variant="ghost" size="icon" className="text-destructive" onClick={() => handleDelete(r.reservationNumber)}>
                          <Trash2 className="w-4 h-4" />
                        </Button>
                      </div>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      <Dialog 
        isOpen={showModal} 
        onClose={() => setShowModal(false)} 
        title={editing ? "Edit Reservation" : "New Reservation"}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <label className="text-sm font-medium">Guest</label>
            <select 
              className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
              value={form.guestId}
              onChange={e => setForm({...form, guestId: e.target.value})}
              required
            >
              <option value="">Select Guest</option>
              {guests.map(g => (
                <option key={g.guestId} value={g.guestId}>{g.firstName} {g.lastName}</option>
              ))}
            </select>
          </div>
          <div className="space-y-2">
            <label className="text-sm font-medium">Room</label>
            <select 
              className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
              value={form.roomId}
              onChange={e => setForm({...form, roomId: e.target.value})}
              required
            >
              <option value="">Select Room</option>
              {rooms.map(room => (
                <option key={room.roomId} value={room.roomId}>Room {room.roomNumber} ({room.status})</option>
              ))}
            </select>
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <label className="text-sm font-medium">Check-in Date</label>
              <Input type="date" required min={today} value={form.checkInDate} onChange={e => setForm({...form, checkInDate: e.target.value})} />
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">Check-out Date</label>
              <Input type="date" required min={form.checkInDate || today} value={form.checkOutDate} onChange={e => setForm({...form, checkOutDate: e.target.value})} />
            </div>
          </div>
          <div className="space-y-2">
            <label className="text-sm font-medium">Number of Guests</label>
            <Input type="number" required value={form.numberOfGuests} onChange={e => setForm({...form, numberOfGuests: e.target.value})} />
          </div>
          {editing && (
            <div className="space-y-2">
              <label className="text-sm font-medium">Status</label>
              <select 
                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
                value={form.status}
                onChange={e => setForm({...form, status: e.target.value})}
              >
                <option value="CONFIRMED">Confirmed</option>
                <option value="CHECKED_IN">Checked In</option>
                <option value="CHECKED_OUT">Checked Out</option>
                <option value="CANCELLED">Cancelled</option>
              </select>
            </div>
          )}
          <div className="space-y-2">
            <label className="text-sm font-medium">Special Requests</label>
            <textarea 
              className="flex min-h-[80px] w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
              value={form.specialRequests} 
              onChange={e => setForm({...form, specialRequests: e.target.value})} 
              placeholder="Any specific requests or requirements..."
            />
          </div>
          <div className="flex justify-end gap-3 pt-4">
            <Button type="button" variant="outline" onClick={() => setShowModal(false)}>Cancel</Button>
            <Button type="submit">{editing ? "Update" : "Create"}</Button>
          </div>
        </form>
      </Dialog>

      <ConfirmDialog 
        isOpen={!!confirmDelete}
        onClose={() => setConfirmDelete(null)}
        onConfirm={confirmDeleteAction}
        title="Cancel Reservation"
        message="Are you sure you want to cancel this reservation? This action cannot be undone."
      />
    </div>
  );
}
