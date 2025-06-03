export const dummyProjects = [
  {
    id: "1",
    owner_id: "user_001",
    name: "Redesign Homepage",
    description: "Update styles and content for the homepage.",
    assigned_to_ids: ["user_005", "user_010"],
    tickets: [],
  },
  {
    id: "2",
    owner_id: "user_002",
    name: "Fix login bug",
    description: "Resolve token expiry issue on login.",
    assigned_to_ids: ["user_007"],
    tickets: [],
  },
];
//
// id: string;
// title: string;
// description: string;
// status: "TODO" | "IN_PROGRESS" | "DONE" | "BLOCKED";
// priority: "LOW" | "MEDIUM" | "HIGH";
// type: "BUG" | "FEATURE" | "QUESTION" | "TASK";
// assigned_to_ids: string[];
// project_id: string;

export const dummyTickets = [
  {
    id: "1",
    title: "Fix login redirect bug",
    description: "Users are not redirected to the dashboard after login.",
    status: "TODO",
    priority: "HIGH",
    type: "BUG",
    assigned_to_ids: ["u1"],
    project_id: "p1"
  },
  {
    id: "2",
    title: "Add dark mode toggle",
    description: "Implement a toggle switch for switching between light and dark themes.",
    status: "IN_PROGRESS",
    priority: "MEDIUM",
    type: "FEATURE",
    assigned_to_ids: ["u2"],
    project_id: "p1"
  },
  {
    id: "3",
    title: "Deployment question",
    description: "What is the correct command to deploy to production?",
    status: "TODO",
    priority: "LOW",
    type: "QUESTION",
    assigned_to_ids: ["u3"],
    project_id: "p1"
  },
  {
    id: "4",
    title: "Refactor auth service",
    description: "Clean up and modularize authentication logic.",
    status: "IN_PROGRESS",
    priority: "HIGH",
    type: "TASK",
    assigned_to_ids: ["u4", "u5"],
    project_id: "p1"
  },
  {
    id: "5",
    title: "Broken image on landing page",
    description: "The hero image fails to load on mobile browsers.",
    status: "TODO",
    priority: "HIGH",
    type: "BUG",
    assigned_to_ids: ["u2"],
    project_id: "p2"
  },
  {
    id: "6",
    title: "Create onboarding tutorial",
    description: "Add step-by-step walkthrough for new users.",
    status: "BLOCKED",
    priority: "MEDIUM",
    type: "FEATURE",
    assigned_to_ids: ["u1"],
    project_id: "p2"
  },
  {
    id: "7",
    title: "Investigate high memory usage",
    description: "Memory usage spikes under heavy load. Needs investigation.",
    status: "IN_PROGRESS",
    priority: "HIGH",
    type: "TASK",
    assigned_to_ids: ["u3", "u4"],
    project_id: "p3"
  },
  {
    id: "8",
    title: "Add unit tests for API routes",
    description: "Ensure all API endpoints are covered by tests.",
    status: "TODO",
    priority: "MEDIUM",
    type: "TASK",
    assigned_to_ids: ["u5"],
    project_id: "p3"
  },
  {
    id: "9",
    title: "Dropdown not working on Safari",
    description: "Navigation dropdown is non-functional in Safari browser.",
    status: "DONE",
    priority: "HIGH",
    type: "BUG",
    assigned_to_ids: ["u2"],
    project_id: "p2"
  },
  {
    id: "10",
    title: "Clarify API usage in docs",
    description: "The API documentation is vague on authentication headers.",
    status: "TODO",
    priority: "LOW",
    type: "QUESTION",
    assigned_to_ids: ["u3"],
    project_id: "p1"
  }
];

export const fullProjectData = {
  id: "1",
  owner_id: "user_001",
  name: "Full Project Example",
  description: "This is a detailed project with multiple tickets and team members.",
  assigned_to_ids: ["user_002", "user_003", "user_004"],
  tickets: [
    {
      id: "ticket_001",
      title: "Initial Setup",
      description: "Set up the project repository and initial configuration.",
      status: "DONE",
      priority: "HIGH",
      type: "TASK",
      assigned_to_ids: ["user_002"],
      project_id: "1"
    },
    {
      id: "ticket_002",
      title: "Design Mockups",
      description: "Create design mockups for the main pages.",
      status: "IN_PROGRESS",
      priority: "MEDIUM",
      type: "FEATURE",
      assigned_to_ids: ["user_003"],
      project_id: "1"
    },
    {
      id: "ticket_003",
      title: "Implement Authentication",
      description: "Add user authentication and authorization features.",
      status: "TODO",
      priority: "HIGH",
      type: "TASK",
      assigned_to_ids: ["user_004"],
      project_id: "1"
    }
  ]
}
export const dummyUsers = [
  {
    id: "user_002",
    email: "user_002@gmail.com",
    first_name: "Alice",
    last_name: "Smith",
    phone_number: "123-456-7890",
    projects: [],
    tickets: [],
  },
  {
    id: "user_003",
    email: "user_003@gmail.com",
    first_name: "Bob",
    last_name: "Johnson",
    phone_number: "987-654-3210",
    projects: [],
    tickets: [],
  },
  {
    id: "user_004",
    email: "user_004",
    first_name: "Charlie",
    last_name: "Brown",
    phone_number: "555-555-5555",
    projects: [],
    tickets: [],
  }
];