import React, { useState, useEffect } from "react";
import "../ui/Table.css";
import "./TicketsTable.css"
import { Ticket } from "../../types/application";

const dummyTickets: Ticket[] = [
  {
    id: "TICKET-001",
    title: "Login page not loading on mobile",
    description: "Users report the login page fails to load on iOS Safari browsers",
    status: "IN_PROGRESS",
    priority: "HIGH",
    type: "BUG",
    assigned_to_ids: ["USER-001", "USER-003"],
    project_id: "PROJ-101"
  },
  {
    id: "TICKET-002",
    title: "Implement dark mode toggle",
    description: "Add a user preference for dark/light mode in account settings",
    status: "TODO",
    priority: "MEDIUM",
    type: "FEATURE",
    assigned_to_ids: ["USER-002"],
    project_id: "PROJ-101"
  },
  {
    id: "TICKET-003",
    title: "Database query optimization",
    description: "The reports page is loading slowly due to unoptimized queries",
    status: "BLOCKED",
    priority: "HIGH",
    type: "TASK",
    assigned_to_ids: ["USER-004"],
    project_id: "PROJ-102"
  },
  {
    id: "TICKET-004",
    title: "Password reset email template",
    description: "Update the password reset email to match new branding guidelines",
    status: "DONE",
    priority: "LOW",
    type: "TASK",
    assigned_to_ids: ["USER-005"],
    project_id: "PROJ-103"
  },
  {
    id: "TICKET-005",
    title: "404 error on deleted user profiles",
    description: "When visiting a deleted user's profile, show a proper message instead of 404",
    status: "TODO",
    priority: "MEDIUM",
    type: "BUG",
    assigned_to_ids: ["USER-001"],
    project_id: "PROJ-102"
  }
];

export default function TicketsTable({ tickets: initialTickets }: { tickets: Ticket[] }) {
  const [tickets, setTickets] = useState<Ticket[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Simulate API loading
    const timer = setTimeout(() => {
      // Use provided tickets if available, otherwise use dummy data
      setTickets(initialTickets && initialTickets.length > 0 ? initialTickets : dummyTickets);
      setIsLoading(false);
    }, 500); // Simulate network delay

    return () => clearTimeout(timer);
  }, [initialTickets]);

  if (isLoading) {
    return <div className="loading">Loading tickets...</div>;
  }

  return (
      <div className="tickets-container">
        <div className="table-header">
          <h1>Tickets</h1>
          <div className="table-meta">
            <span>Total: {tickets.length}</span>
            <span>•</span>
            <span>
            Open: {tickets.filter(t => t.status !== "DONE").length}
          </span>
          </div>
        </div>

        <table className="tickets-table" role="grid">
          <thead>
          <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Type</th>
            <th>Status</th>
            <th>Priority</th>
            <th>Assigned To</th>
            <th>Project</th>
          </tr>
          </thead>
          <tbody>
          {tickets.map((ticket) => (
              <tr key={ticket.id} className={`ticket-row status-${ticket.status.toLowerCase()}`}>
                <td className="ticket-id">{ticket.id}</td>
                <td className="ticket-title">
                  <div className="title">{ticket.title}</div>
                  <div className="description">{ticket.description}</div>
                </td>
                <td className="ticket-type">
                <span className={`type-badge ${ticket.type.toLowerCase()}`}>
                  {ticket.type}
                </span>
                </td>
                <td className="ticket-status">
                <span className={`status-badge ${ticket.status.toLowerCase()}`}>
                  {ticket.status}
                </span>
                </td>
                <td className="ticket-priority">
                <span className={`priority-badge ${ticket.priority.toLowerCase()}`}>
                  {ticket.priority}
                </span>
                </td>
                <td className="ticket-assignees">
                  {ticket.assigned_to_ids.length > 0
                      ? ticket.assigned_to_ids.join(", ")
                      : "Unassigned"}
                </td>
                <td className="ticket-project">{ticket.project_id}</td>
              </tr>
          ))}
          </tbody>
        </table>
      </div>
  );
}