import { usePageHeader } from "../contexts/PageHeaderContext.tsx";

export default function Header() {
  const { header } = usePageHeader();

  return (
      <header className="h-full px-6 flex items-center justify-between
                       bg-white/10 backdrop-blur-md
                       border-b border-white/10
                       shadow-md text-white rounded-bl-xl">
        <h1 className="text-xl font-semibold tracking-wide">
          {header.title || "Dashboard"}
        </h1>
        <div>{header.actions}</div>
      </header>
  );
}
