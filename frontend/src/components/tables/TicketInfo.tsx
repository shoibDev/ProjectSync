import React from "react";
import { Ticket } from "../../types/application";
import "./TicketInfo.css";

type Props = {
  ticket: Ticket;
};

export default function TicketInfo({ ticket }: Props) {
  return (
      <div className="ticket-info">
        <h2>Ticket Details</h2>
        <div className="ticket-details-grid">
          <div><strong>ID:</strong> <span className="value monospace">{ticket.id}</span></div>
          <div><strong>Status:</strong> <span className={`status-badge ${ticket.status.toLowerCase()}`}>{ticket.status}</span></div>
          <div><strong>Priority:</strong> <span className={`priority-badge ${ticket.priority.toLowerCase()}`}>{ticket.priority}</span></div>
          <div><strong>Type:</strong> <span className={`type-badge ${ticket.type.toLowerCase()}`}>{ticket.type}</span></div>
          <div><strong>Assigned To:</strong> <span className="value">{ticket.assigned_to_ids.join(", ") || "Unassigned"}</span></div>
          <div><strong>Project ID:</strong> <span className="value">{ticket.project_id}</span></div>
        </div>
      </div>
  );
}
