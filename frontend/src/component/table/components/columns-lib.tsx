'use client'

import { ColumnDef } from '@tanstack/react-table'
import { DataTableColumnHeader } from './data-table-column-header'
import { LibraryUI } from '../../../model/library'

export const columns: ColumnDef<LibraryUI>[] = [
    // {
    //   accessorKey: "#",
    //   header: () => <div className="font-bold">#</div>,
    //   cell: ({ row }) => (
    //     <div>1</div>
    //   ),
    //   enableSorting: false,
    //   enableHiding: false,
    // },
    {
        accessorKey: 'name',
        accessorFn: (row) => row.name,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Name" />
        ),
        cell: ({ row }) => (
            <div className="max-w-[700px] truncate">{row.original.name}</div>
        ),
    },
    {
        accessorKey: 'version',
        accessorFn: (row) => row.version,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Version" />
        ),
        cell: ({ row }) => (
            <div className="max-w-[400px] truncate">{row.original.version}</div>
        ),
    },
    {
        accessorKey: 'ecosystem',
        accessorFn: (row) => row.ecosystem,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Ecosystem" />
        ),
        cell: ({ row }) => (
            <div className="max-w-[400px] truncate">
                {row.original.ecosystem}
            </div>
        ),
        filterFn: (row, id, value) => {
            return value.includes(row.original.ecosystem)
        },
    },
    // {
    //   id: "actions",
    //   cell: ({ row }) => <DataTableRowActions row={row} />,
    // },
]
