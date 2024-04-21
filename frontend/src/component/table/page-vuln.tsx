import { columns } from './components/columns-vuln'
import { DataTable } from './components/data-table'
import { UserNav } from './components/user-nav'
import { useEffect, useMemo, useState } from 'react'
import { libraries } from '../../data/libraries'
import {
    AnalysisUIResult,
    ReportForm,
    Repository,
    User,
} from '../../model/library'
import { ScrollArea, ScrollBar } from '../ui/scroll-area'
import { severities } from '../../data/helper'
import {
    generateReport,
    getAnalysisUIResult,
    getAuthAnalysisUIResult,
} from '../../api/apiCall'
import { ToastAction } from '../ui/toast'
import { useToast } from '../ui/use-toast'
import {
    DEFAULT_ERROR_MESSAGE,
    ERROR_LABEL,
    GENERATE_REPORT_SUCCESS_MESSAGE,
    NO_DATA_REPORT_MESSAGE,
    SUCCESS_LABEL,
    UPDATE_PROJECT_SUCCESS_MESSAGE,
    VULN_ANALYSIS_SUCCESS_MESSAGE,
} from '../../common/common'
import { Skeleton } from '../ui/skeleton'
import { useDispatch, useSelector } from 'react-redux'
import { RootState } from '../../redux/store'
import { update } from '../../redux/slice/vulnScanSlice'

export default function VulnerabilityPage({
    file,
    reportData,
    repo,
}: {
    file: File | undefined
    reportData: ReportForm | undefined
    repo: Repository | undefined
}) {
    const analysisUIResult: AnalysisUIResult = useSelector(
        (state: RootState) => state.vulnScan
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
            let data = ''
            if (repo && repo.id && user.githubToken) {
                data = await getAuthAnalysisUIResult(file, repo.id, user.githubToken)
            } else {
                data = await getAnalysisUIResult(file)
            }

            if (typeof data === 'string') {
                dispatch(update({ ...analysisUIResult, libs: [] }))
                handlingToastAction(ERROR_LABEL, data || DEFAULT_ERROR_MESSAGE)
            } else {
                dispatch(update(data))
                handlingToastAction(
                    SUCCESS_LABEL,
                    VULN_ANALYSIS_SUCCESS_MESSAGE
                )
            }
            setLoading(false)
        }
    }

    const handlingReport = async () => {
        if (reportData && analysisUIResult.libs.length > 0) {
            const data: string | boolean = await generateReport(
                analysisUIResult,
                reportData,
                'vulns'
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

    useEffect(() => {
        if (file) {
            setLoading(true)
            fetchData()
        }
    }, [file])

    useEffect(() => {
        if (reportData) {
            if (analysisUIResult.libs.length > 0) {
                handlingReport()
            } else {
                handlingToastAction(ERROR_LABEL, NO_DATA_REPORT_MESSAGE)
            }
        }
    }, [reportData])

    useEffect(() => {
        loading &&
            dispatch(update({ ...analysisUIResult, libs: Array(10).fill({}) }))
    }, [loading])

    // return (
    //   <>
    //     <div className="hidden h-full flex-1 flex-col space-y-8 p-8 md:flex">
    //       {/* <div className="flex items-center justify-between space-y-2">
    //         <div>
    //           <h2 className="text-2xl font-bold tracking-tight">Welcome back!</h2>
    //           <p className="text-muted-foreground">
    //             Here&apos;s a list of your tasks for this month!
    //           </p>
    //         </div>
    //         <div className="flex items-center space-x-2">
    //           <UserNav />
    //         </div>
    //       </div> */}
    //       <DataTable data={libraryData} columns={columns} />
    //     </div>
    //   </>
    // )
    return (
        <div className="mx-auto">
            <ScrollArea className="h-[640px] w-full px-4">
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
