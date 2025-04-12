import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Activity, Brain, Server } from "lucide-react"

export default function OverviewPage() {
  return (
    <div className="space-y-8 p-6">
      <div className="flex items-center justify-between space-y-2">
        <h1 className="text-4xl font-bold tracking-tight">Dashboard Overview</h1>
      </div>
      
      <div className="grid gap-6 grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
        <Card className="hover:bg-card/80 transition-colors">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-3">
            <CardTitle className="text-base font-semibold">AI Processing Status</CardTitle>
            <Brain className="h-5 w-5 text-primary" />
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-primary">Active</div>
            <p className="text-sm text-muted-foreground mt-1">
              System is running normally
            </p>
          </CardContent>
        </Card>

        <Card className="hover:bg-card/80 transition-colors">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-3">
            <CardTitle className="text-base font-semibold">Recent Results</CardTitle>
            <Activity className="h-5 w-5 text-primary" />
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-primary">24</div>
            <p className="text-sm text-muted-foreground mt-1">
              Processed in last hour
            </p>
          </CardContent>
        </Card>

        <Card className="hover:bg-card/80 transition-colors">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-3">
            <CardTitle className="text-base font-semibold">System Health</CardTitle>
            <Server className="h-5 w-5 text-primary" />
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-primary">98%</div>
            <p className="text-sm text-muted-foreground mt-1">
              All systems operational
            </p>
          </CardContent>
        </Card>
      </div>

      <div className="grid gap-6 grid-cols-1 md:grid-cols-2 lg:grid-cols-7">
        <Card className="col-span-full lg:col-span-4 hover:bg-card/80 transition-colors">
          <CardHeader>
            <CardTitle className="text-xl font-semibold">Recent Activity</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-5">
              {[
                "Model training completed",
                "New data batch processed",
                "System update installed",
              ].map((activity, i) => (
                <div key={i} className="flex items-center gap-4">
                  <div className="h-2.5 w-2.5 rounded-full bg-primary animate-pulse" />
                  <div className="text-base">{activity}</div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        <Card className="col-span-full lg:col-span-3 hover:bg-card/80 transition-colors">
          <CardHeader>
            <CardTitle className="text-xl font-semibold">System Resources</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-6">
              <div className="space-y-2">
                <div className="text-base font-medium">CPU Usage</div>
                <div className="h-2.5 rounded-full bg-secondary">
                  <div className="h-full w-1/2 rounded-full bg-primary transition-all duration-500" />
                </div>
              </div>
              <div className="space-y-2">
                <div className="text-base font-medium">Memory Usage</div>
                <div className="h-2.5 rounded-full bg-secondary">
                  <div className="h-full w-3/4 rounded-full bg-primary transition-all duration-500" />
                </div>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
} 