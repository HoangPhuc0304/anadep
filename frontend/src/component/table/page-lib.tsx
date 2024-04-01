import { columns } from './components/columns-lib'
import { DataTable } from './components/data-table'
import { useEffect, useMemo, useState } from 'react'
import { ScrollArea, ScrollBar } from '../ui/scroll-area'
import { ecosystems } from '../../data/helper'
import {
    LibraryUI,
    ReportForm,
    Repository,
    ScanningResult,
    User,
} from '../../model/library'
import { useToast } from '../ui/use-toast'
import { Skeleton } from '../ui/skeleton'
import { ToastAction } from '../ui/toast'
import {
    generateReport,
    getScanUIResult,
    updateRepoWithSbom,
} from '../../api/apiCall'
import {
    DEFAULT_ERROR_MESSAGE,
    DEPENDENCY_SCAN_SUCCESS_MESSAGE,
    ERROR_LABEL,
    GENERATE_REPORT_SUCCESS_MESSAGE,
    NO_DATA_REPORT_MESSAGE,
    SUCCESS_LABEL,
    UPDATE_PROJECT_SUCCESS_MESSAGE,
} from '../../common/common'
import { useDispatch, useSelector } from 'react-redux'
import { RootState } from '@/redux/store'
import { update } from '../../redux/slice/sbomSlice'

export default function SbomPage({
    file,
    reportData,
    repo,
}: {
    file: File | undefined
    reportData: ReportForm | undefined
    repo: Repository | undefined
}) {
    const scanningResult: ScanningResult = useSelector(
        (state: RootState) => state.sbom
    )
    const user: User = useSelector((state: RootState) => state.user.currentUser)
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
        if (file) {
            const data = await getScanUIResult(file)
            if (typeof data === 'string') {
                handlingToastAction(ERROR_LABEL, data || DEFAULT_ERROR_MESSAGE)
                dispatch(update({ ...scanningResult, libraries: [] }))
            } else {
                dispatch(update(data))
                handlingToastAction(
                    SUCCESS_LABEL,
                    DEPENDENCY_SCAN_SUCCESS_MESSAGE
                )
                if (repo && data.libraries) {
                    if (data.libraries.length > 0) {
                        handlingUpdateRepo(data)
                    }
                }
            }
            setLoading(false)
        }
    }

    const handlingReport = async () => {
        if (reportData && scanningResult.libraries.length > 0) {
            const data: string | boolean = await generateReport(
                scanningResult.libraries,
                reportData,
                'sbom'
            )
            if (typeof data === 'string' || !data) {
                handlingToastAction(ERROR_LABEL, data || DEFAULT_ERROR_MESSAGE)
            } else {
                handlingToastAction(
                    SUCCESS_LABEL,
                    GENERATE_REPORT_SUCCESS_MESSAGE
                )
            }
        }
    }

    const handlingUpdateRepo = async (result: ScanningResult) => {
        if (repo && user.githubToken) {
            const data = await updateRepoWithSbom(
                repo,
                result,
                user.githubToken
            )
            if (typeof data === 'string' && data.length !== 0) {
                handlingToastAction(ERROR_LABEL, data || DEFAULT_ERROR_MESSAGE)
            }
        }
    }

    useEffect(() => {
        if (file) {
            setLoading(true)
            fetchData()
        }
    }, [file])

    useEffect(() => {
        if (reportData) {
            if (scanningResult.libraries.length > 0) {
                handlingReport()
            } else {
                handlingToastAction(ERROR_LABEL, NO_DATA_REPORT_MESSAGE)
            }
        }
    }, [reportData])

    useEffect(() => {
        loading &&
            dispatch(
                update({ ...scanningResult, libraries: Array(10).fill({}) })
            )
    }, [loading])

    return (
        <div className="mx-auto">
            <ScrollArea className="h-[640px] w-full px-4">
                {loading ? (
                    <DataTable
                        data={scanningResult.libraries}
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
                        data={scanningResult.libraries}
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
