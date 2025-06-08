import api from "./axios.ts"
import {API_ENDPOINTS} from "../constants/apiEndPoints.ts";
import { Ticket } from "../types/application.ts";
import { AxiosResponse } from "axios";
import {CreateTicketFormData} from "../types/ticket";

export const createTicket = async (ticket: CreateTicketFormData): Promise<Ticket> => {
  try {
    const response: AxiosResponse<Ticket> = await api.post(API_ENDPOINTS.TICKETS.CREATE, ticket);
    console.log(response.data)
    return response.data;
  } catch (error) {
    console.error("Error creating ticket:", error);
    throw error;
  }
}