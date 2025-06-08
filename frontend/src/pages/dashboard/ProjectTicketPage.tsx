import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { useHeader } from "../../contexts/HeaderContext";
import {User, Project, Ticket} from "../../types/application";
import { getProjectById } from "../../api/projects.ts";
import ProjectMemberTable from "../../components/tables/ProjectMemberTable.tsx";
import ProjectTicketsTable from "../../components/tables/ProjectTicketsTable.tsx";
import TicketInfo from "../../components/tables/TicketInfo.tsx";
import "../../components/tables/TicketInfo.css";
import "./ProjectTicketPage.css";

export default function ProjectTicketPage() {
  const { projectId } = useParams<{ projectId: string }>();
  const { setTitle } = useHeader();
  const [project, setProject] = useState<Project | null>(null);
  const [tickets, setTickets] = useState<Ticket[]>([]);
  const [selectedTicket, setSelectedTicket] = useState<Ticket | null>(null);


  useEffect(() => {
    setTitle("Project Tickets");
    const fetchProject = async () => {
      try {
        if (projectId) {
          const projectData = await getProjectById(projectId);
          setProject(projectData);
          setTickets(projectData.tickets || []);
        } else {
          console.log("Project ID is not provided");
        }
      } catch (error) {
        console.error("Error fetching project:", error);
      }
    };
    fetchProject();
  }, [projectId, setTitle]);

  const handleSelectTicket = (ticket: Ticket) => {
    setSelectedTicket(ticket);
  };

  if (!project) {
    return <div>Loading...</div>;
  }

  return (
    <div className="project-tickets-page">
      <div className="project-header">
        <h1>{project.name} - Tickets</h1>
        <p>{project.description}</p>
      </div>

      <div className="project-grid">
        <div className="member-table-container">
          <ProjectMemberTable
            members={project.assignedToIds || []}
            projectId={project.id}
          />
        </div>

        <div className="tickets-table-container">
          <ProjectTicketsTable
            tickets={tickets}
            memberIds={project.assignedToIds || []}
            onSelectTicket={handleSelectTicket}
            projectId={project.id}
          />
        </div>

        <div className="project-info-container">
          {selectedTicket ? (
            <TicketInfo ticket={selectedTicket} />
          ) : (
            <div className="project-info">
              <h2>Project Tickets Overview</h2>
              <p>Select a ticket to view its details</p>
              <ul className="tickets-list">
                {tickets.map(ticket => (
                  <li 
                    key={ticket.id} 
                    className={`ticket-item ${ticket.status.toLowerCase()}`}
                    onClick={() => handleSelectTicket(ticket)}
                    style={{ cursor: "pointer" }}
                  >
                    <span className="ticket-title">{ticket.title}</span>
                    <span className="ticket-status">{ticket.status}</span>
                  </li>
                ))}
              </ul>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
