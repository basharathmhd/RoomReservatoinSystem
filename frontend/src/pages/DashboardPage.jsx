import { Card, CardContent, CardHeader, CardTitle } from "../components/ui/Card";
import { BedDouble, Users, CalendarCheck, DollarSign, ArrowUpRight } from "lucide-react";

function StatCard({ icon: Icon, label, value, color, description }) {
  return (
    <Card className="overflow-hidden">
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle className="text-sm font-medium text-muted-foreground">
          {label}
        </CardTitle>
        <div className={`p-2 rounded-lg ${color} bg-opacity-10`}>
          <Icon className={`h-4 w-4 ${color.replace('bg-', 'text-')}`} />
        </div>
      </CardHeader>
      <CardContent>
        <div className="text-2xl font-bold">{value}</div>
        {description && (
          <p className="text-xs text-muted-foreground mt-1 flex items-center gap-1">
            <ArrowUpRight className="h-3 w-3 text-emerald-500" />
            {description}
          </p>
        )}
      </CardContent>
    </Card>
  );
}

export default function DashboardPage() {
  const [stats, setStats] = useState({ rooms: 0, guests: 0, reservations: 0 });

  useEffect(() => {
    loadStats();
  }, []);

  async function loadStats() {
    try {
      const [roomsRes, guestsRes, reservationsRes] = await Promise.allSettled([
        api.getRooms(),
        api.getGuests(),
        api.getReservations(),
      ]);

      setStats({
        rooms: roomsRes.status === 'fulfilled' ? (roomsRes.value.data?.length || 0) : 0,
        guests: guestsRes.status === 'fulfilled' ? (guestsRes.value.data?.length || 0) : 0,
        reservations: reservationsRes.status === 'fulfilled' ? (reservationsRes.value.data?.length || 0) : 0,
      });
    } catch (err) {
      console.error('Failed to load stats:', err);
    }
  }

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-3xl font-bold tracking-tight">Dashboard</h2>
        <p className="text-muted-foreground">
          Overview of Ocean View Resort performance and status.
        </p>
      </div>

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <StatCard 
          icon={BedDouble} 
          label="Total Rooms" 
          value={stats.rooms} 
          color="bg-blue-500" 
          description="2 available now"
        />
        <StatCard 
          icon={Users} 
          label="Active Guests" 
          value={stats.guests} 
          color="bg-emerald-500" 
          description="+12% from last month"
        />
        <StatCard 
          icon={CalendarCheck} 
          label="Reservations" 
          value={stats.reservations} 
          color="bg-violet-500" 
          description="4 arriving today"
        />
        <StatCard 
          icon={DollarSign} 
          label="Revenue" 
          value="$12,450" 
          color="bg-amber-500" 
          description="+8.2% from last week"
        />
      </div>

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-7">
        <Card className="col-span-4">
          <CardHeader>
            <CardTitle>Recent Activity</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <p className="text-sm text-muted-foreground text-center py-8">
                Detailed activity logs will appear here.
              </p>
            </div>
          </CardContent>
        </Card>

        <Card className="col-span-3">
          <CardHeader>
            <CardTitle>Quick Actions</CardTitle>
          </CardHeader>
          <CardContent className="grid gap-2">
            <a href="/reservations" className="flex items-center gap-3 p-3 rounded-md hover:bg-accent transition-colors group">
              <div className="w-8 h-8 rounded-full bg-primary/10 flex items-center justify-center">
                <CalendarCheck className="w-4 h-4 text-primary" />
              </div>
              <span className="text-sm font-medium">New Reservation</span>
            </a>
            <a href="/guests" className="flex items-center gap-3 p-3 rounded-md hover:bg-accent transition-colors group">
              <div className="w-8 h-8 rounded-full bg-emerald-500/10 flex items-center justify-center">
                <Users className="w-4 h-4 text-emerald-500" />
              </div>
              <span className="text-sm font-medium">Add Guest</span>
            </a>
            <a href="/rooms" className="flex items-center gap-3 p-3 rounded-md hover:bg-accent transition-colors group">
              <div className="w-8 h-8 rounded-full bg-violet-500/10 flex items-center justify-center">
                <BedDouble className="w-4 h-4 text-violet-500" />
              </div>
              <span className="text-sm font-medium">Manage Rooms</span>
            </a>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
