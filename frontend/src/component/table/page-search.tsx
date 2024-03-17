import { columns } from './components/columns-search'
import { DataTable } from './components/data-table'
import { useEffect, useMemo, useState } from 'react'
import { LibraryScanUI } from '../../model/library'
import { ScrollArea, ScrollBar } from '../ui/scroll-area'
import { severities } from '../../data/helper'
import { getSearchUIResult } from '../../api/apiCall'
import { ToastAction } from '../ui/toast'
import { useToast } from '../ui/use-toast'
import {
    DEFAULT_ERROR_MESSAGE,
    ERROR_LABEL,
    SUCCESS_LABEL,
    VULN_SEARCH_SUCCESS_MESSAGE,
} from '../../common/common'
import { Skeleton } from '../ui/skeleton'
import { useDispatch, useSelector } from 'react-redux'
import { RootState } from '../../redux/store'
import { update } from '../../redux/slice/vulnSearchSlice'

export default function SearchingPage({
    name,
    ecosystem,
    isSearch,
    setIsSearch,
}: {
    name: string
    ecosystem: string
    isSearch: boolean
    setIsSearch: React.Dispatch<React.SetStateAction<boolean>>
}) {
    const libraryData: LibraryScanUI[] = useSelector(
        (state: RootState) => state.vulnSearch
    )
    const dispatch = useDispatch()

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
        const packageName = name.slice(0, name.lastIndexOf(':'))
        const version = name.slice(name.lastIndexOf(':') + 1)
        const data = await getSearchUIResult(packageName, version, ecosystem)
        if (typeof data === 'string') {
            handlingToastAction(ERROR_LABEL, data || DEFAULT_ERROR_MESSAGE)
            dispatch(update([]))
        } else {
            dispatch(update(data))
            handlingToastAction(SUCCESS_LABEL, VULN_SEARCH_SUCCESS_MESSAGE)
        }
        setLoading(false)
        setIsSearch(false)
    }

    useEffect(() => {
        if (isSearch) {
            setLoading(true)
            fetchData()
        }
    }, [isSearch])

    useEffect(() => {
        loading && dispatch(update(Array(10).fill({})))
    }, [loading])

    return (
        <div className="mx-auto relative z-0">
            <ScrollArea className="h-[640px] w-full px-4">
                {loading ? (
                    <DataTable
                        data={libraryData}
                        columns={loadingColumns}
                        input={{ column: 'packageName', title: 'Package Name' }}
                        filter={{
                            column: 'severity',
                            title: 'Severity',
                            dataset: severities,
                        }}
                    />
                ) : (
                    <DataTable
                        data={libraryData}
                        columns={columns}
                        input={{ column: 'packageName', title: 'Package Name' }}
                        filter={{
                            column: 'severity',
                            title: 'Severity',
                            dataset: severities,
                        }}
                    />
                )}
                <ScrollBar orientation="horizontal" />
            </ScrollArea>
        </div>
    )
}
