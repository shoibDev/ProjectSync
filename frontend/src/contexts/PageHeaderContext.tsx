import { createContext, useContext, useState, ReactNode } from "react";

type HeaderConfig = {
  title?: string;
  actions?: ReactNode;
};

const PageHeaderContext = createContext<{
  header: HeaderConfig;
  setHeader: (config: HeaderConfig) => void;
}>({
  header: {},
  setHeader: () => {},
});

export const usePageHeader = () => useContext(PageHeaderContext);

export function PageHeaderProvider({ children }: { children: ReactNode }) {
  const [header, setHeader] = useState<HeaderConfig>({});

  return (
      <PageHeaderContext.Provider value={{ header, setHeader }}>
        {children}
      </PageHeaderContext.Provider>
  );
}