import { useState, useEffect, FormEvent } from "react";
import Select from "react-select";
import Modal from "../ui/Modal";
import { getAllUsers } from "../../api/user";
import { createTicket } from "../../api/ticket.ts";
import { User } from "../../types/application";
import { showError, showSuccess } from "../../utils/toast";
import "./CreateTicketForm.css";
import { CreateTicketFormData } from "../../types/ticket";

type TicketModalProps = {
  isOpen: boolean;
  onClose: () => void;
  projectId?: string;
};

type UserOption = {
  value: string;
  label: string;
};

const statusOptions = [
  { value: "TODO", label: "To Do" },
  { value: "IN_PROGRESS", label: "In Progress" },
  { value: "DONE", label: "Done" },
  { value: "BLOCKED", label: "Blocked" },
];

const priorityOptions = [
  { value: "LOW", label: "Low" },
  { value: "MEDIUM", label: "Medium" },
  { value: "HIGH", label: "High" },
];

const typeOptions = [
  { value: "BUG", label: "Bug" },
  { value: "FEATURE", label: "Feature" },
  { value: "QUESTION", label: "Question" },
  { value: "TASK", label: "Task" },
];

export default function CreateTicketForm({ isOpen, onClose, projectId }: TicketModalProps) {
  const [assignedTo, setAssignedTo] = useState<string[]>([]);
  const [userOptions, setUserOptions] = useState<UserOption[]>([]);

  const [status, setStatus] = useState(statusOptions[0]);
  const [priority, setPriority] = useState(priorityOptions[0]);
  const [type, setType] = useState(typeOptions[0]);

  useEffect(() => {
    (async () => {
      try {
        const users: User[] = await getAllUsers();
        const options = users.map((user) => ({
          value: user.id,
          label: user.name || user.email || `User ${user.id}`,
        }));
        setUserOptions(options);
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    })();
  }, []);

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    const formData = new FormData(event.target as HTMLFormElement);
    const rawData = Object.fromEntries(formData.entries());

    const data: CreateTicketFormData = {
      title: rawData.title as string,
      description: rawData.description as string,
      status: status.value,
      priority: priority.value,
      type: type.value,
      assignedToIds: assignedTo,
      projectId: rawData.project_id as string,
    };

    try {
      await createTicket(data);
      showSuccess("Ticket created successfully");
      onClose();
    } catch (err: any) {
      showError(err.response?.data?.message || "Failed to create ticket");
    }
  };

  return (
      <Modal isOpen={isOpen} onClose={onClose} title="Create Ticket">
        <div className="create-ticket-container">
          <form className="create-ticket-form" onSubmit={handleSubmit}>
            <div className="form-grid">
              <div className="form-group">
                <label htmlFor="title">Title</label>
                <input
                    id="title"
                    name="title"
                    type="text"
                    placeholder="Enter ticket title"
                    required
                />
              </div>

              <div className="form-group">
                <label>Type</label>
                <Select
                    options={typeOptions}
                    value={type}
                    onChange={(opt) => setType(opt!)}
                    classNamePrefix="react-select"
                    classNames={{
                      option: (state) => `type-${state.data.value.toLowerCase()}`
                    }}
                />
              </div>

              <div className="form-group full-width">
                <label htmlFor="description">Description</label>
                <textarea
                    id="description"
                    name="description"
                    placeholder="Enter ticket description"
                    required
                ></textarea>
              </div>

              <div className="form-group">
                <label>Status</label>
                <Select
                    options={statusOptions}
                    value={status}
                    onChange={(opt) => setStatus(opt!)}
                    classNamePrefix="react-select"
                    classNames={{
                      option: (state) => `status-${state.data.value.toLowerCase()}`
                    }}
                />
              </div>

              <div className="form-group">
                <label>Priority</label>
                <Select
                    options={priorityOptions}
                    value={priority}
                    onChange={(opt) => setPriority(opt!)}
                    classNamePrefix="react-select"
                    classNames={{
                      option: (state) => `priority-${state.data.value.toLowerCase()}`
                    }}
                />
              </div>

              <div className="form-group full-width">
                <label>Assign To</label>
                <Select
                    isMulti
                    options={userOptions}
                    onChange={(selectedOptions) =>
                        setAssignedTo(selectedOptions.map((option) => option.value))
                    }
                    classNamePrefix="react-select"
                    placeholder="Select users..."
                />
              </div>

              <div className="form-group full-width">
                <label htmlFor="project_id">Project ID</label>
                <input
                    id="project_id"
                    name="project_id"
                    type="text"
                    readOnly
                    value={projectId || ""}
                    style={{ backgroundColor: "#f3f4f6", cursor: "not-allowed" }}
                />
              </div>
            </div>

            <button type="submit" className="btn btn-primary">
              Create Ticket
            </button>
          </form>
        </div>
      </Modal>
  );
}
