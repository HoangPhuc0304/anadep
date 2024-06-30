import { columns } from './components/columns-search'
import { DataTable } from './components/data-table'
import { useEffect, useMemo, useState } from 'react'
import { AnalysisUIResult } from '../../model/library'
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
    const analysisUIResult: AnalysisUIResult = useSelector(
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
        let packageName
        let version = null
        if (name.indexOf('?') > -1) {
            packageName = name.slice(0, name.lastIndexOf('?'))
            version = name.slice(name.lastIndexOf('?') + 1)
        } else {
            packageName = name
        }
        const data = await getSearchUIResult(packageName, version, ecosystem)
        if (typeof data === 'string') {
            handlingToastAction(ERROR_LABEL, data || DEFAULT_ERROR_MESSAGE)
            dispatch(update({ ...analysisUIResult, libs: [] }))
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
        loading &&
            dispatch(update({ ...analysisUIResult, libs: Array(10).fill({}) }))
    }, [loading])

    return (
        <div className="mx-auto relative z-0">
            <ScrollArea className="w-full px-4 mb-4" style={{height: "calc(100vh - 210px)"}}>
                {loading ? (
                    <DataTable
                        data={analysisUIResult.libs}
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
                        data={analysisUIResult.libs}
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
