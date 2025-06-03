import React, { useState, useEffect } from "react";
import "../ui/Table.css";
import { Modal } from "../ui/Modal.tsx";
import { Ticket } from "../../types/application";

type ProjectTicketsTableProps = {
  tickets: Ticket[];
  onSelectTicket?: (ticket: Ticket) => void;
};

export default function ProjectTicketsTable({ 
  tickets: initialTickets, 
  onSelectTicket 
}: ProjectTicketsTableProps) {
  const [tickets, setTickets] = useState<Ticket[]>([]);

  useEffect(() => {
    setTickets(initialTickets);
  }, [initialTickets]);

  const handleTicketClick = (ticket: Ticket) => {
    if (onSelectTicket) {
      onSelectTicket(ticket);
    }
  };

  return (
      <div className="tickets-table">
        <div className="table-header">
          <h1>Project Tickets</h1>
          <button className="create-btn" onClick={() => console.log("Create Ticket")}>
            Create Ticket
          </button>
        </div>

        <table role="grid">
          <thead>
          <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Description</th>
          </tr>
          </thead>
          <tbody>
          {tickets.map((ticket) => (
              <tr key={ticket.id} onClick={() => handleTicketClick(ticket)} style={{ cursor: "pointer" }}>
                <td>{ticket.id}</td>
                <td>{ticket.title}</td>
                <td>{ticket.description}</td>
              </tr>
          ))}
          </tbody>
        </table>
      </div>
  );
}
