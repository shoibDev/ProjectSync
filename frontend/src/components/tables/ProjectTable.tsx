import React, { useState } from "react";
import "../ui/Table.css";

import { Project } from "../../types/application";
import CreateProject from "../forms/CreateProject.tsx";
import { useNavigate } from "react-router-dom";

type Props = {
  projects: Project[];
};

export const ProjectTable: React.FC<Props> = ({ projects }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const navigate = useNavigate();

  return (
      <div className="members">
        <div className="table-header">
          <h1>Edit Name/Email</h1>
          <button className="create-btn" onClick={() => setIsModalOpen(true)}>
            Create
          </button>
        </div>

        <table role="grid">
          <thead>
          <tr>
            <th>Owner ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Assigned To</th>
          </tr>
          </thead>
          <tbody>
          {projects.map((project) => (
              <tr key={project.id}>
                <td>{project.ownerId}</td>
                <td
                    onClick={() => navigate(`/projects/${project.id}`)}
                    style={{ cursor: "pointer", color: "#3182ce", textDecoration: "underline" }}
                >
                  {project.name}
                </td>
                <td>{project.description}</td>
                <td>{project.assignedToIds.join(", ")}</td>
              </tr>
          ))}
          </tbody>
        </table>

        {/* Show the modal if open */}
        {isModalOpen && (
            <CreateProject
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
            />
        )}
      </div>
  );
};
