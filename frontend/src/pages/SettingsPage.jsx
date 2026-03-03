import { useState } from "react";
import { 
  Settings as SettingsIcon, 
  User as UserIcon, 
  Bell, 
  Shield, 
  Database, 
  Palette,
  Globe,
  Save,
  Trash2,
  Hotel
} from "lucide-react";
import { Button } from "../components/ui/Button";
import { Input } from "../components/ui/Input";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "../components/ui/Card";
import { Badge } from "../components/ui/Badge";

export default function SettingsPage() {
  const [activeTab, setActiveTab] = useState("general");

  const tabs = [
    { id: "general", label: "General", icon: SettingsIcon },
    { id: "profile", label: "My Profile", icon: UserIcon },
    { id: "appearance", label: "Appearance", icon: Palette },
    { id: "security", label: "Security", icon: Shield },
    { id: "notifications", label: "Notifications", icon: Bell },
    { id: "system", label: "System", icon: Database },
  ];

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-3xl font-bold tracking-tight">Settings</h2>
        <p className="text-muted-foreground">Configure resort preferences and system parameters.</p>
      </div>

      <div className="flex flex-col md:flex-row gap-8">
        {/* Sidebar Tabs */}
        <aside className="w-full md:w-64 space-y-1">
          {tabs.map((tab) => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`
                w-full flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-medium transition-all
                ${activeTab === tab.id 
                  ? "bg-primary text-primary-foreground shadow-sm" 
                  : "text-muted-foreground hover:bg-muted hover:text-foreground"
                }
              `}
            >
              <tab.icon className="w-4 h-4" />
              {tab.label}
            </button>
          ))}
        </aside>

        {/* Settings Content */}
        <div className="flex-1 space-y-6">
          {activeTab === "general" && (
            <div className="space-y-6 animate-in fade-in slide-in-from-right-4 duration-300">
              <Card>
                <CardHeader>
                  <CardTitle>Resort Information</CardTitle>
                  <CardDescription>Update the public identity of your establishment.</CardDescription>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="grid grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <label className="text-sm font-medium">Resort Name</label>
                      <Input defaultValue="Ocean View Resort" />
                    </div>
                    <div className="space-y-2">
                      <label className="text-sm font-medium">Primary Contact</label>
                      <Input defaultValue="+1 (555) 123-4567" />
                    </div>
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Address</label>
                    <Input defaultValue="123 Coastal Highway, Seaside Bliss, FL 33139" />
                  </div>
                  <div className="grid grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <label className="text-sm font-medium">Timezone</label>
                      <select className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2">
                        <option>Eastern Standard Time (EST)</option>
                        <option>Pacific Standard Time (PST)</option>
                        <option>Greenwich Mean Time (GMT)</option>
                      </select>
                    </div>
                    <div className="space-y-2">
                      <label className="text-sm font-medium">Currency</label>
                      <select className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2">
                        <option>USD ($)</option>
                        <option>EUR (€)</option>
                        <option>GBP (£)</option>
                      </select>
                    </div>
                  </div>
                </CardContent>
              </Card>

              <Card className="border-destructive/20 bg-destructive/5">
                <CardHeader>
                  <CardTitle className="text-destructive">Maintenance Mode</CardTitle>
                  <CardDescription>Temporarily disable guest check-ins for system maintenance.</CardDescription>
                </CardHeader>
                <CardContent className="flex items-center justify-between">
                  <div>
                    <p className="text-sm font-medium">Mode Status</p>
                    <p className="text-xs text-muted-foreground">Currently: Operational</p>
                  </div>
                  <Button variant="destructive">Activate Maintenance</Button>
                </CardContent>
              </Card>
            </div>
          )}

          {activeTab === "profile" && (
            <div className="space-y-6 animate-in fade-in slide-in-from-right-4 duration-300">
              <Card>
                <CardHeader className="flex flex-row items-center gap-4">
                  <div className="w-16 h-16 rounded-full bg-primary flex items-center justify-center text-white text-2xl font-bold">
                    A
                  </div>
                  <div>
                    <CardTitle>Admin User</CardTitle>
                    <CardDescription>System Administrator Account</CardDescription>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4 pt-4 border-t">
                  <div className="grid grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <label className="text-sm font-medium">Full Name</label>
                      <Input defaultValue="Administrator" />
                    </div>
                    <div className="space-y-2">
                      <label className="text-sm font-medium">Email Address</label>
                      <Input defaultValue="admin@oceanview.com" />
                    </div>
                  </div>
                  <Button className="gap-2">
                    <Save className="w-4 h-4" /> Save Profile
                  </Button>
                </CardContent>
              </Card>
            </div>
          )}

          {activeTab === "system" && (
            <div className="space-y-6 animate-in fade-in slide-in-from-right-4 duration-300">
              <Card>
                <CardHeader>
                  <CardTitle>System Information</CardTitle>
                  <CardDescription>Technical details regarding the application instance.</CardDescription>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="grid grid-cols-2 gap-6">
                    <div className="space-y-1">
                      <p className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">Version</p>
                      <p className="text-lg font-bold">1.2.4-stable</p>
                    </div>
                    <div className="space-y-1">
                      <p className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">Environment</p>
                      <Badge variant="success">PRODUCTION</Badge>
                    </div>
                    <div className="space-y-1">
                      <p className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">Uptime</p>
                      <p className="text-sm">99.98% (34 days, 12:44:10)</p>
                    </div>
                    <div className="space-y-1">
                      <p className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">Platform</p>
                      <p className="text-sm">Java Servlet Container (Tomcat 10.1)</p>
                    </div>
                  </div>
                  <div className="pt-4 border-t flex gap-3">
                    <Button variant="outline" className="gap-2">
                      <Database className="w-4 h-4" /> Backup Database
                    </Button>
                    <Button variant="ghost" className="text-destructive">
                      <Trash2 className="w-4 h-4 mr-2" /> Flush Cache
                    </Button>
                  </div>
                </CardContent>
              </Card>
            </div>
          )}

          {(activeTab === "appearance" || activeTab === "security" || activeTab === "notifications") && (
            <Card className="animate-in fade-in duration-300">
              <CardContent className="py-20 text-center space-y-4">
                <div className="w-16 h-16 rounded-full bg-muted flex items-center justify-center mx-auto">
                   <SettingsIcon className="w-8 h-8 text-muted-foreground animate-spin-slow" />
                </div>
                <div>
                   <h3 className="text-lg font-semibold">Under Construction</h3>
                   <p className="text-sm text-muted-foreground max-w-sm mx-auto">
                     This settings category is currently being refined for the next version of the resort management suite.
                   </p>
                </div>
              </CardContent>
            </Card>
          )}
        </div>
      </div>
    </div>
  );
}
