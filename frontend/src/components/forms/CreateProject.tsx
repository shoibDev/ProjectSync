import { useState, useEffect, FormEvent } from 'react';
import Select from 'react-select';
import Modal from '../ui/Modal.tsx';
import './CreateProject.css';
import { getAllUsers } from "../../api/user.ts";
import { assignUserToProject, createProject } from "../../api/projects.ts";
import { Project, User } from '../../types/application.tsx';
import { CreateProjectFormData } from "../../types/project";
import { validateCreateProject } from "../../validations/validateCreateProject";
import { showError, showSuccess } from "../../utils/toast";

type ProjectModalProps = {
  isOpen: boolean;
  onClose: () => void;
};

type UserOption = {
  value: string;
  label: string;
};

export default function CreateProject({ isOpen, onClose }: ProjectModalProps) {
  const [assignedTo, setAssignedTo] = useState<string[]>([]);
  const [userOptions, setUserOptions] = useState<UserOption[]>([]);

  useEffect(() => {
    (async () => {
      try {
        const users: User[] = await getAllUsers();
        const options = users.map((user: any) => ({
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

    const data: CreateProjectFormData = {
      name: rawData.name as string,
      description: rawData.description as string,
    };

    const errors = validateCreateProject(data);
    if (errors.length > 0) {
      showError(errors[0]);
      return;
    }

    try {
      const projectResponse: Project = await createProject(data);
      await Promise.all(
          assignedTo.map(async (userId) => {
            await assignUserToProject(projectResponse.id, userId);
          })
      );
      showSuccess("Project created successfully");
      onClose();
    } catch (err: any) {
      showError(err.response?.data?.message || "Failed to create project");
    }
  };

  return (
      <Modal isOpen={isOpen} onClose={onClose} title="Create Project">
        <div className="create-project-container">
          <form className="create-project-form" onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="name">Project Name</label>
              <input
                  id="name"
                  name="name"
                  type="text"
                  placeholder="Enter project name"
                  required
              />
            </div>

            <div className="form-group">
              <label htmlFor="description">Description</label>
              <textarea
                  id="description"
                  name="description"
                  placeholder="Enter project description"
                  required
              ></textarea>
            </div>

            <div className="form-group">
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

            <button type="submit" className="btn btn-primary">
              Create Project
            </button>
          </form>
        </div>
      </Modal>
  );
}
