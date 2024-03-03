import { columns } from './components/columns-lib'
import { DataTable } from './components/data-table'
import { useEffect, useMemo, useState } from 'react'
import { ScrollArea, ScrollBar } from '../ui/scroll-area'
import { ecosystems } from '../../data/helper'
import { LibraryUI } from '../../model/library'
import { useToast } from '../ui/use-toast'
import { Skeleton } from '../ui/skeleton'
import { ToastAction } from '../ui/toast'
import { getScanUIResult } from '../../api/apiCall'
import {
    DEFAULT_ERROR_MESSAGE,
    DEPENDENCY_SCAN_SUCCESS_MESSAGE,
    ERROR_LABEL,
    SUCCESS_LABEL,
} from '../../common/common'

export default function SbomPage({ file }: { file: File | undefined }) {
    const [libraryData, setLibraryData] = useState<LibraryUI[]>([])
    const [loading, setLoading] = useState<boolean>(false)
    const { toast } = useToast()

    const loadingColumns = useMemo(
        () =>
            loading
                ? columns.map((column) => ({
                      ...column,
                      accessorFn: () => '',
                      cell: () => <Skeleton className="h-[40px] w-full" />,
                  }))
                : columns,
        [loading, columns]
    )

    const handlingToastAction = (title: string, description: string) => {
        toast({
            title,
            description,
            action: <ToastAction altText="Hide">Hide</ToastAction>,
        })
    }

    const fetchData = async () => {
        if (file) {
            const data = await getScanUIResult(file)
            if (typeof data === 'string') {
                handlingToastAction(ERROR_LABEL, data || DEFAULT_ERROR_MESSAGE)
                setLibraryData([])
            } else {
                setLibraryData(data)
                handlingToastAction(
                    SUCCESS_LABEL,
                    DEPENDENCY_SCAN_SUCCESS_MESSAGE
                )
            }
            setLoading(false)
        }
    }

    useEffect(() => {
        if (file) {
            setLoading(true)
            fetchData()
        }
    }, [file])

    useEffect(() => {
        loading && setLibraryData(Array(10).fill({}))
    }, [loading])

    return (
        <div className="mx-auto">
            <ScrollArea className="h-[640px] w-full px-4">
                {loading ? (
                    <DataTable
                        data={libraryData}
                        columns={loadingColumns}
                        input={{ column: 'name', title: 'Name' }}
                        filter={{
                            column: 'ecosystem',
                            title: 'Ecosystem',
                            dataset: ecosystems,
                        }}
                    />
                ) : (
                    <DataTable
                        data={libraryData}
                        columns={columns}
                        input={{ column: 'name', title: 'Name' }}
                        filter={{
                            column: 'ecosystem',
                            title: 'Ecosystem',
                            dataset: ecosystems,
                        }}
                    />
                )}
                <ScrollBar orientation="horizontal" />
            </ScrollArea>
        </div>
    )
}
