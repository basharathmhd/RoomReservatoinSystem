import { useState, useEffect } from "react";
import { api } from "../services/api";
import { 
  Plus, 
  Search, 
  Pencil, 
  Trash2, 
  BedDouble, 
  Filter
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

export default function RoomsPage() {
  const [rooms, setRooms] = useState([]);
  const [roomTypes, setRoomTypes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [confirmDelete, setConfirmDelete] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({ 
    roomNumber: "", 
    typeId: "", 
    floor: "", 
    capacity: "", 
    status: "AVAILABLE", 
    amenities: "" 
  });

  useEffect(() => {
    loadData();
  }, []);

  async function loadData() {
    setLoading(true);
    try {
      const [roomsRes, typesRes] = await Promise.all([
        api.getRooms(),
        api.getRoomTypes()
      ]);
      setRooms(roomsRes.data || []);
      setRoomTypes(typesRes.data || []);
    } catch (err) {
      console.error("Failed to load rooms:", err);
    } finally {
      setLoading(false);
    }
  }

  const filteredRooms = rooms.filter(room => 
    room.roomNumber.toLowerCase().includes(searchTerm.toLowerCase())
  );

  function openCreate() {
    setEditing(null);
    setForm({ roomNumber: "", typeId: "", floor: "", capacity: "", status: "AVAILABLE", amenities: "" });
    setShowModal(true);
  }

  function openEdit(room) {
    setEditing(room);
    setForm({ 
      roomNumber: room.roomNumber, 
      typeId: room.typeId || "", 
      floor: room.floor, 
      capacity: room.capacity, 
      status: room.status, 
      amenities: room.amenities || "" 
    });
    setShowModal(true);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      if (editing) {
        await api.updateRoom(editing.roomId, form);
        toast.success("Room updated successfully");
      } else {
        await api.createRoom(form);
        toast.success("Room created successfully");
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
      await api.deleteRoom(confirmDelete);
      toast.success("Room deleted successfully");
      loadData();
    } catch (err) {
      toast.error(err.message || "Failed to delete room");
    } finally {
      setConfirmDelete(null);
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-3xl font-bold tracking-tight">Rooms</h2>
          <p className="text-muted-foreground">Manage your resort rooms and inventory.</p>
        </div>
        <Button onClick={openCreate} className="gap-2">
          <Plus className="w-4 h-4" /> Add Room
        </Button>
      </div>

      <Card>
        <CardHeader className="pb-3">
          <div className="flex items-center gap-4">
            <div className="relative flex-1 max-w-sm">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder="Search rooms..."
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
                <TableHead>Room #</TableHead>
                <TableHead>Floor</TableHead>
                <TableHead>Capacity</TableHead>
                <TableHead>Status</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={5} className="text-center py-10 text-muted-foreground">Loading rooms...</TableCell>
                </TableRow>
              ) : filteredRooms.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={5} className="text-center py-10 text-muted-foreground">No rooms found.</TableCell>
                </TableRow>
              ) : (
                filteredRooms.map((room) => (
                  <TableRow key={room.roomId}>
                    <TableCell className="font-medium">
                      <div className="flex items-center gap-2">
                        <BedDouble className="w-4 h-4 text-muted-foreground" />
                        <span>{room.roomNumber}</span>
                      </div>
                    </TableCell>
                    <TableCell>{room.floor}</TableCell>
                    <TableCell>{room.capacity}</TableCell>
                    <TableCell>
                      <Badge variant={
                        room.status === "AVAILABLE" ? "success" : 
                        room.status === "OCCUPIED" ? "destructive" : 
                        room.status === "MAINTENANCE" ? "warning" : "secondary"
                      }>
                        {room.status}
                      </Badge>
                    </TableCell>
                    <TableCell className="text-right">
                      <div className="flex justify-end gap-2">
                        <Button variant="ghost" size="icon" onClick={() => openEdit(room)}>
                          <Pencil className="w-4 h-4" />
                        </Button>
                        <Button variant="ghost" size="icon" className="text-destructive" onClick={() => handleDelete(room.roomId)}>
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
        title={editing ? "Edit Room" : "Add Room"}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <label className="text-sm font-medium">Room Number</label>
            <Input 
              required 
              value={form.roomNumber} 
              onChange={e => setForm({...form, roomNumber: e.target.value})} 
              placeholder="e.g. 101"
            />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <label className="text-sm font-medium">Floor</label>
              <select 
                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
                required
                value={form.floor}
                onChange={e => setForm({...form, floor: e.target.value})}
              >
                <option value="">Select Floor</option>
                {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map(f => (
                  <option key={f} value={f}>Floor {f}</option>
                ))}
              </select>
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">Capacity</label>
              <Input 
                type="number" 
                required 
                value={form.capacity} 
                onChange={e => setForm({...form, capacity: e.target.value})} 
              />
            </div>
          </div>
          <div className="space-y-2">
            <label className="text-sm font-medium">Type</label>
            <select 
              className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
              value={form.typeId}
              onChange={e => setForm({...form, typeId: e.target.value})}
              required
            >
              <option value="">Select Type</option>
              {roomTypes.map(t => (
                <option key={t.typeId} value={t.typeId}>{t.typeName}</option>
              ))}
            </select>
          </div>
          <div className="space-y-2">
            <label className="text-sm font-medium">Status</label>
            <select 
              className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
              value={form.status}
              onChange={e => setForm({...form, status: e.target.value})}
            >
              <option value="AVAILABLE">Available</option>
              <option value="OCCUPIED">Occupied</option>
              <option value="MAINTENANCE">Maintenance</option>
              <option value="RESERVED">Reserved</option>
            </select>
          </div>
          <div className="space-y-2">
            <label className="text-sm font-medium">Amenities</label>
            <select 
              className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
              value={form.amenities}
              onChange={e => setForm({...form, amenities: e.target.value})}
            >
              <option value="">Select Amenities</option>
              <option value="WiFi, AC, TV">WiFi, AC, TV</option>
              <option value="WiFi, AC, TV, Mini Bar">WiFi, AC, TV, Mini Bar</option>
              <option value="WiFi, AC, TV, Ocean View">WiFi, AC, TV, Ocean View</option>
              <option value="WiFi, AC, TV, Mini Bar, Ocean View">WiFi, AC, TV, Mini Bar, Ocean View</option>
              <option value="Basic">Basic</option>
              <option value="None">None</option>
            </select>
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
        title="Delete Room"
        message="Are you sure you want to delete this room? This action cannot be undone."
      />
    </div>
  );
}
