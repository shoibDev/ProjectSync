import TicketsTable from "../../components/tables/TicketsTable.tsx";
import { dummyTickets } from "../../utils/dummyData.ts";

export default function TicketPage() {
  return (
    <div className="ticket-page">
      <h1>Ticket Page</h1>
      <p>This is the ticket page where you can view and manage tickets.</p>

      {/* You can add your TicketsTable component here */}
      <TicketsTable
        tickets={dummyTickets}
      />
    </div>
  )
}