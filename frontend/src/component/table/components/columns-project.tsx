'use client'

import { ColumnDef } from '@tanstack/react-table'
import { DataTableColumnHeader } from './data-table-column-header'
import { Repository } from '../../../model/library'
import { Link } from 'react-router-dom'
import { Button } from '../../ui/button'
import { DataTableRowProjectActions } from './row-actions-project'
import { statuses } from '../../../data/helper'
import { Badge } from '../../../component/ui/badge'

export const columns: ColumnDef<Repository>[] = [
    {
        accessorKey: 'projectName',
        accessorFn: (row) => row.fullName,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Project Name" />
        ),
        cell: ({ row }) => (
            <Link
                to={row.original.id || row.original.githubUrl}
                className="max-w-[300px] line-clamp-2 font-medium"
            >
                <Button variant="link" style={{ padding: 0 }}>
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        className="w-4 mr-1"
                        data-name="Layer 1"
                        viewBox="0 0 24 24"
                        id="github"
                    >
                        <path d="M12,2.2467A10.00042,10.00042,0,0,0,8.83752,21.73419c.5.08752.6875-.21247.6875-.475,0-.23749-.01251-1.025-.01251-1.86249C7,19.85919,6.35,18.78423,6.15,18.22173A3.636,3.636,0,0,0,5.125,16.8092c-.35-.1875-.85-.65-.01251-.66248A2.00117,2.00117,0,0,1,6.65,17.17169a2.13742,2.13742,0,0,0,2.91248.825A2.10376,2.10376,0,0,1,10.2,16.65923c-2.225-.25-4.55-1.11254-4.55-4.9375a3.89187,3.89187,0,0,1,1.025-2.6875,3.59373,3.59373,0,0,1,.1-2.65s.83747-.26251,2.75,1.025a9.42747,9.42747,0,0,1,5,0c1.91248-1.3,2.75-1.025,2.75-1.025a3.59323,3.59323,0,0,1,.1,2.65,3.869,3.869,0,0,1,1.025,2.6875c0,3.83747-2.33752,4.6875-4.5625,4.9375a2.36814,2.36814,0,0,1,.675,1.85c0,1.33752-.01251,2.41248-.01251,2.75,0,.26251.1875.575.6875.475A10.0053,10.0053,0,0,0,12,2.2467Z"></path>
                    </svg>
                    {row.original.fullName}
                </Button>
            </Link>
        ),
    },
    {
        accessorKey: 'owner',
        accessorFn: (row) => row.owner,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Owner" />
        ),
        cell: ({ row }) => (
            <div className="max-w-[200px] truncate font-medium">
                {row.original.owner}
            </div>
        ),
        enableSorting: false,
    },
    {
        accessorKey: 'status',
        accessorFn: (row) => row.vulnerabilitySummary?.status,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Status" />
        ),
        cell: ({ row }) => {
            const color = statuses.find(
                (s) => s.value === row.original.vulnerabilitySummary?.status
            )?.color
            return (
                <div className="max-w-[200px] line-clamp-2 font-medium">
                    {row.original.vulnerabilitySummary?.status && (
                        <Badge
                            style={{
                                backgroundColor: `${color ? color : 'hsl(var(--foreground))'}`,
                            }}
                        >
                            {row.original.vulnerabilitySummary?.status}
                        </Badge>
                    )}
                </div>
            )
        },
        filterFn: (row, id, value) => {
            return value.includes(row.original.vulnerabilitySummary?.status)
        },
        enableSorting: false,
    },
    {
        accessorKey: 'analysis',
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Analysis" />
        ),
        cell: ({ row }) => {
            return (
                <div className="min-w-[150px] max-w-[300px] line-clamp-2 font-medium grid grid-cols-4 gap-2">
                    <div>
                        <span className="mr-1">
                            {row.original.vulnerabilitySummary?.critical}
                        </span>
                        <span
                            className="rounded-lg text-white"
                            style={{
                                padding: '4px',
                                backgroundColor: `${statuses[4].color}`,
                            }}
                        >
                            C
                        </span>
                    </div>
                    <div>
                        <span className="mr-1">
                            {row.original.vulnerabilitySummary?.high}
                        </span>
                        <span
                            className="rounded-lg text-white"
                            style={{
                                padding: '4px',
                                backgroundColor: `${statuses[3].color}`,
                            }}
                        >
                            H
                        </span>
                    </div>
                    <div>
                        <span className="mr-1">
                            {row.original.vulnerabilitySummary?.medium}
                        </span>
                        <span
                            className="rounded-lg text-white"
                            style={{
                                padding: '4px',
                                backgroundColor: `${statuses[2].color}`,
                            }}
                        >
                            M
                        </span>
                    </div>
                    <div>
                        <span className="mr-1">
                            {row.original.vulnerabilitySummary?.low}
                        </span>
                        <span
                            className="rounded-lg text-white"
                            style={{
                                padding: '4px',
                                backgroundColor: `${statuses[1].color}`,
                            }}
                        >
                            L
                        </span>
                    </div>
                </div>
            )
        },
        enableSorting: false,
    },
    {
        accessorKey: 'ecosystem',
        accessorFn: (row) => row.vulnerabilityResult?.ecosystem,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Ecosystem" />
        ),
        cell: ({ row }) => (
            <div className="max-w-[200px] line-clamp-2 font-medium">
                {row.original.vulnerabilityResult?.ecosystem}
            </div>
        ),
        enableSorting: false,
    },
    {
        accessorKey: 'dependency',
        accessorFn: (row) => row.vulnerabilityResult?.libraryCount,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Dependencies" />
        ),
        cell: ({ row }) => (
            <div className="max-w-[200px] line-clamp-2 font-medium">
                {row.original.vulnerabilityResult?.libraryCount}
            </div>
        ),
        enableSorting: false,
    },
    {
        accessorKey: 'issue',
        accessorFn: (row) => row.vulnerabilityResult?.issuesCount,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Issues" />
        ),
        cell: ({ row }) => (
            <div className="max-w-[200px] line-clamp-2 font-medium">
                {row.original.vulnerabilityResult?.issuesCount}
            </div>
        ),
        enableSorting: false,
    },
    {
        accessorKey: 'duration',
        accessorFn: (row) => row.vulnerabilityResult?.responseTime,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Duration (ms)" />
        ),
        cell: ({ row }) => (
            <div className="max-w-[200px] line-clamp-2 font-medium">
                {row.original.vulnerabilityResult?.responseTime}
            </div>
        ),
    },
    {
        accessorKey: 'updatedBy',
        accessorFn: (row) => row.updatedBy,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Updated By" />
        ),
        cell: ({ row }) => (
            <div className="max-w-[200px] line-clamp-2 font-medium">
                {row.original.updatedBy}
            </div>
        ),
        enableSorting: false,
    },
    {
        id: 'actions',
        cell: ({ row }) => <DataTableRowProjectActions row={row} />,
    },
]
