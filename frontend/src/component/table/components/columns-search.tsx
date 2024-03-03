'use client'

import { ColumnDef } from '@tanstack/react-table'
import { DataTableColumnHeader } from './data-table-column-header'
import LibraryScanUI from '../../../model/library'
import { Link } from 'react-router-dom'
import { Button } from '../../ui/button'
import { Badge } from '../../ui/badge'
import { severities } from '../../../data/helper'
import { Sheet, SheetTrigger } from '../../ui/sheet'
import VulnerabilityDetailSheetContent from './sheet-vuln'

export const columns: ColumnDef<LibraryScanUI>[] = [
    {
        accessorKey: 'id',
        accessorFn: (row) => row.vuln.id,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Database Id" />
        ),
        cell: ({ row }) => (
            <Link
                to={`/vuln/${row.original.vuln.id}`}
                target="_blank"
                className="max-w-[300px] line-clamp-2 font-medium"
            >
                <Button variant="link">{row.original.vuln.id}</Button>
            </Link>
        ),
        enableSorting: false,
        enableHiding: false,
    },
    {
        accessorKey: 'packageName',
        accessorFn: (row) => row.info.name,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Package Name" />
        ),
        cell: ({ row }) => (
            <Sheet>
                <SheetTrigger asChild>
                    <div className="max-w-[500px] truncate font-medium">
                        {row.original.info.name}
                    </div>
                </SheetTrigger>
                <VulnerabilityDetailSheetContent lib={row.original} />
            </Sheet>
        ),
    },
    {
        accessorKey: 'version',
        accessorFn: (row) => row.info.version,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Version" />
        ),
        cell: ({ row }) => (
            <Sheet>
                <SheetTrigger asChild>
                    <div className="max-w-[100px] line-clamp-2 font-medium">
                        {row.original.info.version}
                    </div>
                </SheetTrigger>
                <VulnerabilityDetailSheetContent lib={row.original} />
            </Sheet>
        ),
        enableSorting: false,
    },
    {
        accessorKey: 'summary',
        accessorFn: (row) => row.vuln.summary,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Summary" />
        ),
        cell: ({ row }) => (
            <Sheet>
                <SheetTrigger asChild>
                    <div className="max-w-[500px] line-clamp-2 font-medium">
                        {row.original.vuln.summary}
                    </div>
                </SheetTrigger>
                <VulnerabilityDetailSheetContent lib={row.original} />
            </Sheet>
        ),
        enableSorting: false,
    },
    {
        accessorKey: 'fixVersion',
        accessorFn: (row) => row.vuln.fixed,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Fix Version" />
        ),
        cell: ({ row }) => (
            <Sheet>
                <SheetTrigger asChild>
                    <div className="max-w-[100px] line-clamp-2 font-medium">
                        {row.original.vuln.fixed}
                    </div>
                </SheetTrigger>
                <VulnerabilityDetailSheetContent lib={row.original} />
            </Sheet>
        ),
        enableSorting: false,
    },
    {
        accessorKey: 'severity',
        accessorFn: (row) => row.vuln.severity[0]?.ranking,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Severity" />
        ),
        cell: ({ row }) => {
            const color = severities.find(
                (s) => s.value === row.original.vuln.severity[0]?.ranking
            )?.color
            return (
                <Sheet>
                    <SheetTrigger asChild>
                        <div className="max-w-[100px] line-clamp-2 font-medium">
                            <Badge
                                style={{
                                    backgroundColor: `${color ? color : 'hsl(var(--foreground))'}`,
                                }}
                            >
                                {row.original.vuln.severity[0]?.ranking}
                            </Badge>
                        </div>
                    </SheetTrigger>
                    <VulnerabilityDetailSheetContent lib={row.original} />
                </Sheet>
            )
        },
        filterFn: (row, id, value) => {
            return value.includes(row.original.vuln.severity[0]?.ranking)
        },
        enableSorting: false,
    },
    {
        accessorKey: 'score',
        accessorFn: (row) => row.vuln.severity[0]?.baseScore,
        header: ({ column }) => (
            <DataTableColumnHeader column={column} title="Score" />
        ),
        cell: ({ row }) => {
            return (
                <Sheet>
                    <SheetTrigger asChild>
                        <div className="max-w-[100px] line-clamp-2 font-medium">
                            <span>
                                {row.original.vuln.severity[0]?.baseScore}
                            </span>
                        </div>
                    </SheetTrigger>
                    <VulnerabilityDetailSheetContent lib={row.original} />
                </Sheet>
            )
        },
    },
]
