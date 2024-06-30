import { columns } from './components/columns-project'
import { DataTable } from './components/data-table'
import React, { useEffect, useMemo, useState } from 'react'
import { Repository, User } from '../../model/library'
import { ScrollArea, ScrollBar } from '../ui/scroll-area'
import { statuses } from '../../data/helper'
import { getRepos, isInstallGithubApp } from '../../api/apiCall'
import { ToastAction } from '../ui/toast'
import { useToast } from '../ui/use-toast'
import { DEFAULT_ERROR_MESSAGE, ERROR_LABEL } from '../../common/common'
import { Skeleton } from '../ui/skeleton'
import { useSelector } from 'react-redux'
import { RootState } from '../../redux/store'
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger,
} from '../ui/alert-dialog'
import { Button } from '../ui/button'
import { Link } from 'react-router-dom'
import { Alert, AlertDescription, AlertTitle } from '../ui/alert'
import { Terminal } from 'lucide-react'

export default function ProjectPage() {
    const [loading, setLoading] = useState<boolean>(false)
    const [projects, setProjects] = useState<Repository[]>([])
    const [isInstallApp, setIsInstallApp] = useState<boolean>(true)
    const { toast } = useToast()
    const user: User = useSelector((state: RootState) => state.user.currentUser)
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
    const buttonRef = React.useRef<HTMLButtonElement>(null)

    const handlingToastAction = (title: string, description: string) => {
        toast({
            title,
            description,
            action: <ToastAction altText="Hide">Hide</ToastAction>,
        })
    }

    const fetchData = async () => {
        if (user.githubToken) {
            const data = await getRepos(user.githubToken)
            if (typeof data === 'string') {
                handlingToastAction(ERROR_LABEL, data || DEFAULT_ERROR_MESSAGE)
            } else {
                setProjects(data)
            }
            setLoading(false)
        }
    }

    const fetchInstall = async () => {
        if (user.githubToken && user.githubUserId) {
            const isInstalled = await isInstallGithubApp(
                user.githubToken,
                user.githubUserId
            )
            if (typeof isInstalled === 'string') {
                handlingToastAction(
                    ERROR_LABEL,
                    isInstalled || DEFAULT_ERROR_MESSAGE
                )
            } else if (isInstalled === false) {
                setIsInstallApp(false)
            }
        }
    }

    useEffect(() => {
        setLoading(true)
        fetchInstall()
        fetchData()
    }, [])

    useEffect(() => {
        loading && setProjects(Array(10).fill({}))
    }, [loading])

    return (
        <div className="mx-auto">
            <AlertDialog
                open={!isInstallApp}
                onOpenChange={() => setIsInstallApp(true)}
            >
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>
                            Install & Authorize Anadep GitHub App
                        </AlertDialogTitle>
                        <AlertDialogDescription>
                            By install GitHub app and provide necessary
                            permissions. We can scan & analyze vulnerabilities
                            and security actions on your private repositories.
                        </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                        <AlertDialogCancel>Cancel</AlertDialogCancel>
                        <AlertDialogAction>
                            <Link
                                to={`${process.env.REACT_APP_AUTH_GITHUB_APP}/installations/new`}
                            >
                                Go to GitHub App
                            </Link>
                        </AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>

            <ScrollArea className="w-full px-4 mb-4" style={{height: "calc(100vh - 190px)"}}>
                {loading ? (
                    <DataTable
                        data={projects}
                        columns={loadingColumns}
                        input={{ column: 'projectName', title: 'Project Name' }}
                        filter={{
                            column: 'status',
                            title: 'Status',
                            dataset: statuses,
                        }}
                    />
                ) : (
                    <DataTable
                        data={projects}
                        columns={columns}
                        input={{ column: 'projectName', title: 'Project Name' }}
                        filter={{
                            column: 'status',
                            title: 'Status',
                            dataset: statuses,
                        }}
                    />
                )}
                <ScrollBar orientation="horizontal" />
            </ScrollArea>
        </div>
    )
}
