'use client'
import * as React from 'react'
import {
    AlertCircle,
    Archive,
    Boxes,
    FileText,
    MessagesSquare,
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
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger,
} from '../../component/ui/dropdown-menu'

import { accounts } from './nav/data'
import SbomPage from '../../component/table/page-lib'
import { ToastAction } from '../../component/ui/toast'
import { useToast } from '../../component/ui/use-toast'
import { downloadFileFormGitHubUrl } from '../../api/apiCall'
import { DEFAULT_ERROR_MESSAGE, ERROR_LABEL } from '../../common/common'

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

export function SbomScan() {
    const defaultLayout = [0, 100]
    const defaultCollapsed = true
    const navCollapsedSize = 4
    const [isCollapsed, setIsCollapsed] = React.useState(defaultCollapsed)
    const { toast } = useToast()
    const inputRef = React.useRef<HTMLInputElement>(null)
    const [file, setFile] = React.useState<File>()

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

    const handlingSearch = async (url: string | undefined) => {
        if (url) {
            const data = await downloadFileFormGitHubUrl(url)
            if (typeof data === 'string') {
                handlingToastAction(ERROR_LABEL, data || DEFAULT_ERROR_MESSAGE)
                return
            }
            const blob = new Blob([data], { type: 'application/zip' })
            const file = new File([blob], 'source.zip')
            setFile(file)
        }
    }

    const handlingFormat = (format: string) => {
        console.log(format)
    }

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
                                title: 'Vulnerability Scanning',
                                href: '/namespace',
                                icon: ShieldCheck,
                                variant: 'ghost',
                            },
                            {
                                title: 'Vulnerability Searching',
                                href: '/search',
                                icon: Search,
                                variant: 'ghost',
                            },
                            {
                                title: 'SBOM',
                                href: '/sbom',
                                icon: Boxes,
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
                    <Tabs defaultValue="all">
                        <div className="flex h-[52px] justify-between items-center px-4 py-2">
                            <h1 className="text-xl font-bold">SBOM</h1>
                            <div className="flex items-center p-2">
                                <div className="flex items-center gap-2"></div>
                                <Separator
                                    orientation="vertical"
                                    className="mx-2 h-6"
                                />
                                <DropdownMenu>
                                    <DropdownMenuTrigger asChild>
                                        <Button variant="ghost" size="icon">
                                            <FileText className="h-4 w-4" />
                                            <span className="sr-only">
                                                Export scan data
                                            </span>
                                        </Button>
                                    </DropdownMenuTrigger>
                                    <DropdownMenuContent align="end">
                                        <DropdownMenuItem
                                            onClick={() => {
                                                handlingFormat('pdf')
                                                handlingToastAction(
                                                    'Report Processing',
                                                    'This action can take some minutes'
                                                )
                                            }}
                                        >
                                            PDF
                                        </DropdownMenuItem>
                                        <DropdownMenuItem
                                            onClick={() => {
                                                handlingFormat('csv')
                                                handlingToastAction(
                                                    'Report Processing',
                                                    'This action can take some minutes'
                                                )
                                            }}
                                        >
                                            CSV
                                        </DropdownMenuItem>
                                        <DropdownMenuItem
                                            onClick={() => {
                                                handlingFormat('json')
                                                handlingToastAction(
                                                    'Report Processing',
                                                    'This action can take some minutes'
                                                )
                                            }}
                                        >
                                            JSON
                                        </DropdownMenuItem>
                                    </DropdownMenuContent>
                                </DropdownMenu>
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
                                                        inputRef.current?.value
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
                    <SbomPage file={file} />
                </ResizablePanel>
            </ResizablePanelGroup>
        </TooltipProvider>
    )
}
