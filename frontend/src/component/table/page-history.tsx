import { useEffect, useMemo, useState } from 'react'
import { Repository, User } from '../../model/library'
import { ScrollArea, ScrollBar } from '../ui/scroll-area'
import { ecosystems, statuses } from '../../data/helper'
import { getHistories } from '../../api/apiCall'
import { ToastAction } from '../ui/toast'
import { useToast } from '../ui/use-toast'
import { DEFAULT_ERROR_MESSAGE, ERROR_LABEL } from '../../common/common'
import { Skeleton } from '../ui/skeleton'
import { useSelector } from 'react-redux'
import { RootState } from '../../redux/store'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '../ui/tabs'
import { sbomColumns } from './components/columns-history-sbom'
import { vulnsColumns } from './components/columns-history-vulns'
import { DataTable } from './components/data-table-history'
import { Button } from '../ui/button'
import { Link } from 'react-router-dom'
import { GitPullRequestArrow } from 'lucide-react'

export default function HistoryPage({
    repo,
}: {
    repo: Repository | undefined
}) {
    const [vulnsLoading, setVulnsLoading] = useState<boolean>(true)
    const [sbomLoading, setSbomLoading] = useState<boolean>(true)
    const [vulnsHistories, setVulnsHistories] = useState<Repository[]>([])
    const [sbomHistories, setSbomHistories] = useState<Repository[]>([])
    const { toast } = useToast()
    const user: User = useSelector((state: RootState) => state.user.currentUser)

    const vulnsLoadingColumns = useMemo(
        () =>
            vulnsLoading
                ? vulnsColumns.map((column) => ({
                    ...column,
                    accessorFn: () => '',
                    cell: () => <Skeleton className="h-[40px] w-full" />,
                }))
                : vulnsColumns,
        [vulnsLoading, vulnsColumns]
    )
    const sbomLoadingColumns = useMemo(
        () =>
            sbomLoading
                ? sbomColumns.map((column) => ({
                    ...column,
                    accessorFn: () => '',
                    cell: () => <Skeleton className="h-[40px] w-full" />,
                }))
                : sbomColumns,
        [sbomLoading, sbomColumns]
    )

    const handlingToastAction = (title: string, description: string) => {
        toast({
            title,
            description,
            action: <ToastAction altText="Hide">Hide</ToastAction>,
        })
    }

    const fetchData = async () => {
        if (user.githubToken && repo && repo.id) {
            const vulnsData = await getHistories(
                repo?.id,
                'vulns',
                user.githubToken
            )
            const sbomData = await getHistories(
                repo?.id,
                'sbom',
                user.githubToken
            )

            if (typeof vulnsData === 'string') {
                handlingToastAction(
                    ERROR_LABEL,
                    vulnsData || DEFAULT_ERROR_MESSAGE
                )
            } else {
                setVulnsHistories(
                    vulnsData.map(
                        (h: {
                            id: any
                            updatedBy: any
                            vulnerabilitySummary: any
                            vulnerabilityResult: any
                            createdAt: any
                        }) => ({
                            ...repo,
                            vulnerabilityResult: h.vulnerabilityResult,
                            vulnerabilitySummary: h.vulnerabilitySummary,
                            updatedBy: h.updatedBy,
                            createdAt: new Date(h.createdAt).toLocaleString(),
                            historyId: h.id,
                        })
                    )
                )
                setVulnsLoading(false)
            }

            if (typeof sbomData === 'string') {
                handlingToastAction(
                    ERROR_LABEL,
                    sbomData || DEFAULT_ERROR_MESSAGE
                )
            } else {
                setSbomHistories(
                    sbomData.map(
                        (h: {
                            id: any
                            scanningResult: any
                            updatedBy: any
                            createdAt: any
                        }) => ({
                            ...repo,
                            scanningResult: h.scanningResult,
                            updatedBy: h.updatedBy,
                            createdAt: new Date(h.createdAt).toLocaleString(),
                            historyId: h.id,
                        })
                    )
                )
                setSbomLoading(false)
            }
        }
    }

    useEffect(() => {
        fetchData()
    }, [repo])

    useEffect(() => {
        vulnsLoading && setVulnsHistories(Array(10).fill({}))
        sbomLoading && setSbomHistories(Array(10).fill({}))
    }, [])

    return (
        <div className="mx-auto">
            <Tabs defaultValue="vulns">
                <TabsList className="grid grid-cols-2 w-[200px] mx-4">
                    <TabsTrigger value="vulns">Vulnerability</TabsTrigger>
                    <TabsTrigger value="sbom">SBOM</TabsTrigger>
                </TabsList>
                <TabsContent value="vulns">
                    <ScrollArea className="h-[640px] w-full px-4">
                        {vulnsLoading ? (
                            <DataTable
                                data={vulnsHistories}
                                columns={vulnsLoadingColumns}
                                filter={{
                                    column: 'status',
                                    title: 'Status',
                                    dataset: statuses,
                                }}
                            />
                        ) : (
                            <DataTable
                                data={vulnsHistories}
                                columns={vulnsColumns}
                                filter={{
                                    column: 'status',
                                    title: 'Status',
                                    dataset: statuses,
                                }}
                            />
                        )}
                        <ScrollBar orientation="horizontal" />
                    </ScrollArea>
                </TabsContent>
                <TabsContent value="sbom">
                    <ScrollArea className="h-[640px] w-full px-4">
                        {sbomLoading ? (
                            <DataTable
                                data={sbomHistories}
                                columns={sbomLoadingColumns}
                                filter={{
                                    column: 'ecosystem',
                                    title: 'Ecosystem',
                                    dataset: ecosystems,
                                }}
                            />
                        ) : (
                            <DataTable
                                data={sbomHistories}
                                columns={sbomColumns}
                                filter={{
                                    column: 'ecosystem',
                                    title: 'Ecosystem',
                                    dataset: ecosystems,
                                }}
                            />
                        )}
                        <ScrollBar orientation="horizontal" />
                    </ScrollArea>
                </TabsContent>
            </Tabs>
        </div>
    )
}
