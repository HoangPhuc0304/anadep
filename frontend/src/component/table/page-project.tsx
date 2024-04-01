import { columns } from './components/columns-project'
import { DataTable } from './components/data-table'
import { useEffect, useMemo, useState } from 'react'
import { Repository, User } from '../../model/library'
import { ScrollArea, ScrollBar } from '../ui/scroll-area'
import { statuses } from '../../data/helper'
import { getRepos } from '../../api/apiCall'
import { ToastAction } from '../ui/toast'
import { useToast } from '../ui/use-toast'
import { DEFAULT_ERROR_MESSAGE, ERROR_LABEL } from '../../common/common'
import { Skeleton } from '../ui/skeleton'
import { useSelector } from 'react-redux'
import { RootState } from '../../redux/store'

export default function ProjectPage() {
    const [loading, setLoading] = useState<boolean>(false)
    const [projects, setProjects] = useState<Repository[]>([])
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

    useEffect(() => {
        setLoading(true)
        fetchData()
    }, [])

    useEffect(() => {
        loading && setProjects(Array(10).fill({}))
    }, [loading])

    return (
        <div className="mx-auto">
            <ScrollArea className="h-[640px] w-full px-4">
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
