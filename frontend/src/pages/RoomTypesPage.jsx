import { useState, useEffect } from "react";
import { api } from "../services/api";
import { 
  Plus, 
  Search, 
  Pencil, 
  Trash2, 
  Tag, 
  Filter
} from "lucide-react";
import { Button } from "../components/ui/Button";
import { Input } from "../components/ui/Input";
import { Card, CardContent, CardHeader } from "../components/ui/Card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "../components/ui/Table";
import { Dialog } from "../components/ui/Dialog";
import { ConfirmDialog } from "../components/ui/ConfirmDialog";
import { toast } from "react-hot-toast";

export default function RoomTypesPage() {
  const [types, setTypes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [confirmDelete, setConfirmDelete] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({ 
    typeId: "", 
    typeName: "", 
    baseRate: "", 
    description: "", 
    maxOccupancy: "", 
    amenities: "" 
  });

  useEffect(() => {
    loadData();
  }, []);

  async function loadData() {
    setLoading(true);
    try {
      const res = await api.getRoomTypes();
      setTypes(res.data || []);
    } catch (err) {
      console.error("Failed to load room types:", err);
    } finally {
      setLoading(false);
    }
  }

  const filtered = types.filter(t => 
    t.typeName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  function openCreate() {
    setEditing(null);
    setForm({ typeId: "", typeName: "", baseRate: "", description: "", maxOccupancy: "", amenities: "" });
    setShowModal(true);
  }

  function openEdit(type) {
    setEditing(type);
    setForm({ 
      typeId: type.typeId, 
      typeName: type.typeName, 
      baseRate: type.baseRate, 
      description: type.description || "", 
      maxOccupancy: type.maxOccupancy, 
      amenities: type.amenities || "" 
    });
    setShowModal(true);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      if (editing) {
        await api.updateRoomType(editing.typeId, form);
        toast.success("Room type updated successfully");
      } else {
        await api.createRoomType(form);
        toast.success("Room type created successfully");
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
      await api.deleteRoomType(confirmDelete);
      toast.success("Room type deleted successfully");
      loadData();
    } catch (err) {
      toast.error(err.message || "Failed to delete room type");
    } finally {
      setConfirmDelete(null);
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-3xl font-bold tracking-tight">Room Types</h2>
          <p className="text-muted-foreground">Manage your resort's room categories.</p>
        </div>
        <Button onClick={openCreate} className="gap-2">
          <Plus className="w-4 h-4" /> Add Room Type
        </Button>
      </div>

      <Card>
        <CardHeader className="pb-3">
          <div className="flex items-center gap-4">
            <div className="relative flex-1 max-w-sm">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder="Search types..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-9"
              />
            </div>
          </div>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Type ID</TableHead>
                <TableHead>Name</TableHead>
                <TableHead>Base Rate</TableHead>
                <TableHead>Max Occupancy</TableHead>
                <TableHead>Amenities</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={6} className="text-center py-10 text-muted-foreground">Loading room types...</TableCell>
                </TableRow>
              ) : filtered.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={6} className="text-center py-10 text-muted-foreground">No room types found.</TableCell>
                </TableRow>
              ) : (
                filtered.map((type) => (
                  <TableRow key={type.typeId}>
                    <TableCell className="font-medium">
                      <div className="flex items-center gap-2">
                        <Tag className="w-4 h-4 text-muted-foreground" />
                        <span>{type.typeId}</span>
                      </div>
                    </TableCell>
                    <TableCell>{type.typeName}</TableCell>
                    <TableCell>${type.baseRate}</TableCell>
                    <TableCell>{type.maxOccupancy} Guests</TableCell>
                    <TableCell className="max-w-[150px] truncate" title={type.amenities}>{type.amenities || "None"}</TableCell>
                    <TableCell className="text-right">
                      <div className="flex justify-end gap-2">
                        <Button variant="ghost" size="icon" onClick={() => openEdit(type)}>
                          <Pencil className="w-4 h-4" />
                        </Button>
                        <Button variant="ghost" size="icon" className="text-destructive" onClick={() => handleDelete(type.typeId)}>
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
        title={editing ? "Edit Room Type" : "Add Room Type"}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <label className="text-sm font-medium">Type ID (Unique Identifier)</label>
            <Input 
              required 
              value={form.typeId} 
              disabled={!!editing}
              onChange={e => setForm({...form, typeId: e.target.value.toUpperCase()})} 
              placeholder="e.g. DLX-KNG"
            />
          </div>
          <div className="space-y-2">
            <label className="text-sm font-medium">Type Name</label>
            <Input 
              required 
              value={form.typeName} 
              onChange={e => setForm({...form, typeName: e.target.value})} 
              placeholder="e.g. Deluxe King"
            />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <label className="text-sm font-medium">Base Rate ($)</label>
              <Input 
                type="number" 
                step="0.01"
                required 
                value={form.baseRate} 
                onChange={e => setForm({...form, baseRate: e.target.value})} 
              />
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">Max Occupancy</label>
              <Input 
                type="number" 
                required 
                value={form.maxOccupancy} 
                onChange={e => setForm({...form, maxOccupancy: e.target.value})} 
              />
            </div>
          </div>
          <div className="space-y-2">
            <label className="text-sm font-medium">Description</label>
            <textarea 
              className="flex min-h-[80px] w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
              value={form.description} 
              onChange={e => setForm({...form, description: e.target.value})} 
              placeholder="Room description..."
            />
          </div>
          <div className="space-y-2">
            <label className="text-sm font-medium">Amenities</label>
            <Input 
              value={form.amenities} 
              onChange={e => setForm({...form, amenities: e.target.value})} 
              placeholder="Ocean View, King Bed, Mini Bar..."
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
        title="Delete Room Type"
        message="Are you sure you want to delete this room type? This action cannot be undone."
      />
    </div>
  );
}
