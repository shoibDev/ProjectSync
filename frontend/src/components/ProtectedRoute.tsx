import {ReactNode} from "react";

export default function ProtectedRoute({ children }: { children: ReactNode }) {
  // Auth will be added later, for now just render the children
  return <> {children} </>
}