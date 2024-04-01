'use client'
import * as React from 'react'
import {
    AlertCircle,
    Archive,
    Boxes,
    FileText,
    GitPullRequestArrow,
    MessagesSquare,
    PlusCircleIcon,
    Search,
    ShieldCheck,
    ShoppingCart,
    Users2,
} from 'lucide-react'

import { AccountSwitcher } from './nav/account-switcher'
import { Nav } from './nav/nav'
import { cn } from '../../lib/utils'
import { Separator } from '../../component/ui/separator'
import { Input } from '../../component/ui/input'
import { Tabs } from '../../component/ui/tabs'
import { TooltipProvider } from '../../component/ui/tooltip'

import { GripVertical } from 'lucide-react'
import * as ResizablePrimitive from 'react-resizable-panels'
import { Button } from '../../component/ui/button'

import { accounts } from './nav/data'
import SbomPage from '../../component/table/page-lib'
import { ToastAction } from '../../component/ui/toast'
import { useToast } from '../../component/ui/use-toast'
import {
    downloadFileFormGitHubUrl,
    getAuthRepos,
    saveRepo,
} from '../../api/apiCall'
import {
    ADD_PROJECT_SUCCESS_MESSAGE,
    DEFAULT_ERROR_MESSAGE,
    ERROR_LABEL,
    SUCCESS_LABEL,
} from '../../common/common'
import { z } from 'zod'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import {
    Form,
    FormControl,
    FormDescription,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from '../../component/ui/form'
import { ReportForm, Repository, User } from '../../model/library'
import {
    Dialog,
    DialogClose,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from '../../component/ui/dialog'
import { RadioGroup, RadioGroupItem } from '../../component/ui/radio-group'
import { useSelector } from 'react-redux'
import { RootState } from '../../redux/store'
import { ScrollArea, ScrollBar } from '../../component/ui/scroll-area'
import { Skeleton } from '../../component/ui/skeleton'

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

const formSchema = z.object({
    projectName: z
        .string()
        .min(1, {
            message: 'Project name must not be empty.',
        })
        .max(100, {
            message: 'Project name must not be longer than 100 characters.',
        }),
    author: z
        .string()
        .min(1, {
            message: 'Author name must not be empty.',
        })
        .max(50, {
            message: 'Author name must not be longer than 50 characters.',
        }),
    format: z.enum(['pdf', 'excel', 'json'], {
        required_error: 'You need to select a format type.',
    }),
})

const formProjectSchema = z.object({
    name: z.string().min(1, {
        message: 'Please choose a project.',
    }),
})

const defaultValues: Partial<ReportForm> = {
    // name: "Your name",
    // dob: new Date("2023-01-23"),
}
const defaultProjectValues: Partial<Repository> = {}

export function SbomScan() {
    const defaultLayout = [0, 100]
    const defaultCollapsed = true
    const navCollapsedSize = 4
    const [isCollapsed, setIsCollapsed] = React.useState(defaultCollapsed)
    const { toast } = useToast()
    const inputRef = React.useRef<HTMLInputElement>(null)
    const [file, setFile] = React.useState<File>()
    const [repos, setRepos] = React.useState<Repository[]>([])
    const [repo, setRepo] = React.useState<Repository>()
    const form = useForm<ReportForm>({
        resolver: zodResolver(formSchema),
        defaultValues,
    })
    const formProject = useForm<Repository>({
        resolver: zodResolver(formProjectSchema),
        defaultValues: defaultProjectValues,
    })
    const [reportData, setReportData] = React.useState<ReportForm>()
    const [loading, setLoading] = React.useState<boolean>(false)
    const user: User = useSelector((state: RootState) => state.user.currentUser)

    const handlingToastAction = (title: string, description: string) => {
        toast({
            title,
            description,
            action: <ToastAction altText="Hide">Hide</ToastAction>,
        })
    }

    const handlingUploadFile = (e: React.ChangeEvent<HTMLInputElement>) => {
        const files = e.target.files ? Array.from(e.target.files) : null
        setFile(files?.at(0))
    }

    const handlingSearch = async (
        url: string | undefined,
        accessToken: any
    ) => {
        if (url) {
            const data = await downloadFileFormGitHubUrl(url, accessToken)
            if (typeof data === 'string') {
                handlingToastAction(ERROR_LABEL, data || DEFAULT_ERROR_MESSAGE)
                return
            }
            const blob = new Blob([data], { type: 'application/zip' })
            const file = new File([blob], 'source.zip')
            setFile(file)
        }
    }

    const handlingSubmit = (data: ReportForm) => {
        handlingToastAction(
            'Report Processing',
            'This action can take some minutes'
        )
        setReportData(data)
    }

    const handlingProjectSubmit = async (data: Repository) => {
        let repo = repos.find((r) => r.fullName === data.name)
        if (repo && user.githubToken) {
            const dataRepo = await saveRepo(
                { ...repo, userId: user.id },
                user.githubToken
            )
            if (typeof dataRepo === 'string') {
                handlingToastAction(
                    ERROR_LABEL,
                    dataRepo || DEFAULT_ERROR_MESSAGE
                )
            } else {
                handlingToastAction(
                    'Report Processing',
                    'This action can take some minutes'
                )
                setRepo(dataRepo)
            }
            handlingSearch(repo.githubUrl, user.githubToken)
        }
    }

    const handlingGetRepos = () => {
        setLoading(true)
        fetchData()
    }

    const fetchData = async () => {
        const data = await getAuthRepos(user.githubToken || '')
        if (typeof data === 'string') {
            handlingToastAction(ERROR_LABEL, data || DEFAULT_ERROR_MESSAGE)
        } else {
            setRepos(data)
        }
        setLoading(false)
    }

    React.useEffect(() => {
        loading && setRepos(Array(10).fill({}))
    }, [loading])

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
                        links={
                            user.id
                                ? [
                                      {
                                          title: 'Vulnerability Searching',
                                          href: '/search',
                                          icon: Search,
                                          variant: 'ghost',
                                      },
                                      {
                                          title: 'Vulnerability Scanning',
                                          href: '/namespace',
                                          icon: ShieldCheck,
                                          variant: 'ghost',
                                      },
                                      {
                                          title: 'SBOM',
                                          href: '/sbom',
                                          icon: Boxes,
                                          variant: 'default',
                                      },
                                      {
                                          title: 'Project',
                                          href: '/project',
                                          icon: GitPullRequestArrow,
                                          variant: 'ghost',
                                      },
                                  ]
                                : [
                                      {
                                          title: 'Vulnerability Searching',
                                          href: '/search',
                                          icon: Search,
                                          variant: 'ghost',
                                      },
                                      {
                                          title: 'Vulnerability Scanning',
                                          href: '/namespace',
                                          icon: ShieldCheck,
                                          variant: 'ghost',
                                      },
                                      {
                                          title: 'SBOM',
                                          href: '/sbom',
                                          icon: Boxes,
                                          variant: 'default',
                                      },
                                  ]
                        }
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
                    <Tabs defaultValue="all">
                        <div className="flex h-[52px] justify-between items-center px-4 py-2">
                            <h1 className="text-xl font-bold">SBOM</h1>
                            <div className="flex items-center p-2">
                                <div className="flex items-center gap-2"></div>
                                {user.id && (
                                    <Dialog>
                                        <DialogTrigger
                                            asChild
                                            onClick={handlingGetRepos}
                                        >
                                            <Button>
                                                <PlusCircleIcon className="mr-2" />
                                                Add Projects
                                            </Button>
                                        </DialogTrigger>
                                        <DialogContent className="sm:max-w-[425px]">
                                            <Form {...formProject}>
                                                <form
                                                    onSubmit={formProject.handleSubmit(
                                                        handlingProjectSubmit
                                                    )}
                                                    className="space-y-8"
                                                >
                                                    <DialogHeader>
                                                        <DialogTitle>
                                                            Add GitHub Project
                                                        </DialogTitle>
                                                        <DialogDescription>
                                                            Please select the
                                                            project you want to
                                                            be integrated with
                                                            our service
                                                        </DialogDescription>
                                                    </DialogHeader>
                                                    <FormField
                                                        control={
                                                            formProject.control
                                                        }
                                                        name="name"
                                                        render={({ field }) => (
                                                            <FormItem className="space-y-3">
                                                                <FormLabel>
                                                                    Please
                                                                    choose a
                                                                    project
                                                                </FormLabel>
                                                                <ScrollArea className="h-[450px] w-full px-4">
                                                                    <FormControl>
                                                                        <RadioGroup
                                                                            onValueChange={
                                                                                field.onChange
                                                                            }
                                                                            defaultValue={
                                                                                field.value
                                                                            }
                                                                            className="flex flex-col space-y-1"
                                                                        >
                                                                            {loading
                                                                                ? repos.map(
                                                                                      (
                                                                                          rp,
                                                                                          index
                                                                                      ) => (
                                                                                          <Skeleton
                                                                                              className="h-[40px] w-full"
                                                                                              key={
                                                                                                  index +
                                                                                                  1
                                                                                              }
                                                                                          />
                                                                                      )
                                                                                  )
                                                                                : repos.map(
                                                                                      (
                                                                                          rp
                                                                                      ) => (
                                                                                          <FormItem
                                                                                              className="flex items-center space-x-3 space-y-0"
                                                                                              key={
                                                                                                  rp.fullName
                                                                                              }
                                                                                          >
                                                                                              <FormControl>
                                                                                                  <RadioGroupItem
                                                                                                      value={
                                                                                                          rp.fullName
                                                                                                      }
                                                                                                  />
                                                                                              </FormControl>
                                                                                              <FormLabel className="font-normal">
                                                                                                  {
                                                                                                      rp.fullName
                                                                                                  }
                                                                                              </FormLabel>
                                                                                          </FormItem>
                                                                                      )
                                                                                  )}
                                                                        </RadioGroup>
                                                                    </FormControl>
                                                                    <ScrollBar orientation="horizontal" />
                                                                </ScrollArea>
                                                                <FormMessage />
                                                            </FormItem>
                                                        )}
                                                    />
                                                    <DialogFooter>
                                                        <DialogClose asChild>
                                                            <Button
                                                                type="button"
                                                                variant="secondary"
                                                            >
                                                                Cancel
                                                            </Button>
                                                        </DialogClose>
                                                        <Button type="submit">
                                                            Add
                                                        </Button>
                                                    </DialogFooter>
                                                </form>
                                            </Form>
                                        </DialogContent>
                                    </Dialog>
                                )}
                                <Separator
                                    orientation="vertical"
                                    className="mx-2 h-6"
                                />
                                <Dialog>
                                    <DialogTrigger asChild>
                                        <Button variant="ghost" size="icon">
                                            <FileText className="h-4 w-4" />
                                            <span className="sr-only">
                                                Export scan data
                                            </span>
                                        </Button>
                                    </DialogTrigger>
                                    <DialogContent className="sm:max-w-[425px]">
                                        <Form {...form}>
                                            <form
                                                onSubmit={form.handleSubmit(
                                                    handlingSubmit
                                                )}
                                                className="space-y-8"
                                            >
                                                <DialogHeader>
                                                    <DialogTitle>
                                                        Export scan data
                                                    </DialogTitle>
                                                    <DialogDescription>
                                                        Please provide project
                                                        information and format
                                                        of data you like to
                                                        export
                                                    </DialogDescription>
                                                </DialogHeader>
                                                <FormField
                                                    control={form.control}
                                                    name="projectName"
                                                    render={({ field }) => (
                                                        <FormItem>
                                                            <FormLabel>
                                                                Project Name
                                                            </FormLabel>
                                                            <FormControl>
                                                                <Input
                                                                    placeholder="Your project name"
                                                                    {...field}
                                                                />
                                                            </FormControl>
                                                            <FormDescription>
                                                                This is the
                                                                project name
                                                                that will be
                                                                displayed on
                                                                your report.
                                                            </FormDescription>
                                                            <FormMessage />
                                                        </FormItem>
                                                    )}
                                                />
                                                <FormField
                                                    control={form.control}
                                                    name="author"
                                                    render={({ field }) => (
                                                        <FormItem className="flex flex-col">
                                                            <FormLabel>
                                                                Author
                                                            </FormLabel>
                                                            <FormControl>
                                                                <Input
                                                                    placeholder="Author name"
                                                                    {...field}
                                                                />
                                                            </FormControl>
                                                            <FormDescription>
                                                                This is the
                                                                author name that
                                                                will be
                                                                displayed on
                                                                your report.
                                                            </FormDescription>
                                                            <FormMessage />
                                                        </FormItem>
                                                    )}
                                                />
                                                <FormField
                                                    control={form.control}
                                                    name="format"
                                                    render={({ field }) => (
                                                        <FormItem className="space-y-3">
                                                            <FormLabel>
                                                                Please choose a
                                                                format
                                                            </FormLabel>
                                                            <FormControl>
                                                                <RadioGroup
                                                                    onValueChange={
                                                                        field.onChange
                                                                    }
                                                                    defaultValue={
                                                                        field.value
                                                                    }
                                                                    className="flex flex-col space-y-1"
                                                                >
                                                                    <FormItem className="flex items-center space-x-3 space-y-0">
                                                                        <FormControl>
                                                                            <RadioGroupItem value="pdf" />
                                                                        </FormControl>
                                                                        <FormLabel className="font-normal">
                                                                            PDF
                                                                        </FormLabel>
                                                                    </FormItem>
                                                                    <FormItem className="flex items-center space-x-3 space-y-0">
                                                                        <FormControl>
                                                                            <RadioGroupItem value="excel" />
                                                                        </FormControl>
                                                                        <FormLabel className="font-normal">
                                                                            EXCEL
                                                                        </FormLabel>
                                                                    </FormItem>
                                                                    <FormItem className="flex items-center space-x-3 space-y-0">
                                                                        <FormControl>
                                                                            <RadioGroupItem value="json" />
                                                                        </FormControl>
                                                                        <FormLabel className="font-normal">
                                                                            JSON
                                                                        </FormLabel>
                                                                    </FormItem>
                                                                </RadioGroup>
                                                            </FormControl>
                                                            <FormMessage />
                                                        </FormItem>
                                                    )}
                                                />
                                                <DialogFooter>
                                                    <DialogClose asChild>
                                                        <Button
                                                            type="button"
                                                            variant="secondary"
                                                        >
                                                            Cancel
                                                        </Button>
                                                    </DialogClose>
                                                    <Button type="submit">
                                                        Export
                                                    </Button>
                                                </DialogFooter>
                                            </form>
                                        </Form>
                                    </DialogContent>
                                </Dialog>
                            </div>
                        </div>
                        <Separator />
                        <div className="bg-background/95 p-4 backdrop-blur supports-[backdrop-filter]:bg-background/60">
                            <form>
                                <div className="vulnerability-scan-form flex justify-between items-center">
                                    <div className="vulnerability-scan-form-left relative w-full">
                                        <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
                                        <Input
                                            ref={inputRef}
                                            placeholder="Github URL"
                                            className="pl-8"
                                        />
                                    </div>
                                    <div className="vulnerability-scan-form-right flex items-center px-2">
                                        <div className="px-2">
                                            <Button
                                                onClick={(e) => {
                                                    e.preventDefault()
                                                    handlingSearch(
                                                        inputRef.current?.value,
                                                        user.githubToken
                                                    )
                                                    handlingToastAction(
                                                        'Analysis Processing',
                                                        'This action can take some minutes'
                                                    )
                                                }}
                                            >
                                                OK
                                            </Button>
                                        </div>
                                        <span className="transition-colors font-medium text-sm mx-2">
                                            {' '}
                                            Or{' '}
                                        </span>
                                        <div
                                            className="grid items-center gap-1.5 px-2"
                                            style={{ width: '250px' }}
                                        >
                                            <Input
                                                id="zip-file"
                                                type="file"
                                                onChange={(e) => {
                                                    handlingUploadFile(e)
                                                    handlingToastAction(
                                                        'Analysis Processing',
                                                        'This action can take some minutes'
                                                    )
                                                }}
                                            />
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </Tabs>
                    <SbomPage file={file} reportData={reportData} repo={repo} />
                </ResizablePanel>
            </ResizablePanelGroup>
        </TooltipProvider>
    )
}
