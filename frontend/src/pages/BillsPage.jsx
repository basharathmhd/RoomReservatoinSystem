import { useState, useEffect } from "react";
import { api } from "../services/api";
import { 
  Receipt, 
  Plus, 
  Search, 
  Eye, 
  DollarSign,
  Calendar,
  Clock,
  ArrowRight
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
import { toast } from "react-hot-toast";

export default function BillsPage() {
  const [bills, setBills] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [viewingBill, setViewingBill] = useState(null);
  const [formData, setFormData] = useState({
    reservationNumber: "",
    roomCharges: "",
    serviceCharges: "",
    taxAmount: "",
    discountAmount: "",
    notes: "",
    status: "UNPAID",
    issueDate: new Date().toISOString().split('T')[0]
  });

  useEffect(() => {
    loadData();
  }, []);

  async function loadData() {
    setLoading(true);
    try {
      const [billsRes, resRes] = await Promise.all([
        api.getBills(),
        api.getReservations()
      ]);
      setBills(billsRes.data || []);
      setReservations(resRes.data || []);
    } catch (err) {
      console.error("Failed to load bills:", err);
    } finally {
      setLoading(false);
    }
  }

  const filteredBills = bills.filter(b => 
    b.billId.toLowerCase().includes(searchTerm.toLowerCase()) ||
    b.reservationNumber.toLowerCase().includes(searchTerm.toLowerCase())
  );

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      await api.createBill(formData);
      toast.success("Bill generated successfully");
      setShowModal(false);
      loadData();
    } catch (err) {
      toast.error("Failed to create bill: " + err.message);
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-3xl font-bold tracking-tight">Bills & Invoices</h2>
          <p className="text-muted-foreground">Monitor billing cycles and outstanding guest balances.</p>
        </div>
        <Button onClick={() => setShowModal(true)} className="gap-2">
          <Plus className="w-4 h-4" /> Generate New Bill
        </Button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card className="bg-emerald-50/50 border-emerald-100">
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-emerald-600">Total Paid</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">$42,500.00</div>
          </CardContent>
        </Card>
        <Card className="bg-amber-50/50 border-amber-100">
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-amber-600">Outstanding</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">$12,840.50</div>
          </CardContent>
        </Card>
        <Card className="bg-blue-50/50 border-blue-100">
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-blue-600">Tax Collected</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">$5,620.00</div>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader className="pb-3">
          <div className="relative flex-1 max-w-sm">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
            <Input
              placeholder="Search bill ID or reservation..."
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
                <TableHead>Bill ID</TableHead>
                <TableHead>Reservation</TableHead>
                <TableHead>Date</TableHead>
                <TableHead>Total Amount</TableHead>
                <TableHead>Status</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={6} className="text-center py-10 text-muted-foreground">Loading bills...</TableCell>
                </TableRow>
              ) : filteredBills.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={6} className="text-center py-10 text-muted-foreground">No bills found.</TableCell>
                </TableRow>
              ) : (
                filteredBills.map((bill) => (
                  <TableRow key={bill.billId}>
                    <TableCell className="font-mono text-xs">{bill.billId}</TableCell>
                    <TableCell>
                      <div className="flex items-center gap-1.5">
                        <Calendar className="w-3 h-3 text-muted-foreground" />
                        <span className="font-mono text-xs">{bill.reservationNumber}</span>
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center gap-1.5 text-xs">
                        <Clock className="w-3 h-3 text-muted-foreground" />
                        {bill.issueDate}
                      </div>
                    </TableCell>
                    <TableCell className="font-semibold text-primary">
                      ${bill.totalAmount?.toFixed(2)}
                    </TableCell>
                    <TableCell>
                      <Badge variant={bill.status === "PAID" ? "success" : "warning"}>
                        {bill.status}
                      </Badge>
                    </TableCell>
                    <TableCell className="text-right">
                      <Button variant="ghost" size="sm" className="gap-1.5 text-xs" onClick={() => setViewingBill(bill)}>
                        <Eye className="w-3.5 h-3.5" /> View
                      </Button>
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
        title="Generate New Bill"
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <label className="text-sm font-medium">Select Reservation</label>
            <select 
              className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
              value={formData.reservationNumber}
              onChange={e => setFormData({...formData, reservationNumber: e.target.value})}
              required
            >
              <option value="">Choose reservation...</option>
              {reservations.map(res => (
                <option key={res.reservationNumber} value={res.reservationNumber}>
                  {res.reservationNumber} (Guest: {res.guestId})
                </option>
              ))}
            </select>
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <label className="text-sm font-medium">Issue Date</label>
              <Input type="date" value={formData.issueDate} onChange={e => setFormData({...formData, issueDate: e.target.value})} />
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">Status</label>
              <select 
                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
                value={formData.status}
                onChange={e => setFormData({...formData, status: e.target.value})}
              >
                <option value="UNPAID">Unpaid</option>
                <option value="PAID">Paid</option>
              </select>
            </div>
          </div>
          <div className="grid grid-cols-2 gap-4 border-t pt-4">
            <div className="space-y-1">
              <label className="text-xs font-medium text-muted-foreground uppercase">Room Charges</label>
              <Input type="number" step="0.01" required value={formData.roomCharges} onChange={e => setFormData({...formData, roomCharges: e.target.value})} />
            </div>
            <div className="space-y-1">
              <label className="text-xs font-medium text-muted-foreground uppercase">Service Charges</label>
              <Input type="number" step="0.01" value={formData.serviceCharges} onChange={e => setFormData({...formData, serviceCharges: e.target.value})} />
            </div>
            <div className="space-y-1">
              <label className="text-xs font-medium text-muted-foreground uppercase">Discount</label>
              <Input type="number" step="0.01" value={formData.discountAmount} onChange={e => setFormData({...formData, discountAmount: e.target.value})} />
            </div>
          </div>
          <div className="space-y-2 pt-2">
            <label className="text-sm font-medium">Notes</label>
            <textarea 
              className="flex min-h-[60px] w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
              value={formData.notes} 
              onChange={e => setFormData({...formData, notes: e.target.value})} 
              placeholder="Any additional billing notes..."
            />
          </div>
          <div className="flex justify-end gap-3 pt-4">
            <Button type="button" variant="outline" onClick={() => setShowModal(false)}>Cancel</Button>
            <Button type="submit">Create Bill</Button>
          </div>
        </form>
      </Dialog>

      <Dialog
        isOpen={!!viewingBill}
        onClose={() => setViewingBill(null)}
        title="Invoice Details"
      >
        {viewingBill && (
          <div className="space-y-6 print:block">
            <div className="flex items-center justify-between border-b pb-4">
              <div>
                <h3 className="font-bold text-lg">Ocean View Resort</h3>
                <p className="text-sm text-muted-foreground">123 Coastal Highway</p>
              </div>
              <div className="text-right">
                <h4 className="font-bold">INVOICE</h4>
                <p className="text-sm text-muted-foreground font-mono">{viewingBill.billId}</p>
              </div>
            </div>
            
            <div className="grid grid-cols-2 gap-4 text-sm">
              <div>
                <p className="text-muted-foreground">Billed To Configuration:</p>
                <p className="font-medium">Reservation: {viewingBill.reservationNumber}</p>
                <p>Status: {viewingBill.status}</p>
              </div>
              <div className="text-right">
                <p className="text-muted-foreground">Issue Date:</p>
                <p className="font-medium">{viewingBill.issueDate}</p>
              </div>
            </div>

            <div className="border rounded-md overflow-hidden">
              <Table>
                <TableHeader className="bg-muted">
                  <TableRow>
                    <TableHead>Description</TableHead>
                    <TableHead className="text-right">Amount</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  <TableRow>
                    <TableCell>Room Charges</TableCell>
                    <TableCell className="text-right">${viewingBill.roomCharges?.toFixed(2) || '0.00'}</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Service Charges</TableCell>
                    <TableCell className="text-right">${viewingBill.serviceCharges?.toFixed(2) || '0.00'}</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Discount Applied</TableCell>
                    <TableCell className="text-right text-emerald-600">-${viewingBill.discountAmount?.toFixed(2) || '0.00'}</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell className="font-medium">Subtotal</TableCell>
                    <TableCell className="text-right font-medium">
                       ${((viewingBill.roomCharges || 0) + (viewingBill.serviceCharges || 0) - (viewingBill.discountAmount || 0)).toFixed(2)}
                    </TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Tax Amount</TableCell>
                    <TableCell className="text-right">${viewingBill.taxAmount?.toFixed(2) || '0.00'}</TableCell>
                  </TableRow>
                  <TableRow className="bg-muted/50">
                    <TableCell className="font-bold">Total Final Amount</TableCell>
                    <TableCell className="text-right font-bold text-primary">${viewingBill.finalAmount?.toFixed(2) || viewingBill.totalAmount?.toFixed(2) || '0.00'}</TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </div>

            <div className="flex justify-end gap-3 pt-4 print:hidden">
              <Button variant="outline" onClick={() => window.print()}>Print Invoice</Button>
              <Button onClick={() => setViewingBill(null)}>Close</Button>
            </div>
          </div>
        )}
      </Dialog>
    </div>
  );
}
