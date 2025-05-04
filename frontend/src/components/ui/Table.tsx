import React from "react";

interface Column<T> {
  key: keyof T;
  label: string;
  render?: (row: T) => React.ReactNode;
  headerRender?: () => React.ReactNode;
}

interface TableProps<T> {
  columns: Column<T>[];
  data: T[];
  width?: string;
  height?: string;
}

export default function Table<T>({
                                   columns,
                                   data,
                                   width = "w-full",
                                   height = "h-auto",
                                 }: TableProps<T>) {
  return (
      <div
          className={`overflow-x-auto overflow-y-auto rounded-xl border border-white/10 backdrop-blur-lg shadow-md ${width} ${height} bg-white/5`}
      >
        <table className="min-w-full text-sm text-left text-gray-200">
          <thead
              className="sticky top-0 z-10 text-sm font-semibold text-white uppercase bg-gray-800 border-b border-white/10 shadow-sm">

          <tr>
            {columns.map((col) => (
                <th key={String(col.key)} className="px-4 py-3 whitespace-nowrap">
                  {col.headerRender ? col.headerRender() : col.label}
                </th>
            ))}
          </tr>
          </thead>
          <tbody className="divide-y divide-white/10">
          {data.map((row, rowIndex) => (
              <tr
                  key={rowIndex}
                  className="hover:bg-white/10 transition duration-200"
              >
                {columns.map((col) => (
                    <td
                        key={String(col.key)}
                        className="px-4 py-3 whitespace-nowrap text-gray-100"
                    >
                      {col.render ? col.render(row) : String(row[col.key])}
                    </td>
                ))}
              </tr>
          ))}
          </tbody>
        </table>
      </div>
  );
}
