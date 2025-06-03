import { dummyUsers } from "../../utils/dummyData";
import '../ui/Table.css';
import { User } from "../../types/application";
import React, { useState, useEffect } from "react";
import Modal from "../ui/Modal.tsx";

export default function ProjectMemberTable({
                                             members,
                                             projectId,
                                           }: {
  members: User[];
  projectId: string;
}) {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [projectMembers, setProjectMembers] = useState<User[]>([]);

  useEffect(() => {
    // Match IDs to full user objects
    const matchedUsers = dummyUsers.filter((user) => members.includes(user.id));
    console.log(matchedUsers);
    setProjectMembers(matchedUsers);
  }, [members]);

  return (
      <div className="members">
        <div className="table-header">
          <h1>Project Members</h1>
          <button className="create-btn" onClick={() => setIsModalOpen(true)}>
            Add Member
          </button>
        </div>

        {/* Scrollable wrapper */}
        <div className="table-scroll-container">
          <table role="grid">
            <thead>
            <tr>
              <th>User ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Phone</th>
            </tr>
            </thead>
            <tbody>
            {projectMembers.map((member) => (
                <tr key={member.id}>
                  <td>{member.id}</td>
                  <td>{`${member.first_name} ${member.last_name}`}</td>
                  <td>{member.email}</td>
                  <td>{member.phone_number}</td>
                </tr>
            ))}
            </tbody>
          </table>
        </div>

        <Modal
            isOpen={isModalOpen}
            onClose={() => setIsModalOpen(false)}
            title="Add Project Member"
        />
      </div>
  );
}
