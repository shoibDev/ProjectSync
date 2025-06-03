import React, { useState } from "react";
import "../ui/Table.css";

import { Project } from "../../types/application";
import Modal from "../ui/Modal.tsx"

type Props = {
  projects: Project[];
};

export const ProjectTable: React.FC<Props> = ({ projects }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

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
                <td>{project.owner_id}</td>
                <td>{project.name}</td>
                <td>{project.description}</td>
                <td>{project.assigned_to_ids.join(", ")}</td>
              </tr>
          ))}
          </tbody>
        </table>

        <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Create Project" />
      </div>
  );
};
