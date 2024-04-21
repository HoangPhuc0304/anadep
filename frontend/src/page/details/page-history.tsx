'use client'
import * as React from 'react'
import {
    AlertCircle,
    Archive,
    Boxes,
    GitPullRequestArrow,
    MessagesSquare,
    Search,
    ShieldCheck,
    ShoppingCart,
    Users2,
} from 'lucide-react'

import { AccountSwitcher } from '../namespace/nav/account-switcher'
import { Nav } from '../namespace/nav/nav'
import { cn } from '../../lib/utils'
import { Separator } from '../../component/ui/separator'
import { Tabs } from '../../component/ui/tabs'
import { TooltipProvider } from '../../component/ui/tooltip'

import { GripVertical } from 'lucide-react'
import * as ResizablePrimitive from 'react-resizable-panels'
import { accounts } from '../namespace/nav/data'
import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator,
} from '../../component/ui/breadcrumb'
import { useParams, useSearchParams } from 'react-router-dom'
import { History, Repository, User } from '../../model/library'
import { useSelector } from 'react-redux'
import { RootState } from '../../redux/store'
import {
    getHistoryById,
    getRepoById,
    getVulnerabilitySummary,
} from '../../api/apiCall'
import { ToastAction } from '../../component/ui/toast'
import { useToast } from '../../component/ui/use-toast'
import { DEFAULT_ERROR_MESSAGE, ERROR_LABEL } from '../../common/common'
import HistoryVulnsPage from '../../component/table/page-history-vulns'
import HistorySbomPage from '../../component/table/page-history-sbom'
import { getVulnerabilityStatus } from '../../util/util'

const ResizablePanelGroup = ({
    className,
    ...props
}: React.ComponentProps<typeof ResizablePrimitive.PanelGroup>) => (
    <ResizablePrimitive.PanelGroup
        className={cn(
            'flex h-full w-full data-[panel-group-direction=vertical]:flex-col',
            className
        )}
        {...props}
    />
)

const ResizablePanel = ResizablePrimitive.Panel

const ResizableHandle = ({
    withHandle,
    className,
    ...props
}: React.ComponentProps<typeof ResizablePrimitive.PanelResizeHandle> & {
    withHandle?: boolean
}) => (
    <ResizablePrimitive.PanelResizeHandle
        className={cn(
            'relative flex w-px items-center justify-center bg-border after:absolute after:inset-y-0 after:left-1/2 after:w-1 after:-translate-x-1/2 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring focus-visible:ring-offset-1 data-[panel-group-direction=vertical]:h-px data-[panel-group-direction=vertical]:w-full data-[panel-group-direction=vertical]:after:left-0 data-[panel-group-direction=vertical]:after:h-1 data-[panel-group-direction=vertical]:after:w-full data-[panel-group-direction=vertical]:after:-translate-y-1/2 data-[panel-group-direction=vertical]:after:translate-x-0 [&[data-panel-group-direction=vertical]>div]:rotate-90',
            className
        )}
        {...props}
    >
        {withHandle && (
            <div className="z-10 flex h-4 w-3 items-center justify-center rounded-sm border bg-border">
                <GripVertical className="h-2.5 w-2.5" />
            </div>
        )}
    </ResizablePrimitive.PanelResizeHandle>
)

export function HistoryDetails() {
    const defaultLayout = [0, 100]
    const defaultCollapsed = true
    const navCollapsedSize = 4
    const [isCollapsed, setIsCollapsed] = React.useState(defaultCollapsed)
    const { toast } = useToast()
    const [repo, setRepo] = React.useState<Repository>()
    const [history, setHistory] = React.useState<History>()
    const { repoId, id } = useParams()
    const [searchParams, setSearchParams] = useSearchParams()
    const fixAvailable = searchParams.get('fixAvailable');

    const user: User = useSelector((state: RootState) => state.user.currentUser)

    const handlingToastAction = (title: string, description: string) => {
        toast({
            title,
            description,
            action: <ToastAction altText="Hide">Hide</ToastAction>,
        })
    }

    const handlingRetrieveRepoAndHistory = async () => {
        if (repoId && id && user.githubToken) {
            const repoData = await getRepoById(repoId, user.githubToken)
            const historyData = await getHistoryById(
                repoId,
                id,
                user.githubToken
            )
            if (
                typeof repoData === 'string' ||
                typeof historyData === 'string'
            ) {
                handlingToastAction(
                    ERROR_LABEL,
                    repoData || DEFAULT_ERROR_MESSAGE
                )
            } else {
                if (historyData.vulnerabilityResult?.libs) {
                    const vulnerabilitySummary = await getVulnerabilitySummary(
                        historyData.vulnerabilityResult?.libs
                    )
                    setHistory(historyData)
                    setRepo({
                        ...repoData,
                        vulnerabilityResult: historyData?.vulnerabilityResult,
                        vulnerabilitySummary: {
                            ...vulnerabilitySummary,
                            status: getVulnerabilityStatus(
                                vulnerabilitySummary
                            ),
                        },
                    })
                } else {
                    setHistory(historyData)
                    setRepo({
                        ...repoData,
                        scanningResult: historyData?.scanningResult,
                    })
                }
            }
        }
    }

    React.useEffect(() => {
        handlingRetrieveRepoAndHistory()
    }, [repoId, id])

    return (
        <TooltipProvider delayDuration={0}>
            <ResizablePanelGroup
                direction="horizontal"
                onLayout={(sizes: number[]) => {
                    document.cookie = `react-resizable-panels:layout=${JSON.stringify(
                        sizes
                    )}`
                }}
                className="h-full min-h-[800px] max-h-[800px] items-stretch"
            >
                <ResizablePanel
                    defaultSize={defaultLayout[0]}
                    collapsedSize={navCollapsedSize}
                    collapsible={true}
                    minSize={0}
                    maxSize={0}
                    className={cn(
                        isCollapsed &&
                            'min-w-[50px] transition-all duration-300 ease-in-out'
                    )}
                >
                    <div
                        className={cn(
                            'flex h-[52px] items-center justify-center',
                            isCollapsed ? 'h-[52px]' : 'px-2'
                        )}
                    >
                        <AccountSwitcher
                            isCollapsed={isCollapsed}
                            accounts={accounts}
                        />
                    </div>
                    <Separator />
                    <Nav
                        isCollapsed={isCollapsed}
                        links={[
                            {
                                title: 'Searching',
                                href: '/search',
                                icon: Search,
                                variant: 'ghost',
                            },
                            {
                                title: 'Vulnerability',
                                href: '/namespace',
                                icon: ShieldCheck,
                                variant: 'ghost',
                            },
                            {
                                title: 'SBOM',
                                href: '/sbom',
                                icon: Boxes,
                                variant: 'ghost',
                            },
                            {
                                title: 'Project',
                                href: '/project',
                                icon: GitPullRequestArrow,
                                variant: 'default',
                            },
                        ]}
                    />
                    <Separator />
                    <Nav
                        isCollapsed={isCollapsed}
                        links={[
                            {
                                title: 'Social',
                                label: '972',
                                icon: Users2,
                                variant: 'ghost',
                            },
                            {
                                title: 'Updates',
                                label: '342',
                                icon: AlertCircle,
                                variant: 'ghost',
                            },
                            {
                                title: 'Forums',
                                label: '128',
                                icon: MessagesSquare,
                                variant: 'ghost',
                            },
                            {
                                title: 'Shopping',
                                label: '8',
                                icon: ShoppingCart,
                                variant: 'ghost',
                            },
                            {
                                title: 'Promotions',
                                label: '21',
                                icon: Archive,
                                variant: 'ghost',
                            },
                        ]}
                    />
                </ResizablePanel>
                <ResizableHandle withHandle />
                <ResizablePanel defaultSize={defaultLayout[1]} minSize={30}>
                    <Tabs defaultValue="all" className="relative z-10">
                        <div className="flex h-[52px] justify-between items-center px-4 py-2 z-1">
                            <h1 className="text-xl font-bold">Project</h1>
                        </div>
                        <Separator />
                        <div className="bg-background/95 p-4 backdrop-blur supports-[backdrop-filter]:bg-background/60">
                            <div className="vulnerability-scan-form flex justify-between items-center">
                                <Breadcrumb>
                                    <BreadcrumbList>
                                        <BreadcrumbItem>
                                            <BreadcrumbLink href="/project">
                                                Dashboard
                                            </BreadcrumbLink>
                                        </BreadcrumbItem>
                                        <BreadcrumbSeparator />
                                        <BreadcrumbItem>
                                            <BreadcrumbLink
                                                href={`/project/${repoId}`}
                                            >
                                                {repo?.fullName}
                                            </BreadcrumbLink>
                                        </BreadcrumbItem>
                                        <BreadcrumbSeparator />
                                        <BreadcrumbItem>
                                            <BreadcrumbLink
                                                href={`/project/${repoId}/history/${id}`}
                                            >
                                                {history?.createdAt
                                                    ? new Date(
                                                          history.createdAt
                                                      ).toLocaleString()
                                                    : history?.id}
                                            </BreadcrumbLink>
                                        </BreadcrumbItem>
                                    </BreadcrumbList>
                                </Breadcrumb>
                            </div>
                        </div>
                    </Tabs>
                    {history?.type.toLocaleLowerCase() === 'vulns' ? (
                        <HistoryVulnsPage repo={repo} history={history} fixAvailable={fixAvailable}/>
                    ) : (
                        <HistorySbomPage repo={repo} history={history} />
                    )}
                </ResizablePanel>
            </ResizablePanelGroup>
        </TooltipProvider>
    )
}
