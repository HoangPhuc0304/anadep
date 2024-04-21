import { useEffect, useState } from 'react'
import { FixResult, History, Repository, User } from '../../model/library'
import { ScrollArea, ScrollBar } from '../ui/scroll-area'
import { severities, statuses } from '../../data/helper'
import { ToastAction } from '../ui/toast'
import { useToast } from '../ui/use-toast'
import { useSelector } from 'react-redux'
import { RootState } from '../../redux/store'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '../ui/tabs'
import { columns } from './components/columns-vuln'
import { DataTable } from './components/data-table'
import { Card, CardContent, CardHeader, CardTitle } from '../ui/card'
import { Badge } from '../ui/badge'
import { Separator } from '../ui/separator'
import { Link } from 'react-router-dom'
import { Button } from '../ui/button'
import { GitPullRequestArrow, ShieldAlert } from 'lucide-react'
import { Table, TableBody, TableCaption, TableCell, TableFooter, TableHead, TableHeader, TableRow } from '../ui/table'
import { createPullRequest, createSecurityAdvisory, getAutoFix } from '../../api/apiCall'
import { DEFAULT_ERROR_MESSAGE, ERROR_LABEL, PULL_REQUEST_SUCCESS_MESSAGE, SECURITY_ADVISORY_SUCCESS_MESSAGE, SUCCESS_LABEL } from '../../common/common'
import { Skeleton } from '../ui/skeleton'

export default function HistoryVulnsPage({
    repo,
    history,
    fixAvailable
}: {
    repo: Repository | undefined
    history: History | undefined
    fixAvailable: string | null
}) {
    const [repoData, setRepoData] = useState<Repository>()
    const [historyData, setHistoryData] = useState<History>()
    const [fixResult, setFixResult] = useState<FixResult>()
    const [loading, setLoading] = useState<boolean>(true)
    const { toast } = useToast()
    const user: User = useSelector((state: RootState) => state.user.currentUser)

    const handlingToastAction = (title: string, description: string) => {
        toast({
            title,
            description,
            action: <ToastAction altText="Hide">Hide</ToastAction>,
        })
    }

    const handleCreatePullRequest = async () => {
        handlingToastAction(
            'Pull Request Processing',
            'This action can take some seconds'
        )

        if (repoData?.id && historyData?.id && user.githubToken) {
            const data = await createPullRequest(repoData.id, historyData.id, user.githubToken);
            if (typeof data === 'string') {
                handlingToastAction(ERROR_LABEL, data || DEFAULT_ERROR_MESSAGE)
            } else {
                handlingToastAction(SUCCESS_LABEL, PULL_REQUEST_SUCCESS_MESSAGE)
            }
        }
    }

    const handleCreateSecurityAdvisory = async () => {
        handlingToastAction(
            'Security Advisory Processing',
            'This action can take some seconds'
        )

        if (repoData?.id && historyData?.id && user.githubToken) {
            const data = await createSecurityAdvisory(repoData.id, historyData.id, user.githubToken);
            if (typeof data === 'string' || data === false) {
                handlingToastAction(ERROR_LABEL, data || DEFAULT_ERROR_MESSAGE)
            } else {
                handlingToastAction(SUCCESS_LABEL, SECURITY_ADVISORY_SUCCESS_MESSAGE)
            }
        }
    }

    const fetchData = async () => {
        if (historyData?.vulnerabilityResult) {
            const data = await getAutoFix(historyData.vulnerabilityResult);
            if (typeof data === 'string') {
                handlingToastAction(ERROR_LABEL, data || DEFAULT_ERROR_MESSAGE)
            } else {
                setFixResult(data)
            }
            setLoading(false)
        }
    }

    useEffect(() => {
        setRepoData(repo)
        setHistoryData(history)
        fixAvailable && fetchData()
    }, [repo, history])

    return (
        <div className="mx-auto">
            <Tabs defaultValue="summary">
                <TabsList className="grid grid-cols-2 w-[200px] mx-4">
                    <TabsTrigger value="summary">Summary</TabsTrigger>
                    <TabsTrigger value="details">Details</TabsTrigger>
                </TabsList>
                <TabsContent value="summary">
                    <ScrollArea className="h-[640px] w-full px-4">
                        <div className="grid grid-cols-3 gap-3">
                            <Card className="">
                                <CardHeader>
                                    <CardTitle>General Information</CardTitle>
                                </CardHeader>
                                <CardContent>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Name:
                                        </div>
                                        <div>{repoData?.fullName}</div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Owner:
                                        </div>
                                        <div>{repoData?.owner}</div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Public Repository:
                                        </div>
                                        <div>
                                            {repoData?.isPublic
                                                ? 'True'
                                                : 'False'}
                                        </div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            GitHub URL:
                                        </div>
                                        <div>
                                            <Link
                                                to={repoData?.githubUrl || '#'}
                                                target="_blank"
                                                className="font-medium"
                                            >
                                                <Button
                                                    variant="link"
                                                    style={{ padding: 0 }}
                                                >
                                                    {repoData?.githubUrl}
                                                </Button>
                                            </Link>
                                        </div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Language:
                                        </div>
                                        <div>{repoData?.language}</div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Updated By:
                                        </div>
                                        <div>{repoData?.updatedBy}</div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Created At:
                                        </div>
                                        <div>
                                            {repoData?.createdAt &&
                                                new Date(
                                                    repoData?.createdAt
                                                ).toLocaleString()}
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                            <Card className="">
                                <CardHeader>
                                    <CardTitle>Summary Analysis</CardTitle>
                                </CardHeader>
                                <CardContent>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Ecosystem:
                                        </div>
                                        <div>
                                            {
                                                history?.vulnerabilityResult
                                                    ?.ecosystem
                                            }
                                        </div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Dependencies:
                                        </div>
                                        <div>
                                            {
                                                history?.vulnerabilityResult
                                                    ?.libraryCount
                                            }
                                        </div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Vulnerable Dependencies:
                                        </div>
                                        <div>
                                            {
                                                history?.vulnerabilityResult
                                                    ?.issuesCount
                                            }
                                        </div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Include Safe:
                                        </div>
                                        <div>
                                            {history?.vulnerabilityResult
                                                ?.includeSafe
                                                ? 'True'
                                                : 'False'}
                                        </div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Duration (ms):
                                        </div>
                                        <div>
                                            {
                                                history?.vulnerabilityResult
                                                    ?.responseTime
                                            }
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                            <Card className="">
                                <CardHeader>
                                    <CardTitle>Summary Vulnerability</CardTitle>
                                </CardHeader>
                                <CardContent>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Low:
                                        </div>
                                        <div>
                                            {
                                                repoData?.vulnerabilitySummary
                                                    ?.low
                                            }
                                        </div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Medium:
                                        </div>
                                        <div>
                                            {
                                                repoData?.vulnerabilitySummary
                                                    ?.medium
                                            }
                                        </div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            High:
                                        </div>
                                        <div>
                                            {
                                                repoData?.vulnerabilitySummary
                                                    ?.high
                                            }
                                        </div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Critical:
                                        </div>
                                        <div>
                                            {
                                                repoData?.vulnerabilitySummary
                                                    ?.critical
                                            }
                                        </div>
                                    </div>
                                    <Separator />
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Status:
                                        </div>
                                        <div>
                                            <Badge
                                                style={{
                                                    backgroundColor: `${statuses.find(
                                                        (s) =>
                                                            s.value ===
                                                            repoData
                                                                ?.vulnerabilitySummary
                                                                ?.status
                                                    )?.color ||
                                                        'hsl(var(--foreground))'
                                                        }`,
                                                }}
                                            >
                                                {
                                                    repoData
                                                        ?.vulnerabilitySummary
                                                        ?.status
                                                }
                                            </Badge>
                                        </div>
                                    </div>
                                    {fixAvailable && <Button className='text-center my-4' onClick={handleCreateSecurityAdvisory}>
                                        <ShieldAlert className="mr-2 w-5 h-5" />
                                        Create Security Advisory
                                    </Button>}
                                </CardContent>
                            </Card>
                        </div>

                        {fixAvailable
                            && <>
                                <Button className='text-center my-4' onClick={handleCreatePullRequest}>
                                    <GitPullRequestArrow className="mr-2 w-5 h-5" />
                                    Create Pull Request
                                </Button>

                                <div className="">
                                    <Card className="">
                                        <CardHeader>
                                            <CardTitle>Fix vulnerabilities</CardTitle>
                                        </CardHeader>
                                        <CardContent>
                                            <Table>
                                                <TableCaption>A list of fix vulnerabilities.</TableCaption>
                                                <TableHeader>
                                                    <TableRow>
                                                        <TableHead></TableHead>
                                                        <TableHead>Name</TableHead>
                                                        <TableHead>Current Version</TableHead>
                                                        <TableHead>Fix Version</TableHead>
                                                    </TableRow>
                                                </TableHeader>
                                                <TableBody>
                                                    {loading
                                                        ? Array(10).fill({}).map((val, index) => <TableRow key={index}>
                                                            <TableCell>
                                                                <Skeleton className="h-[24px] w-full" />
                                                            </TableCell>
                                                            <TableCell>
                                                                <Skeleton className="h-[24px] w-full" />
                                                            </TableCell>
                                                            <TableCell>
                                                                <Skeleton className="h-[24px] w-full" />
                                                            </TableCell>
                                                            <TableCell>
                                                                <Skeleton className="h-[24px] w-full" />
                                                            </TableCell>
                                                        </TableRow>)
                                                        : fixResult?.libs.map((lib, index) => (
                                                            <TableRow key={index}>
                                                                <TableCell>
                                                                    <Badge
                                                                        style={{
                                                                            backgroundColor: severities.find(s => s.value === lib.severity)?.color,
                                                                        }}
                                                                    >
                                                                        {lib.severity}
                                                                    </Badge>
                                                                </TableCell>
                                                                <TableCell>{lib.name}</TableCell>
                                                                <TableCell>{lib.currentVersion}</TableCell>
                                                                <TableCell>{lib.fixedVersion}</TableCell>
                                                            </TableRow>
                                                        ))
                                                    }
                                                </TableBody>
                                            </Table>
                                        </CardContent>
                                    </Card>
                                </div>
                            </>
                        }

                        <ScrollBar orientation="horizontal" />
                    </ScrollArea>
                </TabsContent>
                <TabsContent value="details">
                    <ScrollArea className="h-[640px] w-full px-4">
                        {repoData?.vulnerabilityResult?.libs && (
                            <DataTable
                                data={repoData?.vulnerabilityResult?.libs}
                                columns={columns}
                                input={{
                                    column: 'packageName',
                                    title: 'Package Name',
                                }}
                                filter={{
                                    column: 'severity',
                                    title: 'Severity',
                                    dataset: severities,
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
