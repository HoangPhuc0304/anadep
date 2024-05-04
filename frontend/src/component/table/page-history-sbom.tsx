import { useEffect, useState } from 'react'
import { History, Repository, User } from '../../model/library'
import { ScrollArea, ScrollBar } from '../ui/scroll-area'
import { ecosystems } from '../../data/helper'
import { ToastAction } from '../ui/toast'
import { useToast } from '../ui/use-toast'
import { useSelector } from 'react-redux'
import { RootState } from '../../redux/store'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '../ui/tabs'
import { DataTable } from './components/data-table'
import { Card, CardContent, CardHeader, CardTitle } from '../ui/card'
import { Link } from 'react-router-dom'
import { Button } from '../ui/button'
import { columns } from './components/columns-lib'

export default function HistorySbomPage({
    repo,
    history,
}: {
    repo: Repository | undefined
    history: History | undefined
}) {
    const [repoData, setRepoData] = useState<Repository>()
    const [historyData, setHistoryData] = useState<History>()
    const { toast } = useToast()
    const user: User = useSelector((state: RootState) => state.user.currentUser)

    const handlingToastAction = (title: string, description: string) => {
        toast({
            title,
            description,
            action: <ToastAction altText="Hide">Hide</ToastAction>,
        })
    }

    useEffect(() => {
        setRepoData(repo)
        setHistoryData(history)
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
                        <div className="grid grid-cols-2 gap-2">
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
                                        <div>{historyData?.updatedBy}</div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Created At:
                                        </div>
                                        <div>
                                            {historyData?.createdAt &&
                                                new Date(
                                                    historyData?.createdAt
                                                ).toLocaleString()}
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                            <Card className="">
                                <CardHeader>
                                    <CardTitle>Summary Scanning</CardTitle>
                                </CardHeader>
                                <CardContent>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Ecosystem:
                                        </div>
                                        <div>
                                            {history?.scanningResult?.ecosystem}
                                        </div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Dependencies:
                                        </div>
                                        <div>
                                            {
                                                history?.scanningResult
                                                    ?.libraryCount
                                            }
                                        </div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Include Safe:
                                        </div>
                                        <div>
                                            {history?.scanningResult
                                                ?.includeTransitive
                                                ? 'True'
                                                : 'False'}
                                        </div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Path:
                                        </div>
                                        <div>{history?.path}</div>
                                    </div>
                                    <div className="mb-2">
                                        <div className="items-start font-bold">
                                            Duration (ms):
                                        </div>
                                        <div>
                                            {
                                                history?.scanningResult
                                                    ?.responseTime
                                            }
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                        </div>
                        <ScrollBar orientation="horizontal" />
                    </ScrollArea>
                </TabsContent>
                <TabsContent value="details">
                    <ScrollArea className="h-[640px] w-full px-4">
                        {repoData?.scanningResult?.libraries && (
                            <DataTable
                                data={repoData?.scanningResult?.libraries}
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
                </TabsContent>
            </Tabs>
        </div>
    )
}
