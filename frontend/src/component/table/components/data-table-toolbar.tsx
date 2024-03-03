'use client'

import { Cross2Icon } from '@radix-ui/react-icons'
import { Table } from '@tanstack/react-table'

import { Button } from '../../ui/button'
import { Input } from '../../ui/input'
import { DataTableViewOptions } from './data-table-view-options'

import { FilterOption } from '../../../data/helper'
import { DataTableFacetedFilter } from './data-table-faceted-filter'

interface DataTableToolbarProps<TData> {
    table: Table<TData>
    input: {
        column: string
        title: string
    }
    filter: {
        column: string
        title: string
        dataset: FilterOption[]
    }
}

export function DataTableToolbar<TData>({
    table,
    input,
    filter,
}: DataTableToolbarProps<TData>) {
    const isFiltered = table.getState().columnFilters.length > 0

    return (
        <div className="flex items-center justify-between">
            <div className="flex flex-1 items-center space-x-2 h-12">
                <Input
                    placeholder={`Filter ${input.title}`}
                    value={
                        (table
                            .getColumn(input.column)
                            ?.getFilterValue() as string) ?? ''
                    }
                    onChange={(event) => {
                        return table
                            .getColumn(input.column)
                            ?.setFilterValue(event.target.value)
                    }}
                    className="h-8 w-[150px] lg:w-[250px]"
                />
                {table.getColumn(filter.column) && (
                    <DataTableFacetedFilter
                        column={table.getColumn(filter.column)}
                        title={filter.title}
                        options={filter.dataset}
                    />
                )}
                {isFiltered && (
                    <Button
                        variant="ghost"
                        onClick={() => table.resetColumnFilters()}
                        className="h-8 px-2 lg:px-3"
                    >
                        Reset
                        <Cross2Icon className="ml-2 h-4 w-4" />
                    </Button>
                )}
            </div>
            <DataTableViewOptions table={table} />
        </div>
    )
}
