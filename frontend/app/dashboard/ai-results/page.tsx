"use client"

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Eye } from "lucide-react"

const results = [
  {
    id: "RES-001",
    type: "Image Classification",
    status: "Completed",
    confidence: 98.5,
    timestamp: "2024-03-10 14:30:00",
  },
  {
    id: "RES-002",
    type: "Text Analysis",
    status: "Processing",
    confidence: null,
    timestamp: "2024-03-10 14:29:00",
  },
  {
    id: "RES-003",
    type: "Object Detection",
    status: "Completed",
    confidence: 95.2,
    timestamp: "2024-03-10 14:25:00",
  },
  {
    id: "RES-004",
    type: "Sentiment Analysis",
    status: "Failed",
    confidence: null,
    timestamp: "2024-03-10 14:20:00",
  },
]

export default function AIResultsPage() {
  return (
    <div className="space-y-8">
      <div className="flex items-center justify-between space-y-2">
        <h1 className="text-3xl font-bold tracking-tight">AI Results</h1>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Recent Results</CardTitle>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>ID</TableHead>
                <TableHead>Type</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Confidence</TableHead>
                <TableHead>Timestamp</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {results.map((result) => (
                <TableRow key={result.id}>
                  <TableCell className="font-medium">{result.id}</TableCell>
                  <TableCell>{result.type}</TableCell>
                  <TableCell>
                    <Badge
                      variant={
                        result.status === "Completed"
                          ? "default"
                          : result.status === "Processing"
                          ? "secondary"
                          : "destructive"
                      }
                    >
                      {result.status}
                    </Badge>
                  </TableCell>
                  <TableCell>
                    {result.confidence ? `${result.confidence}%` : "-"}
                  </TableCell>
                  <TableCell>{result.timestamp}</TableCell>
                  <TableCell className="text-right">
                    <Button variant="ghost" size="icon">
                      <Eye className="h-4 w-4" />
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  )
} 