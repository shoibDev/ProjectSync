import React, { useState, useEffect } from "react";
import "../ui/Table.css";
import { Ticket } from "../../types/application";
import CreateTicketForm from "../forms/CreateTicketForm";

type ProjectTicketsTableProps = {
  tickets: Ticket[];
  memberIds: string[];
  onSelectTicket?: (ticket: Ticket) => void;
  projectId?: string;
};

export default function ProjectTicketsTable({ 
  tickets: initialTickets,
  memberIds,
  onSelectTicket,
  projectId
}: ProjectTicketsTableProps) {
  const [tickets, setTickets] = useState<Ticket[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);

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
          <button className="create-btn" onClick={() => setIsModalOpen(true)}>
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

        <CreateTicketForm 
          isOpen={isModalOpen} 
          onClose={() => setIsModalOpen(false)} 
          projectId={projectId}
        />
      </div>
  );
}
