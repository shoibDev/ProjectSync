export const CreateTicketFormData = {
  title: "",
  description: "",
  status: "TODO" as "TODO" | "IN_PROGRESS" | "DONE" | "BLOCKED",
  priority: "LOW" as "LOW" | "MEDIUM" | "HIGH",
  type: "BUG" as "BUG" | "FEATURE" | "QUESTION" | "TASK",
  assignedToIds: [] as string[],
  projectId: ""
}