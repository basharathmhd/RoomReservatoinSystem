import { useState, useEffect } from "react";
import { api } from "../services/api";
import { 
  Plus, 
  Search, 
  Pencil, 
  Trash2, 
  User, 
  Filter,
  Mail,
  Phone
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
import { Dialog } from "../components/ui/Dialog";

export default function GuestsPage() {
  const [guests, setGuests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({ 
    firstName: "", 
    lastName: "", 
    contactNumber: "", 
    email: "", 
    address: "", 
    identificationNumber: "", 
    nationality: "", 
    dateOfBirth: "" 
  });

  useEffect(() => {
    loadGuests();
  }, []);

  async function loadGuests(q) {
    setLoading(true);
    try {
      const res = await api.getGuests(q);
      setGuests(res.data || []);
    } catch (err) {
      console.error("Failed to load guests:", err);
    } finally {
      setLoading(false);
    }
  }

  function handleSearch(e) {
    e.preventDefault();
    loadGuests(searchTerm);
  }

  function openCreate() {
    setEditing(null);
    setForm({ 
      firstName: "", 
      lastName: "", 
      contactNumber: "", 
      email: "", 
      address: "", 
      identificationNumber: "", 
      nationality: "", 
      dateOfBirth: "" 
    });
    setShowModal(true);
  }

  function openEdit(g) {
    setEditing(g);
    setForm({ 
      firstName: g.firstName, 
      lastName: g.lastName, 
      contactNumber: g.contactNumber || "", 
      email: g.email || "", 
      address: g.address || "", 
      identificationNumber: g.identificationNumber || "", 
      nationality: g.nationality || "", 
      dateOfBirth: g.dateOfBirth || "" 
    });
    setShowModal(true);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      if (editing) {
        await api.updateGuest(editing.guestId, form);
      } else {
        await api.createGuest(form);
      }
      setShowModal(false);
      loadGuests();
    } catch (err) {
      alert(err.message);
    }
  }

  async function handleDelete(id) {
    if (!confirm("Delete this guest?")) return;
    try {
      await api.deleteGuest(id);
      loadGuests();
    } catch (err) {
      alert(err.message);
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-3xl font-bold tracking-tight">Guests</h2>
          <p className="text-muted-foreground">Manage guest profiles and contact information.</p>
        </div>
        <Button onClick={openCreate} className="gap-2">
          <Plus className="w-4 h-4" /> Add Guest
        </Button>
      </div>

      <Card>
        <CardHeader className="pb-3">
          <form onSubmit={handleSearch} className="flex items-center gap-4">
            <div className="relative flex-1 max-w-sm">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder="Search guests..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-9"
              />
            </div>
            <Button type="submit" variant="secondary">Search</Button>
            <Button type="button" variant="outline" size="icon">
              <Filter className="w-4 h-4" />
            </Button>
          </form>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Name</TableHead>
                <TableHead>Contact</TableHead>
                <TableHead>Identification</TableHead>
                <TableHead>Nationality</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={5} className="text-center py-10 text-muted-foreground">Loading guests...</TableCell>
                </TableRow>
              ) : guests.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={5} className="text-center py-10 text-muted-foreground">No guests found.</TableCell>
                </TableRow>
              ) : (
                guests.map((g) => (
                  <TableRow key={g.guestId}>
                    <TableCell>
                      <div className="flex flex-col">
                        <span className="font-medium">{g.firstName} {g.lastName}</span>
                        <div className="flex items-center gap-3 mt-1 text-xs text-muted-foreground">
                          <span className="flex items-center gap-1"><Mail className="w-3 h-3" /> {g.email || "—"}</span>
                        </div>
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center gap-1 text-sm">
                        <Phone className="w-3 h-3 text-muted-foreground" />
                        {g.contactNumber}
                      </div>
                    </TableCell>
                    <TableCell>{g.identificationNumber || "—"}</TableCell>
                    <TableCell>{g.nationality || "—"}</TableCell>
                    <TableCell className="text-right">
                      <div className="flex justify-end gap-2">
                        <Button variant="ghost" size="icon" onClick={() => openEdit(g)}>
                          <Pencil className="w-4 h-4" />
                        </Button>
                        <Button variant="ghost" size="icon" className="text-destructive" onClick={() => handleDelete(g.guestId)}>
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
        title={editing ? "Edit Guest" : "Add Guest"}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <label className="text-sm font-medium">First Name</label>
              <Input required value={form.firstName} onChange={e => setForm({...form, firstName: e.target.value})} />
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">Last Name</label>
              <Input required value={form.lastName} onChange={e => setForm({...form, lastName: e.target.value})} />
            </div>
          </div>
          <div className="space-y-2">
            <label className="text-sm font-medium">Contact Number</label>
            <Input required value={form.contactNumber} onChange={e => setForm({...form, contactNumber: e.target.value})} />
          </div>
          <div className="space-y-2">
            <label className="text-sm font-medium">Email</label>
            <Input type="email" value={form.email} onChange={e => setForm({...form, email: e.target.value})} />
          </div>
          <div className="space-y-2">
            <label className="text-sm font-medium">Address</label>
            <Input value={form.address} onChange={e => setForm({...form, address: e.target.value})} />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <label className="text-sm font-medium">ID Number</label>
              <Input value={form.identificationNumber} onChange={e => setForm({...form, identificationNumber: e.target.value})} />
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">Nationality</label>
              <Input value={form.nationality} onChange={e => setForm({...form, nationality: e.target.value})} />
            </div>
          </div>
          <div className="space-y-2">
            <label className="text-sm font-medium">Date of Birth</label>
            <Input type="date" value={form.dateOfBirth} onChange={e => setForm({...form, dateOfBirth: e.target.value})} />
          </div>
          <div className="flex justify-end gap-3 pt-4">
            <Button type="button" variant="outline" onClick={() => setShowModal(false)}>Cancel</Button>
            <Button type="submit">{editing ? "Update" : "Create"}</Button>
          </div>
        </form>
      </Dialog>
    </div>
  );
}
