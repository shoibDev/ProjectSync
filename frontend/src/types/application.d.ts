export type Ticket = {
  id: string;
  title: string;
  description: string;
  status: "TODO" | "IN_PROGRESS" | "DONE" | "BLOCKED";
  priority: "LOW" | "MEDIUM" | "HIGH";
  type: "BUG" | "FEATURE" | "QUESTION" | "TASK";
  assigned_to_ids: string[];
  project_id: string;
}

export type Project = {
  id: string;
  ownerId: string;
  name: string;
  description: string;
  assignedToIds: string[];
  tickets: Ticket[];
}

export type User = {
  id: string;
  email: string;
  first_name: string;
  last_name: string;
  phone_number: string;
  projects: Project[];
  tickets: Ticket[];
}