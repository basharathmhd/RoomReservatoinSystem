import { useState, useEffect } from "react";
import { api } from "../services/api";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "../components/ui/Card";
import { 
  CalendarCheck, 
  Users, 
  Banknote, 
  Home,
  DoorOpen
} from "lucide-react";

export default function ReportsPage() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadReports();
  }, []);

  async function loadReports() {
    try {
      setLoading(true);
      const result = await api.getReports();
      if (result.success) {
        setData(result.data);
      } else {
        setError(result.message);
      }
    } catch (error) {
      setError("Failed to load reports");
      console.error(error);
    } finally {
      setLoading(false);
    }
  }

  if (loading) {
    return (
      <div className="flex h-64 items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-4 bg-destructive/10 text-destructive rounded-xl border border-destructive/20">
        <p className="font-semibold">Error Loading Reports</p>
        <p className="text-sm">{error}</p>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col md:flex-row md:items-end justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight text-foreground">Reports & Analytics</h1>
          <p className="text-muted-foreground mt-1">
            Overview of system performance and key metrics
          </p>
        </div>
      </div>

      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
        
        {/* Total Revenue */}
        <div className="rounded-xl border border-border bg-card p-6 shadow-sm flex flex-col gap-2 relative overflow-hidden group">
          <div className="absolute -right-4 -top-4 w-24 h-24 bg-primary/10 rounded-full group-hover:scale-150 transition-transform duration-500 ease-in-out"></div>
          <div className="flex flex-row items-center justify-between space-y-0 pb-2 relative z-10">
            <h3 className="tracking-tight text-sm font-medium text-muted-foreground">Total Revenue</h3>
            <Banknote className="h-4 w-4 text-emerald-500" />
          </div>
          <div className="relative z-10">
            <div className="text-3xl font-bold">${data?.totalRevenue?.toFixed(2) || '0.00'}</div>
            <p className="text-xs text-emerald-500 font-medium mt-1">Paid accounts only</p>
          </div>
        </div>

        {/* Total Reservations */}
        <div className="rounded-xl border border-border bg-card p-6 shadow-sm flex flex-col gap-2 relative overflow-hidden group">
          <div className="absolute -right-4 -top-4 w-24 h-24 bg-blue-500/10 rounded-full group-hover:scale-150 transition-transform duration-500 ease-in-out"></div>
          <div className="flex flex-row items-center justify-between space-y-0 pb-2 relative z-10">
            <h3 className="tracking-tight text-sm font-medium text-muted-foreground">Total Bookings</h3>
            <CalendarCheck className="h-4 w-4 text-blue-500" />
          </div>
          <div className="relative z-10">
            <div className="text-3xl font-bold">{data?.totalReservations || 0}</div>
            <p className="text-xs text-muted-foreground mt-1">All-time reservations</p>
          </div>
        </div>

        {/* Total Guests */}
        <div className="rounded-xl border border-border bg-card p-6 shadow-sm flex flex-col gap-2 relative overflow-hidden group">
          <div className="absolute -right-4 -top-4 w-24 h-24 bg-orange-500/10 rounded-full group-hover:scale-150 transition-transform duration-500 ease-in-out"></div>
          <div className="flex flex-row items-center justify-between space-y-0 pb-2 relative z-10">
            <h3 className="tracking-tight text-sm font-medium text-muted-foreground">Registered Guests</h3>
            <Users className="h-4 w-4 text-orange-500" />
          </div>
          <div className="relative z-10">
            <div className="text-3xl font-bold">{data?.totalGuests || 0}</div>
            <p className="text-xs text-muted-foreground mt-1">Unique profiles</p>
          </div>
        </div>

        {/* Available Rooms */}
        <div className="rounded-xl border border-border bg-card p-6 shadow-sm flex flex-col gap-2 relative overflow-hidden group">
          <div className="absolute -right-4 -top-4 w-24 h-24 bg-indigo-500/10 rounded-full group-hover:scale-150 transition-transform duration-500 ease-in-out"></div>
          <div className="flex flex-row items-center justify-between space-y-0 pb-2 relative z-10">
            <h3 className="tracking-tight text-sm font-medium text-muted-foreground">Available Rooms</h3>
            <DoorOpen className="h-4 w-4 text-indigo-500" />
          </div>
          <div className="relative z-10">
            <div className="text-3xl font-bold">{data?.availableRooms || 0}</div>
            <p className="text-xs text-muted-foreground mt-1">Ready for occupancy</p>
          </div>
        </div>
        
        {/* Booked Rooms */}
        <div className="rounded-xl border border-border bg-card p-6 shadow-sm flex flex-col gap-2 relative overflow-hidden group">
          <div className="absolute -right-4 -top-4 w-24 h-24 bg-rose-500/10 rounded-full group-hover:scale-150 transition-transform duration-500 ease-in-out"></div>
          <div className="flex flex-row items-center justify-between space-y-0 pb-2 relative z-10">
            <h3 className="tracking-tight text-sm font-medium text-muted-foreground">Booked Rooms</h3>
            <Home className="h-4 w-4 text-rose-500" />
          </div>
          <div className="relative z-10">
            <div className="text-3xl font-bold">{data?.bookedRooms || 0}</div>
            <p className="text-xs flex items-center gap-1 text-muted-foreground mt-1">
              Currently occupied or reserved
            </p>
          </div>
        </div>

      </div>
    </div>
  );
}
