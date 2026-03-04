import { useState, useEffect } from "react";
import { api } from "../services/api";
import { 
  Plus, 
  Search, 
  Pencil, 
  Trash2, 
  User as UserIcon, 
  Shield,
  Fingerprint,
  CheckCircle2,
  XCircle
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

export default function UsersPage() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [confirmDelete, setConfirmDelete] = useState(null);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({ 
    username: "", 
    password: "", 
    fullName: "", 
    role: "STAFF", 
    isActive: "true" 
  });

  useEffect(() => {
    loadUsers();
  }, []);

  async function loadUsers() {
    setLoading(true);
    try {
      const res = await api.getUsers();
      setUsers(res.data || []);
    } catch (err) {
      console.error("Failed to load users:", err);
    } finally {
      setLoading(false);
    }
  }

  const filteredUsers = users.filter(u => 
    u.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    u.username.toLowerCase().includes(searchTerm.toLowerCase())
  );

  function openCreate() {
    setEditing(null);
    setForm({ 
      username: "", 
      password: "", 
      fullName: "", 
      role: "STAFF", 
      isActive: "true" 
    });
    setShowModal(true);
  }

  function openEdit(u) {
    setEditing(u);
    setForm({ 
      username: u.username, 
      password: "", 
      fullName: u.fullName, 
      role: u.role, 
      isActive: String(u.isActive !== false) 
    });
    setShowModal(true);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      const data = { ...form };
      if (editing && !data.password) delete data.password;
      
      if (editing) {
        await api.updateUser(editing.userId, data);
        toast.success("User updated successfully");
      } else {
        await api.createUser(data);
        toast.success("User created successfully");
      }
      setShowModal(false);
      loadUsers();
    } catch (err) {
      toast.error(err.message || "An error occurred");
    }
  }

  function handleDelete(id) {
    setConfirmDelete(id);
  }

  async function confirmDeleteAction() {
    try {
      await api.deleteUser(confirmDelete);
      toast.success("User deleted successfully");
      loadUsers();
    } catch (err) {
      toast.error(err.message || "Failed to delete user");
    } finally {
      setConfirmDelete(null);
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-3xl font-bold tracking-tight">Users</h2>
          <p className="text-muted-foreground">Manage system access and staff roles.</p>
        </div>
        <Button onClick={openCreate} className="gap-2">
          <Plus className="w-4 h-4" /> Add User
        </Button>
      </div>

      <Card>
        <CardHeader className="pb-3">
          <div className="relative flex-1 max-w-sm">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
            <Input
              placeholder="Search users..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-9"
            />
          </div>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>User</TableHead>
                <TableHead>Username</TableHead>
                <TableHead>Role</TableHead>
                <TableHead>Status</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={5} className="text-center py-10 text-muted-foreground">Loading users...</TableCell>
                </TableRow>
              ) : filteredUsers.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={5} className="text-center py-10 text-muted-foreground">No users found.</TableCell>
                </TableRow>
              ) : (
                filteredUsers.map((u) => (
                  <TableRow key={u.userId}>
                    <TableCell>
                      <div className="flex items-center gap-3">
                        <div className="w-8 h-8 rounded-full bg-primary/10 flex items-center justify-center">
                          <UserIcon className="w-4 h-4 text-primary" />
                        </div>
                        <span className="font-medium">{u.fullName}</span>
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center gap-1.5 text-muted-foreground">
                        <Fingerprint className="w-3.5 h-3.5" />
                        {u.username}
                      </div>
                    </TableCell>
                    <TableCell>
                      <Badge variant={u.role === "ADMIN" ? "destructive" : u.role === "MANAGER" ? "warning" : "secondary"}>
                        <Shield className="w-3 h-3 mr-1" />
                        {u.role}
                      </Badge>
                    </TableCell>
                    <TableCell>
                      {u.isActive !== false ? (
                        <div className="flex items-center gap-1.5 text-emerald-600 text-xs font-medium">
                          <CheckCircle2 className="w-3.5 h-3.5" /> Active
                        </div>
                      ) : (
                        <div className="flex items-center gap-1.5 text-destructive text-xs font-medium">
                          <XCircle className="w-3.5 h-3.5" /> Inactive
                        </div>
                      )}
                    </TableCell>
                    <TableCell className="text-right">
                      <div className="flex justify-end gap-2">
                        <Button variant="ghost" size="icon" onClick={() => openEdit(u)}>
                          <Pencil className="w-4 h-4" />
                        </Button>
                        <Button variant="ghost" size="icon" className="text-destructive" onClick={() => handleDelete(u.userId)}>
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
        title={editing ? "Edit User" : "Add User"}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <label className="text-sm font-medium">Full Name</label>
            <Input required value={form.fullName} onChange={e => setForm({...form, fullName: e.target.value})} placeholder="John Doe" />
          </div>
          <div className="space-y-2">
            <label className="text-sm font-medium">Username</label>
            <Input required value={form.username} onChange={e => setForm({...form, username: e.target.value})} placeholder="jdoe" />
          </div>
          <div className="space-y-2">
            <label className="text-sm font-medium">{editing ? "New Password (optional)" : "Password"}</label>
            <Input 
              type="password" 
              value={form.password} 
              onChange={e => setForm({...form, password: e.target.value})} 
              placeholder={editing ? "Leave empty to keep current" : "••••••••"}
              {...(!editing && { required: true })} 
            />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <label className="text-sm font-medium">Role</label>
              <select 
                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
                value={form.role}
                onChange={e => setForm({...form, role: e.target.value})}
              >
                <option value="STAFF">Staff</option>
                <option value="MANAGER">Manager</option>
                <option value="ADMIN">Admin</option>
              </select>
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">Status</label>
              <select 
                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
                value={form.isActive}
                onChange={e => setForm({...form, isActive: e.target.value})}
              >
                <option value="true">Active</option>
                <option value="false">Inactive</option>
              </select>
            </div>
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
        title="Delete User"
        message="Are you sure you want to delete this user? This action cannot be undone."
      />
    </div>
  );
}
